package com.yatzee.yatzee_api.dto;

import com.yatzee.yatzee_api.enums.GameType;
import com.yatzee.yatzee_api.enums.OpponentType;

import java.time.LocalDateTime;

public class GameSelectionResponse {

    private Long id;
    private String userEmail;
    private GameType gameType;
    private OpponentType opponentType;
    private Integer playerCount;
    private LocalDateTime createdAt;

    public GameSelectionResponse() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

