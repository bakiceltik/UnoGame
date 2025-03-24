package com.duocardgame.domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.duocardgame.domain.factory.CardFactory;

public class Deck {
    private final List<Card> cards;
    private final CardFactory cardFactory;

    public Deck() {
        this.cards = new ArrayList<>();
        this.cardFactory = new CardFactory();
        initializeDeck();
    }

    public Deck(CardFactory cardFactory) {
        this.cards = new ArrayList<>();
        this.cardFactory = cardFactory;
        initializeDeck();
    }

    public void clear() {
        cards.clear();
    }

    public void initializeDeck() {
        cards.addAll(cardFactory.createStandardDeck());
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