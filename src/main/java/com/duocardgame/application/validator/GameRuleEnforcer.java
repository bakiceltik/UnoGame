package com.duocardgame.application.validator;

import com.duocardgame.domain.model.Card;
import com.duocardgame.domain.model.CardType;
import com.duocardgame.domain.model.Color;
import com.duocardgame.domain.model.Player;

public class GameRuleEnforcer {

    public boolean isCardPlayable(Player player, Card card, Card topCard, Color currentColor) {
        // Wild Draw Four kartı için özel kontrol
        if (card.getType() == CardType.WILD_DRAW_FOUR) {
            // Oyuncunun elinde, mevcut renkte kart olup olmadığını kontrol et
            boolean hasMatchingColorCard = false;
            for (Card handCard : player.getHand()) {
                // Wild kartları hariç tut, sadece normal renk kartlarını kontrol et
                if (handCard != card && handCard.getColor() == topCard.getColor() &&
                        handCard.getType() != CardType.WILD && handCard.getType() != CardType.WILD_DRAW_FOUR) {
                    hasMatchingColorCard = true;
                    break;
                }
            }

            // Eğer oyuncunun elinde eşleşen renkte kart varsa ve bu bir Wild Draw Four
            // kartıysa, oynanamaz
            if (hasMatchingColorCard) {
                return false;
            }
        }

        // Normal kart uyumluluğu kontrolü
        if (card.isPlayable(topCard) || card.getColor() == currentColor ||
                card.getType() == CardType.WILD || card.getType() == CardType.WILD_DRAW_FOUR ||
                card.getType() == CardType.SHUFFLE_HANDS) {
            return true;
        }

        return false;
    }

    public boolean canPlayDrawnCard(Player player, Card drawnCard, Card topCard, Color currentColor) {
        // Temel oynanabilirlik kurallarını kontrol et
        if (drawnCard.isPlayable(topCard) || drawnCard.getColor() == currentColor) {
            return true;
        }
        // Wild kartı sadece eldeki kartlarda topCard'ın rengiyle veya mevcut renkle eşleşen kart yoksa oynanabilir
        else if (drawnCard.getType() == CardType.WILD) {
            // Oyuncunun elinde eşleşen renkte başka kart var mı kontrol et
            boolean hasMatchingColorCard = false;
            for (Card handCard : player.getHand()) {
                // Wild kartları hariç tut, sadece normal renk kartlarını kontrol et
                if (handCard != drawnCard &&
                        (handCard.getColor() == topCard.getColor() || handCard.getColor() == currentColor) &&
                        handCard.getType() != CardType.WILD &&
                        handCard.getType() != CardType.WILD_DRAW_FOUR) {
                    hasMatchingColorCard = true;
                    break;
                }
            }

            // Eğer oyuncunun elinde eşleşen renkte kart yoksa Wild oynanabilir
            if (!hasMatchingColorCard) {
                return true;
            }
        }
        // Wild Draw Four kartı özel duruma bağlı
        else if (drawnCard.getType() == CardType.WILD_DRAW_FOUR) {
            // Oyuncunun elinde eşleşen renkte başka kart var mı kontrol et
            boolean hasMatchingColorCard = false;
            for (Card handCard : player.getHand()) {
                // Wild kartları hariç tut, sadece normal renk kartlarını kontrol et
                if (handCard != drawnCard &&
                        (handCard.getColor() == topCard.getColor() || handCard.getColor() == currentColor) &&
                        handCard.getType() != CardType.WILD &&
                        handCard.getType() != CardType.WILD_DRAW_FOUR) {
                    hasMatchingColorCard = true;
                    break;
                }
            }

            // Eğer oyuncunun elinde eşleşen renkte kart yoksa Wild Draw Four oynanabilir
            if (!hasMatchingColorCard) {
                return true;
            }
        }

        return false;
    }
} 