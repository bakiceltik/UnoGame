package com.duocardgame.application.service;

import com.duocardgame.application.mediator.GameMediator;
import com.duocardgame.domain.model.Card;
import com.duocardgame.domain.model.Color;
import com.duocardgame.domain.model.Player;

import java.util.List;
import java.util.Optional;


public class GameService {
    private final GameMediator gameMediator;
    private final TurnManager turnManager;

    public GameService(GameMediator gameMediator) {
        this.gameMediator = gameMediator;
        this.turnManager = new TurnManager(gameMediator);
    }

    public boolean playCard(Player player, Card card) {
        try {
            gameMediator.playCard(player, card);
            return true;
        } catch (IllegalArgumentException | IllegalStateException e) {
            return false;
        }
    }

 
    public void drawCard(Player player) {
        gameMediator.drawCard(player);
    }


    public void changeColor(Color newColor) {
        gameMediator.changeColor(newColor);
    }

            
    public void nextTurn() {
        turnManager.nextTurn();
    }

    public void skipTurn() {
        turnManager.skipTurn();
    }

    
    public Player getCurrentPlayer() {
        return turnManager.getCurrentPlayer();
    }

    
    public Card getTopCard() {
        return gameMediator.getTopCard();
    }


    public List<Player> getPlayers() {
        return gameMediator.getPlayers();
    }


    public boolean isGameOver() {
        return gameMediator.isGameOver();
    }


    public boolean isRoundOver() {
        return gameMediator.isRoundOver();
    }


    public List<Card> getPlayableCards(Player player) {
        return player.getPlayableCards(getTopCard(), gameMediator.getCurrentColor(), new com.duocardgame.domain.rules.StandardPlayRules());
    }


    public Color getCurrentColor() {
        return gameMediator.getCurrentColor();
    }


    public void saveGameState() {
        gameMediator.saveGameState();
    }
}