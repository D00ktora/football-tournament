package com.github.d00ktora.football_tournament.service.tournament;

import com.github.d00ktora.football_tournament.entities.Record;
import com.github.d00ktora.football_tournament.entities.dto.TournamentResultDTO;
import com.github.d00ktora.football_tournament.repository.MatchRepository;
import com.github.d00ktora.football_tournament.repository.RecordRepository;
import com.github.d00ktora.football_tournament.enums.Mode;
import com.github.d00ktora.football_tournament.service.sweep.SweepService;
import com.github.d00ktora.football_tournament.entities.dto.sweep.MatchParticipation;
import com.github.d00ktora.football_tournament.entities.dto.sweep.PlayerPair;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TournamentService {
	private final MatchRepository matchRepository;
	private final RecordRepository recordRepository;
	private final SweepService sweepService;

	public List<TournamentResultDTO> processTournament(Mode mode) {

		Map<PlayerPair, Integer> globalStats = new HashMap<>();

		List<Long> matchIds = matchRepository.selectAllByMatchId();

		for (Long matchId : matchIds) {
			List<Record> records = recordRepository.findByMatchId(matchId);
			List<MatchParticipation> participations = fromRecordToMatchParticipation(records);
			Map<PlayerPair, Integer> matchStats = sweepService.processMatch(participations, mode);
			merge(globalStats, matchStats);
		}

		List<TournamentResultDTO> results = new ArrayList<>();
		addTournamentResults(globalStats, results);
		results.sort(Comparator.comparingInt(TournamentResultDTO::getPlayedTimeTogether).reversed());
		return results;
	}

	private void addTournamentResults(Map<PlayerPair, Integer> globalStats, List<TournamentResultDTO> results) {
		for (PlayerPair playerPair : globalStats.keySet()) {
			TournamentResultDTO tournamentResultDTO = new TournamentResultDTO();
			tournamentResultDTO.setPlayer1Id(playerPair.getPlayer1());
			tournamentResultDTO.setPlayer2Id(playerPair.getPlayer2());
			tournamentResultDTO.setPlayedTimeTogether(globalStats.get(playerPair));
			results.add(tournamentResultDTO);
		}
	}

	private List<MatchParticipation> fromRecordToMatchParticipation(List<Record> records) {
		List<MatchParticipation> participations = new ArrayList<>();
		for (Record r : records) {
			MatchParticipation participant = new MatchParticipation(r.getPlayer().getId(), r.getPlayer().getTeam().getId(), r.getFromMinutes(), r.getToMinutes());
			participations.add(participant);
		}
		return participations;
	}

	private void merge(Map<PlayerPair, Integer> globalStats, Map<PlayerPair, Integer> matchStats) {
		for (Map.Entry<PlayerPair, Integer> entry : matchStats.entrySet()) {
			PlayerPair pair = entry.getKey();
			Integer minutes = entry.getValue();
			globalStats.merge(pair, minutes, Integer::sum);
		}
	}
}
