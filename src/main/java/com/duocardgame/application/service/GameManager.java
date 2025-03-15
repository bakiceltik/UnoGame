package com.duocardgame.application.service;

import com.duocardgame.domain.mediator.GameMediator;
import com.duocardgame.domain.mediator.DuoGameMediator;
import com.duocardgame.domain.model.Card;
import com.duocardgame.domain.model.Player;
import com.duocardgame.dataaccess.repository.GameRepository;
import com.duocardgame.dataaccess.repository.CSVGameRepository;

import java.util.List;

/**
 * GameManager, oyun akışını yöneten ve Domain Layer ile iletişim kuran sınıftır.
 * Application Layer'ın ana bileşenidir.
 */
public class GameManager {
    private final GameMediator gameMediator;
    private final String gameStatusMessage;
    
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
    }
    
    /**
     * Oyun döngüsünü çalıştırır. Oyun bitene kadar döngüyü sürdürür.
     * 
     * @return Kazanan oyuncu
     */
    public Player runGameLoop() {
        while (!gameMediator.isGameOver()) {
            playTurn();
        }
        
        return getWinner();
    }
    
    /**
     * Mevcut oyuncunun turunu oynar.
     */
    public void playTurn() {
        Player currentPlayer = gameMediator.getCurrentPlayer();
        Card topCard = gameMediator.getTopCard();
        
        // Mevcut oyuncu kart oynar
        if (topCard != null) {
            java.util.Optional<Card> playedCard = currentPlayer.playCard(topCard);
            
            if (playedCard.isPresent()) {
                gameMediator.playCard(currentPlayer, playedCard.get());
            } else {
                // Eğer oynanabilir kart yoksa, desteden kart çek
                gameMediator.drawCard(currentPlayer);
            }
        }
    }
    
    /**
     * Oyun durumunu döndürür.
     * 
     * @return Oyun durumu
     */
    public String getGameStatus() {
        if (gameMediator.isGameOver()) {
            return "Oyun bitti! Kazanan: " + getWinner().getName();
        }
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
} 