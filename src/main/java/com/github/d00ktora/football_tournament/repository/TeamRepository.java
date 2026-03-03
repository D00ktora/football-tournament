package com.github.d00ktora.football_tournament.repository;

import com.github.d00ktora.football_tournament.entities.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
}
