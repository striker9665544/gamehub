//src/com/yatzee/yatzee_api/entity/Game
package com.yatzee.yatzee_api.entity;


import jakarta.persistence.*;

import java.time.LocalDateTime;

import com.yatzee.yatzee_api.enums.GameStatus;
import com.yatzee.yatzee_api.enums.GameType;

@Entity
@Table(name = "games")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private GameType type;

    @Enumerated(EnumType.STRING)
    private GameStatus status;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // --- Getters and Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GameType getGameType() {
        return type;
    }

    public void setGameType(GameType type) {
        this.type = type;
    }

    public GameStatus getGameStatus() {
        return status;
    }

    public void setGameStatus(GameStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // --- Optional: toString() for debugging ---

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                ", createdBy=" +
                '}';
    }
}
