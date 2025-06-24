package com.yatzee.yatzee_api.controller;

import com.yatzee.yatzee_api.dto.GameSelectionRequest;
import com.yatzee.yatzee_api.dto.GameSelectionResponse;
import com.yatzee.yatzee_api.service.GameSelectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/games")
@CrossOrigin(origins = "http://localhost:5173") // adjust in production
public class GameSelectionController {

    @Autowired
    private GameSelectionService gameSelectionService;

    @PostMapping("/select")
    public GameSelectionResponse selectGame(@RequestBody GameSelectionRequest request) {
        return gameSelectionService.selectGame(request);
    }

    @GetMapping("/user")
    public List<GameSelectionResponse> getUserSelections(@RequestParam String email) {
        return gameSelectionService.getSelectionsForUser(email);
    }
}

