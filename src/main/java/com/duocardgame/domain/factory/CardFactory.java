package com.duocardgame.domain.factory;

import java.util.ArrayList;
import java.util.List;

import com.duocardgame.domain.model.ActionCard;
import com.duocardgame.domain.model.Card;
import com.duocardgame.domain.model.CardType;
import com.duocardgame.domain.model.Color;
import com.duocardgame.domain.model.NumberCard;


public class CardFactory {

    public List<Card> createStandardDeck() {
        List<Card> cards = new ArrayList<>();
        
        Color[] colors = {Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW};
        
        for (Color color : colors) {
            cards.add(createNumberCard(color, 0));
            
            for (int i = 1; i <= 9; i++) {
                cards.add(createNumberCard(color, i));
                cards.add(createNumberCard(color, i));
            }
            
            cards.add(createActionCard(color, CardType.DRAW_TWO));
            cards.add(createActionCard(color, CardType.DRAW_TWO));
            
            cards.add(createActionCard(color, CardType.REVERSE));
            cards.add(createActionCard(color, CardType.REVERSE));
            
            cards.add(createActionCard(color, CardType.SKIP));
            cards.add(createActionCard(color, CardType.SKIP));
        }
        
        cards.add(createActionCard(Color.WILD, CardType.WILD));
        cards.add(createActionCard(Color.WILD, CardType.WILD));
        
        cards.add(createActionCard(Color.WILD, CardType.WILD_DRAW_FOUR));
        cards.add(createActionCard(Color.WILD, CardType.WILD_DRAW_FOUR));
        
        cards.add(createActionCard(Color.WILD, CardType.SHUFFLE_HANDS));
        
        return cards;
    }
    

    public NumberCard createNumberCard(Color color, int number) {
        return new NumberCard(color, number);
    }
  
    public ActionCard createActionCard(Color color, CardType type) {
        return new ActionCard(color, type);
    }
} 