package com.github.d00ktora.football_tournament.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArchivedMatchesRepository extends JpaRepository<ArchiveMatch, Long> {
}
