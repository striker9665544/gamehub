// src/services/gameService.js
import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api/game';

export const createLudoGameAndGetState = async (playerCount) => {
  const token = localStorage.getItem('token');
  const request = { type: 'LUDO', playerCount };
  
  // Note the new endpoint URL
  const response = await axios.post(`${API_BASE_URL}/ludo/create-and-start`, request, {
    headers: { 'Authorization': `Bearer ${token}` }
  });
  
  return response.data; // This will be the LudoGameCreationResponse object
};

// ✅ ADD THIS FUNCTION BACK FOR THE YATZEE LOBBY
export const getGameDetails = async (gameId) => {
    const token = localStorage.getItem('token');
    const response = await axios.get(`${API_BASE_URL}/${gameId}`, {
        headers: { 'Authorization': `Bearer ${token}` }
    });
    return response.data;
};

export const startGame = async (gameId) => {
const token = localStorage.getItem('token');
await axios.post(`${API_BASE_URL}/${gameId}/start`, {}, {
headers: { 'Authorization': `Bearer ${token}`}
});
}