package com.github.d00ktora.football_tournament.repository;

import com.github.d00ktora.football_tournament.entities.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
}
