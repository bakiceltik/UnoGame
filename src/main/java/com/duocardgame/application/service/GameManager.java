package com.duocardgame.application.service;

import com.duocardgame.application.mediator.GameMediator;
import com.duocardgame.application.mediator.DuoGameMediator;
import com.duocardgame.domain.model.Card;
import com.duocardgame.domain.model.CardType;
import com.duocardgame.domain.model.Color;
import com.duocardgame.domain.model.Player;
import com.duocardgame.dataaccess.repository.GameRepository;
import com.duocardgame.dataaccess.repository.CSVGameRepository;

import java.util.List;
import java.util.Optional;


public class GameManager {
    private final GameMediator gameMediator;
    private String gameStatusMessage;
    private final GameRepository gameRepository;

  
    public GameManager(String csvFilePath) {
        this.gameRepository = new CSVGameRepository(csvFilePath);
        this.gameMediator = new DuoGameMediator(gameRepository);
        this.gameStatusMessage = "Game is running...";
    }

   
    public void startGame() {
        gameMediator.startGame();
        updateGameStatus();
    }

    public Player runGameLoop() {
        while (!gameMediator.isGameOver()) {
            playTurn();
            updateGameStatus();
        }

        return getWinner();
    }

    
    private void updateGameStatus() {
        if (gameMediator.isGameOver()) {
            gameStatusMessage = "Game over! Winner: " + getWinner().getName();
        } else if (gameMediator.isRoundOver()) {
            gameStatusMessage = "Round over! New round starting...";
        } else {
            gameStatusMessage = "Game is running...";
        }
    }

  
    public void playTurn() {
        Player currentPlayer = gameMediator.getCurrentPlayer();
        Card topCard = gameMediator.getTopCard();
        Color currentColor = gameMediator.getCurrentColor();

        // if top card is null, throw exception
        if (topCard == null) {
            throw new IllegalStateException("Top card not found");
        }

        // get playable cards for current player
        List<Card> playableCards = currentPlayer.getPlayableCards(topCard, currentColor, new com.duocardgame.domain.rules.StandardPlayRules());

        if (!playableCards.isEmpty()) {
            // if there is a playable card, let the ai play it
            Optional<Card> aiDecision = currentPlayer.playCard(topCard, currentColor, new com.duocardgame.domain.rules.StandardPlayRules());

            if (aiDecision.isPresent()) {
                gameMediator.playCard(currentPlayer, aiDecision.get());
            } else {
                throw new IllegalStateException("AI did not play a card, but there is a playable card");
            }
        } else {
            gameMediator.drawCard(currentPlayer);
        }

        updateGameStatus();
    }

    public boolean playSelectedCard(int cardIndex) {
        Player currentPlayer = gameMediator.getCurrentPlayer();
        Card topCard = gameMediator.getTopCard();
        Color currentColor = gameMediator.getCurrentColor();

        if (cardIndex < 0 || cardIndex >= currentPlayer.getHandSize()) {
            return false;
        }

        List<Card> hand = currentPlayer.getHand();
        Card selectedCard = hand.get(cardIndex);

        if (!selectedCard.isPlayable(topCard) && selectedCard.getColor() != currentColor &&
                !(selectedCard.getType() == CardType.WILD || selectedCard.getType() == CardType.WILD_DRAW_FOUR ||
                        selectedCard.getType() == CardType.SHUFFLE_HANDS)) {
            return false;
        }


        gameMediator.playCard(currentPlayer, selectedCard);
        updateGameStatus();
        return true;
    }

   
    public boolean drawCardForCurrentPlayer() {
        Player currentPlayer = gameMediator.getCurrentPlayer();
        gameMediator.drawCard(currentPlayer);
        updateGameStatus();
        return true;
    }

   
    public void selectColor(Color newColor) {
        gameMediator.changeColor(newColor);
        updateGameStatus();
    }

   
    public String getGameStatus() {
        return gameStatusMessage;
    }

   
    public List<Player> getPlayers() {
        return gameMediator.getPlayers();
    }

    
    public Player getCurrentPlayer() {
        return gameMediator.getCurrentPlayer();
    }

    
    public Player getDealerPlayer() {
        return gameMediator.getDealerPlayer();
    }

   
    public Card getTopCard() {
        return gameMediator.getTopCard();
    }

   
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

 
    public boolean isGameOver() {
        return gameMediator.isGameOver();
    }

 
    public boolean isRoundOver() {
        return gameMediator.isRoundOver();
    }

  
    public int getRemainingCardCount() {
        return gameMediator.getRemainingCardCount();
    }


    public int getDiscardPileCount() {
        return gameMediator.getDiscardPileCount();
    }

    public List<String[]> getGameHistory() {
        return gameRepository.readAllGameStates();
    }


    public Color getCurrentColor() {
        return gameMediator.getCurrentColor();
    }
}