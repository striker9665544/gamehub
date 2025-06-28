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
    /*@GetMapping("/{gameId}")
    public ResponseEntity<Game> getGameById(@PathVariable Long gameId) {
        return gameRepository.findById(gameId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }*/

    // Endpoint for creating a game lobby (primarily for Yatzee)
    @PostMapping("/create-lobby")
    public ResponseEntity<Game> createLobby(@RequestBody GameCreateRequest request, Authentication authentication) {
        Game game = gameService.createGameLobby(request, authentication.getName());
        return ResponseEntity.ok(game);
    }

    // Endpoint to get details for a lobby
    @GetMapping("/{gameId}")
    public ResponseEntity<Game> getGameById(@PathVariable Long gameId) {
        return gameRepository.findById(gameId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint for the "Start Game" button in a lobby
    @PostMapping("/{gameId}/start")
    public ResponseEntity<Void> startGame(@PathVariable Long gameId) {
       gameService.startGame(gameId);
       return ResponseEntity.ok().build();
    }
}