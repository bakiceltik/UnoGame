package com.duocardgame.domain.strategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.duocardgame.domain.model.Card;
import com.duocardgame.domain.model.Color;

/**
 * Eldeki renklerin sıklığına göre en uygun rengi seçen strateji
 */
public class FrequencyColorChooser implements IColorChooser {
    
    private final Random random;
    
    public FrequencyColorChooser() {
        this.random = new Random();
    }
    
    @Override
    public Color chooseColor(List<Card> hand) {
        Map<Color, Integer> colorCounts = new HashMap<>();

        for (Card card : hand) {
            Color cardColor = card.getColor();
            // Joker kartları sayma
            if (cardColor != Color.WILD) {
                colorCounts.put(cardColor, colorCounts.getOrDefault(cardColor, 0) + 1);
            }
        }

        // En çok sahip olunan rengi bul
        Color maxColor = Color.BLUE; // Varsayılan renk
        int maxCount = 0;

        for (Map.Entry<Color, Integer> entry : colorCounts.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                maxColor = entry.getKey();
            }
        }

        // Eğer hiç renk kart yoksa veya eşit sayıda kart varsa rastgele bir renk seç
        if (maxCount == 0 || colorCounts.values().stream().distinct().count() == 1) {
            Color[] colors = { Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW };
            return colors[random.nextInt(colors.length)];
        }

        return maxColor;
    }
} 