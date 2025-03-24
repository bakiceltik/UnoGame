package com.duocardgame.presentation.util;

import java.util.Scanner;

/**
 * Uygulama hatalarının yönetiminden sorumlu sınıf.
 */
public class ErrorHandler {
    
    // ANSI Renk Kodları
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_BOLD = "\u001B[1m";
    
    /**
     * Oyun hatalarını işler ve kullanıcıya bilgi verir.
     * 
     * @param e Oluşan istisna
     */
    public void handleGameError(Exception e) {
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
    private void printHeader(String message) {
        System.out.println();
        System.out.println(ANSI_CYAN + ANSI_BOLD + "══════════════════════════════════════════════");
        System.out.println("  " + message);
        System.out.println("══════════════════════════════════════════════" + ANSI_RESET);
        System.out.println();
    }
} 