// src/pages/GameSelectionPage.jsx
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
//import { createLudoGameAndGetState } from '../services/gameService';
import { createLudoGameAndGetState, createGameLobby } from '../services/gameService';

const GameSelectionPage = () => {
  const navigate = useNavigate();
  const [error, setError] = useState('');

  const handleSelection = async (gameName, mode) => {
    setError('');
    const gameType = gameName.toUpperCase();

    try {
      if (gameType === 'LUDO') {
        const playerCount = parseInt(mode.charAt(0), 10);
        const response = await createLudoGameAndGetState(playerCount);
        // Ludo Quick Start: Go directly to the game board with initial state
        navigate(`/play/game/${response.game.id}`, { state: { initialState: response.initialState } });

      } else if (gameType === 'YATZEE') {
        const playerCount = mode.toLowerCase().includes('computer') ? 2 : 1;
        const createdGame = await createGameLobby(gameType, playerCount);
        // Yatzee: Go to the lobby first
        navigate(`/game/${createdGame.id}`);
      }
    } catch (err) {
      console.error('Failed to create game:', err);
      const errorMessage = err.response?.data?.message || 'Could not create the game.';
      setError(errorMessage);
    }
  };

  const gameOptions = [
    { name: 'Ludo', image: '/images/ludo.PNG', modes: ['2 Players', '3 Players', '4 Players'] },
    { name: 'Yatzee', image: '/images/yatzee.PNG', modes: ['Player', 'Computer'] },
  ];

  return (
    <div className="min-h-screen bg-gray-900 text-white flex flex-col items-center px-4 py-8">
      <h1 className="text-3xl font-bold mb-8">🎮 Choose a Game to Play</h1>
      {error && <p className="text-red-500 bg-red-100 p-3 rounded-md mb-6">{error}</p>}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-8 w-full max-w-4xl">
        {gameOptions.map((game) => (
          <div key={game.name} className="bg-gray-800 rounded-xl p-6 shadow-md">
            <img src={game.image} alt={game.name} className="rounded-lg w-full h-40 object-cover mb-4" />
            <h2 className="text-xl font-bold mb-4 text-center">{game.name}</h2>
            <div className="flex flex-wrap justify-center gap-3">
              {game.modes.map((mode) => (
                <button
                  key={mode}
                  onClick={() => handleSelection(game.name, mode)}
                  className="bg-blue-600 hover:bg-blue-700 px-4 py-2 rounded text-sm"
                >
                  {mode}
                </button>
              ))}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default GameSelectionPage;