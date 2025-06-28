import React, { useState, useEffect } from 'react';
import { useParams, useLocation } from 'react-router-dom';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

// Import all game components
import LudoBoard from '../components/LudoBoard';
import YahtzeeDice from '../components/YahtzeeDice';
import YahtzeeSheet from '../components/YahtzeeSheet';

// Import services
import { getPlayersInGame } from '../services/playerService';
import { getGameDetails } from '../services/gameService'; // We need this
import { decodeToken } from '../utils/jwtUtils';

const Dice = ({ value }) => (
    <div className="w-20 h-20 bg-white rounded-lg flex items-center justify-center shadow-lg">
        <span className="text-5xl font-bold text-black">{value}</span>
    </div>
);

const GamePage = () => {
    const { gameId } = useParams();
    const location = useLocation();

    // --- General State ---
    const [gameType, setGameType] = useState(null);
    const [stompClient, setStompClient] = useState(null);
    const [isLoading, setIsLoading] = useState(true);

    // --- Ludo Specific State ---
    const [ludoGameState, setLudoGameState] = useState(location.state?.initialState || null);
    const [ludoDiceResult, setLudoDiceResult] = useState(null);
    const [humanPlayerColor, setHumanPlayerColor] = useState(null);

    // --- Yahtzee Specific State ---
    const [yahtzeeGameState, setYahtzeeGameState] = useState(null);

    const currentUser = decodeToken(localStorage.getItem('token'));
    const isMyTurnForLudo = ludoGameState?.currentTurn === humanPlayerColor;

    // --- Effects ---

    // 1. Fetch the game type first to determine how to proceed
    useEffect(() => {
        if (gameId) {
            getGameDetails(gameId).then(details => {
                setGameType(details.gameType);
                // If it's a Ludo game that already has state passed from navigation, we're not loading.
                if (details.gameType === 'LUDO' && ludoGameState) {
                    setIsLoading(false);
                }
            }).catch(err => {
                console.error("Failed to get game details:", err);
                setIsLoading(false);
            });
        }
    }, [gameId, ludoGameState]);

    // 2. Fetch player-specific info (like color for Ludo)
    useEffect(() => {
        if (gameId && gameType === 'LUDO') {
            getPlayersInGame(gameId).then(players => {
                const humanPlayer = players.find(p => !p.isBot && p.user?.email === currentUser.sub);
                if (humanPlayer) setHumanPlayerColor(humanPlayer.color);
            }).catch(err => console.error("Failed to fetch player data:", err));
        }
    }, [gameId, gameType, currentUser.sub]);

    // 3. Setup WebSocket connection once we know the game type
    useEffect(() => {
        if (!gameId || !gameType) return;
        
        const socket = new SockJS('http://localhost:8080/ws');
        const client = new Client({
            webSocketFactory: () => socket,
            onConnect: () => {
                console.log(`Connected to ${gameType} game socket!`);
                setStompClient(client);

                if (gameType === 'LUDO') {
                    client.subscribe(`/topic/game/${gameId}/state`, (msg) => {
                        setLudoGameState(JSON.parse(msg.body));
                        setLudoDiceResult(null); // Clear dice on state update
                    });
                    client.subscribe(`/topic/game/${gameId}/dice`, (msg) => {
                        setLudoDiceResult(JSON.parse(msg.body));
                    });
                } else if (gameType === 'YATZEE') {
                    // We will need a new controller for this on the backend
                    client.subscribe(`/topic/yatzee/${gameId}/state`, (msg) => {
                        setYahtzeeGameState(JSON.parse(msg.body));
                        setIsLoading(false); // We have state, no longer loading
                    });
                    // Request initial state for Yatzee
                    client.publish({ destination: `/app/yatzee/${gameId}/getState` });
                }
            },
        });
        client.activate();
        
        return () => { if (client && client.active) client.deactivate(); };
    }, [gameId, gameType]);

    // --- Ludo Action Handlers ---
    const handleLudoRollDice = () => {
        if (stompClient && isMyTurnForLudo) {
            stompClient.publish({
                destination: `/app/ludo/${gameId}/roll`,
                body: humanPlayerColor,
            });
        }
    };
    const handleLudoMoveToken = (tokenId) => {
        if (stompClient && isMyTurnForLudo && ludoDiceResult) {
            const moveRequest = { gameId: parseInt(gameId), playerColor: humanPlayerColor, tokenId };
            stompClient.publish({
                destination: `/app/ludo/${gameId}/move`,
                body: JSON.stringify(moveRequest),
            });
        }
    };

        // ✅ Yahtzee Handlers
    const handleYatzeeRoll = () => {
        if (stompClient) stompClient.publish({ destination: `/app/yatzee/${gameId}/roll` });
    };
    const handleYatzeeHold = (dieIndex) => {
        if (stompClient) stompClient.publish({ destination: `/app/yatzee/${gameId}/hold`, body: JSON.stringify(dieIndex) });
    };
    const handleYatzeeScore = (category) => {
        if (stompClient) stompClient.publish({ destination: `/app/yatzee/${gameId}/score`, body: category });
    };

    // --- Render Logic ---
    if (isLoading) {
        return <div className="text-white text-center p-10 text-2xl animate-pulse">Loading Game...</div>;
    }

    if (gameType === 'LUDO') {
        return (
            <div className="min-h-screen bg-gray-800 flex flex-col md:flex-row items-center justify-center gap-8 p-4">
                <LudoBoard gameState={ludoGameState} onTokenClick={handleLudoMoveToken} humanPlayerColor={humanPlayerColor} />
                <div className="flex flex-col items-center justify-center bg-gray-700 p-8 rounded-lg shadow-lg w-full md:w-auto">
                    <h2 className="text-2xl text-white font-bold mb-4">Actions</h2>
                    {ludoDiceResult ? <Dice value={ludoDiceResult.value} /> : <div className="w-20 h-20 bg-gray-600 rounded-lg flex items-center justify-center"><span className="text-gray-400">Roll!</span></div>}
                    <button 
                        onClick={handleLudoRollDice} 
                        disabled={!stompClient || !isMyTurnForLudo || !!ludoDiceResult}
                        className="mt-6 bg-blue-600 text-white px-8 py-3 rounded-lg font-bold text-xl disabled:bg-gray-500 disabled:cursor-not-allowed transform hover:scale-105 transition-transform"
                    >
                        Roll Dice
                    </button>
                    {ludoDiceResult && isMyTurnForLudo && (
                        <div className="mt-4 text-center">
                            <p className="text-white font-semibold">Click a token on the board to move.</p>
                        </div>
                    )}
                    {!isMyTurnForLudo && ludoGameState && (
                        <p className="mt-4 text-yellow-400 animate-pulse">Waiting for {ludoGameState.currentTurn}...</p>
                    )}
                </div>
            </div>
        );
    }
    
    if (gameType === 'YATZEE') {
        return (
            <div className="min-h-screen bg-gray-900 flex flex-col md:flex-row items-start justify-center gap-8 p-8">
                <YahtzeeSheet 
                    scoreCard={yahtzeeGameState?.scoreCard} 
                    onScoreClick={handleYatzeeScore} 
                />
                <YahtzeeDice 
                    dice={yahtzeeGameState?.dice || []} 
                    held={yahtzeeGameState?.held || []} 
                    rollsLeft={yahtzeeGameState?.rollsLeft || 0}
                    onDieClick={handleYatzeeHold}
                    onRollClick={handleYatzeeRoll}
                />
            </div>
        );
    }

    return <div className="text-white text-center p-10 text-2xl">Error: Game type not supported or found.</div>;
};

export default GamePage;