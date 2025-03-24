package com.duocardgame.domain.strategy;

import java.util.List;
import java.util.Optional;
import com.duocardgame.domain.model.Card;
import com.duocardgame.domain.model.Color;


public interface IPlayStrategy {
    Optional<Card> selectCard(List<Card> playableCards, Card topCard, Color currentColor, List<Card> hand);
} 