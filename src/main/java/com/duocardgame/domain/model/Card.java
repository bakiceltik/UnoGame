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
    
    // Her kart için puan değerini hesapla
    public abstract int getPointValue();
    
    // Kartın oynanabilir olup olmadığını kontrol et
    public boolean isPlayable(Card topCard) {
        // Aynı renk veya aynı tip kartlar oynanabilir
        return this.color == topCard.getColor() || 
               (this.type == topCard.getType() && this.type != CardType.NUMBER) ||
               (this.type == CardType.NUMBER && topCard.getType() == CardType.NUMBER && 
                ((NumberCard)this).getNumber() == ((NumberCard)topCard).getNumber());
    }
    
    @Override
    public String toString() {
        return color + " " + type;
    }
} 