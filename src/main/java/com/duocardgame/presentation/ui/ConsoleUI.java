package com.duocardgame.presentation.ui;

import com.duocardgame.application.service.GameManager;
import com.duocardgame.domain.model.Card;
import com.duocardgame.domain.model.CardType;
import com.duocardgame.domain.model.Color;
import com.duocardgame.domain.model.Player;

import java.util.List;
import java.util.Scanner;

/**
 * ConsoleUI, konsola dayalı kullanıcı arayüzü sınıfıdır.
 */
public class ConsoleUI {
    private final GameManager gameManager;
    private final Scanner scanner;
    private static final int DELAY_MS = 1000; // 1 saniye bekleme süresi
    
    // ANSI Renk Kodları
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";
    private static final String ANSI_BOLD = "\u001B[1m";
    
    /**
     * GameManager ile bir ConsoleUI oluşturur.
     * 
     * @param gameManager Oyun yöneticisi
     */
    public ConsoleUI(GameManager gameManager) {
        this.gameManager = gameManager;
        this.scanner = new Scanner(System.in);
    }
    
    /**
     * Oyunu başlatır ve kullanıcı arayüzünü gösterir.
     */
    public void startGame() {
        printWelcomeMessage();
        
        System.out.println(ANSI_CYAN + "Game starting...\n" + ANSI_RESET);
        sleep(1000);
        
        // Oyunu başlat
        gameManager.startGame();
        
        // Oyun döngüsü
        gameLoop();
        
        // Oyun bittiğinde sonuçları göster
        displayGameResults();
        
        scanner.close();
    }
    
    /**
     * Oyun döngüsünü çalıştırır.
     */
    private void gameLoop() {
        int roundNumber = 1;
        
        while (!gameManager.isGameOver()) {
            System.out.println(ANSI_CYAN + ANSI_BOLD);
            System.out.println("╔════════════════════════════════════════════════════╗");
            System.out.println("║                 ROUND " + roundNumber + "                          ║");
            System.out.println("╚════════════════════════════════════════════════════╝" + ANSI_RESET);
            
            // Her round başında skor durumunu göster
            displayScores();
            
            boolean roundIsActive = true;
            
            while (roundIsActive && !gameManager.isGameOver()) {
                displayGameState();
                
                // Oyuncu sırası
                Player currentPlayer = gameManager.getCurrentPlayer();
                System.out.println(ANSI_BOLD + currentPlayer.getName() + " sırası" + ANSI_RESET);
                
                // Eğer kullanıcı girişi bekliyorsak:
                waitForNextTurn();
                
                gameManager.playTurn();
                
                // Round bitip bitmediğini kontrol et
                if (gameManager.isRoundOver()) {
                    roundIsActive = false;
                    displayRoundResults(roundNumber);
                    roundNumber++;
                }
                
                // Oyun durumunu tekrar göstermek için kısa bir bekle
                sleep(1000);
            }
        }
    }
    
    /**
     * Oyuncuların skorlarını gösterir.
     */
    private void displayScores() {
        System.out.println(ANSI_GREEN + ANSI_BOLD);
        System.out.println("╔════════════════════════════════════════════════════╗");
        System.out.println("║                SCOR TABLE                          ║");
        System.out.println("╚════════════════════════════════════════════════════╝" + ANSI_RESET);
        
        List<Player> players = gameManager.getPlayers();
        
        for (Player player : players) {
            System.out.println(player.getName() + ": " + player.getTotalScore() + " point");
        }
        
        // Kart destelerinin durumunu göster
        System.out.println("\nNumber of cards remaining: " + ANSI_BOLD + gameManager.getRemainingCardCount() + ANSI_RESET);
        System.out.println("Number of cards in the trash pile: " + ANSI_BOLD + gameManager.getDiscardPileCount() + ANSI_RESET);
        
        System.out.println(); // Boş satır
    }
    
    /**
     * Karşılama mesajını yazdırır.
     */
    private void printWelcomeMessage() {
        System.out.println("\n" + ANSI_CYAN + ANSI_BOLD);
        System.out.println("======================================================");
        System.out.println("                NEW GAME STARTING                     ");
        System.out.println("======================================================" + ANSI_RESET);
        System.out.println();
        
        System.out.println(ANSI_CYAN + ANSI_BOLD);
        System.out.println("╔════════════════════════════════════════════════════╗");
        System.out.println("║             WELCOME TO THE DUO GAME                ║");
        System.out.println("╚════════════════════════════════════════════════════╝" + ANSI_RESET);
        
        // Geçmiş oyun kayıtlarını göstermeyi kaldırdık
        
        System.out.println(ANSI_YELLOW + "\nGame Rules:" + ANSI_RESET);
        System.out.println(" - Objective: To get rid of all your cards");
        System.out.println(" - You can play a card that matches the top card's color or number");
        System.out.println(" - If you have no playable card, you must draw from the deck");
        System.out.println(" - The first player to empty their hand wins the round");
        System.out.println(" - The player who reaches 500 points wins the game");
        
        System.out.println(ANSI_YELLOW + "\nSpecial Cards:" + ANSI_RESET);
        System.out.println(" - " + ANSI_BOLD + "Draw Two" + ANSI_RESET + ": Next player draws 2 cards and loses their turn");
        System.out.println(" - " + ANSI_BOLD + "Reverse" + ANSI_RESET + ": Reverses the turn order");
        System.out.println(" - " + ANSI_BOLD + "Skip" + ANSI_RESET + ": Skips the next player's turn");
        System.out.println(" - " + ANSI_BOLD + "Wild" + ANSI_RESET + ": You can choose the new color");
        System.out.println(" - " + ANSI_BOLD + "Wild Draw Four" + ANSI_RESET + ": Declare a color, next player draws 4 cards");
        System.out.println(" - " + ANSI_BOLD + "Shuffle Hands" + ANSI_RESET + ": All hands are shuffled and redistributed");
        
        System.out.println("\nPress ENTER to continue...");
        scanner.nextLine();
    }
    
    /**
     * Oyun durumunu ekrana yazdırır.
     */
    private void displayGameState() {
        System.out.println("\n" + ANSI_CYAN + ANSI_BOLD);
        System.out.println("======================================================");
        System.out.println("                A NEW MOVE IS STARTING               ");
        System.out.println("======================================================" + ANSI_RESET);
        System.out.println();
        
        // Üst kart bilgisini göster
        Card topCard = gameManager.getTopCard();
        System.out.println(ANSI_CYAN + ANSI_BOLD);
        System.out.println("╔════════════════════════════════════════════════════╗");
        System.out.println("║             GAME STATE                             ║");
        System.out.println("╚════════════════════════════════════════════════════╝" + ANSI_RESET);
        
        if (topCard != null) {
            System.out.println("Top card: " + formatCard(topCard));
        } else {
            System.out.println("No card has been opened yet.");
        }
        
        // Deste ve çöp destesi durumunu göster
        System.out.println("Number of cards remaining in the deck: " + ANSI_BOLD + gameManager.getRemainingCardCount() + ANSI_RESET);
        System.out.println("Number of cards in the trash pile: " + ANSI_BOLD + gameManager.getDiscardPileCount() + ANSI_RESET);
        
        // Oyuncu bilgilerini göster
        System.out.println(ANSI_CYAN + ANSI_BOLD);
        System.out.println("╔════════════════════════════════════════════════════╗");
        System.out.println("║                 PLAYERS                            ║");
        System.out.println("╚════════════════════════════════════════════════════╝" + ANSI_RESET);
        
        List<Player> players = gameManager.getPlayers();
        Player currentPlayer = gameManager.getCurrentPlayer();
        
        for (Player player : players) {
            StringBuilder playerInfo = new StringBuilder();
            
            // Mevcut oyuncuyu işaretle
            if (player == currentPlayer) {
                playerInfo.append(ANSI_YELLOW + ANSI_BOLD + "► " + ANSI_RESET);
            } else {
                playerInfo.append("  ");
            }
            
            playerInfo.append(player == currentPlayer ? ANSI_BOLD : "")
                      .append(player.getName())
                      .append(" (Score: ").append(player.getTotalScore()).append(") - ")
                      .append("You have ").append(player.getHandSize()).append(" cards in your hand.")
                      .append(player == currentPlayer ? ANSI_RESET : "");
            
            System.out.println(playerInfo);
            
            // Mevcut oyuncunun kartlarını göster
            if (player == currentPlayer && player.getHandSize() > 0) {
                System.out.println("  Cards: ");
                List<Card> hand = player.getHand();
                int cardsPerLine = 4; // Her satırda maksimum kart sayısı
                
                for (int i = 0; i < hand.size(); i++) {
                    StringBuilder cardLine = new StringBuilder();
                    cardLine.append("    ");
                    
                    // Aynı satırda birden fazla kart göster
                    for (int j = 0; j < cardsPerLine && i + j < hand.size(); j++) {
                        cardLine.append((i + j + 1)).append(". ").append(formatCard(hand.get(i + j)));
                        cardLine.append("   ");
                    }
                    
                    System.out.println(cardLine.toString());
                    i += cardsPerLine - 1; // Gösterilen son kartı atla
                }
            }
        }
        
        // Oyun durumu mesajı göster
        System.out.println("\n" + ANSI_YELLOW + gameManager.getGameStatus() + ANSI_RESET);
    }
    
    /**
     * Kartı renkli formatta gösterir.
     * 
     * @param card Gösterilecek kart
     * @return Renkli kart gösterimi
     */
    private String formatCard(Card card) {
        String colorCode;
        
        switch (card.getColor()) {
            case RED:
                colorCode = ANSI_RED;
                break;
            case GREEN:
                colorCode = ANSI_GREEN;
                break;
            case BLUE:
                colorCode = ANSI_BLUE;
                break;
            case YELLOW:
                colorCode = ANSI_YELLOW;
                break;
            case WILD:
                colorCode = ANSI_PURPLE;
                break;
            default:
                colorCode = ANSI_WHITE;
        }
        
        return colorCode + ANSI_BOLD + card.toString() + ANSI_RESET;
    }
    
    /**
     * Bir sonraki tur için bekler.
     */
    private void waitForNextTurn() {
        Player currentPlayer = gameManager.getCurrentPlayer();
        System.out.println(ANSI_YELLOW + currentPlayer.getName() + " playing..." + ANSI_RESET);
        System.out.println("Press ENTER to continue.");
        scanner.nextLine();
    }
    
    /**
     * Oyun sonuçlarını gösterir.
     */
    private void displayGameResults() {
        System.out.println("\n" + ANSI_CYAN + ANSI_BOLD);
        System.out.println("╔════════════════════════════════════════════════════╗");
        System.out.println("║             GAME RESULTS                           ║");
        System.out.println("╚════════════════════════════════════════════════════╝" + ANSI_RESET);
        
        // Kazananı ve oyuncuların puanlarını göster
        Player winner = findWinner();
        System.out.println(ANSI_YELLOW + ANSI_BOLD + "WİNNER: " + winner.getName() + " (Total score: " + winner.getTotalScore() + ")" + ANSI_RESET);
        
        System.out.println("\nAll Players:");
        List<Player> players = gameManager.getPlayers();
        for (Player player : players) {
            if (player == winner) {
                System.out.println(ANSI_GREEN + "  " + player.getName() + ": " + player.getTotalScore() + " points" + ANSI_RESET + " 🏆");
            } else {
                System.out.println("  " + player.getName() + ": " + player.getTotalScore() + " points");
            }
        }
        
        System.out.println("\n" + ANSI_CYAN + "The game is over. Restart the program to play again." + ANSI_RESET);
        
        // Oyun istatistiklerini göster
        System.out.println("\nGame Statistics:");
        System.out.println("  Total Number of Players: " + players.size());
        System.out.println("  Wineer Player: " + winner.getName());
        System.out.println("  Total Number of Rounds: " + gameManager.getGameHistory().size());
    }
    
    /**
     * Kazanan oyuncuyu bulur.
     * 
     * @return Kazanan oyuncu
     */
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
    
    /**
     * Konsol ekranını temizler.
     */
    private void clearScreen() {
        // Bazı platformlarda çalışmayabilir
        System.out.print("\033[H\033[2J");
        System.out.flush();
        
        // Alternatif olarak boş satırlar yazdır - azaltılmış satır sayısı
        for (int i = 0; i < 5; i++) {
            System.out.println();
        }
    }
    
    /**
     * Belirtilen milisaniye kadar bekler.
     * 
     * @param ms Beklenecek milisaniye
     */
    private void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Round sonuçlarını gösterir.
     * 
     * @param roundNumber Round numarası
     */
    private void displayRoundResults(int roundNumber) {
        // Round sonu ayracı
        System.out.println("\n" + ANSI_YELLOW + ANSI_BOLD);
        System.out.println("╔════════════════════════════════════════════════════╗");
        System.out.println("║             ROUND " + roundNumber + " RESULTS      ║");
        System.out.println("╚════════════════════════════════════════════════════╝" + ANSI_RESET);
        
        List<Player> players = gameManager.getPlayers();
        
        // Tur kazananını daha belirgin göster
        Player roundWinner = null;
        for (Player player : players) {
            if (player.hasEmptyHand()) {
                roundWinner = player;
                break;
            }
        }
        
        if (roundWinner != null) {
            System.out.println("\n" + ANSI_GREEN + ANSI_BOLD + "★★★ ROUND WİNNER: " + roundWinner.getName() + " ★★★" + ANSI_RESET);
        }
        
        System.out.println("\n" + ANSI_CYAN + "Score Table:" + ANSI_RESET);
        for (Player player : players) {
            String marker = (player == roundWinner) ? " ⭐" : "";
            System.out.println(ANSI_BOLD + player.getName() + ANSI_RESET + ": " + 
                              player.getTotalScore() + " point" + marker);
        }
        
        System.out.println("\n" + ANSI_YELLOW + ANSI_BOLD);
        System.out.println("╔════════════════════════════════════════════════════╗");
        System.out.println("║              PRESS ENTER TO CONTINUE               ║");
        System.out.println("╚════════════════════════════════════════════════════╝" + ANSI_RESET);
        scanner.nextLine();
        
        // Round sonrasında özel bir ayraç ekleyelim
        System.out.println("\n" + ANSI_PURPLE + ANSI_BOLD);
        System.out.println("********************************************************");
        System.out.println("*                    NEW ROUND                        *");
        System.out.println("********************************************************" + ANSI_RESET);
    }
} 