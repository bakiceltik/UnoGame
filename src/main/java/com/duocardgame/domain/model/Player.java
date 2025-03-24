package com.duocardgame.domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.duocardgame.domain.rules.IPlayable;
import com.duocardgame.domain.strategy.IColorChooser;
import com.duocardgame.domain.strategy.IPlayStrategy;

public class Player {
    private final String name;
    private final List<Card> hand;
    private int totalScore;
    private final IPlayStrategy playStrategy;
    private final IColorChooser colorChooser;

    public Player(String name, IPlayStrategy playStrategy, IColorChooser colorChooser) {
        this.name = name;
        this.hand = new ArrayList<>();
        this.totalScore = 0;
        this.playStrategy = playStrategy;
        this.colorChooser = colorChooser;
    }

    public String getName() {
        return name;
    }

    public List<Card> getHand() {
        return Collections.unmodifiableList(hand);
    }

    public void addCardToHand(Card card) {
        hand.add(card);
    }

    public void clearHand() {
        hand.clear();
    }

    public int getHandSize() {
        return hand.size();
    }

    public boolean hasEmptyHand() {
        return hand.isEmpty();
    }

    public void addToScore(int points) {
        this.totalScore += points;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public int calculateHandValue() {
        return hand.stream().mapToInt(Card::getPointValue).sum();
    }

    public List<Card> getPlayableCards(Card topCard, Color currentColor, IPlayable playRules) {
        List<Card> playableCards = new ArrayList<>();

        for (Card card : hand) {
            if (playRules.isPlayable(card, topCard, currentColor)) {
                playableCards.add(card);
            }
        }
        
        return playableCards;
    }

    public Optional<Card> playCard(Card topCard, Color currentColor, IPlayable playRules) {
        List<Card> playableCards = getPlayableCards(topCard, currentColor, playRules);
        
        if (playableCards.isEmpty()) {
            return Optional.empty();
        }
        
        Optional<Card> selectedCard = playStrategy.selectCard(playableCards, topCard, currentColor, hand);
        
        selectedCard.ifPresent(this::removeCardFromHand);
        return selectedCard;
    }

    public Color chooseColor() {
        return colorChooser.chooseColor(hand);
    }

    public void removeCardFromHand(Card card) {
        hand.remove(card);
    }

    @Override
    public String toString() {
        return name + " (Skor: " + totalScore + ")";
    }
}