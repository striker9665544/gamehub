package com.yatzee.yatzee_api.repository;

import com.yatzee.yatzee_api.model.GameSelection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameSelectionRepository extends JpaRepository<GameSelection, Long> {

    List<GameSelection> findByUserEmail(String userEmail);
}

