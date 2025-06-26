// src/com/yatzee/yatzee_api/dto/LudoGameCreationResponse.java
package com.yatzee.yatzee_api.dto;

import com.yatzee.yatzee_api.entity.Game;

public class LudoGameCreationResponse {
    private Game game;
    private LudoGameState initialState;

    public LudoGameCreationResponse(Game game, LudoGameState initialState) {
        this.game = game;
        this.initialState = initialState;
    }
    // Getters and Setters
    public Game getGame() { return game; }
    public void setGame(Game game) { this.game = game; }
    public LudoGameState getInitialState() { return initialState; }
    public void setInitialState(LudoGameState initialState) { this.initialState = initialState; }
}