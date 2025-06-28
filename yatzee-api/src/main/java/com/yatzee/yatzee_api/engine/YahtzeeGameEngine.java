// src/com/yatzee/yatzee_api/engine/YahtzeeGameEngine.java
package com.yatzee.yatzee_api.engine;

import com.yatzee.yatzee_api.model.YahtzeeScoreCard;
import java.util.*;

public class YahtzeeGameEngine {
    private final List<Integer> dice = new ArrayList<>(Arrays.asList(1, 1, 1, 1, 1));
    private final boolean[] held = new boolean[5]; // false means not held
    private int rollsLeft = 3;
    private final Random random = new Random();
    
    // In a multiplayer game, you would have a map of players to scorecards.
    // For now, we'll use a single scorecard for simplicity.
    private final YahtzeeScoreCard scoreCard = new YahtzeeScoreCard();

    public void roll() {
        if (rollsLeft > 0) {
            for (int i = 0; i < 5; i++) {
                if (!held[i]) {
                    dice.set(i, random.nextInt(6) + 1);
                }
            }
            rollsLeft--;
        }
    }

    public void toggleHold(int index) {
        if (index >= 0 && index < 5) {
            held[index] = !held[index];
        }
    }
    
    // This is where you would calculate the score for a given category.
    // This is a complex part of the logic. We'll start with a placeholder.
    private int calculateScore(String category) {
        // Placeholder: returns a random score for demonstration
        return random.nextInt(20);
    }

    public boolean scoreTurn(String category) {
        if (rollsLeft < 3) { // Must have rolled at least once
            int score = calculateScore(category);
            boolean success = scoreCard.setScore(category, score);
            if(success) {
                // Reset for the next turn
                rollsLeft = 3;
                Arrays.fill(held, false);
                return true;
            }
        }
        return false;
    }

    // Getters for the game state
    public List<Integer> getDice() { return dice; }
    public boolean[] getHeld() { return held; }
    public int getRollsLeft() { return rollsLeft; }
    public YahtzeeScoreCard getScoreCard() { return scoreCard; }
}