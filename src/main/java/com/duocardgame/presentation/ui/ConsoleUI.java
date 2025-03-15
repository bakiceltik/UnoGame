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
        
        System.out.println(ANSI_CYAN + "Oyun başlatılıyor...\n" + ANSI_RESET);
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
        while (!gameManager.isGameOver()) {
            displayGameState();
            
            // Oyuncu sırası
            Player currentPlayer = gameManager.getCurrentPlayer();
            System.out.println(ANSI_BOLD + currentPlayer.getName() + " sırası" + ANSI_RESET);
            
            // Eğer kullanıcı girişi bekliyorsak:
            waitForNextTurn();
            
            gameManager.playTurn();
            
            // Oyun durumunu tekrar göstermek için 1 saniye bekle
            sleep(1000);
        }
    }
    
    /**
     * Karşılama mesajını yazdırır.
     */
    private void printWelcomeMessage() {
        clearScreen();
        System.out.println(ANSI_CYAN + ANSI_BOLD);
        System.out.println("╔════════════════════════════════════════════════════╗");
        System.out.println("║         DUO KART OYUNUNA HOŞGELDİNİZ               ║");
        System.out.println("╚════════════════════════════════════════════════════╝" + ANSI_RESET);
        
        System.out.println(ANSI_YELLOW + "\nOyun Kuralları:" + ANSI_RESET);
        System.out.println(" - Amaç: Elinizdeki tüm kartları bitirmek");
        System.out.println(" - Üst kart ile aynı renk veya sayıdaki kartları oynayabilirsiniz");
        System.out.println(" - Oynayacak kartınız yoksa desteden kart çekmelisiniz");
        System.out.println(" - İlk elindeki kartları bitiren oyuncu turu kazanır");
        System.out.println(" - 500 puana ulaşan oyuncu oyunu kazanır");
        
        System.out.println(ANSI_YELLOW + "\nÖzel Kartlar:" + ANSI_RESET);
        System.out.println(" - " + ANSI_BOLD + "Draw Two" + ANSI_RESET + ": Sonraki oyuncu 2 kart çeker ve sırasını kaybeder");
        System.out.println(" - " + ANSI_BOLD + "Reverse" + ANSI_RESET + ": Oyun yönünü değiştirir");
        System.out.println(" - " + ANSI_BOLD + "Skip" + ANSI_RESET + ": Sonraki oyuncunun sırası atlanır");
        System.out.println(" - " + ANSI_BOLD + "Wild" + ANSI_RESET + ": İstediğiniz rengi seçebilirsiniz");
        System.out.println(" - " + ANSI_BOLD + "Wild Draw Four" + ANSI_RESET + ": Renk seçer ve sonraki oyuncu 4 kart çeker");
        System.out.println(" - " + ANSI_BOLD + "Shuffle Hands" + ANSI_RESET + ": Tüm oyuncuların kartları karıştırılıp yeniden dağıtılır");
        
        System.out.println("\nDevam etmek için ENTER tuşuna basın...");
        scanner.nextLine();
    }
    
    /**
     * Oyun durumunu ekrana yazdırır.
     */
    private void displayGameState() {
        clearScreen();
        
        // Üst kart bilgisini göster
        Card topCard = gameManager.getTopCard();
        System.out.println(ANSI_CYAN + ANSI_BOLD);
        System.out.println("╔════════════════════════════════════════════════════╗");
        System.out.println("║             OYUN DURUMU                            ║");
        System.out.println("╚════════════════════════════════════════════════════╝" + ANSI_RESET);
        
        if (topCard != null) {
            System.out.println("Üst kart: " + formatCard(topCard));
        } else {
            System.out.println("Henüz kart açılmadı.");
        }
        
        // Oyuncu bilgilerini göster
        System.out.println(ANSI_CYAN + ANSI_BOLD);
        System.out.println("╔════════════════════════════════════════════════════╗");
        System.out.println("║             OYUNCULAR                              ║");
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
                      .append(" (Skor: ").append(player.getTotalScore()).append(") - ")
                      .append("Elinde ").append(player.getHandSize()).append(" kart var")
                      .append(player == currentPlayer ? ANSI_RESET : "");
            
            System.out.println(playerInfo);
            
            // Mevcut oyuncunun kartlarını göster
            if (player == currentPlayer && player.getHandSize() > 0) {
                System.out.println("  Kartlar: ");
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
        System.out.println(ANSI_YELLOW + currentPlayer.getName() + " oynuyor..." + ANSI_RESET);
        System.out.println("Devam etmek için ENTER tuşuna basın.");
        scanner.nextLine();
    }
    
    /**
     * Oyun sonuçlarını gösterir.
     */
    private void displayGameResults() {
        clearScreen();
        
        System.out.println(ANSI_CYAN + ANSI_BOLD);
        System.out.println("╔════════════════════════════════════════════════════╗");
        System.out.println("║             OYUN SONUÇLARI                         ║");
        System.out.println("╚════════════════════════════════════════════════════╝" + ANSI_RESET);
        
        // Kazananı ve oyuncuların puanlarını göster
        Player winner = findWinner();
        System.out.println(ANSI_YELLOW + ANSI_BOLD + "KAZANAN: " + winner.getName() + " (Toplam Puan: " + winner.getTotalScore() + ")" + ANSI_RESET);
        
        System.out.println("\nTüm Oyuncular:");
        List<Player> players = gameManager.getPlayers();
        for (Player player : players) {
            if (player == winner) {
                System.out.println(ANSI_GREEN + "  " + player.getName() + ": " + player.getTotalScore() + " puan" + ANSI_RESET + " 🏆");
            } else {
                System.out.println("  " + player.getName() + ": " + player.getTotalScore() + " puan");
            }
        }
        
        System.out.println("\n" + ANSI_CYAN + "Oyun sona erdi. Tekrar oynamak için programı yeniden başlatın." + ANSI_RESET);
        
        // Oyun istatistiklerini göster
        System.out.println("\nOyun İstatistikleri:");
        System.out.println("  Toplam Oyuncu Sayısı: " + players.size());
        System.out.println("  Kazanan Oyuncu: " + winner.getName());
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
} 