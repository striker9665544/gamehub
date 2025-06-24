package com.yatzee.yatzee_api.service;

import com.yatzee.yatzee_api.dto.GameSelectionRequest;
import com.yatzee.yatzee_api.dto.GameSelectionResponse;
import com.yatzee.yatzee_api.enums.GameType;
import com.yatzee.yatzee_api.enums.OpponentType;
import com.yatzee.yatzee_api.model.GameSelection;
import com.yatzee.yatzee_api.repository.GameSelectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GameSelectionService {

    @Autowired
    private GameSelectionRepository gameSelectionRepository;

    public GameSelectionResponse selectGame(GameSelectionRequest request) {
        GameSelection selection = new GameSelection();
        selection.setUserEmail(request.getUserEmail());
        selection.setGameType(request.getGameType());
        selection.setOpponentType(request.getOpponentType());

        // Only set playerCount if the game is LUDO
        if (request.getGameType() == GameType.LUDO) {
            selection.setPlayerCount(request.getPlayerCount());
        } else {
            selection.setPlayerCount(null); // Not needed for YATZEE
        }

        selection.setCreatedAt(LocalDateTime.now());
        GameSelection saved = gameSelectionRepository.save(selection);

        return mapToResponse(saved);
    }

    public List<GameSelectionResponse> getSelectionsForUser(String email) {
        return gameSelectionRepository.findByUserEmail(email)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private GameSelectionResponse mapToResponse(GameSelection selection) {
        GameSelectionResponse response = new GameSelectionResponse();
        response.setId(selection.getId());
        response.setUserEmail(selection.getUserEmail());
        response.setGameType(selection.getGameType());
        response.setOpponentType(selection.getOpponentType());
        response.setPlayerCount(selection.getPlayerCount());
        response.setCreatedAt(selection.getCreatedAt());
        return response;
    }
}

