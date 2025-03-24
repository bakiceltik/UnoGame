package com.duocardgame.application.mediator;

import com.duocardgame.domain.model.Card;
import com.duocardgame.domain.model.CardType;
import com.duocardgame.domain.model.Color;
import com.duocardgame.domain.model.Deck;
import com.duocardgame.domain.model.Player;
import com.duocardgame.dataaccess.repository.GameRepository;
import com.duocardgame.application.handler.CardEffectHandler;
import com.duocardgame.application.manager.DeckManager;
import com.duocardgame.application.manager.GameStateManager;
import com.duocardgame.application.manager.PlayerTurnManager;
import com.duocardgame.application.validator.GameRuleEnforcer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class DuoGameMediator implements GameMediator {
    private final List<Player> players;
    private final Random random;
    private final GameRepository gameRepository;
    private boolean gameOver;
    private static final int WINNING_SCORE = 500;
    private static final int INITIAL_CARDS = 7;

    private final DeckManager deckManager;
    private final PlayerTurnManager turnManager;
    private final CardEffectHandler effectHandler;
    private final GameStateManager stateManager;
    private final GameRuleEnforcer ruleEnforcer;

    public DuoGameMediator(GameRepository gameRepository) {
        this.players = new ArrayList<>();
        this.random = new Random();
        this.gameRepository = gameRepository;
        this.gameOver = false;
        
        this.deckManager = new DeckManager();
        this.turnManager = new PlayerTurnManager();
        this.effectHandler = new CardEffectHandler(this);
        this.stateManager = new GameStateManager(gameRepository);
        this.ruleEnforcer = new GameRuleEnforcer();
    }

    @Override
    public void startGame() {
        int playerCount = random.nextInt(3) + 2;

        for (int i = 1; i <= playerCount; i++) {
            players.add(new Player("Player" + i, 
                                   new com.duocardgame.domain.strategy.RandomPlayStrategy(), 
                                   new com.duocardgame.domain.strategy.FrequencyColorChooser()));
        }

        startNewRound();
    }

    @Override
    public void startNewRound() {
        for (Player player : players) {
            player.clearHand();
        }

        deckManager.resetDecks();
        
        turnManager.determineFirstDealer(players, deckManager);
        
        deckManager.dealInitialCards(players, turnManager.getDealerIndex(), INITIAL_CARDS);
        
        Card firstCard = deckManager.drawFirstCard();
        if (firstCard.getType() != CardType.NUMBER) {
            effectHandler.handleFirstActionCard(firstCard, players, turnManager);
        }
        
        turnManager.setCurrentPlayerIndex((turnManager.getDealerIndex() + 1) % players.size());
    }

    @Override
    public void playCard(Player player, Card card) {
        if (getCurrentPlayer() != player) {
            throw new IllegalStateException("A player whose turn has not come cannot play a card.");
        }

        Card topCard = getTopCard();
        if (!ruleEnforcer.isCardPlayable(player, card, topCard, deckManager.getCurrentColor())) {
            throw new IllegalArgumentException("This card is not compatible");
        }
        
        player.removeCardFromHand(card);
        deckManager.addToDiscardPile(card);
        
        if (card.getColor() != Color.WILD) {
            deckManager.setCurrentColor(card.getColor());
            System.out.println("\n█ Color Update █");
            System.out.println("» New active color is now: " + deckManager.getCurrentColor());
        }

        System.out.println("\n█ Card Played █");
        System.out.println("» " + player.getName() + " played that card: " + card);
        System.out.println("» Number of cards in the trash pile: " + deckManager.getDiscardPileCount());
        System.out.println("» New active color: " + deckManager.getCurrentColor());
        System.out.println("» " + player.getName() + " number of cards remaining: " + player.getHand().size());

        effectHandler.applyCardEffect(player, card);

        if (isRoundOver()) {
            endRound(player);
        } else if (card.getType() != CardType.SKIP &&
                card.getType() != CardType.DRAW_TWO &&
                card.getType() != CardType.WILD_DRAW_FOUR) {
            turnManager.nextTurn(players.size());
        }
    }

    @Override
    public void drawCard(Player player) {
        boolean isInitialDraw = turnManager.getCurrentPlayerIndex() == -1 || player == null || getCurrentPlayer() == null;

        if (isInitialDraw || getCurrentPlayer() == player) {
            Optional<Card> drawnCard = deckManager.drawCard();

            System.out.println("\n█ Drawing cards from the deck... █");
            System.out.println("» Deck size before card is drawn: " + (deckManager.getRemainingCardCount() + 1));
            System.out.println("» Deck size after card is drawn: " + deckManager.getRemainingCardCount());

            if (drawnCard.isPresent()) {
                if (player != null) {
                    player.addCardToHand(drawnCard.get());
                    System.out.println("\n" + player.getName() + " drew a card: " + drawnCard.get());
                    System.out.println("» " + player.getName() + " the number of cards in your hand: " + player.getHand().size());
                }

                boolean canPlayDrawnCard = false;
                if (!isInitialDraw) {
                    canPlayDrawnCard = ruleEnforcer.canPlayDrawnCard(player, drawnCard.get(), getTopCard(), deckManager.getCurrentColor());
                }

                if (canPlayDrawnCard) {
                    boolean playCard = random.nextBoolean();

                    if (playCard) {
                        System.out.println("\n" + player.getName() + " plays the card he/she drew: " + drawnCard.get());
                        playCard(player, drawnCard.get());
                        return;
                    }
                }

                if (!isInitialDraw) {
                    turnManager.nextTurn(players.size());
                }

                return;
            }
        } else {
            throw new IllegalStateException("A player whose turn is not yet can't draw a card.");
        }
    }

    @Override
    public void changeColor(Color newColor) {
        deckManager.setCurrentColor(newColor);
    }

    @Override
    public void applyCardEffect(Player player, Card card) {
        effectHandler.applyCardEffect(player, card);
    }

    private void endRound(Player winner) {
        System.out.println("\n=========== END OF ROUND ===========");

        int roundPoints = 0;
        for (Player player : players) {
            if (player != winner) {
                int playerHandValue = player.calculateHandValue();
                roundPoints += playerHandValue;
                System.out.println(player.getName() + " value of cards remaining in hand: " + playerHandValue);
            }
        }

        winner.addToScore(roundPoints);

        System.out.println("\n*** " + winner.getName() + " won the round and " + roundPoints + " earned points! ***\n");
        System.out.println("Total score: " + winner.getTotalScore());

        System.out.println("\nSaving game state...");
        stateManager.saveGameState(players);

        if (winner.getTotalScore() >= WINNING_SCORE) {
            gameOver = true;
            System.out.println(winner.getName() + " " + winner.getTotalScore() + " won the game with points!");
        } else {
            System.out.println("New tour begins...\n");
            startNewRound();
        }
    }

    @Override
    public void nextTurn() {
        turnManager.nextTurn(players.size());
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
        return players.get(turnManager.getCurrentPlayerIndex());
    }

    @Override
    public Card getTopCard() {
        return deckManager.getTopCard();
    }

    @Override
    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    @Override
    public Player getDealerPlayer() {
        return players.get(turnManager.getDealerIndex());
    }

    @Override
    public void saveGameState() {
        stateManager.saveGameState(players);
    }

    @Override
    public int getRemainingCardCount() {
        return deckManager.getRemainingCardCount();
    }

    @Override
    public int getDiscardPileCount() {
        return deckManager.getDiscardPileCount();
    }

    @Override
    public Color getCurrentColor() {
        return deckManager.getCurrentColor();
    }

    public Optional<Card> getDrawPileCard() {
        return deckManager.drawCard();
    }
}