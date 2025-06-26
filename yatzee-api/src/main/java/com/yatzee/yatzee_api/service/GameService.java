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
import java.util.List;

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

    @Transactional
    public LudoGameCreationResponse createAndStartLudoGame(GameCreateRequest request, String creatorEmail) {
        int playerCount = request.getPlayerCount();
        if (playerCount < 2 || playerCount > 4) {
            throw new IllegalArgumentException("Ludo requires 2, 3, or 4 players.");
        }
        User creator = userRepository.findByEmail(creatorEmail)
                .orElseThrow(() -> new RuntimeException("Creator user not found: " + creatorEmail));

        Game game = new Game();
        game.setGameType(GameType.LUDO);
        game.setGameStatus(GameStatus.RUNNING);
        Game savedGame = gameRepository.save(game);

        List<Color> availableColors = new ArrayList<>(Arrays.asList(Color.values()));
        Collections.shuffle(availableColors);
        List<Color> participatingColors = new ArrayList<>();

        Player creatorPlayer = new Player(creator, savedGame, false, availableColors.get(0).name());
        playerRepository.save(creatorPlayer);
        participatingColors.add(availableColors.get(0));

        for (int i = 1; i < playerCount; i++) {
            Player botPlayer = new Player(null, savedGame, true, availableColors.get(i).name());
            playerRepository.save(botPlayer);
            participatingColors.add(availableColors.get(i));
        }

        ludoService.createGameEngine(savedGame.getId(), participatingColors);
        LudoGameState initialState = ludoService.getGameState(savedGame.getId());
        
        return new LudoGameCreationResponse(savedGame, initialState);
    }
}