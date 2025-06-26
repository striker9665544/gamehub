// src/com/yatzee/yatzee_api/dto/GameCreateRequest.java
package com.yatzee.yatzee_api.dto;

import com.yatzee.yatzee_api.enums.GameType;

public class GameCreateRequest {
    private GameType type;
    private Integer playerCount; // For Ludo (2, 3, or 4)

    // Getters and Setters
    public GameType getType() { return type; }
    public void setType(GameType type) { this.type = type; }
    public Integer getPlayerCount() { return playerCount; }
    public void setPlayerCount(Integer playerCount) { this.playerCount = playerCount; }
}