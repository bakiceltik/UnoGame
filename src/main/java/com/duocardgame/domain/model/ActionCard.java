package com.duocardgame.domain.model;

public class ActionCard extends Card {
    
    public ActionCard(Color color, CardType type) {
        super(color, type);
        
        if (type == CardType.NUMBER) {
            throw new IllegalArgumentException("ActionCard, NUMBER tipinde olamaz");
        }
    }
    
    @Override
    public int getPointValue() {
        switch (getType()) {
            case DRAW_TWO:
            case REVERSE:
            case SKIP:
                return 20;
            case WILD:
            case WILD_DRAW_FOUR:
                return 50;
            case SHUFFLE_HANDS:
                return 40;
            default:
                throw new IllegalStateException("Bilinmeyen kart tipi: " + getType());
        }
    }
    
    @Override
    public String toString() {
        return getColor() + " " + getType();
    }
} 