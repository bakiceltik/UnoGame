package com.duocardgame.presentation.ui;

import com.duocardgame.application.service.GameManager;
import com.duocardgame.domain.model.Player;

/**
 * Oyun akışı ve mantığını yöneten ana kontrol sınıfı.
 */
public class GameController {
    private final GameManager gameManager;
    private final DisplayManager displayManager;
    private final UserInteractionManager interactionManager;
    private static final int DELAY_MS = 1000; // 1 saniye bekleme süresi
    
    /**
     * Yeni bir GameController örneği oluşturur.
     * 
     * @param gameManager Oyun yöneticisi
     */
    public GameController(GameManager gameManager) {
        this.gameManager = gameManager;
        this.displayManager = new DisplayManager(gameManager);
        this.interactionManager = new UserInteractionManager();
    }
    
    /**
     * Oyunu başlatır ve oyun döngüsünü yönetir.
     */
    public void startGame() {
        interactionManager.showWelcomeMessage();

        System.out.println(ConsoleFormatter.ANSI_CYAN + "Game starting...\n" + ConsoleFormatter.ANSI_RESET);
        interactionManager.sleep(DELAY_MS);

        // Oyunu başlat
        gameManager.startGame();

        // Oyun döngüsü
        gameLoop();

        // Oyun bittiğinde sonuçları göster
        displayManager.displayGameResults();

        // Kaynakları serbest bırak
        interactionManager.close();
    }
    
    /**
     * Oyun döngüsünü çalıştırır.
     */
    private void gameLoop() {
        int roundNumber = 1;

        while (!gameManager.isGameOver()) {
            System.out.println(ConsoleFormatter.ANSI_CYAN + ConsoleFormatter.ANSI_BOLD);
            System.out.println("╔════════════════════════════════════════════════════╗");
            System.out.println("║                 ROUND " + roundNumber + "                          ║");
            System.out.println("╚════════════════════════════════════════════════════╝" + ConsoleFormatter.ANSI_RESET);

            // Her round başında skor durumunu göster
            displayManager.displayScores();

            boolean roundIsActive = true;

            while (roundIsActive && !gameManager.isGameOver()) {
                displayManager.displayGameState();

                // Oyuncu sırası
                Player currentPlayer = gameManager.getCurrentPlayer();
                System.out.println(ConsoleFormatter.ANSI_BOLD + currentPlayer.getName() + " sırası" + ConsoleFormatter.ANSI_RESET);

                // Eğer kullanıcı girişi bekliyorsak:
                waitForNextTurn();

                gameManager.playTurn();

                // Round bitip bitmediğini kontrol et
                if (gameManager.isRoundOver()) {
                    roundIsActive = false;
                    displayManager.displayRoundResults(roundNumber);
                    roundNumber++;
                }

                // Oyun durumunu tekrar göstermek için kısa bir bekle
                interactionManager.sleep(DELAY_MS);
            }
        }
    }
    
    /**
     * Bir sonraki tur için bekler.
     */
    private void waitForNextTurn() {
        // Oyun otomatik ilerleyeceği için burada aktif olarak beklemiyoruz
        // Ama isteğe bağlı bekletme eklenebilir
        //interactionManager.waitForUserInput();
    }
    
    /**
     * UserInteractionManager nesnesini döndürür.
     * 
     * @return UserInteractionManager nesnesi
     */
    public UserInteractionManager getInteractionManager() {
        return interactionManager;
    }
    
    /**
     * DisplayManager nesnesini döndürür.
     * 
     * @return DisplayManager nesnesi
     */
    public DisplayManager getDisplayManager() {
        return displayManager;
    }
} 