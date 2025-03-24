package com.duocardgame.application.manager;

import com.duocardgame.domain.model.Card;
import com.duocardgame.domain.model.Color;
import com.duocardgame.domain.model.Deck;
import com.duocardgame.domain.model.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DeckManager {
    private final Deck drawPile;
    private final List<Card> discardPile;
    private Color currentColor;

    public DeckManager() {
        this.drawPile = new Deck();
        this.discardPile = new ArrayList<>();
    }

    public void resetDecks() {
        drawPile.shuffle();
        discardPile.clear();
    }

    public Optional<Card> drawCard() {
        Optional<Card> drawnCard = drawPile.drawCard();

        // Çekme destesi boş ise, çöp destesini karıştırıp yeni çekme destesi yap
        if (!drawnCard.isPresent() && !discardPile.isEmpty()) {
            Card topCard = discardPile.remove(discardPile.size() - 1);
            drawPile.addCards(discardPile);
            discardPile.clear();
            discardPile.add(topCard);
            drawPile.shuffle();

            System.out.println("\n█ IMPORTANT: Trash deck has been converted to pull deck! █");
            System.out.println("» New pile size: " + drawPile.size());
            System.out.println("» Trash pile size: " + discardPile.size());

            drawnCard = drawPile.drawCard();
            System.out.println("» New deck size after card is drawn: " + drawPile.size());
        }

        return drawnCard;
    }

    public Card drawFirstCard() {
        Optional<Card> firstCard = drawPile.drawCard();
        if (firstCard.isPresent()) {
            Card card = firstCard.get();
            discardPile.add(card);
            currentColor = card.getColor();
            return card;
        }
        return null;
    }

    public void dealInitialCards(List<Player> players, int dealerIndex, int initialCards) {
        for (int i = 0; i < initialCards; i++) {
            for (int j = 0; j < players.size(); j++) {
                int playerIndex = (dealerIndex + 1 + j) % players.size();
                Player player = players.get(playerIndex);

                Optional<Card> drawnCard = drawPile.drawCard();
                if (drawnCard.isPresent()) {
                    player.addCardToHand(drawnCard.get());
                }
            }
        }
    }

    public void addToDiscardPile(Card card) {
        discardPile.add(card);
    }

    public Card getTopCard() {
        if (discardPile.isEmpty()) {
            return null;
        }
        return discardPile.get(discardPile.size() - 1);
    }

    public int getRemainingCardCount() {
        return drawPile.size();
    }

    public int getDiscardPileCount() {
        return discardPile.size();
    }

    public Color getCurrentColor() {
        return currentColor;
    }

    public void setCurrentColor(Color color) {
        this.currentColor = color;
    }

    public boolean isEmpty() {
        return drawPile.isEmpty();
    }

    public Deck getDrawPile() {
        return drawPile;
    }

    public List<Card> getDiscardPile() {
        return discardPile;
    }
} 