// src/com/yatzee/yatzee_api/model/YahtzeeScoreCard.java
package com.yatzee.yatzee_api.model;

import java.util.HashMap;
import java.util.Map;

public class YahtzeeScoreCard {
    // This map will store the scores for each category, e.g., "aces", "threeOfAKind"
    // A null value means the category has not been scored yet.
    private Map<String, Integer> scores = new HashMap<>();

    public YahtzeeScoreCard() {
        // Initialize all categories with null
        scores.put("aces", null);
        scores.put("twos", null);
        scores.put("threes", null);
        scores.put("fours", null);
        scores.put("fives", null);
        scores.put("sixes", null);
        scores.put("threeOfAKind", null);
        scores.put("fourOfAKind", null);
        scores.put("fullHouse", null);
        scores.put("smallStraight", null);
        scores.put("largeStraight", null);
        scores.put("yahtzee", null);
        scores.put("chance", null);
    }

    public Map<String, Integer> getScores() {
        return scores;
    }

    public boolean setScore(String category, int score) {
        if (scores.containsKey(category) && scores.get(category) == null) {
            scores.put(category, score);
            return true;
        }
        return false; // Category already scored or invalid
    }

    public int getTotalScore() {
        // Simple sum for now. A full implementation would include the upper section bonus.
        return scores.values().stream().filter(s -> s != null).mapToInt(Integer::intValue).sum();
    }
}