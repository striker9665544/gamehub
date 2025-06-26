// src/hooks/useWebSocket.js
import { useState, useEffect, useRef, useCallback } from 'react';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

const useWebSocket = (topic, onMessageReceived) => {
  const [isConnected, setIsConnected] = useState(false);
  // Use a ref to hold the client instance. This prevents it from being re-created on every render.
  const clientRef = useRef(null); 
  const memoizedOnMessageReceived = useCallback(onMessageReceived, [onMessageReceived]);

  useEffect(() => {
    // Only try to connect if there's a topic and we haven't created a client yet.
    if (topic && !clientRef.current) {
        const token = localStorage.getItem('token');
        if (!token) {
            console.error("No auth token found, WebSocket connection aborted.");
            return;
        }

        const socketUrl = `http://localhost:8080/ws?token=${token}`;
        
        // Create a new client and store it in the ref
        const client = new Client({
            webSocketFactory: () => new SockJS(socketUrl), // Create a new socket instance here
            debug: (str) => {
                console.log('STOMP DEBUG: ' + str);
            },
            reconnectDelay: 5000,
            onConnect: () => {
                setIsConnected(true);
                console.log(`WebSocket Connected! Subscribing to ${topic}`);
                client.subscribe(topic, (message) => {
                    try {
                        const payload = JSON.parse(message.body);
                        memoizedOnMessageReceived(payload);
                    } catch (e) {
                        memoizedOnMessageReceived(message.body);
                    }
                });
            },
            onDisconnect: () => {
                setIsConnected(false);
                console.log('WebSocket Disconnected!');
            },
            onStompError: (frame) => {
                console.error('Broker reported error: ' + frame.headers['message']);
                console.error('Additional details: ' + frame.body);
            },
        });

        // Activate the client
        client.activate();
        // Store the active client in our ref so it persists across renders
        clientRef.current = client;
    }

    // This cleanup function will run ONLY when the component using the hook unmounts.
    return () => {
        if (clientRef.current && clientRef.current.active) {
            console.log('Deactivating WebSocket client on component unmount.');
            clientRef.current.deactivate();
            clientRef.current = null; // Clear the ref
        }
    };
  }, [topic, memoizedOnMessageReceived]); // Dependency array ensures this effect runs only when the topic changes.

  return { isConnected };
};

export default useWebSocket;