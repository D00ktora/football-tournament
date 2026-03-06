package com.github.d00ktora.football_tournament.service.match;

import com.github.d00ktora.football_tournament.entities.dto.archivedMatche.ArchiveMatch;
import com.github.d00ktora.football_tournament.entities.Match;
import com.github.d00ktora.football_tournament.repository.ArchivedMatchesRepository;
import com.github.d00ktora.football_tournament.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchService {
	private final ArchivedMatchesRepository archivedMatchesRepository;
	private final MatchRepository matchRepository;

	public void archiveMatch(Match match) {
		ArchiveMatch archiveMatch = new ArchiveMatch();
		archiveMatch.setDate(match.getDate());
		archiveMatch.setScore(match.getScore());
		archiveMatch.setTeamAId(match.getTeamA().getId());
		archiveMatch.setTeamBId(match.getTeamB().getId());
		archivedMatchesRepository.save(archiveMatch);
	}

	public List<Match> getAllMatchesByTeamId(Long teamId) {
		return matchRepository.findAllMatchesByTeamId(teamId);
	}

	public Match getMatchById(Long matchId) {
		return matchRepository.findById(matchId).orElse(null);
	}
}
