package com.duocardgame.domain.rules;

import com.duocardgame.domain.model.Card;
import com.duocardgame.domain.model.Color;

public interface IPlayable {
    /**
     * Kartın oynanabilir olup olmadığını kontrol eder
     * 
     * @param card Oynanmak istenen kart
     * @param topCard Yığının en üstündeki kart
     * @param currentColor Oyundaki mevcut renk
     * @return Kart oynanabilirse true, aksi halde false
     */
    boolean isPlayable(Card card, Card topCard, Color currentColor);
    
    /**
     * Kartın oynanabilir olup olmadığını kontrol eder
     * 
     * @param topCard Yığının en üstündeki kart
     * @param currentColor Oyundaki mevcut renk
     * @return Kart oynanabilirse true, aksi halde false
     */
    boolean isPlayable(Card topCard, Color currentColor);
} 