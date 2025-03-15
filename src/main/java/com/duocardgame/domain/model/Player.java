package com.duocardgame.domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

public class Player {
    private final String name;
    private final List<Card> hand;
    private int totalScore;
    private final Random random;

    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
        this.totalScore = 0;
        this.random = new Random();
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

    // Eldeki kartların toplam puan değerini hesapla
    public int calculateHandValue() {
        return hand.stream().mapToInt(Card::getPointValue).sum();
    }

    // Oynanabilir kartları bul
    public List<Card> getPlayableCards(Card topCard) {
        List<Card> playableCards = new ArrayList<>();

        // Önce eldeki kartlarda topCard'ın rengiyle eşleşen kart var mı kontrol et
        boolean hasMatchingColorCard = hasMatchingColorCard(topCard.getColor());
        for (Card card : hand) {
            if (card.isPlayable(topCard) && card.getType() != CardType.WILD_DRAW_FOUR) {
                playableCards.add(card);
            } else if (card.getType() == CardType.WILD || card.getType() == CardType.SHUFFLE_HANDS) {
                // Wild kartlar ve shuffle hands her zaman oynanabilir
                playableCards.add(card);
            } else if (card.getType() == CardType.WILD_DRAW_FOUR) {
                // Wild Draw Four kartı sadece eldeki kartlarda topCard'ın rengiyle eşleşen kart
                // yoksa oynanabilir
                if (!hasMatchingColorCard) {
                    playableCards.add(card);
                }
            }
        }
        System.out.println("Oynanabilir kartlar: " + playableCards);
        return playableCards;
    }

    // Eldeki kartlar arasında belirli bir renge sahip kart var mı kontrol et
    private boolean hasMatchingColorCard(Color color) {
        for (Card card : hand) {
            // Wild kartları hariç tut, sadece normal renk kartlarını kontrol et
            if (card.getColor() == color && card.getType() != CardType.WILD
                    && card.getType() != CardType.WILD_DRAW_FOUR) {
                return true;
            }
        }
        return false;
    }

    // Rastgele bir kart oynama stratejisi
    public Optional<Card> playCard(Card topCard) {
        List<Card> playableCards = getPlayableCards(topCard);

        if (playableCards.isEmpty()) {
            return Optional.empty();
        }

        // Eğer aynı renk ve aynı sayı/tip kartlar varsa, rastgele seçim yap
        boolean playColorCard = random.nextBoolean();

        List<Card> sameColorCards = new ArrayList<>();
        List<Card> sameNumberOrTypeCards = new ArrayList<>();
        List<Card> wildCards = new ArrayList<>();

        for (Card card : playableCards) {
            if (card.getType() == CardType.WILD || card.getType() == CardType.WILD_DRAW_FOUR) {
                wildCards.add(card);
            } else if (card.getColor() == topCard.getColor()) {
                sameColorCards.add(card);
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

        hand.remove(selectedCard);
        return Optional.of(selectedCard);
    }

    // Joker kartlar için en uygun rengi seç (eldeki renklerin dağılımına göre)
    public Color chooseColor() {
        Map<Color, Integer> colorCounts = new HashMap<>();

        for (Card card : hand) {
            Color cardColor = card.getColor();
            // Joker kartları sayma
            if (cardColor != Color.WILD) {
                colorCounts.put(cardColor, colorCounts.getOrDefault(cardColor, 0) + 1);
            }
        }

        // En çok sahip olunan rengi bul
        Color maxColor = Color.BLUE; // Varsayılan renk
        int maxCount = 0;

        for (Map.Entry<Color, Integer> entry : colorCounts.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                maxColor = entry.getKey();
            }
        }

        // Eğer hiç renk kart yoksa veya eşit sayıda kart varsa rastgele bir renk seç
        if (maxCount == 0 || colorCounts.values().stream().distinct().count() == 1) {
            Color[] colors = { Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW };
            return colors[random.nextInt(colors.length)];
        }

        return maxColor;
    }

    // Kartı elden çıkar
    public void removeCardFromHand(Card card) {
        hand.remove(card);
    }

    @Override
    public String toString() {
        return name + " (Skor: " + totalScore + ")";
    }
}