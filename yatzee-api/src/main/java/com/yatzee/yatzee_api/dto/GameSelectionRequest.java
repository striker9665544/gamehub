package com.yatzee.yatzee_api.dto;

import com.yatzee.yatzee_api.enums.GameType;
import com.yatzee.yatzee_api.enums.OpponentType;

public class GameSelectionRequest {

    private String userEmail;
    private GameType gameType;
    private OpponentType opponentType;
    private Integer playerCount; // Optional – required only for LUDO

    public GameSelectionRequest() {}

    // Getters and Setters
    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public GameType getGameType() {
        return gameType;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public OpponentType getOpponentType() {
        return opponentType;
    }

    public void setOpponentType(OpponentType opponentType) {
        this.opponentType = opponentType;
    }

    public Integer getPlayerCount() {
        return playerCount;
    }

    public void setPlayerCount(Integer playerCount) {
        this.playerCount = playerCount;
    }
}

