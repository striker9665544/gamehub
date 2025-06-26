//src/com/yatzee/yatzee_api/dto/DiceRollResult
package com.yatzee.yatzee_api.dto;

import com.yatzee.yatzee_api.enums.Color;

public class DiceRollResult {
    private int value;
    private Color nextTurn;

    public DiceRollResult() {
    }

    public DiceRollResult(int value, Color nextTurn) {
        this.value = value;
        this.nextTurn = nextTurn;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Color getNextTurn() {
        return nextTurn;
    }

    public void setNextTurn(Color nextTurn) {
        this.nextTurn = nextTurn;
    }
}
