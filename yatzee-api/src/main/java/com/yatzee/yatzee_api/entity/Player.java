package com.yatzee.yatzee_api.entity;

import jakarta.persistence.*;

@Entity // <-- ADDED: Marks this class as a JPA entity
@Table(name = "player") // <-- ADDED: Specifies the table name
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id") // This column can be null for bots
    private User user;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @Column(name = "is_bot") // Explicitly name the column
    private boolean isBot;

    private String color;

    @Column(name = "is_ready")
    private boolean isReady = false;

    // Default constructor is required by JPA
    public Player() {
    }

    public Player(User user, Game game, boolean isBot, String color) {
        this.user = user;
        this.game = game;
        this.isBot = isBot;
        this.color = color;
    }

    // --- Getters and Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public boolean isBot() {
        return isBot;
    }

    public void setBot(boolean bot) {
        isBot = bot;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }
}