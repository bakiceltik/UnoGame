package com.duocardgame.domain.handler;

import java.util.Arrays;

import com.duocardgame.domain.model.Card;
import com.duocardgame.domain.model.CardType;
import com.duocardgame.domain.model.Color;
import com.duocardgame.domain.strategy.IColorChooser;


public class WildCardHandler {
    
    public boolean canPlayWildDrawFour(Card[] hand, Color currentColor) {
        // Wild Draw Four card, but if there is no other card with the current color in the hand
        for (Card card : hand) {
            // Exclude wild cards, only check normal color cards
            if (card.getColor() == currentColor && 
                card.getType() != CardType.WILD &&
                card.getType() != CardType.WILD_DRAW_FOUR) {
                return false;
            }
        }
        return true;
    }
    
 
    public Color chooseColorAfterWild(IColorChooser colorChooser, Card[] hand) {
        return colorChooser.chooseColor(Arrays.asList(hand));
    }
} 