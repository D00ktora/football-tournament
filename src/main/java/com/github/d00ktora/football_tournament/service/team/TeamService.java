package com.github.d00ktora.football_tournament.service.team;

import com.github.d00ktora.football_tournament.entities.Team;
import com.github.d00ktora.football_tournament.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamService {
	private final TeamRepository teamRepository;

	public boolean checkIfTeamExists(Long teamId) {
		return teamRepository.existsById(teamId);
	}

	public Team getTeamById(Long teamId) {
		return teamRepository.findById(teamId).orElse(null);
	}
}
