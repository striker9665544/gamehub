// src/pages/GameLobbyPage.jsx
import React, { useState, useEffect, useCallback } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { getGameDetails, startGame as startGameService } from '../services/gameService';
import { getPlayersInGame, joinGame } from '../services/playerService';
import { decodeToken } from '../utils/jwtUtils';
import useWebSocket from '../hooks/useWebSocket';
import { startGame } from '../services/gameService';

const GameLobbyPage = () => {
    // ... (useState, useParams, etc. are the same)
    const { gameId } = useParams();
    const navigate = useNavigate();

    const [gameDetails, setGameDetails] = useState(null);
    const [players, setPlayers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [selectedColor, setSelectedColor] = useState('');

    const lobbyTopic = `/topic/game/${gameId}/lobby`;
    const handleLobbyUpdate = (message) => {
        if (message === "GAME_STARTED") {
            console.log("Game start signal received! Navigating...");
            navigate(`/play/game/${gameId}`);
        } else {
            console.log("Received lobby update:", message);
            setPlayers(message);
        }
    };
    const { isConnected } = useWebSocket(lobbyTopic, handleLobbyUpdate);
    
    // ... (useEffect for initial data fetch is the same)
    useEffect(() => {
        const fetchInitialData = async () => {
            setLoading(true);
            try {
                const [gameDetailsRes, playersRes] = await Promise.all([
                    getGameDetails(gameId),
                    getPlayersInGame(gameId)
                ]);
                setGameDetails(gameDetailsRes);
                setPlayers(playersRes);
            } catch (err) {
                // ... (error handling)
            } finally {
                setLoading(false);
            }
        };
        fetchInitialData();
    }, [gameId]);


    // ✅ --- REVISED LOGIC --- ✅

    const currentUser = decodeToken(localStorage.getItem('token'));
    
    // Find the full player object for the current user
    const currentPlayerObject = players.find(p => p.user?.email === currentUser.sub);

    // The user has "fully" joined if their player object exists AND has a color.
    const hasPlayerJoinedWithColor = currentPlayerObject && currentPlayerObject.color;

    const takenColors = players.map(p => p.color).filter(Boolean);
    const availableColors = ['RED', 'GREEN', 'BLUE', 'YELLOW'].filter(c => !takenColors.includes(c));
    
    // Set a default color for the dropdown if not already set.
    useEffect(() => {
        if (availableColors.length > 0 && !selectedColor) {
            setSelectedColor(availableColors[0]);
        }
    }, [availableColors, selectedColor]);

    const handleJoinGame = async () => {
        if (!selectedColor) {
            setError("Please select a color.");
            return;
        }
        setError('');
        try {
            await joinGame(gameId, selectedColor);
            // The WebSocket will update the state, no need for alert or fetch.
        } catch (err) {
            setError(err.response?.data?.message || 'An error occurred while joining.');
        }
    };
    
    const handleStartGame = () => {
        startGame(gameId)
            .then(() => {
                // Navigate to the game page after starting
                navigate(`/play/game/${gameId}`);
            })
            .catch(err => console.error("Failed to start game:", err));
    };
    
    const allPlayersHaveColor = players.length > 0 && players.every(p => p.color);

    // --- End of Revised Logic ---

    // The JSX part
    if (loading) {
        return <div className="text-white text-center p-10">Loading Lobby...</div>;
    }

    return (
        <div className="min-h-screen bg-gray-900 text-white p-8">
            <div className="max-w-2xl mx-auto bg-gray-800 rounded-lg shadow-xl p-6">
                {/* ... (Lobby header is the same) ... */}
                <h1 className="text-3xl font-bold mb-2">Game Lobby</h1>
                <div className="flex justify-between items-center mb-4">
                    <h2 className="text-xl text-cyan-400">Game ID: {gameDetails.id}</h2>
                    <span className={`text-xs font-bold px-2 py-1 rounded ${isConnected ? 'bg-green-500' : 'bg-red-500'}`}>
                        {isConnected ? 'LIVE' : 'OFFLINE'}
                    </span>
                </div>
                {/* ... (Game details and Player list are the same) ... */}
                
                {error && <p className="text-red-500 my-4 bg-red-100 p-2 rounded">{error}</p>}

                {/* ✅ --- REVISED JSX LOGIC --- ✅ */}

                {/* Show the "Join" section if the current user has NOT chosen a color yet */}
                {!hasPlayerJoinedWithColor && (
                    <div className="mt-6 p-4 border-t border-gray-700">
                        <h3 className="text-xl font-bold mb-3">Choose Your Color</h3>
                        {availableColors.length > 0 ? (
                            <div className="flex items-center gap-4">
                                <select
                                    value={selectedColor}
                                    onChange={(e) => setSelectedColor(e.target.value)}
                                    className="bg-gray-700 text-white p-2 rounded"
                                >
                                    {availableColors.map(color => (
                                        <option key={color} value={color}>{color}</option>
                                    ))}
                                </select>
                                <button
                                    onClick={handleJoinGame}
                                    disabled={!isConnected}
                                    className="bg-green-600 hover:bg-green-700 px-6 py-2 rounded font-bold disabled:bg-gray-500"
                                >
                                    Confirm Color
                                </button>
                            </div>
                        ) : (
                            <p className="text-yellow-400">All colors have been taken. Waiting for game to start.</p>
                        )}
                    </div>
                )}
                
                {/* Show the "Start Game" button if the current user HAS chosen a color,
                    and if all players in the lobby have chosen a color. */}
                {hasPlayerJoinedWithColor && (
                   <div className="text-center mt-8">
                        <button onClick={handleStartGame} className="bg-blue-600 ...">
                            Start Game
                        </button>
                     {!allPlayersHaveColor && <p className="text-xs text-gray-400 mt-2">Waiting for all players to pick a color...</p>}
                     {players.length < 2 && <p className="text-xs text-gray-400 mt-2">Waiting for more players...</p>}
                   </div>
                )}
            </div>
        </div>
    );
};

export default GameLobbyPage;