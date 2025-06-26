// src/com/yatzee/yatzee_api/config/WebSocketConfig.java
package com.yatzee.yatzee_api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker // This annotation enables WebSocket message handling, backed by a message broker.
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Registers the "/ws" endpoint, enabling SockJS fallback options so that
        // alternate transports can be used if WebSocket is not available.
        // This is the HTTP URL that clients will connect to for the WebSocket handshake.
        registry.addEndpoint("/ws")
                .setAllowedOrigins("http://localhost:5173") // Allow our React frontend
                .withSockJS(); // Use SockJS for better cross-browser compatibility
    }
    
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // "/topic" is for broadcasting to everyone subscribed to a path
        // "/queue" is for user-specific messages
        config.enableSimpleBroker("/topic", "/queue");
        config.setApplicationDestinationPrefixes("/app");
        // ✅ ADD THIS LINE: Sets the prefix for user-specific destinations
        config.setUserDestinationPrefix("/user"); 
    }
}