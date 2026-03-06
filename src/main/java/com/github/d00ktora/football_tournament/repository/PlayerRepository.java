package com.github.d00ktora.football_tournament.repository;

import com.github.d00ktora.football_tournament.entities.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
	Optional<Player> findByFullName(String fullName);
	List<Player> findAllByTeamId(Long teamId);
	Player findByTeamId(Long teamId);
}
