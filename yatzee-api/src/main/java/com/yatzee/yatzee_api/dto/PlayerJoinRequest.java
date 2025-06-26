// src/com/yatzee/yatzee_api/dto/PlayerJoinRequest.java
package com.yatzee.yatzee_api.dto;

public class PlayerJoinRequest {
    private Long gameId;
    private String color; // e.g., "RED", "BLUE"

    // Getters and Setters
    public Long getGameId() { return gameId; }
    public void setGameId(Long gameId) { this.gameId = gameId; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
}