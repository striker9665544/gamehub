//src/com/yatzee/yatzee_api/model/PlayerState
package com.yatzee.yatzee_api.model;

import com.yatzee.yatzee_api.enums.Color;

import java.util.ArrayList;
import java.util.List;

public class PlayerState {
    private Color color;
    private List<Token> tokens;
    private boolean isReady;
    private boolean isCompleted;

    public PlayerState(Color color) {
        this.color = color;
        this.tokens = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            tokens.add(new Token(i));
        }
        this.isReady = false;
        this.isCompleted = false;
    }

    // --- Getters & Setters ---
    public Color getColor() { return color; }
    public List<Token> getTokens() { return tokens; }

    public boolean isReady() { return isReady; }
    public void setReady(boolean ready) { isReady = ready; }

    public boolean isCompleted() { return isCompleted; }
    public void setCompleted(boolean completed) { isCompleted = completed; }

    public boolean allTokensCompleted() {
        return tokens.stream().allMatch(Token::isCompleted);
    }

    public Token getTokenById(int id) {
        return tokens.stream()
                .filter(t -> t.getId() == id)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Invalid token id"));
    }
}
