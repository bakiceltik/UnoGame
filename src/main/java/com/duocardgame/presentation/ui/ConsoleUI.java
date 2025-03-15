package com.duocardgame.presentation.ui;

import com.duocardgame.application.service.GameManager;
import com.duocardgame.domain.model.Card;
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
        while (!gameManager.isGameOver()) {
            displayGameState();
            waitForNextTurn();
            gameManager.playTurn();
        }
    }
    
    /**
     * Karşılama mesajını yazdırır.
     */
    private void printWelcomeMessage() {
        System.out.println("╔═════════════════════════════════════════╗");
        System.out.println("║       DUO KART OYUNUNA HOŞGELDİNİZ      ║");
        System.out.println("╚═════════════════════════════════════════╝");
        System.out.println();
        System.out.println("Oyun başlıyor...");
        System.out.println();
    }
    
    /**
     * Oyun durumunu ekrana yazdırır.
     */
    private void displayGameState() {
        clearScreen();
        
        // Üst kart bilgisini göster
        Card topCard = gameManager.getTopCard();
        System.out.println("╔═════════════════════════════════════════╗");
        System.out.println("║             OYUN DURUMU                 ║");
        System.out.println("╚═════════════════════════════════════════╝");
        System.out.println();
        
        if (topCard != null) {
            System.out.println("Üst kart: " + topCard);
        } else {
            System.out.println("Henüz kart açılmadı.");
        }
        System.out.println();
        
        // Oyuncu bilgilerini göster
        System.out.println("╔═════════════════════════════════════════╗");
        System.out.println("║             OYUNCULAR                   ║");
        System.out.println("╚═════════════════════════════════════════╝");
        
        List<Player> players = gameManager.getPlayers();
        Player currentPlayer = gameManager.getCurrentPlayer();
        
        for (Player player : players) {
            StringBuilder playerInfo = new StringBuilder();
            
            // Mevcut oyuncuyu işaretle
            if (player == currentPlayer) {
                playerInfo.append("► ");
            } else {
                playerInfo.append("  ");
            }
            
            playerInfo.append(player.getName())
                      .append(" (Skor: ").append(player.getTotalScore()).append(") - ")
                      .append("Elinde ").append(player.getHandSize()).append(" kart var");
            
            System.out.println(playerInfo);
            
            // Mevcut oyuncunun kartlarını göster
            if (player == currentPlayer) {
                System.out.println("  Kartlar: ");
                List<Card> hand = player.getHand();
                for (int i = 0; i < hand.size(); i++) {
                    System.out.println("    " + (i + 1) + ". " + hand.get(i));
                }
            }
            
            System.out.println();
        }
    }
    
    /**
     * Bir sonraki tur için bekler.
     */
    private void waitForNextTurn() {
        Player currentPlayer = gameManager.getCurrentPlayer();
        System.out.println(currentPlayer.getName() + " oynuyor...");
        System.out.println("Devam etmek için ENTER tuşuna basın.");
        scanner.nextLine();
    }
    
    /**
     * Oyun sonuçlarını gösterir.
     */
    private void displayGameResults() {
        clearScreen();
        
        System.out.println("╔═════════════════════════════════════════╗");
        System.out.println("║             OYUN SONUÇLARI              ║");
        System.out.println("╚═════════════════════════════════════════╝");
        System.out.println();
        
        // Kazananı ve oyuncuların puanlarını göster
        Player winner = findWinner();
        System.out.println("KAZANAN: " + winner.getName() + " (Toplam Puan: " + winner.getTotalScore() + ")");
        System.out.println();
        
        System.out.println("Tüm Oyuncular:");
        List<Player> players = gameManager.getPlayers();
        for (Player player : players) {
            System.out.println("  " + player.getName() + ": " + player.getTotalScore() + " puan");
        }
        
        System.out.println();
        System.out.println("Oyun sona erdi. Tekrar oynamak için programı yeniden başlatın.");
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
        
        // Alternatif olarak boş satırlar yazdır
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }
} 