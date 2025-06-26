// src/com/yatzee/yatzee_api/controller/PlayerController.java
package com.yatzee.yatzee_api.controller;

import com.yatzee.yatzee_api.dto.PlayerJoinRequest;
import com.yatzee.yatzee_api.entity.Player;
import com.yatzee.yatzee_api.service.PlayerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/player")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping("/join")
    public ResponseEntity<Player> joinGame(
            @RequestBody PlayerJoinRequest request,
            @AuthenticationPrincipal UserDetails userDetails // Injects the authenticated user's details
    ) {
        String userEmail = userDetails.getUsername();
        Player newPlayer = playerService.joinGame(request, userEmail);
        return ResponseEntity.ok(newPlayer);
    }

    @GetMapping("/game/{gameId}")
    public ResponseEntity<List<Player>> getPlayersInGame(@PathVariable Long gameId) {
        return ResponseEntity.ok(playerService.getPlayersByGame(gameId));
    }
}