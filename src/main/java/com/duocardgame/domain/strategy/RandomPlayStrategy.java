package com.duocardgame.domain.strategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.duocardgame.domain.model.Card;
import com.duocardgame.domain.model.CardType;
import com.duocardgame.domain.model.Color;
import com.duocardgame.domain.model.NumberCard;

/**
 * Rastgele kart seçme stratejisi
 */
public class RandomPlayStrategy implements IPlayStrategy {
    
    private final Random random;
    
    public RandomPlayStrategy() {
        this.random = new Random();
    }
    
    @Override
    public Optional<Card> selectCard(List<Card> playableCards, Card topCard, Color currentColor, List<Card> hand) {
        if (playableCards.isEmpty()) {
            return Optional.empty();
        }

        // Eğer aynı renk ve aynı sayı/tip kartlar varsa, rastgele seçim yap
        boolean playColorCard = random.nextBoolean();

        List<Card> sameColorCards = new ArrayList<>();
        List<Card> sameNumberOrTypeCards = new ArrayList<>();
        List<Card> wildCards = new ArrayList<>();
        List<Card> currentColorCards = new ArrayList<>();

        for (Card card : playableCards) {
            if (card.getType() == CardType.WILD || card.getType() == CardType.WILD_DRAW_FOUR) {
                wildCards.add(card);
            } else if (card.getColor() == topCard.getColor()) {
                sameColorCards.add(card);
            } else if (card.getColor() == currentColor) {
                currentColorCards.add(card);
            } else if (topCard.getType() == CardType.NUMBER && card.getType() == CardType.NUMBER &&
                    ((NumberCard) card).getNumber() == ((NumberCard) topCard).getNumber()) {
                sameNumberOrTypeCards.add(card);
            } else if (card.getType() == topCard.getType() && card.getType() != CardType.NUMBER) {
                sameNumberOrTypeCards.add(card);
            }
        }

        Card selectedCard;

        // Renk kartlarını oynamayı tercih ediyorsa ve aynı renkte kart varsa
        if (playColorCard && !sameColorCards.isEmpty()) {
            // En yüksek puanlı renk kartını seç
            selectedCard = Collections.max(sameColorCards,
                    (c1, c2) -> Integer.compare(c1.getPointValue(), c2.getPointValue()));
        }
        // Mevcut renk kartlarını oynamayı tercih ediyorsa
        else if (playColorCard && !currentColorCards.isEmpty()) {
            // En yüksek puanlı mevcut renk kartını seç
            selectedCard = Collections.max(currentColorCards,
                    (c1, c2) -> Integer.compare(c1.getPointValue(), c2.getPointValue()));
        }
        // Sayı/tip kartlarını oynamayı tercih ediyorsa ve aynı sayı/tipte kart varsa
        else if (!playColorCard && !sameNumberOrTypeCards.isEmpty()) {
            selectedCard = sameNumberOrTypeCards.get(random.nextInt(sameNumberOrTypeCards.size()));
        }
        // Joker kartı oyna
        else if (!wildCards.isEmpty()) {
            selectedCard = wildCards.get(random.nextInt(wildCards.size()));
        }
        // Rastgele bir oynanabilir kart seç
        else {
            selectedCard = playableCards.get(random.nextInt(playableCards.size()));
        }

        return Optional.of(selectedCard);
    }
} 