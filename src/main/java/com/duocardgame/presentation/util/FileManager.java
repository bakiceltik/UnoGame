package com.duocardgame.presentation.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Dosya ve dizin yönetiminden sorumlu sınıf.
 */
public class FileManager {
    
    // ANSI Renk Kodları
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BOLD = "\u001B[1m";
    
    /**
     * Data dizinini oluşturur ve CSV dosya yolunu döndürür.
     * 
     * @return CSV dosya yolu
     */
    public String createDataDirectory() {
        try {
            // Veri dizinini oluştur
            Path dataPath = Paths.get("data");
            if (!Files.exists(dataPath)) {
                Files.createDirectories(dataPath);
                System.out.println(ANSI_BOLD + ANSI_CYAN + "'data' dizini oluşturuldu: " + dataPath.toAbsolutePath() + ANSI_RESET);
            } else {
                System.out.println(ANSI_BOLD + ANSI_CYAN + "'data' dizini mevcut: " + dataPath.toAbsolutePath() + ANSI_RESET);
            }
            
            // CSV dosya yolu
            Path csvPath = dataPath.resolve("game_results.csv");
            System.out.println(ANSI_BOLD + ANSI_CYAN + "CSV dosyası yolu: " + csvPath.toAbsolutePath() + ANSI_RESET);
            
            // Dosya varsa içeriğini kontrol et
            if (Files.exists(csvPath)) {
                long size = Files.size(csvPath);
                System.out.println(ANSI_BOLD + ANSI_CYAN + "CSV dosyası boyutu: " + size + " byte" + ANSI_RESET);
                
                if (size > 0) {
                    System.out.println(ANSI_BOLD + ANSI_CYAN + "CSV dosyası içeriği (ilk 5 satır):" + ANSI_RESET);
                    try (BufferedReader reader = Files.newBufferedReader(csvPath)) {
                        for (int i = 0; i < 5; i++) {
                            String line = reader.readLine();
                            if (line == null) break;
                            System.out.println("  " + line);
                        }
                    }
                } else {
                    System.out.println(ANSI_BOLD + ANSI_YELLOW + "CSV dosyası boş." + ANSI_RESET);
                }
            } else {
                System.out.println(ANSI_BOLD + ANSI_YELLOW + "CSV dosyası henüz oluşturulmadı, oyun başladığında oluşturulacak." + ANSI_RESET);
            }
            
            return csvPath.toString();
            
        } catch (IOException e) {
            System.out.println(ANSI_YELLOW + "Uyarı: Dizin oluşturma hatası: " + e.getMessage() + ANSI_RESET);
            e.printStackTrace();
            // Hata durumunda varsayılan yol
            return "game_results.csv";
        }
    }
    
    /**
     * Belirtilen yoldaki dosyayı siler.
     * 
     * @param path Silinecek dosya yolu
     * @return Silme işlemi başarılı ise true, aksi halde false
     */
    public boolean deleteFile(String path) {
        try {
            Path filePath = Paths.get(path);
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                System.out.println(ANSI_CYAN + "Dosya silindi: " + path + ANSI_RESET);
                return true;
            } else {
                System.out.println(ANSI_YELLOW + "Silinecek dosya bulunamadı: " + path + ANSI_RESET);
                return false;
            }
        } catch (IOException e) {
            System.out.println(ANSI_YELLOW + "Dosya silme hatası: " + e.getMessage() + ANSI_RESET);
            return false;
        }
    }
} 