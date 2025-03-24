package com.duocardgame.presentation;

import com.duocardgame.application.service.GameManager;
import com.duocardgame.presentation.ui.ConsoleUI;
import com.duocardgame.presentation.util.ErrorHandler;
import com.duocardgame.presentation.util.FileManager;

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
        printHeader("Starting Duo Card Game");
        
        // Yardımcı sınıfları oluştur
        FileManager fileManager = new FileManager();
        ErrorHandler errorHandler = new ErrorHandler();
        
        try {
            // CSV dosya yolunu oluştur
            String csvFilePath = fileManager.createDataDirectory();
            
            // GameManager örneği oluştur
            GameManager gameManager = new GameManager(csvFilePath);
            
            // Konsol arayüzü oluştur
            ConsoleUI consoleUI = new ConsoleUI(gameManager);
            
            // Oyunu başlat
            consoleUI.startGame();
            
        } catch (Exception e) {
            // Hata durumunda kullanıcı dostu mesaj göster
            errorHandler.handleGameError(e);
        }
    }
    
    /**
     * Başlık formatında mesaj yazdırır.
     * 
     * @param message Başlık mesajı
     */
    private static void printHeader(String message) {
        System.out.println();
        System.out.println("\u001B[36m" + "\u001B[1m" + "══════════════════════════════════════════════");
        System.out.println("  " + message);
        System.out.println("══════════════════════════════════════════════" + "\u001B[0m");
        System.out.println();
    }
} 