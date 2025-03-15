package com.duocardgame.application.service;

import com.duocardgame.domain.mediator.GameMediator;
import com.duocardgame.domain.model.Player;

/**
 * TurnManager, oyuncu sırası ve tur işlemlerini yöneten sınıftır.
 */
public class TurnManager {
    private final GameMediator gameMediator;
    
    /**
     * GameMediator ile bir TurnManager oluşturur.
     * 
     * @param gameMediator Oyun mediatörü
     */
    public TurnManager(GameMediator gameMediator) {
        this.gameMediator = gameMediator;
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
     * Bir sonraki tura geçer.
     */
    public void nextTurn() {
        gameMediator.nextTurn();
    }
    
    /**
     * Mevcut turun yönünü değiştirir.
     */
    public void reverseDirection() {
        // Bu işlem şu anda mediator içerisinde kart etkisiyle yönetiliyor
        // Gerekirse burada ayrıca bir metot eklenebilir
    }
    
    /**
     * Mevcut turu atlar.
     */
    public void skipTurn() {
        gameMediator.nextTurn();
    }
    
    /**
     * Oyun kurucusunu (dealer) döndürür.
     * 
     * @return Oyun kurucusu
     */
    public Player getDealerPlayer() {
        return gameMediator.getDealerPlayer();
    }
} 