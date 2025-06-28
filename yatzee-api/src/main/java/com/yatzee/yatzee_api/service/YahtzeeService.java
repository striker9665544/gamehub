// src/com/yatzee/yatzee_api/service/YahtzeeService.java
package com.yatzee.yatzee_api.service;

import com.yatzee.yatzee_api.dto.YahtzeeGameState;
import com.yatzee.yatzee_api.engine.YahtzeeGameEngine;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class YahtzeeService {
    private final Map<Long, YahtzeeGameEngine> gameEngines = new ConcurrentHashMap<>();

    public void createGameEngine(Long gameId) {
        gameEngines.put(gameId, new YahtzeeGameEngine());
    }

    public YahtzeeGameEngine getEngine(Long gameId) {
        YahtzeeGameEngine engine = gameEngines.get(gameId);
        if (engine == null) {
            throw new IllegalArgumentException("Yahtzee game not found: " + gameId);
        }
        return engine;
    }

    public YahtzeeGameState getGameState(Long gameId) {
        YahtzeeGameEngine engine = getEngine(gameId);
        return new YahtzeeGameState(engine.getDice(), engine.getHeld(), engine.getRollsLeft(), engine.getScoreCard());
    }
}