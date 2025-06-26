// src/com/yatzee/yatzee_api/controller/LudoController.java
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
    }

    @MessageMapping("/ludo/{gameId}/move")
    public void makeMove(@DestinationVariable Long gameId, @Payload LudoMoveRequest request) {
        LudoGameState updatedState = ludoService.makeMove(
                request.getGameId(),
                request.getPlayerColor(),
                request.getTokenId()
        );
        String destination = "/topic/game/" + gameId + "/state";
        messagingTemplate.convertAndSend(destination, updatedState);
    }
}