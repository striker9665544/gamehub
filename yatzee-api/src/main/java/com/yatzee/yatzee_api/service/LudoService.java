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

    public LudoGameEngine getEngine(Long gameId) {
        LudoGameEngine engine = gameEngines.get(gameId);
        if (engine == null) {
            logger.error("FATAL: Ludo game engine not found for ID: {}. Available engines: {}", gameId, gameEngines.keySet());
            throw new IllegalArgumentException("Game engine not found for ID: " + gameId);
        }
        logger.info("Successfully retrieved engine for game ID: {}", gameId);
        return engine;
    }
    
    
    public void createGameEngine(Long gameId, Map<Color, Boolean> playerConfig) {
        logger.info("Creating Ludo game engine for game ID: {} with config: {}", gameId, playerConfig);
        LudoGameEngine engine = new LudoGameEngine(playerConfig);
        gameEngines.put(gameId, engine);
        logger.info("Successfully created Ludo game engine for game ID: {}", gameId);
    }

    // ✅ ADD THIS METHOD: The core bot AI
    public LudoGameState playBotTurn(Long gameId) {
        LudoGameEngine engine = getEngine(gameId);
        
        if (!engine.isBotTurn()) {
            logger.warn("playBotTurn called but it's not a bot's turn. Current turn: {}", engine.getCurrentTurn());
            return getGameState(gameId); // Return current state without changes
        }
        
        logger.info("Playing bot turn for game {} and color {}", gameId, engine.getCurrentTurn());
        
        // 1. Bot rolls the dice
        engine.rollDice();
        
        // 2. Bot gets a list of possible moves
        List<Integer> validMoves = engine.getValidMoves();
        
        // 3. Bot makes a decision (simple AI: pick the first valid move)
        if (!validMoves.isEmpty()) {
            int tokenIdToMove = validMoves.get(0); // Simple AI: move the first possible token
            logger.info("Bot chose to move token ID: {}", tokenIdToMove);
            engine.moveToken(engine.getCurrentTurn(), tokenIdToMove);
        } else {
            // No valid moves, so the turn ends
            logger.info("Bot has no valid moves. Ending turn.");
            engine.endTurn(); // A new method we need to add to the engine
        }
        
        // 4. Return the updated game state
        return getGameState(gameId);
    }
}