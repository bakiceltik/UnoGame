package com.duocardgame.dataaccess.repository;

import java.util.List;

public interface GameRepository {
    void saveGameState(List<String[]> gameState);
    int getRoundCount();
    List<String[]> readAllGameStates();
} 