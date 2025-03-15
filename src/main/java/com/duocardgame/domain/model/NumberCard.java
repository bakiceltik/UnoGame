package com.duocardgame.domain.model;

public class NumberCard extends Card {
    private final int number;
    
    public NumberCard(Color color, int number) {
        super(color, CardType.NUMBER);
        
        if (number < 0 || number > 9) {
            throw new IllegalArgumentException("Sayı kartı değeri 0-9 arasında olmalıdır");
        }
        
        this.number = number;
    }
    
    public int getNumber() {
        return number;
    }
    
    @Override
    public int getPointValue() {
        return number; // Sayı kartlarının puanı, üzerindeki sayı değeridir
    }
    
    @Override
    public String toString() {
        return getColor() + " " + number;
    }
} 