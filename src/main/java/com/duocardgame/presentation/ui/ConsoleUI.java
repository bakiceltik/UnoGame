package com.duocardgame.presentation.ui;

import com.duocardgame.application.service.GameManager;


public class ConsoleUI {
    private final GameController gameController;


    public ConsoleUI(GameManager gameManager) {
        this.gameController = new GameController(gameManager);
    }


    public void startGame() {
        gameController.startGame();
    }
}