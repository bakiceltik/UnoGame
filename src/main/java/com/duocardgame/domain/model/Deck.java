package com.duocardgame.domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Deck {
    private final List<Card> cards;
    
    public Deck() {
        cards = new ArrayList<>();
        initializeDeck();
    }
    
    private void initializeDeck() {
        // Sayı kartları oluştur (her renk için 0'dan bir tane, 1-9'dan iki tane)
        Color[] colors = {Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW};
        
        for (Color color : colors) {
            // Her renk için bir tane 0 kartı
            cards.add(new NumberCard(color, 0));
            
            // Her renk için 1-9 arası sayı kartları (ikişer tane)
            for (int i = 1; i <= 9; i++) {
                cards.add(new NumberCard(color, i));
                cards.add(new NumberCard(color, i));
            }
            
            // Her renk için aksiyon kartları (ikişer tane)
            cards.add(new ActionCard(color, CardType.DRAW_TWO));
            cards.add(new ActionCard(color, CardType.DRAW_TWO));
            
            cards.add(new ActionCard(color, CardType.REVERSE));
            cards.add(new ActionCard(color, CardType.REVERSE));
            
            cards.add(new ActionCard(color, CardType.SKIP));
            cards.add(new ActionCard(color, CardType.SKIP));
            
            // Her renk için birer tane joker
            cards.add(new ActionCard(Color.WILD, CardType.WILD));
            cards.add(new ActionCard(Color.WILD, CardType.WILD_DRAW_FOUR));
        }
        
        // Tek bir el değiştirme kartı
        cards.add(new ActionCard(Color.WILD, CardType.SHUFFLE_HANDS));
    }
    
    public void shuffle() {
        Collections.shuffle(cards);
    }
    
    public Optional<Card> drawCard() {
        if (cards.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(cards.remove(0));
    }
    
    public boolean isEmpty() {
        return cards.isEmpty();
    }
    
    public int size() {
        return cards.size();
    }
    
    public void addCard(Card card) {
        cards.add(card);
    }
    
    public void addCards(List<Card> cardsToAdd) {
        cards.addAll(cardsToAdd);
    }
} 