package com.yatzee.yatzee_api.controller;

import com.yatzee.yatzee_api.dto.DiceRollResult;
import com.yatzee.yatzee_api.dto.LudoGameState;
import com.yatzee.yatzee_api.dto.LudoMoveRequest;
import com.yatzee.yatzee_api.enums.Color;
import com.yatzee.yatzee_api.service.LudoService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class LudoController {

    private final LudoService ludoService;
    private final SimpMessagingTemplate messagingTemplate;

    public LudoController(LudoService ludoService, SimpMessagingTemplate messagingTemplate) {
        this.ludoService = ludoService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/ludo/{gameId}/roll")
    public void rollDice(@DestinationVariable Long gameId, @Payload String playerColorStr) {
        Color playerColor = Color.valueOf(playerColorStr.toUpperCase());
        DiceRollResult result = ludoService.rollDice(gameId, playerColor);
        String destination = "/topic/game/" + gameId + "/dice";
        messagingTemplate.convertAndSend(destination, result);
        
        // After rolling, if there are no valid moves, the turn might switch to a bot.
        LudoGameState currentState = ludoService.getGameState(gameId);
        if (ludoService.getEngine(gameId).isBotTurn()) { // Need to expose getEngine or isBotTurn
            triggerBotTurn(gameId);
        }
    }

    @MessageMapping("/ludo/{gameId}/move")
    public void makeMove(@DestinationVariable Long gameId, @Payload LudoMoveRequest request) {
        // Human makes a move
        LudoGameState updatedState = ludoService.makeMove(
                request.getGameId(),
                request.getPlayerColor(),
                request.getTokenId()
        );
        
        // Broadcast the result of the human's move
        String destination = "/topic/game/" + gameId + "/state";
        messagingTemplate.convertAndSend(destination, updatedState);
        
        // Now, check if it's a bot's turn
        if (ludoService.getEngine(gameId).isBotTurn()) {
            triggerBotTurn(gameId);
        }
    }
    
    private void triggerBotTurn(Long gameId) {
        // Use a separate thread with a delay to simulate the bot "thinking"
        new Thread(() -> {
            try {
                Thread.sleep(1500); // Wait 1.5 seconds
                LudoGameState botState = ludoService.playBotTurn(gameId);
                String destination = "/topic/game/" + gameId + "/state";
                messagingTemplate.convertAndSend(destination, botState);
                
                // After the bot moves, check AGAIN if the new current turn is also a bot
                if (ludoService.getEngine(gameId).isBotTurn()) {
                    triggerBotTurn(gameId); // Recursive call if bot gets another turn (e.g., rolled a 6)
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
}