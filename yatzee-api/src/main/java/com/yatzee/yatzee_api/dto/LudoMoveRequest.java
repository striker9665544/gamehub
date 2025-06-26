//src/com/yatzee/yatzee_api/dto/LudoMoveRequest
package com.yatzee.yatzee_api.dto;

import com.yatzee.yatzee_api.enums.Color;

public class LudoMoveRequest {
    private Long gameId;
    private Color playerColor;
    private int tokenId;

    public LudoMoveRequest() {
    }

    public LudoMoveRequest(Long gameId, Color playerColor, int tokenId) {
        this.gameId = gameId;
        this.playerColor = playerColor;
        this.tokenId = tokenId;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public Color getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(Color playerColor) {
        this.playerColor = playerColor;
    }

    public int getTokenId() {
        return tokenId;
    }

    public void setTokenId(int tokenId) {
        this.tokenId = tokenId;
    }
}
