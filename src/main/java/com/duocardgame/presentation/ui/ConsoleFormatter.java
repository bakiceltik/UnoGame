package com.duocardgame.presentation.ui;

import com.duocardgame.domain.model.Card;
import com.duocardgame.domain.model.Color;

/**
 * Konsol çıktılarının biçimlendirilmesinden sorumlu sınıf.
 */
public class ConsoleFormatter {
    
    // ANSI Renk Kodları
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_BOLD = "\u001B[1m";
    
    /**
     * Kartı renkli formatta gösterir.
     * 
     * @param card Gösterilecek kart
     * @return Renkli kart gösterimi
     */
    public String formatCard(Card card) {
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
     * Renkli formatta gösterir.
     * 
     * @param color Gösterilecek renk
     * @return Renkli renk gösterimi
     */
    public String formatColor(Color color) {
        String colorCode;

        switch (color) {
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

        return colorCode + ANSI_BOLD + color.toString() + ANSI_RESET;
    }
    
    /**
     * Başlık formatında bir metin oluşturur.
     * 
     * @param title Başlık metni
     * @return Biçimlendirilmiş başlık metni
     */
    public String formatHeader(String title) {
        StringBuilder builder = new StringBuilder();
        builder.append("\n").append(ANSI_CYAN).append(ANSI_BOLD);
        builder.append("╔════════════════════════════════════════════════════╗\n");
        builder.append("║             ").append(title);
        
        // Başlık uzunluğuna göre boşluk ekle
        int spaces = 43 - title.length();
        for (int i = 0; i < spaces; i++) {
            builder.append(" ");
        }
        
        builder.append("║\n");
        builder.append("╚════════════════════════════════════════════════════╝");
        builder.append(ANSI_RESET);
        
        return builder.toString();
    }
    
    /**
     * Bölüm ayıracı oluşturur.
     * 
     * @param title Bölüm başlığı
     * @param color Renk (ANSI renk kodu)
     * @return Biçimlendirilmiş bölüm ayıracı
     */
    public String formatSection(String title, String color) {
        StringBuilder builder = new StringBuilder();
        builder.append("\n").append(color).append(ANSI_BOLD);
        builder.append("======================================================\n");
        builder.append("                ").append(title).append("\n");
        builder.append("======================================================");
        builder.append(ANSI_RESET);
        
        return builder.toString();
    }
} 