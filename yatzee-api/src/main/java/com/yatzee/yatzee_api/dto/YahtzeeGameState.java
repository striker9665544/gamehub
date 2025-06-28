// src/com/yatzee/yatzee_api/dto/YahtzeeGameState.java
package com.yatzee.yatzee_api.dto;

import com.yatzee.yatzee_api.model.YahtzeeScoreCard;
import java.util.List;
import java.util.Map;

public class YahtzeeGameState {
    private List<Integer> dice;
    private boolean[] held;
    private int rollsLeft;
    private Map<String, Integer> scoreCard; // We'll send the map directly

    public YahtzeeGameState(List<Integer> dice, boolean[] held, int rollsLeft, YahtzeeScoreCard scoreCard) {
        this.dice = dice;
        this.held = held;
        this.rollsLeft = rollsLeft;
        this.scoreCard = scoreCard.getScores();
    }
    // Getters and Setters...
}