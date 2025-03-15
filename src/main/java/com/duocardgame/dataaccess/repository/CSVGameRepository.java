package com.duocardgame.dataaccess.repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CSVGameRepository implements GameRepository {
    private final String csvFilePath;
    private final String delimiter;
    
    public CSVGameRepository(String csvFilePath) {
        this.csvFilePath = csvFilePath;
        this.delimiter = ",";
        initializeFile();
    }
    
    /**
     * CSV dosyasını başlat ve kontrol et.
     */
    private void initializeFile() {
        File file = new File(csvFilePath);
        try {
            // Eğer dosya yoksa, dizin yapısını oluştur
            if (!file.exists()) {
                Path parentPath = file.getParentFile().toPath();
                Files.createDirectories(parentPath);
                file.createNewFile();
                System.out.println("CSV dosyası oluşturuldu: " + file.getAbsolutePath());
            } else {
                System.out.println("CSV dosyası zaten var: " + file.getAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("CSV dosyası oluşturma hatası: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public void saveGameState(List<String[]> gameState) {
        File file = new File(csvFilePath);
        boolean isNewFile = !file.exists() || file.length() == 0;
        
        try {
            // Dosya yoksa oluştur
            if (!file.exists()) {
                file.createNewFile();
                System.out.println("Yeni CSV dosyası oluşturuldu: " + file.getAbsolutePath());
            }
            
            // Başlık ve veri
            String[] header = gameState.get(0);
            String[] data = gameState.get(1);
            
            // Eğer dosya yeni ise veya boş ise, başlık satırını ekle
            if (isNewFile) {
                try (FileWriter fw = new FileWriter(file);
                     BufferedWriter bw = new BufferedWriter(fw)) {
                    // Başlık satırı yaz
                    bw.write(String.join(delimiter, header));
                    bw.newLine();
                    
                    // Veri satırı yaz
                    bw.write(String.join(delimiter, data));
                    bw.newLine();
                    
                    System.out.println("Başlık ve ilk veri satırı yazıldı: " + csvFilePath);
                }
            } else {
                // Dosya zaten var ve dolu, sadece yeni veri satırını ekle
                try (FileWriter fw = new FileWriter(file, true);
                     BufferedWriter bw = new BufferedWriter(fw)) {
                    bw.write(String.join(delimiter, data));
                    bw.newLine();
                    
                    System.out.println("Yeni veri satırı eklendi: " + csvFilePath);
                }
            }
            
            System.out.println("Game state saved successfully!");
            
        } catch (IOException e) {
            System.err.println("CSV dosyasına yazma hatası: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public int getRoundCount() {
        int lineCount = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
            while (reader.readLine() != null) {
                lineCount++;
            }
            
            // Başlık satırını çıkar
            if (lineCount > 0) {
                lineCount--;
            }
        } catch (IOException e) {
            System.err.println("CSV dosyası okuma hatası: " + e.getMessage());
        }
        return lineCount;
    }
    
    @Override
    public List<String[]> readAllGameStates() {
        List<String[]> allGameStates = new ArrayList<>();
        File file = new File(csvFilePath);
        
        if (!file.exists()) {
            System.out.println("CSV dosyası bulunamadı: " + csvFilePath);
            return allGameStates;
        }
        
        if (file.length() == 0) {
            System.out.println("CSV dosyası boş: " + csvFilePath);
            return allGameStates;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(delimiter);
                allGameStates.add(fields);
            }
            
            System.out.println("CSV dosyasından " + allGameStates.size() + " satır okundu.");
        } catch (IOException e) {
            System.err.println("CSV dosyası okuma hatası: " + e.getMessage());
            e.printStackTrace();
        }
        
        return allGameStates;
    }
} 