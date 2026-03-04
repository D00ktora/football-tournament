package com.github.d00ktora.football_tournament.repository;

import com.github.d00ktora.football_tournament.entities.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {

	@Query("select m.id from Match m")
	List<Long> selectAllByMatchId();
}
