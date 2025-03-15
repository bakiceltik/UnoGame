package com.duocardgame.domain.mediator;

import com.duocardgame.domain.model.Card;
import com.duocardgame.domain.model.CardType;
import com.duocardgame.domain.model.Color;
import com.duocardgame.domain.model.Deck;
import com.duocardgame.domain.model.Player;
import com.duocardgame.dataaccess.repository.GameRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class DuoGameMediator implements GameMediator {
    private final List<Player> players;
    private final Deck drawPile;
    private final List<Card> discardPile;
    private int currentPlayerIndex;
    private int dealerIndex;
    private boolean isClockwise;
    private Color currentColor;
    private final Random random;
    private final GameRepository gameRepository;
    private boolean gameOver;
    private static final int WINNING_SCORE = 500;
    private static final int INITIAL_CARDS = 7;
    
    public DuoGameMediator(GameRepository gameRepository) {
        this.players = new ArrayList<>();
        this.drawPile = new Deck();
        this.discardPile = new ArrayList<>();
        this.random = new Random();
        this.gameRepository = gameRepository;
        this.isClockwise = true; // Varsayılan yön saat yönünün tersi (sola doğru)
        this.gameOver = false;
    }
    
    @Override
    public void startGame() {
        // Rastgele oyuncu sayısını belirle (2-4 arası)
        int playerCount = random.nextInt(3) + 2;
        
        // Oyuncuları oluştur
        for (int i = 1; i <= playerCount; i++) {
            players.add(new Player("Player" + i));
        }
        
        // İlk turu başlat
        startNewRound();
    }
    
    @Override
    public void startNewRound() {
        // Oyuncuların ellerini temizle
        for (Player player : players) {
            player.clearHand();
        }
        
        // Desteyi yeniden oluştur ve karıştır
        drawPile.shuffle();
        discardPile.clear();
        
        // İlk oyuncuyu belirle
        determineFirstDealer();
        
        // Her oyuncuya 7 kart dağıt
        dealInitialCards();
        
        // İlk kartı aç
        drawFirstCard();
        
        // İlk oyuncuyu belirle (soldan başla)
        currentPlayerIndex = (dealerIndex + 1) % players.size();
    }
    
    private void determineFirstDealer() {
        // Her oyuncuya bir kart dağıt, en yüksek karta sahip olan kurpiyedir
        List<Card> dealerCards = new ArrayList<>();
        List<Integer> cardValues = new ArrayList<>();
        
        for (Player player : players) {
            Optional<Card> drawnCard = drawPile.drawCard();
            if (drawnCard.isPresent()) {
                Card card = drawnCard.get();
                dealerCards.add(card);
                cardValues.add(card.getPointValue());
            }
        }
        
        // En yüksek değere sahip oyuncuyu bul
        int maxValue = Collections.max(cardValues);
        dealerIndex = cardValues.indexOf(maxValue);
        
        // Dağıtılan kartları tekrar desteye ekle ve karıştır
        for (Card card : dealerCards) {
            drawPile.addCard(card);
        }
        drawPile.shuffle();
    }
    
    private void dealInitialCards() {
        // Her oyuncuya 7 kart dağıt
        for (int i = 0; i < INITIAL_CARDS; i++) {
            for (int j = 0; j < players.size(); j++) {
                int playerIndex = (dealerIndex + 1 + j) % players.size();
                Player player = players.get(playerIndex);
                
                Optional<Card> drawnCard = drawPile.drawCard();
                if (drawnCard.isPresent()) {
                    player.addCardToHand(drawnCard.get());
                }
            }
        }
    }
    
    private void drawFirstCard() {
        Optional<Card> firstCard = drawPile.drawCard();
        if (firstCard.isPresent()) {
            Card card = firstCard.get();
            discardPile.add(card);
            currentColor = card.getColor();
            
            // İlk kart bir aksiyon kartıysa, etkisini uygula
            if (card.getType() != CardType.NUMBER) {
                handleFirstActionCard(card);
            }
        }
    }
    
    private void handleFirstActionCard(Card card) {
        CardType type = card.getType();
        
        switch (type) {
            case WILD:
                // İlk oyuncu renk seçer
                currentColor = players.get((dealerIndex + 1) % players.size()).chooseColor();
                break;
                
            case WILD_DRAW_FOUR:
                // Bu kartı tekrar desteye koy ve yeni bir kart çek
                drawPile.addCard(card);
                drawPile.shuffle();
                discardPile.remove(discardPile.size() - 1);
                drawFirstCard();
                break;
                
            case DRAW_TWO:
                // İlk oyuncu iki kart çeker ve turunu kaçırır
                Player nextPlayer = players.get((dealerIndex + 1) % players.size());
                // Önceki kontrolü atlamak için özel durum
                // Şimdi kartı doğrudan ele ekliyoruz
                for (int i = 0; i < 2; i++) {
                    Optional<Card> drawnCard = drawPile.drawCard();
                    if (drawnCard.isPresent()) {
                        nextPlayer.addCardToHand(drawnCard.get());
                    }
                }
                // İlk oyuncuyu güncelle
                currentPlayerIndex = (dealerIndex + 2) % players.size();
                break;
                
            case REVERSE:
                // Oyun yönünü değiştir ve kurpiye başlar
                isClockwise = false;
                currentPlayerIndex = dealerIndex;
                break;
                
            case SKIP:
                // İlk oyuncuyu atla
                currentPlayerIndex = (dealerIndex + 2) % players.size();
                break;
                
            case SHUFFLE_HANDS:
                // İlk oyuncu renk seçer
                currentColor = players.get((dealerIndex + 1) % players.size()).chooseColor();
                break;
                
            default:
                break;
        }
    }
    
    @Override
    public void playCard(Player player, Card card) {
        if (getCurrentPlayer() != player) {
            throw new IllegalStateException("Sırası gelmeyen oyuncu kart oynayamaz");
        }
        
        // Kart uyumlu mu kontrol et
        Card topCard = getTopCard();
        if (!card.isPlayable(topCard) && card.getColor() != currentColor) {
            throw new IllegalArgumentException("Bu kart uyumlu değil");
        }
        
        player.removeCardFromHand(card);
        discardPile.add(card);
        currentColor = card.getColor() != Color.WILD ? card.getColor() : currentColor;
        
        // Kart etkisini uygula
        applyCardEffect(player, card);
        
        // Turu tamamla ve bir sonraki oyuncuya geç
        if (isRoundOver()) {
            // Turu bitir ve puanları hesapla
            endRound(player);
        } else if (card.getType() != CardType.SKIP && 
                  card.getType() != CardType.DRAW_TWO && 
                  card.getType() != CardType.WILD_DRAW_FOUR) {
            nextTurn();
        }
    }
    
    @Override
    public void drawCard(Player player) {
        // Yeni tur başlangıcında ilk kartı çekerken özel durum
        boolean isInitialDraw = currentPlayerIndex == -1 || player == null || getCurrentPlayer() == null;
        
        // Eğer yeni turda ilk kart açılıyorsa veya mevcut oyuncu kart çekiyorsa işlem yapılabilir
        if (isInitialDraw || getCurrentPlayer() == player) {
            Optional<Card> drawnCard = drawPile.drawCard();
            
            // Çekme destesi boş ise, çöp destesini karıştırıp yeni çekme destesi yap
            if (!drawnCard.isPresent() && !discardPile.isEmpty()) {
                Card topCard = discardPile.remove(discardPile.size() - 1);
                drawPile.addCards(discardPile);
                discardPile.clear();
                discardPile.add(topCard);
                drawPile.shuffle();
                
                drawnCard = drawPile.drawCard();
            }
            
            if (drawnCard.isPresent()) {
                // Eğer oyuncu null değilse (özel durum değilse) kart ekle
                if (player != null) {
                    player.addCardToHand(drawnCard.get());
                }
                
                // Eğer normal oyun akışındaysak ve çekilen kart oynanabilirse oynat
                if (!isInitialDraw && drawnCard.get().isPlayable(getTopCard()) || 
                    !isInitialDraw && (drawnCard.get().getColor() == currentColor ||
                    drawnCard.get().getType() == CardType.WILD ||
                    drawnCard.get().getType() == CardType.WILD_DRAW_FOUR)) {
                    
                    // Burada oyuncunun kartı oynamak isteyip istemediği sorulabilir
                    // Ama şimdi rastgele bir karar verelim
                    boolean playCard = random.nextBoolean();
                    
                    if (playCard) {
                        // Önemli: playCard metodu içindeki sıra kontrolünü geçmek için burada sıranın 
                        // hala çeken oyuncuda olduğundan emin olalım
                        playCard(player, drawnCard.get());
                        return;
                    }
                }
                
                // Özel durum değilse ve kart oynanmadıysa, bir sonraki oyuncuya geç
                if (!isInitialDraw) {
                    nextTurn();
                }
                
                // Çekilen kartı döndür (özel durum için)
                return;
            }
        } else {
            throw new IllegalStateException("Sırası gelmeyen oyuncu kart çekemez");
        }
    }
    
    @Override
    public void changeColor(Color newColor) {
        this.currentColor = newColor;
    }
    
    @Override
    public void applyCardEffect(Player player, Card card) {
        CardType type = card.getType();
        
        switch (type) {
            case DRAW_TWO:
                Player nextPlayer = getNextPlayer();
                // Önce sırayı bir sonraki oyuncuya geçirelim, böylece kart çekme işlemi doğru oyuncuya yapılabilir
                nextTurn();
                // Şimdi kart çekme işlemini yapalım
                for (int i = 0; i < 2; i++) {
                    if (!drawPile.isEmpty()) {
                        Optional<Card> drawn = drawPile.drawCard();
                        if (drawn.isPresent()) {
                            nextPlayer.addCardToHand(drawn.get());
                        }
                    }
                }
                // Oyuncu zaten turunu kaybettiğinden, tekrar sıra değiştirmeye gerek yok
                break;
                
            case REVERSE:
                isClockwise = !isClockwise;
                break;
                
            case SKIP:
                nextTurn();
                break;
                
            case WILD:
                changeColor(player.chooseColor());
                break;
                
            case WILD_DRAW_FOUR:
                nextPlayer = getNextPlayer();
                changeColor(player.chooseColor());
                // Önce sırayı bir sonraki oyuncuya geçirelim
                nextTurn();
                // Şimdi kart çekme işlemini yapalım
                for (int i = 0; i < 4; i++) {
                    if (!drawPile.isEmpty()) {
                        Optional<Card> drawn = drawPile.drawCard();
                        if (drawn.isPresent()) {
                            nextPlayer.addCardToHand(drawn.get());
                        }
                    }
                }
                // Oyuncu zaten turunu kaybettiğinden, tekrar sıra değiştirmeye gerek yok
                break;
                
            case SHUFFLE_HANDS:
                shuffleHands(player);
                changeColor(player.chooseColor());
                break;
                
            default:
                break;
        }
    }
    
    private void shuffleHands(Player excludedPlayer) {
        List<Card> allCards = new ArrayList<>();
        
        // Tüm oyuncuların kartlarını topla
        for (Player player : players) {
            allCards.addAll(player.getHand());
            player.clearHand();
        }
        
        // Kartları karıştır
        Collections.shuffle(allCards);
        
        // Kartları yeniden dağıt
        int cardIndex = 0;
        while (cardIndex < allCards.size()) {
            for (int i = 0; i < players.size() && cardIndex < allCards.size(); i++) {
                int playerIndex = (currentPlayerIndex + 1 + i) % players.size();
                players.get(playerIndex).addCardToHand(allCards.get(cardIndex++));
            }
        }
    }
    
    private void endRound(Player winner) {
        // Kazanan oyuncuya, diğer oyuncuların ellerinde kalan kartların değerini ekle
        int roundPoints = 0;
        for (Player player : players) {
            if (player != winner) {
                roundPoints += player.calculateHandValue();
            }
        }
        
        winner.addToScore(roundPoints);
        
        // Oyun durumunu kaydet
        saveGameState();
        
        // Kazanan oyuncu 500 puana ulaştıysa oyun biter
        if (winner.getTotalScore() >= WINNING_SCORE) {
            gameOver = true;
        } else {
            // Değilse yeni bir tur başlat
            startNewRound();
        }
    }
    
    @Override
    public void nextTurn() {
        if (isClockwise) {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        } else {
            currentPlayerIndex = (currentPlayerIndex - 1 + players.size()) % players.size();
        }
    }
    
    private Player getNextPlayer() {
        if (isClockwise) {
            return players.get((currentPlayerIndex + 1) % players.size());
        } else {
            return players.get((currentPlayerIndex - 1 + players.size()) % players.size());
        }
    }
    
    @Override
    public boolean isGameOver() {
        return gameOver;
    }
    
    @Override
    public boolean isRoundOver() {
        for (Player player : players) {
            if (player.hasEmptyHand()) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }
    
    @Override
    public Card getTopCard() {
        if (discardPile.isEmpty()) {
            return null;
        }
        return discardPile.get(discardPile.size() - 1);
    }
    
    @Override
    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }
    
    @Override
    public Player getDealerPlayer() {
        return players.get(dealerIndex);
    }
    
    @Override
    public void saveGameState() {
        // Oyun durumunu CSV dosyasına kaydet
        List<String[]> gameState = new ArrayList<>();
        
        // Başlık satırı
        String[] header = new String[players.size() + 1];
        header[0] = "Round";
        for (int i = 0; i < players.size(); i++) {
            header[i + 1] = players.get(i).getName();
        }
        gameState.add(header);
        
        // Veri satırı
        String[] data = new String[players.size() + 1];
        data[0] = String.valueOf(gameRepository.getRoundCount() + 1);
        for (int i = 0; i < players.size(); i++) {
            data[i + 1] = String.valueOf(players.get(i).getTotalScore());
        }
        gameState.add(data);
        
        gameRepository.saveGameState(gameState);
    }
} 