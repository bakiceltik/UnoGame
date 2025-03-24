package com.duocardgame.application.service;

import com.duocardgame.application.mediator.GameMediator;
import com.duocardgame.domain.model.Player;


public class TurnManager {
    private final GameMediator gameMediator;

    public TurnManager(GameMediator gameMediator) {
        this.gameMediator = gameMediator;
    }
    

    public Player getCurrentPlayer() {
        return gameMediator.getCurrentPlayer();
    }
    
    
    public void nextTurn() {
        gameMediator.nextTurn();
    }

    public void reverseDirection() {

    }
    public void skipTurn() {
        gameMediator.nextTurn();
    }
    
    public Player getDealerPlayer() {
        return gameMediator.getDealerPlayer();
    }
} 