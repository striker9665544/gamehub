// src/com/yatzee/yatzee_api/service/GameService.java
package com.yatzee.yatzee_api.service;

import com.yatzee.yatzee_api.dto.GameCreateRequest;
import com.yatzee.yatzee_api.dto.LudoGameCreationResponse;
import com.yatzee.yatzee_api.dto.LudoGameState;
import com.yatzee.yatzee_api.entity.Game;
import com.yatzee.yatzee_api.entity.Player;
import com.yatzee.yatzee_api.entity.User;
import com.yatzee.yatzee_api.enums.Color;
import com.yatzee.yatzee_api.enums.GameStatus;
import com.yatzee.yatzee_api.enums.GameType;
import com.yatzee.yatzee_api.repository.GameRepository;
import com.yatzee.yatzee_api.repository.PlayerRepository;
import com.yatzee.yatzee_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PlayerRepository playerRepository;
    
    @Autowired
    private LudoService ludoService;
    
    @Autowired
    private YahtzeeService yahtzeeService;

 // Replace the existing createAndStartLudoGame method in src/com/yatzee/yatzee_api/service/GameService.java

    @Transactional
    public LudoGameCreationResponse createAndStartLudoGame(GameCreateRequest request, String creatorEmail) {
        int playerCount = request.getPlayerCount();
        if (playerCount < 2 || playerCount > 4) {
            throw new IllegalArgumentException("Ludo requires 2, 3, or 4 players.");
        }
        User creator = userRepository.findByEmail(creatorEmail)
                .orElseThrow(() -> new RuntimeException("Creator user not found: " + creatorEmail));

        // 1. Create and save the game entity
        Game game = new Game();
        game.setGameType(GameType.LUDO);
        game.setGameStatus(GameStatus.RUNNING);
        Game savedGame = gameRepository.save(game);

        // 2. Prepare for player and color assignment
        List<Color> availableColors = new ArrayList<>(Arrays.asList(Color.values()));
        Collections.shuffle(availableColors);
        
        // This map will store which color is a bot. This is what the engine needs.
        Map<Color, Boolean> playerConfig = new HashMap<>();

        // 3. Create the human player (the game creator)
        Color humanColor = availableColors.remove(0); // Get the first random color and remove it from the list
        playerConfig.put(humanColor, false); // Add to config: this color is NOT a bot
        Player creatorPlayer = new Player(creator, savedGame, false, humanColor.name());
        playerRepository.save(creatorPlayer);

        // 4. Create the bot players for the remaining slots
        for (int i = 1; i < playerCount; i++) {
            Color botColor = availableColors.remove(0); // Get the next random color
            playerConfig.put(botColor, true); // Add to config: this color IS a bot
            Player botPlayer = new Player(null, savedGame, true, botColor.name());
            playerRepository.save(botPlayer);
        }

        // 5. Create the game engine using the correct configuration map
        ludoService.createGameEngine(savedGame.getId(), playerConfig);
        
        // 6. Get the initial state from the newly created engine
        LudoGameState initialState = ludoService.getGameState(savedGame.getId());
        
        // 7. Return the complete response object
        return new LudoGameCreationResponse(savedGame, initialState);
    }
    
    @Transactional
    public void startGame(Long gameId) {
        Game game = gameRepository.findById(gameId)
            .orElseThrow(() -> new RuntimeException("Game not found with ID: " + gameId));
        
        if (game.getGameStatus() != GameStatus.WAITING) {
            throw new IllegalStateException("Game is not in a waiting state.");
        }

        game.setGameStatus(GameStatus.RUNNING);
        gameRepository.save(game);
        
        // Check game type and initialize the correct engine
        if (game.getGameType() == GameType.YATZEE) {
            yahtzeeService.createGameEngine(gameId);
            System.out.println("Yatzee Game " + gameId + " has been started.");
        }
        // The Ludo logic is now handled in its Quick Start method, so we don't need it here.
    }
    
    @Transactional
    public Game createGameLobby(GameCreateRequest request, String creatorEmail) {
        User creator = userRepository.findByEmail(creatorEmail)
                .orElseThrow(() -> new RuntimeException("Creator user not found: " + creatorEmail));

        Game game = new Game();
        game.setGameType(request.getType()); // Will be YATZEE
        game.setGameStatus(GameStatus.WAITING);
        Game savedGame = gameRepository.save(game);

        Player creatorPlayer = new Player(creator, savedGame, false, null);
        playerRepository.save(creatorPlayer);
        
        if (request.getPlayerCount() == 2) { // Add a bot if playing vs Computer
            Player botPlayer = new Player(null, savedGame, true, null);
            playerRepository.save(botPlayer);
        }
        return savedGame;
    }
}