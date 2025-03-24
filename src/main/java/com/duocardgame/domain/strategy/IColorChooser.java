package com.duocardgame.domain.strategy;

import java.util.List;
import com.duocardgame.domain.model.Card;
import com.duocardgame.domain.model.Color;


public interface IColorChooser {

    Color chooseColor(List<Card> hand);
} 