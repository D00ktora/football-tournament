package com.github.d00ktora.football_tournament.repository;

import com.github.d00ktora.football_tournament.entities.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {

	@Query("select r from Record r " +
			"join fetch r.player p " +
			"join fetch p.team " +
			"where r.match.id = :matchId " +
			"order by r.fromMinutes")
	List<Record> findByMatchId(Long matchId);
}
