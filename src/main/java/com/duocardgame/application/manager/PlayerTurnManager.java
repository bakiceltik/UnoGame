package com.duocardgame.application.manager;

import com.duocardgame.domain.model.Card;
import com.duocardgame.domain.model.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class PlayerTurnManager {
    private int currentPlayerIndex;
    private int dealerIndex;
    private boolean isClockwise;

    public PlayerTurnManager() {
        this.isClockwise = true; // Varsayılan yön saat yönü (sağa doğru)
    }

    public void determineFirstDealer(List<Player> players, DeckManager deckManager) {
        // Her oyuncuya bir kart dağıt, en yüksek karta sahip olan kurpiyedir
        List<Card> dealerCards = new ArrayList<>();
        List<Integer> cardValues = new ArrayList<>();

        for (int i = 0; i < players.size(); i++) {
            Optional<Card> drawnCard = deckManager.drawCard();
            if (drawnCard.isPresent()) {
                Card card = drawnCard.get();
                dealerCards.add(card);
                cardValues.add(card.getPointValue());
            }
        }

        // En yüksek değere sahip oyuncuyu bul
        int maxValue = Collections.max(cardValues);
        dealerIndex = cardValues.indexOf(maxValue);

        // Dağıtılan kartları tekrar desteye ekle ve karıştır
        for (Card card : dealerCards) {
            deckManager.getDrawPile().addCard(card);
        }
        deckManager.getDrawPile().shuffle();
    }

    public void nextTurn(int playerCount) {
        if (isClockwise) {
            currentPlayerIndex = (currentPlayerIndex + 1) % playerCount;
        } else {
            currentPlayerIndex = (currentPlayerIndex - 1 + playerCount) % playerCount;
        }
    }

    public Player getNextPlayer(List<Player> players) {
        if (isClockwise) {
            return players.get((currentPlayerIndex + 1) % players.size());
        } else {
            return players.get((currentPlayerIndex - 1 + players.size()) % players.size());
        }
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public void setCurrentPlayerIndex(int index) {
        this.currentPlayerIndex = index;
    }

    public int getDealerIndex() {
        return dealerIndex;
    }

    public void setDealerIndex(int index) {
        this.dealerIndex = index;
    }

    public boolean isClockwise() {
        return isClockwise;
    }

    public void reverseDirection() {
        this.isClockwise = !this.isClockwise;
    }
} 