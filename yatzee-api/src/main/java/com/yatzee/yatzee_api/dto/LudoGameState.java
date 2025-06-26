//src/com/yatzee/yatzee_api/dto/LudoGameState
package com.yatzee.yatzee_api.dto;

import com.yatzee.yatzee_api.enums.Color;
import com.yatzee.yatzee_api.model.PlayerState;

import java.util.Map;

public class LudoGameState {

    private Color currentTurn;
    private Map<Color, PlayerState> playerStates;
    private boolean gameCompleted;

    public LudoGameState() {}

    public LudoGameState(Color currentTurn, Map<Color, PlayerState> playerStates) {
        this.currentTurn = currentTurn;
        this.playerStates = playerStates;
        this.gameCompleted = calculateGameCompletion(playerStates);
    }

    public Color getCurrentTurn() {
        return currentTurn;
    }

    public void setCurrentTurn(Color currentTurn) {
        this.currentTurn = currentTurn;
    }

    public Map<Color, PlayerState> getPlayerStates() {
        return playerStates;
    }

    public void setPlayerStates(Map<Color, PlayerState> playerStates) {
        this.playerStates = playerStates;
        this.gameCompleted = calculateGameCompletion(playerStates);
    }

    public boolean isGameCompleted() {
        return gameCompleted;
    }

    public void setGameCompleted(boolean gameCompleted) {
        this.gameCompleted = gameCompleted;
    }

    // Utility method to check if any player has won
    private boolean calculateGameCompletion(Map<Color, PlayerState> playerStates) {
        return playerStates.values().stream().anyMatch(PlayerState::isCompleted);
    }
}
