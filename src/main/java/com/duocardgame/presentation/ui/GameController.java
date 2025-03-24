package com.duocardgame.presentation.ui;

import com.duocardgame.application.service.GameManager;
import com.duocardgame.domain.model.Player;

//Main controller class
public class GameController {
    private final GameManager gameManager;
    private final DisplayManager displayManager;
    private final UserInteractionManager interactionManager;
    private static final int DELAY_MS = 1;     
    
    //Constructor
    public GameController(GameManager gameManager) {
        this.gameManager = gameManager;
        this.displayManager = new DisplayManager(gameManager);
        this.interactionManager = new UserInteractionManager();
    }

    public void startGame() {
        interactionManager.showWelcomeMessage();

        System.out.println(ConsoleFormatter.ANSI_CYAN + "Game starting...\n" + ConsoleFormatter.ANSI_RESET);
        interactionManager.sleep(DELAY_MS);

        gameManager.startGame();

        gameLoop();

        displayManager.displayGameResults();

        interactionManager.close();
    }
    
    private void gameLoop() {
        int roundNumber = 1;

        while (!gameManager.isGameOver()) {
            System.out.println(ConsoleFormatter.ANSI_CYAN + ConsoleFormatter.ANSI_BOLD);
            System.out.println("╔════════════════════════════════════════════════════╗");
            System.out.println("║                 ROUND " + roundNumber + "                          ║");
            System.out.println("╚════════════════════════════════════════════════════╝" + ConsoleFormatter.ANSI_RESET);

            displayManager.displayScores();

            boolean roundIsActive = true;

            while (roundIsActive && !gameManager.isGameOver()) {
                displayManager.displayGameState();

                Player currentPlayer = gameManager.getCurrentPlayer();
                System.out.println(ConsoleFormatter.ANSI_BOLD + currentPlayer.getName() + " sırası" + ConsoleFormatter.ANSI_RESET);


                gameManager.playTurn();

                if (gameManager.isRoundOver()) {
                    roundIsActive = false;
                    displayManager.displayRoundResults(roundNumber);
                    roundNumber++;
                }

                interactionManager.sleep(DELAY_MS);
            }
        }
    }
    
    public UserInteractionManager getInteractionManager() {
        return interactionManager;
    }
    
    public DisplayManager getDisplayManager() {
        return displayManager;
    }
} 