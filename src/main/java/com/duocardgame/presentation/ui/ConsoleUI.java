package com.duocardgame.presentation.ui;

import com.duocardgame.application.service.GameManager;

/**
 * ConsoleUI, konsola dayalı kullanıcı arayüzü sınıfıdır.
 * Oyun akışını kontrol eder ve diğer UI bileşenlerini koordine eder.
 */
public class ConsoleUI {
    private final GameController gameController;

    /**
     * GameManager ile bir ConsoleUI oluşturur.
     * 
     * @param gameManager Oyun yöneticisi
     */
    public ConsoleUI(GameManager gameManager) {
        this.gameController = new GameController(gameManager);
    }

    /**
     * Oyunu başlatır ve kullanıcı arayüzünü gösterir.
     */
    public void startGame() {
        // Oyun kontrolünü GameController'a devret
        gameController.startGame();
    }
}