package com.duocardgame.presentation.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class FileManager {
    
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BOLD = "\u001B[1m";
    

    public String createDataDirectory() {
        try {
            Path dataPath = Paths.get("data");
            if (!Files.exists(dataPath)) {
                Files.createDirectories(dataPath);
                System.out.println(ANSI_BOLD + ANSI_CYAN + "'data' directory created: " + dataPath.toAbsolutePath() + ANSI_RESET);
            } else {
                System.out.println(ANSI_BOLD + ANSI_CYAN + "'data' directory exists: " + dataPath.toAbsolutePath() + ANSI_RESET);
            }
            
            Path csvPath = dataPath.resolve("game_results.csv");
            System.out.println(ANSI_BOLD + ANSI_CYAN + "CSV file path: " + csvPath.toAbsolutePath() + ANSI_RESET);
            
            if (Files.exists(csvPath)) {
                long size = Files.size(csvPath);
                System.out.println(ANSI_BOLD + ANSI_CYAN + "CSV file size: " + size + " bytes" + ANSI_RESET);
                
                if (size > 0) {
                    System.out.println(ANSI_BOLD + ANSI_CYAN + "CSV file content (first 5 lines):" + ANSI_RESET);
                    try (BufferedReader reader = Files.newBufferedReader(csvPath)) {
                        for (int i = 0; i < 5; i++) {
                            String line = reader.readLine();
                            if (line == null) break;
                            System.out.println("  " + line);
                        }
                    }
                } else {
                    System.out.println(ANSI_BOLD + ANSI_YELLOW + "CSV file is empty." + ANSI_RESET);
                }
            } else {
                System.out.println(ANSI_BOLD + ANSI_YELLOW + "CSV file not yet created, will be created when the game starts." + ANSI_RESET);
            }
            
            return csvPath.toString();
            
        } catch (IOException e) {
            System.out.println(ANSI_YELLOW + "Warning: Directory creation error: " + e.getMessage() + ANSI_RESET);
            e.printStackTrace();
            return "game_results.csv";
        }
    }
    
    
    public boolean deleteFile(String path) {
        try {
            Path filePath = Paths.get(path);
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                System.out.println(ANSI_CYAN + "File deleted: " + path + ANSI_RESET);
                return true;
            } else {
                System.out.println(ANSI_YELLOW + "File not found: " + path + ANSI_RESET);
                return false;
            }
        } catch (IOException e) {
            System.out.println(ANSI_YELLOW + "File deletion error: " + e.getMessage() + ANSI_RESET);
            return false;
        }
    }
} 