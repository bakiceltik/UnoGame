package com.duocardgame.domain.strategy;

import java.util.List;
import java.util.Optional;
import com.duocardgame.domain.model.Card;
import com.duocardgame.domain.model.Color;

/**
 * Kart oynama stratejisi tanımlayan arayüz.
 * Farklı stratejilerin uygulanabilmesi için kullanılır.
 */
public interface IPlayStrategy {
    
    /**
     * Oynanacak kartı seçer
     * 
     * @param playableCards Oynanabilir kartlar listesi
     * @param topCard Yığının en üstündeki kart
     * @param currentColor Mevcut oyun rengi
     * @param hand Oyuncunun eli
     * @return Seçilen kart, eğer seçilemediyse empty
     */
    Optional<Card> selectCard(List<Card> playableCards, Card topCard, Color currentColor, List<Card> hand);
} 