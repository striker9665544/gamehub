// src/com/yatzee/yatzee_api/controller/GameController.java
package com.yatzee.yatzee_api.controller;

import com.yatzee.yatzee_api.dto.GameCreateRequest;
import com.yatzee.yatzee_api.dto.LudoGameCreationResponse;
import com.yatzee.yatzee_api.entity.Game; // Import Game
import com.yatzee.yatzee_api.repository.GameRepository; // Import GameRepository
import com.yatzee.yatzee_api.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game")
public class GameController {

    @Autowired
    private GameService gameService;
    
    @Autowired
    private GameRepository gameRepository; // Inject the repository

    // Endpoint for Ludo Quick Start
    @PostMapping("/ludo/create-and-start")
    public ResponseEntity<LudoGameCreationResponse> createLudoGame(
            @RequestBody GameCreateRequest request,
            Authentication authentication
    ) {
        LudoGameCreationResponse response = gameService.createAndStartLudoGame(request, authentication.getName());
        return ResponseEntity.ok(response);
    }
    
    // ✅ ADD THIS ENDPOINT BACK FOR THE YATZEE LOBBY
    @GetMapping("/{gameId}")
    public ResponseEntity<Game> getGameById(@PathVariable Long gameId) {
        return gameRepository.findById(gameId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // You might also need an endpoint for creating a Yatzee game lobby later
    // And an endpoint to start the Yatzee game from the lobby
}