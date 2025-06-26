//src/com/yatzee/yatzee_api/model/Token
package com.yatzee.yatzee_api.model;

import com.yatzee.yatzee_api.enums.TokenState;

public class Token {
    private int id;                 // Token number: 0-3
    private int position;          // Board position (-1 if in home)
    private TokenState state;

    public Token(int id) {
        this.id = id;
        this.state = TokenState.HOME;
        this.position = -1;
    }

    // --- Getters & Setters ---
    public int getId() { return id; }
    public int getPosition() { return position; }
    public TokenState getState() { return state; }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setState(TokenState state) {
        this.state = state;
    }

    public boolean isMovable() {
        return state == TokenState.ACTIVE;
    }

    public boolean isAtHome() {
        return state == TokenState.HOME;
    }

    public boolean isCompleted() {
        return state == TokenState.COMPLETED;
    }
}
