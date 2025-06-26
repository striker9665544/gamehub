// src/com/yatzee/yatzee_api/service/PlayerService.java
package com.yatzee.yatzee_api.service;

import com.yatzee.yatzee_api.dto.PlayerJoinRequest;
import com.yatzee.yatzee_api.entity.Game;
import com.yatzee.yatzee_api.entity.Player;
import com.yatzee.yatzee_api.entity.User;
import com.yatzee.yatzee_api.repository.GameRepository;
import com.yatzee.yatzee_api.repository.PlayerRepository;
import com.yatzee.yatzee_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired; // <-- Make sure this is imported
import org.springframework.messaging.simp.SimpMessagingTemplate; // <-- IMPORT this
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // <-- IMPORT this

import java.util.List;

@Service
public class PlayerService {

    @Autowired // Use field injection for simplicity here
    private PlayerRepository playerRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate; // <-- INJECT the messaging template

    @Transactional // Use @Transactional to ensure DB and message sending are linked
    public Player joinGame(PlayerJoinRequest request, String userEmail) {
        Game game = gameRepository.findById(request.getGameId())
                .orElseThrow(() -> new RuntimeException("Game not found with ID: " + request.getGameId()));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));

        // TODO: Add logic to prevent joining a full or running game
        // TODO: Add logic to prevent a user from joining the same game twice

        Player player = new Player();
        player.setUser(user);
        player.setGame(game);
        player.setBot(false);
        player.setColor(request.getColor());

        Player savedPlayer = playerRepository.save(player);

        // --- WebSocket Broadcasting Logic ---
        // After successfully saving the player, get the updated list of all players
        List<Player> updatedPlayers = playerRepository.findByGame(game);
        
        // Broadcast the updated list to the game-specific topic
        String destination = "/topic/game/" + game.getId() + "/lobby";
        System.out.println("Broadcasting to: " + destination);
        messagingTemplate.convertAndSend(destination, updatedPlayers);
        // --- End of WebSocket Logic ---

        return savedPlayer;
    }
    
    public List<Player> getPlayersByGame(Long gameId) {
        Game game = gameRepository.findById(gameId)
            .orElseThrow(() -> new RuntimeException("Game not found with ID: " + gameId));

        return playerRepository.findByGame(game);
    }
}