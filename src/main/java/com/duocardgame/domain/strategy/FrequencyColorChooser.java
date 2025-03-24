package com.duocardgame.domain.strategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.duocardgame.domain.model.Card;
import com.duocardgame.domain.model.Color;

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
            if (cardColor != Color.WILD) {
                colorCounts.put(cardColor, colorCounts.getOrDefault(cardColor, 0) + 1);
            }
        }

        Color maxColor = Color.BLUE; //example
        int maxCount = 0;

        for (Map.Entry<Color, Integer> entry : colorCounts.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                maxColor = entry.getKey();
            }
        }

        // if there is no color card or all color cards have the same number, select a random color
        if (maxCount == 0 || colorCounts.values().stream().distinct().count() == 1) {
            Color[] colors = { Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW };
            return colors[random.nextInt(colors.length)];
        }

        return maxColor;
    }
} 