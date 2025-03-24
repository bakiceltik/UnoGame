package com.duocardgame.presentation.ui;

import java.util.Scanner;


public class UserInteractionManager {
    private final Scanner scanner;
    private final ConsoleFormatter formatter;
    

    public UserInteractionManager() {
        this.scanner = new Scanner(System.in);
        this.formatter = new ConsoleFormatter();
    }

    public void waitForUserInput() {
        System.out.println("Press ENTER to continue.");
        scanner.nextLine();
    }
    

    public void showWelcomeMessage() {
        System.out.println("\n" + ConsoleFormatter.ANSI_CYAN + ConsoleFormatter.ANSI_BOLD);
        System.out.println("======================================================");
        System.out.println("                NEW GAME STARTING                     ");
        System.out.println("======================================================" + ConsoleFormatter.ANSI_RESET);
        System.out.println();

        System.out.println(formatter.formatHeader("WELCOME TO THE DUO GAME"));

        System.out.println(ConsoleFormatter.ANSI_YELLOW + "\nGame Rules:" + ConsoleFormatter.ANSI_RESET);
        System.out.println(" - Objective: To get rid of all your cards");
        System.out.println(" - You can play a card that matches the top card's color or number");
        System.out.println(" - If you have no playable card, you must draw from the deck");
        System.out.println(" - The first player to empty their hand wins the round");
        System.out.println(" - The player who reaches 500 points wins the game");

        System.out.println(ConsoleFormatter.ANSI_YELLOW + "\nSpecial Cards:" + ConsoleFormatter.ANSI_RESET);
        System.out.println(
                " - " + ConsoleFormatter.ANSI_BOLD + "Draw Two" + ConsoleFormatter.ANSI_RESET + 
                ": Next player draws 2 cards and loses their turn");
        System.out.println(" - " + ConsoleFormatter.ANSI_BOLD + "Reverse" + ConsoleFormatter.ANSI_RESET + 
                ": Reverses the turn order");
        System.out.println(" - " + ConsoleFormatter.ANSI_BOLD + "Skip" + ConsoleFormatter.ANSI_RESET + 
                ": Skips the next player's turn");
        System.out.println(" - " + ConsoleFormatter.ANSI_BOLD + "Wild" + ConsoleFormatter.ANSI_RESET + 
                ": You can choose the new color");
        System.out.println(
                " - " + ConsoleFormatter.ANSI_BOLD + "Wild Draw Four" + ConsoleFormatter.ANSI_RESET + 
                ": Declare a color, next player draws 4 cards");
        System.out.println(
                " - " + ConsoleFormatter.ANSI_BOLD + "Shuffle Hands" + ConsoleFormatter.ANSI_RESET + 
                ": All hands are shuffled and redistributed");

        waitForUserInput();
    }
    

    public void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    

    public void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        for (int i = 0; i < 5; i++) {
            System.out.println();
        }
    }
    
    public void close() {
        scanner.close();
    }
    

    public Scanner getScanner() {
        return scanner;
    }
} 