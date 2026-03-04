package com.github.d00ktora.football_tournament.service.sweep;

import com.github.d00ktora.football_tournament.entities.dto.sweep.Event;
import com.github.d00ktora.football_tournament.entities.dto.sweep.MatchParticipation;
import com.github.d00ktora.football_tournament.entities.dto.sweep.PlayerPair;
import com.github.d00ktora.football_tournament.enums.Mode;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SweepService {
	public Map<PlayerPair, Integer> processMatch(List<MatchParticipation> matchParticipations, Mode mode) {

		Map<PlayerPair, Integer> matchStats = new HashMap<>();

		List<Event> events = convertMatchParticipantsToEvent(matchParticipations);
		sortByTimeStartBeforeEnd(events);

		Map<Long, Long> activePlayers = new HashMap<>();
		int previousTime = 0;
		sweep(mode, events, previousTime, activePlayers, matchStats);

		return matchStats;
	}

	private List<Event> convertMatchParticipantsToEvent(List<MatchParticipation> matchParticipations) {
		List<Event> events = new ArrayList<>();
		for (MatchParticipation p : matchParticipations) {
			events.add(new Event(p.getFromMinute(), true, p.getPlayerId(), p.getTeamId()));
			events.add(new Event(p.getToMinute(), false, p.getPlayerId(), p.getTeamId()));
		}
		return events;
	}

	private void sortByTimeStartBeforeEnd(List<Event> events) {
		events.sort((e1, e2) -> {
			if (e1.getTime() != e2.getTime()) {
				return Integer.compare(e1.getTime(), e2.getTime());
			}
			return Boolean.compare(!e1.getStart(), !e2.getStart());
		});
	}

	private void sweep(Mode mode, List<Event> events, int previousTime, Map<Long, Long> activePlayers, Map<PlayerPair, Integer> matchStats) {
		for (Event event : events) {
			int currentTime = event.getTime();
			int delta = currentTime - previousTime;
			updatePlayersPairTimeTogether(mode, delta, activePlayers, matchStats);
			addRemoveActivePlayers(event, activePlayers);
			previousTime = currentTime;
		}
	}

	private void updatePlayersPairTimeTogether(Mode mode, int delta, Map<Long, Long> activePlayers, Map<PlayerPair, Integer> matchStats) {
		if (delta > 0 && activePlayers.size() > 1) {
			List<Map.Entry<Long, Long>> activeList = new ArrayList<>(activePlayers.entrySet());

			for (int i = 0; i < activeList.size(); i++) {
				for (int j = i + 1; j < activeList.size(); j++) {
					Long player1 = activeList.get(i).getKey();
					Long team1 = activeList.get(i).getValue();

					Long player2 = activeList.get(j).getKey();
					Long team2 = activeList.get(j).getValue();

					if (isValidPair(team1, team2, mode)) {
						PlayerPair pair = new PlayerPair(player1, player2);
						matchStats.merge(pair, delta, Integer::sum);
					}
				}
			}
		}
	}

	private boolean isValidPair(Long team1, Long team2, Mode mode) {
		return switch (mode) {
			case ALL -> true;
			case SAME_TEAM -> team1.equals(team2);
			case OPPOSITE_TEAM -> !team1.equals(team2);
		};
	}

	private void addRemoveActivePlayers(Event event, Map<Long, Long> activePlayers) {
		if (event.getStart()) {
			activePlayers.put(event.getPlayerId(), event.getTeamId());
		} else {
			activePlayers.remove(event.getPlayerId());
		}
	}
}

