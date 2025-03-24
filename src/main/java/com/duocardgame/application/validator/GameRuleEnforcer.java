package com.duocardgame.application.validator;

import com.duocardgame.domain.model.Card;
import com.duocardgame.domain.model.CardType;
import com.duocardgame.domain.model.Color;
import com.duocardgame.domain.model.Player;

public class GameRuleEnforcer {

    public boolean isCardPlayable(Player player, Card card, Card topCard, Color currentColor) {
        if (card.getType() == CardType.WILD_DRAW_FOUR) {
            boolean hasMatchingColorCard = false;
            for (Card handCard : player.getHand()) {
                if (handCard != card && handCard.getColor() == topCard.getColor() &&
                        handCard.getType() != CardType.WILD && handCard.getType() != CardType.WILD_DRAW_FOUR) {
                    hasMatchingColorCard = true;
                    break;
                }
            }
            if (hasMatchingColorCard) {
                return false;
            }
        }

        if (card.isPlayable(topCard) || card.getColor() == currentColor ||
                card.getType() == CardType.WILD || card.getType() == CardType.WILD_DRAW_FOUR ||
                card.getType() == CardType.SHUFFLE_HANDS) {
            return true;
        }

        return false;
    }

    public boolean canPlayDrawnCard(Player player, Card drawnCard, Card topCard, Color currentColor) {
        if (drawnCard.isPlayable(topCard) || drawnCard.getColor() == currentColor) {
            return true;
        }
 
        else if (drawnCard.getType() == CardType.WILD) {
            boolean hasMatchingColorCard = false;
            for (Card handCard : player.getHand()) {
                if (handCard != drawnCard &&
                        (handCard.getColor() == topCard.getColor() || handCard.getColor() == currentColor) &&
                        handCard.getType() != CardType.WILD &&
                        handCard.getType() != CardType.WILD_DRAW_FOUR) {
                    hasMatchingColorCard = true;
                    break;
                }
            }

            if (!hasMatchingColorCard) {
                return true;
            }
        }
        else if (drawnCard.getType() == CardType.WILD_DRAW_FOUR) {
            boolean hasMatchingColorCard = false;
            for (Card handCard : player.getHand()) {
                if (handCard != drawnCard &&
                        (handCard.getColor() == topCard.getColor() || handCard.getColor() == currentColor) &&
                        handCard.getType() != CardType.WILD &&
                        handCard.getType() != CardType.WILD_DRAW_FOUR) {
                    hasMatchingColorCard = true;
                    break;
                }
            }

            if (!hasMatchingColorCard) {
                return true;
            }
        }

        return false;
    }
} 