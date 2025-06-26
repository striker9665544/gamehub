// src/pages/GamePage.jsx
import React, { useState, useEffect } from 'react';
import { useParams, useLocation } from 'react-router-dom';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import LudoBoard from '../components/LudoBoard';
import { getPlayersInGame } from '../services/playerService';
import { decodeToken } from '../utils/jwtUtils';

const Dice = ({ value }) => (
    <div className="w-20 h-20 bg-white rounded-lg flex items-center justify-center shadow-lg">
        <span className="text-5xl font-bold text-black">{value}</span>
    </div>
);

const GamePage = () => {
    const { gameId } = useParams();
    const location = useLocation();

    const [gameState, setGameState] = useState(location.state?.initialState || null);
    const [stompClient, setStompClient] = useState(null);
    const [diceResult, setDiceResult] = useState(null);
    const [humanPlayerColor, setHumanPlayerColor] = useState(null);

    const currentUser = decodeToken(localStorage.getItem('token'));
    const isMyTurn = gameState?.currentTurn === humanPlayerColor;

    useEffect(() => {
        if (gameId) {
            getPlayersInGame(gameId).then(players => {
                const humanPlayer = players.find(p => !p.isBot && p.user?.email === currentUser.sub);
                if (humanPlayer) setHumanPlayerColor(humanPlayer.color);
            }).catch(err => console.error("Failed to fetch player data:", err));
        }
    }, [gameId, currentUser.sub]);

    useEffect(() => {
        if (!gameId) return;
        
        const socket = new SockJS('http://localhost:8080/ws');
        const client = new Client({
            webSocketFactory: () => socket,
            onConnect: () => {
                console.log('Connected to game socket for updates!');
                client.subscribe(`/topic/game/${gameId}/state`, (msg) => {
                    console.log("Received state update via WebSocket");
                    setGameState(JSON.parse(msg.body));
                    setDiceResult(null);
                });
                client.subscribe(`/topic/game/${gameId}/dice`, (msg) => {
                    console.log("Received dice roll via WebSocket");
                    setDiceResult(JSON.parse(msg.body));
                });
            },
        });
        client.activate();
        setStompClient(client);

        return () => {
            if (client && client.active) client.deactivate();
        };
    }, [gameId]);

    const handleRollDice = () => {
        if (stompClient && isMyTurn) {
            stompClient.publish({
                destination: `/app/ludo/${gameId}/roll`,
                body: humanPlayerColor,
            });
        }
    };

    const handleMoveToken = (tokenId) => {
        if (stompClient && isMyTurn && diceResult) {
            const moveRequest = { gameId: parseInt(gameId), playerColor: humanPlayerColor, tokenId };
            stompClient.publish({
                destination: `/app/ludo/${gameId}/move`,
                body: JSON.stringify(moveRequest),
            });
        }
    };

    // The JSX is the same as before, it will now render correctly
    // because `gameState` is populated on the initial render.
    return (
        <div className="min-h-screen bg-gray-800 flex flex-col md:flex-row items-center justify-center gap-8 p-4">
            <LudoBoard gameState={gameState} humanPlayerColor={humanPlayerColor} onTokenClick={handleMoveToken} />
            <div className="flex flex-col items-center justify-center bg-gray-700 p-8 rounded-lg shadow-lg w-full md:w-auto">
                {/* ... same action panel JSX ... */}
            </div>
        </div>
    );
};

export default GamePage;