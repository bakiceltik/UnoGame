package com.duocardgame.application.manager;

import com.duocardgame.dataaccess.repository.GameRepository;
import com.duocardgame.domain.model.Player;

import java.util.ArrayList;
import java.util.List;

public class GameStateManager {
    private final GameRepository gameRepository;

    public GameStateManager(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public void saveGameState(List<Player> players) {
        // Oyun durumunu CSV dosyasına kaydet
        List<String[]> gameState = new ArrayList<>();

        // Başlık satırı
        String[] header = new String[players.size() + 1];
        header[0] = "Round";
        for (int i = 0; i < players.size(); i++) {
            header[i + 1] = players.get(i).getName();
        }
        gameState.add(header);

        // Veri satırı
        String[] data = new String[players.size() + 1];
        data[0] = String.valueOf(gameRepository.getRoundCount() + 1);
        for (int i = 0; i < players.size(); i++) {
            data[i + 1] = String.valueOf(players.get(i).getTotalScore());
        }
        gameState.add(data);

        gameRepository.saveGameState(gameState);

        // Konsola tur sonucunu yazdır
        System.out.println("Round " + (gameRepository.getRoundCount()) + " results were recorded.");
        System.out.println("Player Scores:");
        for (Player player : players) {
            System.out.println(player.getName() + ": " + player.getTotalScore());
        }
        System.out.println();
    }

    public int getRoundCount() {
        return gameRepository.getRoundCount();
    }
} 