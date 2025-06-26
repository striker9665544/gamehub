// src/services/playerService.js (rename from playerApi.js)
import axios from "axios";

const API_BASE_URL = 'http://localhost:8080/api/player';

// Updated to match the new PlayerJoinRequest DTO
export const joinGame = async (gameId, color) => {
  const token = localStorage.getItem("token");
  return await axios.post(
    `${API_BASE_URL}/join`,
    { gameId, color }, // Sends the correct JSON body
    {
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      },
    }
  );
};

// New function to get all players for a game
export const getPlayersInGame = async (gameId) => {
    const token = localStorage.getItem("token");
    const response = await axios.get(`${API_BASE_URL}/game/${gameId}`, {
        headers: { 'Authorization': `Bearer ${token}` }
    });
    return response.data;
};