// src/services/ludoService.js
import axios from "axios";
import { Color } from "../types/enums";

const API_BASE = "/api/ludo";

// ✅ Dice Roll
export const rollDice = async (gameId, playerColor) => {
  const response = await axios.post(`${API_BASE}/roll`, null, {
    params: { gameId, playerColor },
  });
  return response.data;
};

// ✅ Make Move
export const makeMove = async (gameId, playerColor, tokenId) => {
  const response = await axios.post(`${API_BASE}/move`, {
    gameId,
    playerColor,
    tokenId,
  });
  return response.data;
};


// Get Game State - Correct the URL and add the Authorization header
export const getGameState = async (gameId) => {
  // 1. Get the token from local storage
  const token = localStorage.getItem('token');
  
  // 2. Add the headers object to the axios config
  const response = await axios.get(`${API_BASE}/state/${gameId}`, {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });

  return response.data;
};
