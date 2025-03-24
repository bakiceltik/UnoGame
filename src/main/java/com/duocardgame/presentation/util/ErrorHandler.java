package com.duocardgame.presentation.util;

import java.util.Scanner;


public class ErrorHandler {

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_BOLD = "\u001B[1m";
    
  
    public void handleGameError(Exception e) {
        printHeader("Game Error");
        
        System.out.println(ANSI_RED + ANSI_BOLD + "Error: " + e.getMessage() + ANSI_RESET);
        System.out.println();
        
        if (e.getMessage() != null) {
            if (e.getMessage().contains("Player turn not determined")) {
                System.out.println("This error occurred while processing the game turn. It usually happens when starting a new turn.");
                System.out.println("Issue: Player turn was not determined before drawing a card.");
            } else if (e.getMessage().contains("Top card not found")) {
                System.out.println("This error occurred when the game was started or when a new turn was started.");
                System.out.println("Issue: Card not found in the deck or discard pile.");
            } else {
                System.out.println("Unexpected error occurred. The game is not working properly.");
            }
        }
        
        System.out.println();
        System.out.println(ANSI_YELLOW + "Error Details:" + ANSI_RESET);
        e.printStackTrace();
        
        System.out.println();
        System.out.println("Please restart the game");
        
        System.out.println();
        System.out.println("Press ENTER to exit...");
        new Scanner(System.in).nextLine();
    }
    

    private void printHeader(String message) {
        System.out.println();
        System.out.println(ANSI_CYAN + ANSI_BOLD + "══════════════════════════════════════════════");
        System.out.println("  " + message);
        System.out.println("══════════════════════════════════════════════" + ANSI_RESET);
        System.out.println();
    }
} 