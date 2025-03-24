package com.duocardgame.domain.rules;

import com.duocardgame.domain.model.Card;
import com.duocardgame.domain.model.CardType;
import com.duocardgame.domain.model.Color;
import com.duocardgame.domain.model.NumberCard;

public class StandardPlayRules implements IPlayable {

    @Override
    public boolean isPlayable(Card card, Card topCard, Color currentColor) {
        // Aynı renk veya aynı tip kartlar oynanabilir
        if (card.getColor() == topCard.getColor()) {
            return true;
        }
        
        // Kart, mevcut renk ile eşleşiyorsa
        if (card.getColor() == currentColor) {
            return true;
        }
        
        // Aynı tipteki aksiyon kartları
        if (card.getType() == topCard.getType() && card.getType() != CardType.NUMBER) {
            return true;
        }
        
        // Aynı sayıdaki sayı kartları
        if (card.getType() == CardType.NUMBER && topCard.getType() == CardType.NUMBER) {
            NumberCard numberCard = (NumberCard) card;
            NumberCard topNumberCard = (NumberCard) topCard;
            return numberCard.getNumber() == topNumberCard.getNumber();
        }
        
        // WILD ve SHUFFLE_HANDS kartları her zaman oynanabilir
        if (card.getType() == CardType.WILD || card.getType() == CardType.SHUFFLE_HANDS) {
            return true;
        }
        
        return false;
    }

    @Override
    public boolean isPlayable(Card topCard, Color currentColor) {
        // Bu metot diğer isPlayable metoduna yönlendirici olarak kullanılacaktır
        // Çağrıldığında oynanmak istenen kart henüz bilinmediği için
        // sadece bir arayüz uyumluluğu sağlar
        return false;
    }
} 