package com.duocardgame.presentation;

import com.duocardgame.application.service.GameManager;
import com.duocardgame.presentation.ui.ConsoleUI;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.Scanner;

/**
 * Duo Card Game uygulamasının başlangıç sınıfı.
 */
public class DuoCardGameMain {
    
    // ANSI Renk Kodları - Hata mesajları için
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_BOLD = "\u001B[1m";
    
    /**
     * Uygulamanın giriş noktası.
     * 
     * @param args Komut satırı argümanları
     */
    public static void main(String[] args) {
        printHeader("Duo Card Game Başlatılıyor");
        
        // CSV dosya yolunu belirle ve dizin yapısını kontrol et
        String csvFilePath = createDataDirectory();
        
        try {
            // GameManager örneği oluştur
            GameManager gameManager = new GameManager(csvFilePath);
            
            // Konsol arayüzü oluştur
            ConsoleUI consoleUI = new ConsoleUI(gameManager);
            
            // Oyunu başlat
            consoleUI.startGame();
            
        } catch (Exception e) {
            // Hata durumunda kullanıcı dostu mesaj göster
            handleGameError(e);
        }
    }
    
    /**
     * Data dizinini oluşturur ve CSV dosya yolunu döndürür.
     * 
     * @return CSV dosya yolu
     */
    private static String createDataDirectory() {
        try {
            // Veri dizinini oluştur
            Path dataPath = Paths.get("data");
            if (!Files.exists(dataPath)) {
                Files.createDirectories(dataPath);
                System.out.println("'data' dizini oluşturuldu.");
            }
            
            // CSV dosya yolu
            return dataPath.resolve("game_results.csv").toString();
            
        } catch (IOException e) {
            System.out.println(ANSI_YELLOW + "Uyarı: Dizin oluşturma hatası: " + e.getMessage() + ANSI_RESET);
            // Hata durumunda varsayılan yol
            return "game_results.csv";
        }
    }
    
    /**
     * Oyun hatalarını işler ve kullanıcıya bilgi verir.
     * 
     * @param e Oluşan istisna
     */
    private static void handleGameError(Exception e) {
        printHeader("Oyun Hatası Oluştu");
        
        System.out.println(ANSI_RED + ANSI_BOLD + "Hata: " + e.getMessage() + ANSI_RESET);
        System.out.println();
        
        // Yaygın hata tipleri için özel mesajlar
        if (e.getMessage() != null) {
            if (e.getMessage().contains("Sırası gelmeyen oyuncu")) {
                System.out.println("Bu hata oyun sırası işlenirken oluştu. Bu genellikle yeni bir tur başlatılırken meydana gelir.");
                System.out.println("Sorun: Oyuncu sırası belirlenmeden kart çekme işlemi yapılmaya çalışıldı.");
            } else if (e.getMessage().contains("Üst kart bulunamadı")) {
                System.out.println("Bu hata oyun başlatılırken veya yeni tur başlatılırken oluştu.");
                System.out.println("Sorun: Deste veya çöp destesinde kart bulunamadı.");
            } else {
                System.out.println("Beklenmeyen bir hata oluştu. Oyun düzgün çalışamıyor.");
            }
        }
        
        System.out.println();
        System.out.println(ANSI_YELLOW + "Hata Detayları:" + ANSI_RESET);
        e.printStackTrace();
        
        System.out.println();
        System.out.println(ANSI_CYAN + "Öneriler:" + ANSI_RESET);
        System.out.println("1. Oyunu yeniden başlatın");
        System.out.println("2. Eğer hata tekrarlanırsa, lütfen geliştiriciye bildirin");
        
        System.out.println();
        System.out.println("Çıkmak için ENTER tuşuna basın...");
        new Scanner(System.in).nextLine();
    }
    
    /**
     * Başlık formatında mesaj yazdırır.
     * 
     * @param message Başlık mesajı
     */
    private static void printHeader(String message) {
        System.out.println();
        System.out.println(ANSI_CYAN + ANSI_BOLD + "══════════════════════════════════════════════");
        System.out.println("  " + message);
        System.out.println("══════════════════════════════════════════════" + ANSI_RESET);
        System.out.println();
    }
} 