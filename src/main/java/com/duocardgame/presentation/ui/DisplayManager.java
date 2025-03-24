package com.duocardgame.presentation.ui;

import com.duocardgame.application.service.GameManager;
import com.duocardgame.domain.model.Card;
import com.duocardgame.domain.model.Player;

import java.util.List;


public class DisplayManager {
    private final GameManager gameManager;
    private final ConsoleFormatter formatter;
    

    public DisplayManager(GameManager gameManager) {
        this.gameManager = gameManager;
        this.formatter = new ConsoleFormatter();
    }
    

    public void displayGameState() {
        System.out.println(formatter.formatSection("A NEW MOVE IS STARTING", ConsoleFormatter.ANSI_CYAN));
        System.out.println();

        Card topCard = gameManager.getTopCard();
        System.out.println(formatter.formatHeader("GAME STATE"));

        if (topCard != null) {
            System.out.println("Top card: " + formatter.formatCard(topCard));
            System.out.println("Current active color: " + formatter.formatColor(gameManager.getCurrentColor()));
        } else {
            System.out.println("No card has been opened yet.");
        }

        System.out.println("Number of cards remaining in the deck: " + ConsoleFormatter.ANSI_BOLD + 
                gameManager.getRemainingCardCount() + ConsoleFormatter.ANSI_RESET);
        System.out.println("Number of cards in the trash pile: " + ConsoleFormatter.ANSI_BOLD + 
                gameManager.getDiscardPileCount() + ConsoleFormatter.ANSI_RESET);

        System.out.println(formatter.formatHeader("PLAYERS"));

        List<Player> players = gameManager.getPlayers();
        Player currentPlayer = gameManager.getCurrentPlayer();

        for (Player player : players) {
            StringBuilder playerInfo = new StringBuilder();

            if (player == currentPlayer) {
                playerInfo.append(ConsoleFormatter.ANSI_YELLOW + ConsoleFormatter.ANSI_BOLD + "► " + 
                        ConsoleFormatter.ANSI_RESET);
            } else {
                playerInfo.append("  ");
            }

            playerInfo.append(player == currentPlayer ? ConsoleFormatter.ANSI_BOLD : "")
                    .append(player.getName())
                    .append(" (Score: ").append(player.getTotalScore()).append(") - ")
                    .append("You have ").append(player.getHandSize()).append(" cards in your hand.")
                    .append(player == currentPlayer ? ConsoleFormatter.ANSI_RESET : "");

            System.out.println(playerInfo);

            // show current player's cards
            if (player == currentPlayer && player.getHandSize() > 0) {
                System.out.println("  Cards: ");
                List<Card> hand = player.getHand();
                int cardsPerLine = 4;

                for (int i = 0; i < hand.size(); i++) {
                    StringBuilder cardLine = new StringBuilder();
                    cardLine.append("    ");

                
                    for (int j = 0; j < cardsPerLine && i + j < hand.size(); j++) {
                        cardLine.append((i + j + 1)).append(". ").append(formatter.formatCard(hand.get(i + j)));
                        cardLine.append("   ");
                    }

                    System.out.println(cardLine.toString());
                    i += cardsPerLine - 1;
                }
            }
        }

        System.out.println("\n" + ConsoleFormatter.ANSI_YELLOW + gameManager.getGameStatus() + 
                ConsoleFormatter.ANSI_RESET);
    }
    
    
    public void displayScores() {
        System.out.println(formatter.formatHeader("SCOR TABLE"));

        List<Player> players = gameManager.getPlayers();

        for (Player player : players) {
            System.out.println(player.getName() + ": " + player.getTotalScore() + " point");
        }

        System.out.println(
                "\nNumber of cards remaining: " + ConsoleFormatter.ANSI_BOLD + 
                gameManager.getRemainingCardCount() + ConsoleFormatter.ANSI_RESET);
        System.out.println(
                "Number of cards in the trash pile: " + ConsoleFormatter.ANSI_BOLD + 
                gameManager.getDiscardPileCount() + ConsoleFormatter.ANSI_RESET);

        System.out.println(); 
    }
    

    public void displayRoundResults(int roundNumber) {
        System.out.println(formatter.formatHeader("ROUND " + roundNumber + " RESULTS"));

        List<Player> players = gameManager.getPlayers();

        Player roundWinner = null;
        for (Player player : players) {
            if (player.hasEmptyHand()) {
                roundWinner = player;
                break;
            }
        }

        if (roundWinner != null) {
            System.out.println(
                    "\n" + ConsoleFormatter.ANSI_GREEN + ConsoleFormatter.ANSI_BOLD + 
                    "★★★ ROUND WİNNER: " + roundWinner.getName() + " ★★★" + ConsoleFormatter.ANSI_RESET);
        }

        System.out.println("\n" + ConsoleFormatter.ANSI_CYAN + "Score Table:" + ConsoleFormatter.ANSI_RESET);
        for (Player player : players) {
            String marker = (player == roundWinner) ? " ⭐" : "";
            System.out.println(ConsoleFormatter.ANSI_BOLD + player.getName() + ConsoleFormatter.ANSI_RESET + ": " +
                    player.getTotalScore() + " point" + marker);
        }

        System.out.println(formatter.formatHeader("PRESS ENTER TO CONTINUE"));

        System.out.println("\n" + ConsoleFormatter.ANSI_PURPLE + ConsoleFormatter.ANSI_BOLD);
        System.out.println("********************************************************");
        System.out.println("*                    NEW ROUND                        *");
        System.out.println("********************************************************" + ConsoleFormatter.ANSI_RESET);
    }
    

    public void displayGameResults() {
        System.out.println(formatter.formatHeader("GAME RESULTS"));

        Player winner = findWinner();
        System.out.println(ConsoleFormatter.ANSI_YELLOW + ConsoleFormatter.ANSI_BOLD + 
                "WİNNER: " + winner.getName() + " (Total score: " + winner.getTotalScore() + ")" + 
                ConsoleFormatter.ANSI_RESET);

        System.out.println("\nAll Players:");
        List<Player> players = gameManager.getPlayers();
        for (Player player : players) {
            if (player == winner) {
                System.out.println(ConsoleFormatter.ANSI_GREEN + "  " + player.getName() + ": " + 
                        player.getTotalScore() + " points" + ConsoleFormatter.ANSI_RESET + " 🏆");
            } else {
                System.out.println("  " + player.getName() + ": " + player.getTotalScore() + " points");
            }
        }

        System.out.println("\n" + ConsoleFormatter.ANSI_CYAN + 
                "The game is over. Restart the program to play again." + ConsoleFormatter.ANSI_RESET);

        System.out.println("\nGame Statistics:");
        System.out.println("  Total Number of Players: " + players.size());
        System.out.println("  Wineer Player: " + winner.getName());
        System.out.println("  Total Number of Rounds: " + gameManager.getGameHistory().size());
    }
    
    
    private Player findWinner() {
        List<Player> players = gameManager.getPlayers();
        Player winner = players.get(0);

        for (Player player : players) {
            if (player.getTotalScore() > winner.getTotalScore()) {
                winner = player;
            }
        }

        return winner;
    }
} 