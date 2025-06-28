// src/com/yatzee/yatzee_api/controller/YahtzeeController.java
package com.yatzee.yatzee_api.controller;

import com.yatzee.yatzee_api.dto.YahtzeeGameState;
import com.yatzee.yatzee_api.engine.YahtzeeGameEngine;
import com.yatzee.yatzee_api.service.YahtzeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class YahtzeeController {

    private static final Logger logger = LoggerFactory.getLogger(YahtzeeController.class);

    @Autowired
    private YahtzeeService yahtzeeService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // Endpoint for a client to request the initial state
    @MessageMapping("/yatzee/{gameId}/getState")
    public void getState(@DestinationVariable Long gameId) {
        logger.info("Received request for initial Yahtzee state for game {}", gameId);
        broadcastGameState(gameId);
    }

    // Endpoint for a client to roll the dice
    @MessageMapping("/yatzee/{gameId}/roll")
    public void roll(@DestinationVariable Long gameId) {
        YahtzeeGameEngine engine = yahtzeeService.getEngine(gameId);
        engine.roll();
        logger.info("Dice rolled for game {}", gameId);
        broadcastGameState(gameId);
    }

    // Endpoint for a client to hold/unhold a die
    @MessageMapping("/yatzee/{gameId}/hold")
    public void hold(@DestinationVariable Long gameId, @Payload Integer dieIndex) {
        YahtzeeGameEngine engine = yahtzeeService.getEngine(gameId);
        engine.toggleHold(dieIndex);
        logger.info("Die at index {} toggled for game {}", dieIndex, gameId);
        broadcastGameState(gameId);
    }

    // Endpoint for a client to score a category
    @MessageMapping("/yatzee/{gameId}/score")
    public void score(@DestinationVariable Long gameId, @Payload String category) {
        YahtzeeGameEngine engine = yahtzeeService.getEngine(gameId);
        boolean success = engine.scoreTurn(category);
        if (success) {
            logger.info("Category '{}' scored for game {}", category, gameId);
        } else {
            logger.warn("Failed to score category '{}' for game {}", category, gameId);
        }
        broadcastGameState(gameId);
    }

    // Helper method to broadcast the current state to all players in the game
    private void broadcastGameState(Long gameId) {
        YahtzeeGameState gameState = yahtzeeService.getGameState(gameId);
        String destination = "/topic/yatzee/" + gameId + "/state";
        messagingTemplate.convertAndSend(destination, gameState);
        logger.info("Broadcasted Yahtzee state to {}", destination);
    }
}