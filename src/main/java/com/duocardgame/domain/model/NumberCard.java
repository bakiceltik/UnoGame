package com.duocardgame.domain.model;

public class NumberCard extends Card {
    private final int number;
    
    public NumberCard(Color color, int number) {
        super(color, CardType.NUMBER);
        
        if (number < 0 || number > 9) {
            throw new IllegalArgumentException("number must be between 0 and 9");
        }
        
        this.number = number;
    }
    
    public int getNumber() {
        return number;
    }
    
    @Override
    public int getPointValue() {
        return number; 
    }
    
    @Override
    public String toString() {
        return getColor() + " " + number;
    }
} 