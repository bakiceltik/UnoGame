package com.duocardgame.domain.handler;

import java.util.Arrays;

import com.duocardgame.domain.model.Card;
import com.duocardgame.domain.model.CardType;
import com.duocardgame.domain.model.Color;
import com.duocardgame.domain.strategy.IColorChooser;

/**
 * Wild kartlarının özel kurallarını uygulayan sınıf
 */
public class WildCardHandler {
    
    /**
     * Wild Draw Four kartı için oynanabilirlik kuralı
     * 
     * @param hand Oyuncunun eli
     * @param currentColor Mevcut oyun rengi
     * @return Kart oynanabilirse true, aksi halde false
     */
    public boolean canPlayWildDrawFour(Card[] hand, Color currentColor) {
        // Wild Draw Four kartı, ancak elde mevcut renkte başka kart yoksa oynanabilir
        for (Card card : hand) {
            // Wild kartları hariç tut, sadece normal renk kartlarını kontrol et
            if (card.getColor() == currentColor && 
                card.getType() != CardType.WILD &&
                card.getType() != CardType.WILD_DRAW_FOUR) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Wild kart oynandıktan sonra yeni rengi belirleme
     * 
     * @param colorChooser Renk seçme stratejisi
     * @param hand Oyuncunun eli
     * @return Seçilen yeni renk
     */
    public Color chooseColorAfterWild(IColorChooser colorChooser, Card[] hand) {
        return colorChooser.chooseColor(Arrays.asList(hand));
    }
} 