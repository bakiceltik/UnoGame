package com.duocardgame.domain.mediator;

import com.duocardgame.domain.model.Card;
import com.duocardgame.domain.model.Color;
import com.duocardgame.domain.model.Player;

import java.util.List;

public interface GameMediator {
    void startGame();
    void startNewRound();
    void playCard(Player player, Card card);
    void drawCard(Player player);
    void changeColor(Color newColor);
    void applyCardEffect(Player player, Card card);
    void nextTurn();
    boolean isGameOver();
    boolean isRoundOver();
    Player getCurrentPlayer();
    Card getTopCard();
    List<Player> getPlayers();
    Player getDealerPlayer();
    void saveGameState();
    int getRemainingCardCount();
    int getDiscardPileCount();
} 