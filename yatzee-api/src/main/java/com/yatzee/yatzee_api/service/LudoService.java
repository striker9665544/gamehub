// src/com/yatzee/yatzee_api/service/LudoService.java
package com.yatzee.yatzee_api.service;

import com.yatzee.yatzee_api.dto.DiceRollResult;
import com.yatzee.yatzee_api.dto.LudoGameState;
import com.yatzee.yatzee_api.engine.LudoGameEngine;
import com.yatzee.yatzee_api.enums.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LudoService {

    private static final Logger logger = LoggerFactory.getLogger(LudoService.class);
    private final Map<Long, LudoGameEngine> gameEngines = new ConcurrentHashMap<>();

    public void createGameEngine(Long gameId, List<Color> participatingColors) {
        logger.info("Creating Ludo game engine for game ID: {}", gameId);
        LudoGameEngine engine = new LudoGameEngine(participatingColors);
        gameEngines.put(gameId, engine);
        logger.info("Successfully created Ludo game engine for game ID: {}. Total engines: {}", gameId, gameEngines.size());
    }

    public DiceRollResult rollDice(Long gameId, Color playerColor) {
        LudoGameEngine engine = getEngine(gameId);
        if (!engine.getCurrentTurn().equals(playerColor)) {
            throw new IllegalStateException("Not your turn!");
        }
        int value = engine.rollDice();
        return new DiceRollResult(value, engine.getCurrentTurn());
    }

    public LudoGameState makeMove(Long gameId, Color playerColor, int tokenIndex) {
        LudoGameEngine engine = getEngine(gameId);
        engine.moveToken(playerColor, tokenIndex);
        return new LudoGameState(engine.getCurrentTurn(), engine.getAllPlayerStates());
    }

    public LudoGameState getGameState(Long gameId) {
        logger.info("Attempting to get game state for game ID: {}", gameId);
        return new LudoGameState(getEngine(gameId).getCurrentTurn(), getEngine(gameId).getAllPlayerStates());
    }

    private LudoGameEngine getEngine(Long gameId) {
        LudoGameEngine engine = gameEngines.get(gameId);
        if (engine == null) {
            logger.error("FATAL: Ludo game engine not found for ID: {}. Available engines: {}", gameId, gameEngines.keySet());
            throw new IllegalArgumentException("Game engine not found for ID: " + gameId);
        }
        logger.info("Successfully retrieved engine for game ID: {}", gameId);
        return engine;
    }
}