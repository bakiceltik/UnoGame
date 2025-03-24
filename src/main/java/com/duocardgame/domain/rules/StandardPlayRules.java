package com.duocardgame.domain.rules;

import com.duocardgame.domain.model.Card;
import com.duocardgame.domain.model.CardType;
import com.duocardgame.domain.model.Color;
import com.duocardgame.domain.model.NumberCard;

public class StandardPlayRules implements IPlayable {

    @Override
    public boolean isPlayable(Card card, Card topCard, Color currentColor) {
        // Same color or same type cards can be played
        if (card.getColor() == topCard.getColor()) {
            return true;
        }
        
        // Card, current color matches
        if (card.getColor() == currentColor) {
            return true;
        }
        
        // Same type action cards
        if (card.getType() == topCard.getType() && card.getType() != CardType.NUMBER) {
            return true;
        }
        
        // Same number cards
        if (card.getType() == CardType.NUMBER && topCard.getType() == CardType.NUMBER) {
            NumberCard numberCard = (NumberCard) card;
            NumberCard topNumberCard = (NumberCard) topCard;
            return numberCard.getNumber() == topNumberCard.getNumber();
        }
        
        // WILD and SHUFFLE_HANDS cards can always be played
        if (card.getType() == CardType.WILD || card.getType() == CardType.SHUFFLE_HANDS) {
            return true;
        }
        
        return false;
    }

    @Override
    public boolean isPlayable(Card topCard, Color currentColor) {
  
        return false;
    }
} 