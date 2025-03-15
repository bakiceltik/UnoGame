package com.duocardgame.presentation;

import com.duocardgame.application.service.GameManager;
import com.duocardgame.presentation.ui.ConsoleUI;

/**
 * Duo Card Game uygulamasının başlangıç sınıfı.
 */
public class DuoCardGameMain {
    
    /**
     * Uygulamanın giriş noktası.
     * 
     * @param args Komut satırı argümanları
     */
    public static void main(String[] args) {
        // CSV dosya yolunu belirle
        String csvFilePath = "src/main/resources/game_results.csv";
        
        // GameManager örneği oluştur
        GameManager gameManager = new GameManager(csvFilePath);
        
        // Konsol arayüzü oluştur
        ConsoleUI consoleUI = new ConsoleUI(gameManager);
        
        // Oyunu başlat
        consoleUI.startGame();
    }
} 