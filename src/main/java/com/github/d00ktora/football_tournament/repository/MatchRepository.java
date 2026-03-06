package com.github.d00ktora.football_tournament.repository;

import com.github.d00ktora.football_tournament.entities.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {

	@Query("select m.id from Match m")
	List<Long> selectAllByMatchId();

	@Query("select m from Match m where	m.teamA.id = :teamId or m.teamB.id = :teamId")
	List<Match> findAllMatchesByTeamId(@Param("teamId") Long teamId);
}
