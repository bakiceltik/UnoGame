package com.duocardgame.application.service;

import com.duocardgame.domain.mediator.GameMediator;
import com.duocardgame.domain.mediator.DuoGameMediator;
import com.duocardgame.domain.model.Card;
import com.duocardgame.domain.model.Color;
import com.duocardgame.domain.model.Player;
import com.duocardgame.dataaccess.repository.GameRepository;
import com.duocardgame.dataaccess.repository.CSVGameRepository;

import java.util.List;
import java.util.Optional;

/**
 * GameManager, oyun akışını yöneten ve Domain Layer ile iletişim kuran sınıftır.
 * Application Layer'ın ana bileşenidir.
 */
public class GameManager {
    private final GameMediator gameMediator;
    private String gameStatusMessage;
    
    /**
     * CSV dosya yolu ile bir GameManager oluşturur.
     * 
     * @param csvFilePath CSV dosyasının yolu
     */
    public GameManager(String csvFilePath) {
        GameRepository repository = new CSVGameRepository(csvFilePath);
        this.gameMediator = new DuoGameMediator(repository);
        this.gameStatusMessage = "Oyun devam ediyor...";
    }
    
    /**
     * Oyunu başlatır.
     */
    public void startGame() {
        gameMediator.startGame();
        updateGameStatus();
    }
    
    /**
     * Oyun döngüsünü çalıştırır. Oyun bitene kadar döngüyü sürdürür.
     * 
     * @return Kazanan oyuncu
     */
    public Player runGameLoop() {
        while (!gameMediator.isGameOver()) {
            playTurn();
            updateGameStatus();
        }
        
        return getWinner();
    }
    
    /**
     * Oyun durumunu günceller
     */
    private void updateGameStatus() {
        if (gameMediator.isGameOver()) {
            gameStatusMessage = "Oyun bitti! Kazanan: " + getWinner().getName();
        } else if (gameMediator.isRoundOver()) {
            gameStatusMessage = "Tur bitti! Yeni tur başlıyor...";
        } else {
            gameStatusMessage = "Oyun devam ediyor...";
        }
    }
    
    /**
     * Mevcut oyuncunun turunu oynar.
     */
    public void playTurn() {
        Player currentPlayer = gameMediator.getCurrentPlayer();
        Card topCard = gameMediator.getTopCard();
        
        // Eğer üst kart yoksa, hatalı durum kontrolü
        if (topCard == null) {
            throw new IllegalStateException("Üst kart bulunamadı");
        }
        
        // Mevcut oyuncunun oynayabileceği kartları bul
        List<Card> playableCards = currentPlayer.getPlayableCards(topCard);
        
        if (!playableCards.isEmpty()) {
            // Oynanabilir kart varsa, yapay zekaya kartı oynat
            Optional<Card> aiDecision = currentPlayer.playCard(topCard);
            
            if (aiDecision.isPresent()) {
                gameMediator.playCard(currentPlayer, aiDecision.get());
            } else {
                // Yapay zekadan cevap gelmediğinde yazılım hatası
                throw new IllegalStateException("Yapay zeka kart oynamadı, ancak oynanabilir kart var");
            }
        } else {
            // Eğer oynanabilir kart yoksa, desteden kart çek
            gameMediator.drawCard(currentPlayer);
        }
        
        updateGameStatus();
    }
    
    /**
     * Oyuncunun seçilen kartını oynar.
     * 
     * @param cardIndex Oynanacak kartın indeksi
     * @return İşlem başarılı olduysa true, aksi halde false
     */
    public boolean playSelectedCard(int cardIndex) {
        Player currentPlayer = gameMediator.getCurrentPlayer();
        Card topCard = gameMediator.getTopCard();
        
        // İndeks kontrolü
        if (cardIndex < 0 || cardIndex >= currentPlayer.getHandSize()) {
            return false;
        }
        
        // Seçilen kartı al
        List<Card> hand = currentPlayer.getHand();
        Card selectedCard = hand.get(cardIndex);
        
        // Kart oynanabilir mi kontrol et
        if (!selectedCard.isPlayable(topCard)) {
            return false;
        }
        
        // Kartı oyna
        gameMediator.playCard(currentPlayer, selectedCard);
        updateGameStatus();
        return true;
    }
    
    /**
     * Oyuncuya kart çektirir.
     * 
     * @return İşlem başarılı olduysa true, aksi halde false
     */
    public boolean drawCardForCurrentPlayer() {
        Player currentPlayer = gameMediator.getCurrentPlayer();
        gameMediator.drawCard(currentPlayer);
        updateGameStatus();
        return true;
    }
    
    /**
     * Wild kartlardan sonra yeni renk seçer
     *
     * @param newColor Seçilen yeni renk
     */
    public void selectColor(Color newColor) {
        gameMediator.changeColor(newColor);
        updateGameStatus();
    }
    
    /**
     * Oyun durumunu döndürür.
     * 
     * @return Oyun durumu
     */
    public String getGameStatus() {
        return gameStatusMessage;
    }
    
    /**
     * Oyuncuların listesini döndürür.
     * 
     * @return Oyuncular listesi
     */
    public List<Player> getPlayers() {
        return gameMediator.getPlayers();
    }
    
    /**
     * Mevcut oyuncuyu döndürür.
     * 
     * @return Mevcut oyuncu
     */
    public Player getCurrentPlayer() {
        return gameMediator.getCurrentPlayer();
    }
    
    /**
     * Dağıtıcı oyuncuyu döndürür.
     * 
     * @return Dağıtıcı oyuncu
     */
    public Player getDealerPlayer() {
        return gameMediator.getDealerPlayer();
    }
    
    /**
     * Üst kartı döndürür.
     * 
     * @return Üst kart
     */
    public Card getTopCard() {
        return gameMediator.getTopCard();
    }
    
    /**
     * Kazanan oyuncuyu bulur.
     * 
     * @return Kazanan oyuncu
     */
    private Player getWinner() {
        Player winner = null;
        int maxPoints = 0;
        
        for (Player player : gameMediator.getPlayers()) {
            if (player.getTotalScore() > maxPoints) {
                maxPoints = player.getTotalScore();
                winner = player;
            }
        }
        
        return winner;
    }
    
    /**
     * Oyunun bitip bitmediğini kontrol eder.
     * 
     * @return Oyun bittiyse true, aksi halde false
     */
    public boolean isGameOver() {
        return gameMediator.isGameOver();
    }
    
    /**
     * Mevcut turun bitip bitmediğini kontrol eder.
     * 
     * @return Tur bittiyse true, aksi halde false
     */
    public boolean isRoundOver() {
        return gameMediator.isRoundOver();
    }
} 