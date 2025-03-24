package com.duocardgame.domain.rules;

import com.duocardgame.domain.model.Card;
import com.duocardgame.domain.model.Color;

public interface IPlayable {
 
    boolean isPlayable(Card card, Card topCard, Color currentColor);

    boolean isPlayable(Card topCard, Color currentColor);
} 