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
    
    private void initializeFile() {
        File file = new File(csvFilePath);
        if (!file.exists()) {
            try {
                // Dizin yapısını oluştur
                Path parentDir = Paths.get(file.getParent());
                if (parentDir != null && !Files.exists(parentDir)) {
                    Files.createDirectories(parentDir);
                }
                
                // Dosyayı oluştur
                file.createNewFile();
            } catch (IOException e) {
                System.err.println("CSV dosyası oluşturulamadı: " + e.getMessage());
            }
        }
    }
    
    @Override
    public void saveGameState(List<String[]> gameState) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilePath, true))) {
            // Mevcut içeriği kontrol et
            boolean fileExists = new File(csvFilePath).exists();
            
            // Eğer dosya boşsa veya yeniyse başlık satırını ekle
            if (!fileExists || getRoundCount() == 0) {
                String[] header = gameState.get(0);
                writer.write(String.join(delimiter, header));
                writer.newLine();
            }
            
            // Veri satırını ekle
            String[] data = gameState.get(1);
            writer.write(String.join(delimiter, data));
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            System.err.println("CSV dosyasına yazma hatası: " + e.getMessage());
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
    
    public List<String[]> readAllGameStates() {
        List<String[]> allGameStates = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(delimiter);
                allGameStates.add(fields);
            }
        } catch (IOException e) {
            System.err.println("CSV dosyası okuma hatası: " + e.getMessage());
        }
        return allGameStates;
    }
} 