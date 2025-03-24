package com.duocardgame.domain.factory;

import java.util.ArrayList;
import java.util.List;

import com.duocardgame.domain.model.ActionCard;
import com.duocardgame.domain.model.Card;
import com.duocardgame.domain.model.CardType;
import com.duocardgame.domain.model.Color;
import com.duocardgame.domain.model.NumberCard;

/**
 * Kart Factory sınıfı, SRP prensibine uygun olarak kart oluşturma 
 * sorumluluğunu üstlenir.
 */
public class CardFactory {
    
    /**
     * Standart UNO destesi oluşturur
     * 
     * @return Oluşturulan kart listesi
     */
    public List<Card> createStandardDeck() {
        List<Card> cards = new ArrayList<>();
        
        // Sayı kartları oluştur (her renk için 0'dan bir tane, 1-9'dan iki tane)
        Color[] colors = {Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW};
        
        for (Color color : colors) {
            // Her renk için bir tane 0 kartı
            cards.add(createNumberCard(color, 0));
            
            // Her renk için 1-9 arası sayı kartları (ikişer tane)
            for (int i = 1; i <= 9; i++) {
                cards.add(createNumberCard(color, i));
                cards.add(createNumberCard(color, i));
            }
            
            // Her renk için aksiyon kartları (ikişer tane)
            cards.add(createActionCard(color, CardType.DRAW_TWO));
            cards.add(createActionCard(color, CardType.DRAW_TWO));
            
            cards.add(createActionCard(color, CardType.REVERSE));
            cards.add(createActionCard(color, CardType.REVERSE));
            
            cards.add(createActionCard(color, CardType.SKIP));
            cards.add(createActionCard(color, CardType.SKIP));
        }
        
        // Her renk için birer tane joker
        cards.add(createActionCard(Color.WILD, CardType.WILD));
        cards.add(createActionCard(Color.WILD, CardType.WILD));
        
        // Her renk için birer tane +4 joker
        cards.add(createActionCard(Color.WILD, CardType.WILD_DRAW_FOUR));
        cards.add(createActionCard(Color.WILD, CardType.WILD_DRAW_FOUR));
        
        // Tek bir el değiştirme kartı
        cards.add(createActionCard(Color.WILD, CardType.SHUFFLE_HANDS));
        
        return cards;
    }
    
    /**
     * Sayı kartı oluşturur
     * 
     * @param color Kart rengi
     * @param number Kart sayısı
     * @return Oluşturulan sayı kartı
     */
    public NumberCard createNumberCard(Color color, int number) {
        return new NumberCard(color, number);
    }
    
    /**
     * Aksiyon kartı oluşturur
     * 
     * @param color Kart rengi
     * @param type Kart tipi
     * @return Oluşturulan aksiyon kartı
     */
    public ActionCard createActionCard(Color color, CardType type) {
        return new ActionCard(color, type);
    }
} 