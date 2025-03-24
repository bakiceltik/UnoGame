package com.duocardgame.application.handler;

import com.duocardgame.application.mediator.GameMediator;
import com.duocardgame.domain.model.Card;
import com.duocardgame.domain.model.CardType;
import com.duocardgame.domain.model.Color;
import com.duocardgame.domain.model.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CardEffectHandler {
    private final GameMediator gameMediator;

    public CardEffectHandler(GameMediator gameMediator) {
        this.gameMediator = gameMediator;
    }

    public void handleFirstActionCard(Card card, List<Player> players, com.duocardgame.application.manager.PlayerTurnManager turnManager) {
        CardType type = card.getType();
        int dealerIndex = turnManager.getDealerIndex();

        switch (type) {
            case WILD:
                // İlk oyuncu renk seçer
                Color selectedColor = players.get((dealerIndex + 1) % players.size()).chooseColor();
                gameMediator.changeColor(selectedColor);
                System.out.println("\n█ Initial Color Selection █");
                System.out.println("» First card is WILD! " + players.get((dealerIndex + 1) % players.size()).getName() +
                        " selected " + selectedColor + " as the starting color!");
                System.out.println("» All players must now play " + selectedColor + " cards or matching card types.");
                break;

            case WILD_DRAW_FOUR:
                // Bu kartı tekrar desteye koy ve yeni bir kart çek
                gameMediator.drawCard(null);
                break;

            case DRAW_TWO:
                // İlk oyuncu iki kart çeker ve turunu kaçırır
                Player nextPlayer = players.get((dealerIndex + 1) % players.size());
                // Önceki kontrolü atlamak için özel durum
                // Şimdi kartı doğrudan ele ekliyoruz
                for (int i = 0; i < 2; i++) {
                    gameMediator.drawCard(nextPlayer);
                }
                // İlk oyuncuyu güncelle
                turnManager.setCurrentPlayerIndex((dealerIndex + 2) % players.size());
                break;

            case REVERSE:
                // Oyun yönünü değiştir ve kurpiye başlar
                turnManager.reverseDirection();
                turnManager.setCurrentPlayerIndex(dealerIndex);
                break;

            case SKIP:
                // İlk oyuncuyu atla
                turnManager.setCurrentPlayerIndex((dealerIndex + 2) % players.size());
                break;

            case SHUFFLE_HANDS:
                // İlk oyuncu renk seçer
                Color selectedColorShuffle = players.get((dealerIndex + 1) % players.size()).chooseColor();
                gameMediator.changeColor(selectedColorShuffle);
                System.out.println("\n█ Initial Color Selection █");
                System.out.println("» First card is SHUFFLE_HANDS! " + players.get((dealerIndex + 1) % players.size()).getName() +
                        " selected " + selectedColorShuffle + " as the starting color!");
                System.out.println("» All players must now play " + selectedColorShuffle + " cards or matching card types.");
                break;

            default:
                break;
        }
    }

    public void applyCardEffect(Player player, Card card) {
        CardType type = card.getType();

        switch (type) {
            case DRAW_TWO:
                Player nextPlayer = gameMediator.getPlayers().get((gameMediator.getPlayers().indexOf(player) + 1) % gameMediator.getPlayers().size());
                // ÖNEMLİ: Sırayı değiştirmeden ÖNCE kart çekme işlemini yapalım
                // Özel bir durum olarak "force" değerini true olarak gönderelim
                forceDrawCards(nextPlayer, 2);
                // Şimdi sırayı değiştirelim
                gameMediator.nextTurn();
                break;

            case REVERSE:
                // PlayerTurnManager'i direkt olarak kullanamıyoruz, bu yüzden GameMediator üzerinden çağrıyoruz
                gameMediator.nextTurn(); // Bu metod içinde yön değiştirilmeli
                break;

            case SKIP:
                // Bir sonraki oyuncuyu atlamak için iki kez nextTurn() çağrılması gerekir
                gameMediator.nextTurn(); // Bir sonraki oyuncuya geç
                gameMediator.nextTurn(); // Bir sonraki oyuncuyu atla - yani ikinci oyuncuya geç
                break;

            case WILD:
                Color selectedColor = player.chooseColor();
                gameMediator.changeColor(selectedColor);
                System.out.println("\n█ Color Selection █");
                System.out.println("» " + player.getName() + " selected " + selectedColor + " as the new color!");
                System.out.println("» All players must now play " + selectedColor + " cards or matching card types.");
                break;

            case WILD_DRAW_FOUR:
                Player wildDrawFourNextPlayer = gameMediator.getPlayers().get((gameMediator.getPlayers().indexOf(player) + 1) % gameMediator.getPlayers().size());
                Color selectedColorWild4 = player.chooseColor();
                gameMediator.changeColor(selectedColorWild4);
                System.out.println("\n█ Color Selection █");
                System.out.println("» " + player.getName() + " selected " + selectedColorWild4 + " as the new color!");
                System.out.println("» All players must now play " + selectedColorWild4 + " cards or matching card types.");
                
                // ÖNEMLİ: Sırayı değiştirmeden ÖNCE kart çekme işlemini yapalım
                forceDrawCards(wildDrawFourNextPlayer, 4);
                // Sonra sırayı değiştirelim
                gameMediator.nextTurn();
                break;

            case SHUFFLE_HANDS:
                shuffleHands(player, gameMediator.getPlayers());
                Color selectedColorShuffle = player.chooseColor();
                gameMediator.changeColor(selectedColorShuffle);
                System.out.println("\n█ Color Selection █");
                System.out.println("» " + player.getName() + " selected " + selectedColorShuffle + " as the new color!");
                System.out.println("» All players must now play " + selectedColorShuffle + " cards or matching card types.");
                break;

            default:
                break;
        }
    }

    private void shuffleHands(Player currentPlayer, List<Player> players) {
        List<Card> allCards = new ArrayList<>();

        // Tüm oyuncuların kartlarını topla
        for (Player player : players) {
            allCards.addAll(player.getHand());
            player.clearHand();
        }

        // Kartları karıştır
        Collections.shuffle(allCards);

        // Kartları yeniden dağıt
        int cardIndex = 0;
        int currentPlayerIndex = players.indexOf(currentPlayer);
        while (cardIndex < allCards.size()) {
            for (int i = 0; i < players.size() && cardIndex < allCards.size(); i++) {
                int playerIndex = (currentPlayerIndex + 1 + i) % players.size();
                players.get(playerIndex).addCardToHand(allCards.get(cardIndex++));
            }
        }
    }

    /**
     * Özel kart etkilerinde (DRAW_TWO ve WILD_DRAW_FOUR) sırası değişmeden
     * kart çekilmesini sağlamak için yardımcı metod
     */
    private void forceDrawCards(Player player, int count) {
        System.out.println("\n█ Special Card Effect - Force Drawing Cards █");
        System.out.println("» " + player.getName() + " must draw " + count + " cards!");
        
        for (int i = 0; i < count; i++) {
            try {
                // GameMediator üzerinden özel bir drawCard metodu çağrısı yapabiliriz
                // ya da direkt olarak DeckManager'a erişemediğimiz için burada özel durum yaratmalıyız
                
                // DeckManager'dan kart çek
                java.util.Optional<com.duocardgame.domain.model.Card> drawnCard = 
                    ((com.duocardgame.application.mediator.DuoGameMediator)gameMediator).getDrawPileCard();
                
                if (drawnCard.isPresent()) {
                    player.addCardToHand(drawnCard.get());
                    System.out.println("» " + player.getName() + " drew: " + drawnCard.get());
                } else {
                    System.out.println("» No more cards in the deck!");
                    break;
                }
            } catch (Exception e) {
                System.out.println("» Error while drawing card: " + e.getMessage());
            }
        }
        
        System.out.println("» " + player.getName() + " now has " + player.getHand().size() + " cards.");
    }
} 