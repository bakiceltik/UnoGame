package com.duocardgame.presentation;

import com.duocardgame.application.service.GameManager;
import com.duocardgame.presentation.ui.ConsoleUI;
import com.duocardgame.presentation.util.ErrorHandler;
import com.duocardgame.presentation.util.FileManager;

public class DuoCardGameMain {
    

    public static void main(String[] args) {
        printHeader("Starting Duo Card Game");
        
        FileManager fileManager = new FileManager();
        ErrorHandler errorHandler = new ErrorHandler();
        
        try {
         
            String csvFilePath = fileManager.createDataDirectory();
            
           
            GameManager gameManager = new GameManager(csvFilePath);
            
            
            ConsoleUI consoleUI = new ConsoleUI(gameManager);
            
            consoleUI.startGame();
            
        } catch (Exception e) {
            
            errorHandler.handleGameError(e);
        }
    }
    //title
    private static void printHeader(String message) {
        System.out.println();
        System.out.println("\u001B[36m" + "\u001B[1m" + "══════════════════════════════════════════════");
        System.out.println("  " + message);
        System.out.println("══════════════════════════════════════════════" + "\u001B[0m");
        System.out.println();
    }
} 