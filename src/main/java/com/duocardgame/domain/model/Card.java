package com.duocardgame.domain.model;

public abstract class Card {
    private final Color color;
    private final CardType type;
    
    public Card(Color color, CardType type) {
        this.color = color;
        this.type = type;
    }
    
    public Color getColor() {
        return color;
    }
    
    public CardType getType() {
        return type;
    }
    
    public abstract int getPointValue();
    
    public boolean isPlayable(Card topCard) {
        if (this.getColor() == topCard.getColor()) {
            return true;
        }
        
        if (this.getType() == topCard.getType() && this.getType() != CardType.NUMBER) {
            return true;
        }
        
        if (this.getType() == CardType.NUMBER && topCard.getType() == CardType.NUMBER) {
            NumberCard thisCard = (NumberCard) this;
            NumberCard otherCard = (NumberCard) topCard;
            return thisCard.getNumber() == otherCard.getNumber();
        }
        
        if (this.getType() == CardType.WILD || this.getType() == CardType.WILD_DRAW_FOUR || 
            this.getType() == CardType.SHUFFLE_HANDS) {
            return true;
        }
        
        return false;
    }
    
    @Override
    public String toString() {
        return color + " " + type;
    }
} 