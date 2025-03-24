package com.duocardgame.domain.strategy;

import java.util.List;
import com.duocardgame.domain.model.Card;
import com.duocardgame.domain.model.Color;

/**
 * Joker kartlar için renk seçme stratejisi tanımlayan arayüz.
 */
public interface IColorChooser {
    
    /**
     * Eldeki kartlara bakarak en uygun rengi seçer
     * 
     * @param hand Oyuncunun eli
     * @return Seçilen renk
     */
    Color chooseColor(List<Card> hand);
} 