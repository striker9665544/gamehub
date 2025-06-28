//src/com/yatzee/yatzee_api/engine/LudoGameEngine
package com.yatzee.yatzee_api.engine;

import com.yatzee.yatzee_api.enums.Color;
import com.yatzee.yatzee_api.enums.TokenState;
import com.yatzee.yatzee_api.model.PlayerState;
import com.yatzee.yatzee_api.model.Token;

import java.util.*;

public class LudoGameEngine {

    private final Map<Color, PlayerState> playerStates = new HashMap<>();
    private final Queue<Color> turnQueue = new LinkedList<>();
    private Color currentTurn;
    private final Random random = new Random();
    private Integer lastDiceRoll = null;
    private final int WINNING_POSITION = 57;

    // Safe positions like home entry points etc. (standard Ludo safe positions)
    private final Set<Integer> safeSquares = Set.of(1, 9, 14, 22, 27, 35, 40, 48);

    public LudoGameEngine(List<Color> participatingColors) {
        for (Color color : participatingColors) {
            playerStates.put(color, new PlayerState(color));
            turnQueue.add(color);
        }
        this.currentTurn = turnQueue.peek();
    }

    public Color getCurrentTurn() {
        return currentTurn;
    }

    public int rollDice() {
        lastDiceRoll = random.nextInt(6) + 1;
        return lastDiceRoll;
    }

    private int getLastDiceRoll() {
        if (lastDiceRoll == null) {
            throw new IllegalStateException("No dice roll yet.");
        }
        return lastDiceRoll;
    }

    public void endTurn() {
        turnQueue.add(turnQueue.poll());
        currentTurn = turnQueue.peek();
        rotateTurn();
    }

    public PlayerState getPlayerState(Color color) {
        return playerStates.get(color);
    }

    public Map<Color, PlayerState> getAllPlayerStates() {
        return Collections.unmodifiableMap(playerStates);
    }

    public boolean moveToken(Color playerColor, int tokenIndex) {
        if (!isPlayerTurn(playerColor)) {
            throw new IllegalStateException("Not this player's turn.");
        }

        int diceValue = getLastDiceRoll();
        PlayerState playerState = playerStates.get(playerColor);
        List<Token> tokens = playerState.getTokens();

        if (tokenIndex < 0 || tokenIndex >= tokens.size()) {
            throw new IllegalArgumentException("Invalid token index.");
        }

        Token token = tokens.get(tokenIndex);

        if (token.getState() == TokenState.COMPLETED) {
            throw new IllegalStateException("This token has already completed.");
        }

        if (token.getState() == TokenState.HOME) {
            if (diceValue == 6) {
                token.setState(TokenState.ACTIVE);
                token.setPosition(1); // Starting square
            } else {
                throw new IllegalStateException("Need a 6 to move token out of home.");
            }
        } else if (token.getState() == TokenState.ACTIVE) {
            int newPosition = token.getPosition() + diceValue;

            if (newPosition >= WINNING_POSITION) {
                token.setPosition(WINNING_POSITION);
                token.setState(TokenState.COMPLETED);
            } else {
                token.setPosition(newPosition);

                // ✅ Capture Logic START
                if (!isSafeSquare(newPosition)) {
                    for (Map.Entry<Color, PlayerState> entry : playerStates.entrySet()) {
                        if (entry.getKey() != playerColor) {
                            for (Token opponentToken : entry.getValue().getTokens()) {
                                if (opponentToken.getState() == TokenState.ACTIVE &&
                                        opponentToken.getPosition() == newPosition) {
                                    opponentToken.setPosition(0);
                                    opponentToken.setState(TokenState.HOME);
                                }
                            }
                        }
                    }
                }
                // ✅ Capture Logic END
            }
        }

        // Win Check
        if (playerState.getTokens().stream().allMatch(t -> t.getState() == TokenState.COMPLETED)) {
            playerState.setCompleted(true);
        }

        if (diceValue != 6) {
            rotateTurn();
        }

        return true;
    }

    private boolean isSafeSquare(int position) {
        return safeSquares.contains(position);
    }

    private boolean isPlayerTurn(Color color) {
        return !turnQueue.isEmpty() && turnQueue.peek() == color;
    }

    private void rotateTurn() {
        if (!turnQueue.isEmpty()) {
            Color current = turnQueue.poll();
            turnQueue.offer(current);
            currentTurn = turnQueue.peek();
        }
    }
    
    private final Set<Color> botPlayers = new HashSet<>(); // ✅ ADD THIS: To track bot players

    // MODIFIED CONSTRUCTOR: Accept a map of colors to their bot status
    public LudoGameEngine(Map<Color, Boolean> playerConfig) {
        for (Map.Entry<Color, Boolean> entry : playerConfig.entrySet()) {
            Color color = entry.getKey();
            boolean isBot = entry.getValue();

            playerStates.put(color, new PlayerState(color));
            turnQueue.add(color);

            if (isBot) {
                botPlayers.add(color);
            }
        }
        this.currentTurn = turnQueue.peek();
    }
    
    // ✅ ADD THIS METHOD: To check if the current turn belongs to a bot
    public boolean isBotTurn() {
        return botPlayers.contains(currentTurn);
    }
    
    // ✅ ADD THIS METHOD: To get a list of valid moves for the current player
    public List<Integer> getValidMoves() {
        if (lastDiceRoll == null) return Collections.emptyList();
        
        PlayerState currentPlayerState = playerStates.get(currentTurn);
        List<Integer> validTokenIds = new ArrayList<>();
        
        for (Token token : currentPlayerState.getTokens()) {
            if (token.getState() == TokenState.HOME && lastDiceRoll == 6) {
                validTokenIds.add(token.getId());
            } else if (token.getState() == TokenState.ACTIVE) {
                // Simplified logic: for now, any active token is movable.
                // A real implementation would check if the move is valid (e.g., doesn't overshoot the home path).
                validTokenIds.add(token.getId());
            }
        }
        return validTokenIds;
    }
}
