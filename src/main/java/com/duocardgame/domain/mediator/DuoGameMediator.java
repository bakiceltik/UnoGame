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

        for (int i = 0; i < players.size(); i++) {
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
                Color selectedColor = players.get((dealerIndex + 1) % players.size()).chooseColor();
                currentColor = selectedColor;
                System.out.println("\n█ Initial Color Selection █");
                System.out
                        .println("» First card is WILD! " + players.get((dealerIndex + 1) % players.size()).getName() +
                                " selected " + selectedColor + " as the starting color!");
                System.out.println("» All players must now play " + selectedColor + " cards or matching card types.");
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
                Color selectedColorShuffle = players.get((dealerIndex + 1) % players.size()).chooseColor();
                currentColor = selectedColorShuffle;
                System.out.println("\n█ Initial Color Selection █");
                System.out.println(
                        "» First card is SHUFFLE_HANDS! " + players.get((dealerIndex + 1) % players.size()).getName() +
                                " selected " + selectedColorShuffle + " as the starting color!");
                System.out.println(
                        "» All players must now play " + selectedColorShuffle + " cards or matching card types.");
                break;

            default:
                break;
        }
    }

    @Override
    public void playCard(Player player, Card card) {
        if (getCurrentPlayer() != player) {
            throw new IllegalStateException("A player whose turn has not come cannot play a card.");
        }

        // Kart uyumlu mu kontrol et
        Card topCard = getTopCard();

        // Wild Draw Four kartı için özel kontrol
        if (card.getType() == CardType.WILD_DRAW_FOUR || card.getType() == CardType.WILD) {
            // Oyuncunun elinde, mevcut renkte kart olup olmadığını kontrol et
            boolean hasMatchingColorCard = false;
            for (Card handCard : player.getHand()) {
                // Wild kartları hariç tut, sadece normal renk kartlarını kontrol et
                if (handCard != card && handCard.getColor() == topCard.getColor() &&
                        handCard.getType() != CardType.WILD && handCard.getType() != CardType.WILD_DRAW_FOUR) {
                    hasMatchingColorCard = true;
                    break;
                }
            }

            // Eğer oyuncunun elinde eşleşen renkte kart varsa ve bu bir Wild Draw Four
            // kartıysa, oynanamaz
            if (hasMatchingColorCard && card.getType() == CardType.WILD_DRAW_FOUR) {
                throw new IllegalArgumentException(
                        "Wild Draw Four card can only be played if there is no card of the matching color");
            }
        }

        if (!card.isPlayable(topCard) && card.getColor() != currentColor &&
                !(card.getType() == CardType.WILD || card.getType() == CardType.WILD_DRAW_FOUR
                        || card.getType() == CardType.SHUFFLE_HANDS)) {
            throw new IllegalArgumentException("This card is not compatible");
        }

        player.removeCardFromHand(card);
        discardPile.add(card);

        // Normal kartlar için rengi güncelle (Wild kartlar için applyCardEffect içinde
        // yapılıyor)
        if (card.getColor() != Color.WILD) {
            currentColor = card.getColor();
            System.out.println("\n█ Color Update █");
            System.out.println("» New active color is now: " + currentColor);
        }

        // İyileştirilmiş oyun bilgilendirmesi
        System.out.println("\n█ Card Played █");
        System.out.println("» " + player.getName() + " played that card: " + card);
        System.out.println("» Number of cards in the trash pile: " + discardPile.size());
        System.out.println("» New active color: " + currentColor);
        System.out.println("» " + player.getName() + " number of cards remaining: " + player.getHand().size());

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

        // Eğer yeni turda ilk kart açılıyorsa veya mevcut oyuncu kart çekiyorsa işlem
        // yapılabilir
        if (isInitialDraw || getCurrentPlayer() == player) {
            Optional<Card> drawnCard = drawPile.drawCard();

            // Daha detaylı log ekleyelim
            System.out.println("\n█ Drawing cards from the deck... █");
            System.out.println("» Deck size before card is drawn: " + (drawPile.size() + 1));
            System.out.println("» Deck size after card is drawn: " + drawPile.size());

            // Çekme destesi boş ise, çöp destesini karıştırıp yeni çekme destesi yap
            if (!drawnCard.isPresent() && !discardPile.isEmpty()) {
                Card topCard = discardPile.remove(discardPile.size() - 1);
                drawPile.addCards(discardPile);
                discardPile.clear();
                discardPile.add(topCard);
                drawPile.shuffle();

                System.out.println("\n█ IMPORTANT: Trash deck has been converted to pull deck! █");
                System.out.println("» New pile size: " + drawPile.size());
                System.out.println("» Trash pile size: " + discardPile.size());

                drawnCard = drawPile.drawCard();
                System.out.println("» New deck size after card is drawn: " + drawPile.size());
            }

            if (drawnCard.isPresent()) {
                // Eğer oyuncu null değilse (özel durum değilse) kart ekle
                if (player != null) {
                    player.addCardToHand(drawnCard.get());
                    System.out.println("\n" + player.getName() + " drew a card: " + drawnCard.get());
                    System.out.println(
                            "» " + player.getName() + " the number of cards in your hand: " + player.getHand().size());
                }

                // Eğer normal oyun akışındaysak ve çekilen kart oynanabilirse oynat
                boolean canPlayDrawnCard = false;

                // Çekilen kartın oynanabilir olup olmadığını kontrol et
                if (!isInitialDraw) {
                    Card topCard = getTopCard();
                    Card drawnCardObj = drawnCard.get();

                    // Temel oynanabilirlik kurallarını kontrol et
                    if (drawnCardObj.isPlayable(topCard) || drawnCardObj.getColor() == currentColor) {
                        canPlayDrawnCard = true;
                    }
                    // Wild kartı sadece eldeki kartlarda topCard'ın rengiyle veya mevcut renkle
                    // eşleşen kart yoksa
                    // oynanabilir
                    else if (drawnCardObj.getType() == CardType.WILD) {
                        // Oyuncunun elinde eşleşen renkte başka kart var mı kontrol et
                        boolean hasMatchingColorCard = false;
                        for (Card handCard : player.getHand()) {
                            // Wild kartları hariç tut, sadece normal renk kartlarını kontrol et
                            if (handCard != drawnCardObj &&
                                    (handCard.getColor() == topCard.getColor() || handCard.getColor() == currentColor)
                                    &&
                                    handCard.getType() != CardType.WILD &&
                                    handCard.getType() != CardType.WILD_DRAW_FOUR) {
                                hasMatchingColorCard = true;
                                break;
                            }
                        }

                        // Eğer oyuncunun elinde eşleşen renkte kart yoksa Wild oynanabilir
                        if (!hasMatchingColorCard) {
                            canPlayDrawnCard = true;
                        }
                    }
                    // Wild Draw Four kartı özel duruma bağlı
                    else if (drawnCardObj.getType() == CardType.WILD_DRAW_FOUR) {
                        // Oyuncunun elinde eşleşen renkte başka kart var mı kontrol et
                        boolean hasMatchingColorCard = false;
                        for (Card handCard : player.getHand()) {
                            // Wild kartları hariç tut, sadece normal renk kartlarını kontrol et
                            if (handCard != drawnCardObj &&
                                    (handCard.getColor() == topCard.getColor() || handCard.getColor() == currentColor)
                                    &&
                                    handCard.getType() != CardType.WILD &&
                                    handCard.getType() != CardType.WILD_DRAW_FOUR) {
                                hasMatchingColorCard = true;
                                break;
                            }
                        }

                        // Eğer oyuncunun elinde eşleşen renkte kart yoksa Wild Draw Four oynanabilir
                        if (!hasMatchingColorCard) {
                            canPlayDrawnCard = true;
                        }
                    }
                }

                if (canPlayDrawnCard) {
                    // Burada oyuncunun kartı oynamak isteyip istemediği sorulabilir
                    // Ama şimdi rastgele bir karar verelim
                    boolean playCard = random.nextBoolean();

                    if (playCard) {
                        // Önemli: playCard metodu içindeki sıra kontrolünü geçmek için burada sıranın
                        // hala çeken oyuncuda olduğundan emin olalım
                        System.out.println("\n" + player.getName() + " plays the card he/she drew: " + drawnCard.get());
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
            throw new IllegalStateException("A player whose turn is not yet can't draw a card.");
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
                // Önce sırayı bir sonraki oyuncuya geçirelim, böylece kart çekme işlemi doğru
                // oyuncuya yapılabilir
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
                // Bir sonraki oyuncuyu atlamak için iki kez nextTurn() çağrılması gerekir
                nextTurn(); // Bir sonraki oyuncuya geç
                nextTurn(); // Bir sonraki oyuncuyu atla - yani ikinci oyuncuya geç
                break;

            case WILD:
                Color selectedColor = player.chooseColor();
                changeColor(selectedColor);
                System.out.println("\n█ Color Selection █");
                System.out.println("» " + player.getName() + " selected " + selectedColor + " as the new color!");
                System.out.println("» All players must now play " + selectedColor + " cards or matching card types.");
                break;

            case WILD_DRAW_FOUR:
                nextPlayer = getNextPlayer();
                Color selectedColorWild4 = player.chooseColor();
                changeColor(selectedColorWild4);
                System.out.println("\n█ Color Selection █");
                System.out.println("» " + player.getName() + " selected " + selectedColorWild4 + " as the new color!");
                System.out.println(
                        "» All players must now play " + selectedColorWild4 + " cards or matching card types.");
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
                Color selectedColorShuffle = player.chooseColor();
                changeColor(selectedColorShuffle);
                System.out.println("\n█ Color Selection █");
                System.out
                        .println("» " + player.getName() + " selected " + selectedColorShuffle + " as the new color!");
                System.out.println(
                        "» All players must now play " + selectedColorShuffle + " cards or matching card types.");
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
        System.out.println("\n=========== END OF ROUND ===========");

        // Kazanan oyuncuya, diğer oyuncuların ellerinde kalan kartların değerini ekle
        int roundPoints = 0;
        for (Player player : players) {
            if (player != winner) {
                int playerHandValue = player.calculateHandValue();
                roundPoints += playerHandValue;
                System.out.println(player.getName() + " value of cards remaining in hand: " + playerHandValue);
            }
        }

        // Puanları ekle
        winner.addToScore(roundPoints);

        System.out.println("\n*** " + winner.getName() + " won the round and " + roundPoints + " earned points! ***\n");
        System.out.println("Total score: " + winner.getTotalScore());

        // Oyun durumunu kaydet
        System.out.println("\nSaving game state...");
        saveGameState();

        // Kazanan oyuncu 500 puana ulaştıysa oyun biter
        if (winner.getTotalScore() >= WINNING_SCORE) {
            gameOver = true;
            System.out.println(winner.getName() + " " + winner.getTotalScore() + " won the game with points!");
        } else {
            // Değilse yeni bir tur başlat
            System.out.println("New tour begins...\n");
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

        // Konsola tur sonucunu yazdır
        System.out.println("Round " + (gameRepository.getRoundCount()) + " results were recorded.");
        System.out.println("Player Scores:");
        for (Player player : players) {
            System.out.println(player.getName() + ": " + player.getTotalScore());
        }
        System.out.println();
    }

    @Override
    public int getRemainingCardCount() {
        return drawPile.size();
    }

    @Override
    public int getDiscardPileCount() {
        return discardPile.size();
    }

    @Override
    public Color getCurrentColor() {
        return currentColor;
    }
}