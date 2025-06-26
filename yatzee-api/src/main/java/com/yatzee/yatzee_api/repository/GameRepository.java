//src/com/yatzee/yatzee_api/repository/GameRepository
package com.yatzee.yatzee_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yatzee.yatzee_api.entity.Game;

public interface GameRepository extends JpaRepository<Game, Long> {
}

