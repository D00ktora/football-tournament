package com.github.d00ktora.football_tournament.service.team;

import com.github.d00ktora.football_tournament.entities.Player;
import com.github.d00ktora.football_tournament.entities.Team;
import com.github.d00ktora.football_tournament.entities.dto.team.TeamDTO;
import com.github.d00ktora.football_tournament.repository.TeamRepository;
import com.github.d00ktora.football_tournament.service.player.PlayerService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class TeamService {
	private final TeamRepository teamRepository;
	private final PlayerService playerService;

	public TeamDTO getTeamDTOById(Long teamId) {
		Team teamFromRepo = teamRepository.findById(teamId).get();
		return createTeamDTOFromTeam(teamFromRepo);
	}

	public TeamDTO createTeam(TeamDTO teamDTO) throws BadRequestException {
		Team teamFromRepoById = getTeamById(teamDTO.getId());
		Optional<Team> teamFromRepoByName = teamRepository.findByName(teamDTO.getName());
		if (teamFromRepoById == null || teamFromRepoByName.isPresent()) {
			throw new BadRequestException("Team already Exist");
		}

		Team newTeam = new Team();
		newTeam.setName(teamDTO.getName());
		newTeam.setGroup(teamDTO.getGroup());
		newTeam.setManagerFullName(teamDTO.getManagerFullName());
		List<Player> players = new ArrayList<>();
		if (checkIfPlayersExist(teamDTO.getPlayers())){
			for (Long playerId : teamDTO.getPlayers()) {
				Player playerFromRepo = playerService.getPlayerById(playerId);
				players.add(playerFromRepo);
			}
		}
		newTeam.setPlayers(players);
		teamRepository.save(newTeam);
		return createTeamDTOFromTeam(newTeam);
	}

	private boolean checkIfPlayersExist(List<Long> players) throws BadRequestException {
		for (Long player : players) {
			playerService.getPlayerDtoById(player);
		}
		return true;
	}

	public boolean checkIfTeamExists(Long teamId) {
		return teamRepository.existsById(teamId);
	}

	public Team getTeamById(Long teamId) {
		return teamRepository.findById(teamId).orElse(null);
	}

	private static TeamDTO createTeamDTOFromTeam(Team teamFromRepo) {
		TeamDTO teamDTO = new TeamDTO();
		if (teamFromRepo != null) {
			teamDTO.setId(teamFromRepo.getId());
			teamDTO.setName(teamFromRepo.getName());
			teamDTO.setGroup(teamFromRepo.getGroup());
			teamDTO.setManagerFullName(teamFromRepo.getManagerFullName());
		}
		return teamDTO;
	}
}
