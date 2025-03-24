package com.duocardgame.application.service;

import com.duocardgame.application.mediator.GameMediator;
import com.duocardgame.domain.model.Card;
import com.duocardgame.domain.model.Color;
import com.duocardgame.domain.model.Player;

import java.util.List;
import java.util.Optional;

/**
 * GameService, oyun iş mantığı işlemlerini sağlayan servis sınıfıdır.
 */
public class GameService {
    private final GameMediator gameMediator;
    private final TurnManager turnManager;

    /**
     * GameMediator ile bir GameService oluşturur.
     * 
     * @param gameMediator Oyun mediatörü
     */
    public GameService(GameMediator gameMediator) {
        this.gameMediator = gameMediator;
        this.turnManager = new TurnManager(gameMediator);
    }

    /**
     * Bir kartı oynamayı dener.
     * 
     * @param player Kartı oynayan oyuncu
     * @param card   Oynanacak kart
     * @return İşlem başarılıysa true, aksi halde false
     */
    public boolean playCard(Player player, Card card) {
        try {
            gameMediator.playCard(player, card);
            return true;
        } catch (IllegalArgumentException | IllegalStateException e) {
            return false;
        }
    }

    /**
     * Oyuncunun desteden kart çekmesini sağlar.
     * 
     * @param player Kart çeken oyuncu
     */
    public void drawCard(Player player) {
        gameMediator.drawCard(player);
    }

    /**
     * Renk değiştirir.
     * 
     * @param newColor Yeni renk
     */
    public void changeColor(Color newColor) {
        gameMediator.changeColor(newColor);
    }

    /**
     * Tur sırasını değiştirir.
     */
    public void nextTurn() {
        turnManager.nextTurn();
    }

    /**
     * Turun atlanmasını sağlar.
     */
    public void skipTurn() {
        turnManager.skipTurn();
    }

    /**
     * Mevcut oyuncuyu döndürür.
     * 
     * @return Mevcut oyuncu
     */
    public Player getCurrentPlayer() {
        return turnManager.getCurrentPlayer();
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
     * Oyuncuların listesini döndürür.
     * 
     * @return Oyuncular listesi
     */
    public List<Player> getPlayers() {
        return gameMediator.getPlayers();
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
     * Turun bitip bitmediğini kontrol eder.
     * 
     * @return Tur bittiyse true, aksi halde false
     */
    public boolean isRoundOver() {
        return gameMediator.isRoundOver();
    }

    /**
     * Oyuncunun oynayabileceği kartları döndürür.
     * 
     * @param player Oyuncu
     * @return Oynanabilir kartlar listesi
     */
    public List<Card> getPlayableCards(Player player) {
        return player.getPlayableCards(getTopCard(), gameMediator.getCurrentColor());
    }

    /**
     * Mevcut aktif rengi döndürür.
     * 
     * @return Mevcut aktif renk
     */
    public Color getCurrentColor() {
        return gameMediator.getCurrentColor();
    }

    /**
     * Oyun durumunu kaydeder.
     */
    public void saveGameState() {
        gameMediator.saveGameState();
    }
}