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
        try {
            if (!file.exists()) {
                Path parentPath = file.getParentFile().toPath();
                Files.createDirectories(parentPath);
                file.createNewFile();
                System.out.println("CSV file created: " + file.getAbsolutePath());
            } else {
                System.out.println("CSV file already exists: " + file.getAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("CSV file creation error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public void saveGameState(List<String[]> gameState) {
        File file = new File(csvFilePath);
        boolean isNewFile = !file.exists() || file.length() == 0;
        
        try {
            if (!file.exists()) {
                file.createNewFile();
                System.out.println("New CSV file created: " + file.getAbsolutePath());
            }
            
            String[] header = gameState.get(0);
            String[] data = gameState.get(1);
            
            if (isNewFile) {
                try (FileWriter fw = new FileWriter(file);
                     BufferedWriter bw = new BufferedWriter(fw)) {
                    bw.write(String.join(delimiter, header));
                    bw.newLine();
                    
                    bw.write(String.join(delimiter, data));
                    bw.newLine();
                    
                    System.out.println("Header and first data line written: " + csvFilePath);
                }
            } else {
                try (FileWriter fw = new FileWriter(file, true);
                     BufferedWriter bw = new BufferedWriter(fw)) {
                    bw.write(String.join(delimiter, data));
                    bw.newLine();
                    
                    System.out.println("New data line added: " + csvFilePath);
                }
            }
            
            System.out.println("Game state saved successfully!");
            
        } catch (IOException e) {
            System.err.println("CSV file write error: " + e.getMessage());
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
            
            if (lineCount > 0) {
                lineCount--;
            }
        } catch (IOException e) {
            System.err.println("CSV file read error: " + e.getMessage());
        }
        return lineCount;
    }
    
    @Override
    public List<String[]> readAllGameStates() {
        List<String[]> allGameStates = new ArrayList<>();
        File file = new File(csvFilePath);
        
        if (!file.exists()) {
            System.out.println("CSV file not found: " + csvFilePath);
            return allGameStates;
        }
        
        if (file.length() == 0) {
            System.out.println("CSV file is empty: " + csvFilePath);
            return allGameStates;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(delimiter);
                allGameStates.add(fields);
            }
            
            System.out.println("Read " + allGameStates.size() + " lines from CSV file: " + csvFilePath);
        } catch (IOException e) {
            System.err.println("CSV file read error: " + e.getMessage());
            e.printStackTrace();
        }
        
        return allGameStates;
    }
} 