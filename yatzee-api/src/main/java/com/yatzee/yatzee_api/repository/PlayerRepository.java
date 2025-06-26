//src/com/yatzee/yatzee_api/repository/PlayerRepository
package com.yatzee.yatzee_api.repository;

import com.yatzee.yatzee_api.entity.Player;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yatzee.yatzee_api.entity.Game;

public interface PlayerRepository extends JpaRepository<Player, Long> {
	List<Player> findByGame(Game game);
}
