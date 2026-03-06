package com.github.d00ktora.football_tournament.service.team;

import com.github.d00ktora.football_tournament.entities.Match;
import com.github.d00ktora.football_tournament.entities.Player;
import com.github.d00ktora.football_tournament.entities.Team;
import com.github.d00ktora.football_tournament.entities.dto.team.TeamDTO;
import com.github.d00ktora.football_tournament.repository.TeamRepository;
import com.github.d00ktora.football_tournament.service.match.MatchService;
import com.github.d00ktora.football_tournament.service.player.PlayerService;
import com.github.d00ktora.football_tournament.service.records.RecordService;
import jakarta.transaction.Transactional;
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
	private final RecordService recordService;
	private final MatchService matchService;

	public TeamDTO getTeamDTOById(Long teamId) {
		Team teamFromRepo = teamRepository.findById(teamId).get();
		return createTeamDTOFromTeam(teamFromRepo);
	}

	public TeamDTO createTeam(TeamDTO teamDTO) throws BadRequestException {
		Team teamFromRepoById = getTeamById(teamDTO.getId());
		Optional<Team> teamFromRepoByName = teamRepository.findByName(teamDTO.getName());
		checkIfTeamExistByNameAndId(teamFromRepoById, teamFromRepoByName);

		Team newTeam = new Team();
		newTeam.setName(teamDTO.getName());
		newTeam.setGroup(teamDTO.getGroup());
		newTeam.setManagerFullName(teamDTO.getManagerFullName());
		List<Player> players = addPlayers(teamDTO);
		newTeam.setPlayers(players);
		teamRepository.save(newTeam);
		return createTeamDTOFromTeam(newTeam);
	}

	public TeamDTO updateTeam(TeamDTO teamDTO) throws BadRequestException {
			Team teamForUpdate = teamRepository.findById(teamDTO.getId()).get();
		if(checkIfTeamExistsById(teamDTO.getId())) {
			teamForUpdate.setName(teamDTO.getName());
			teamForUpdate.setGroup(teamDTO.getGroup());
			teamForUpdate.setManagerFullName(teamDTO.getManagerFullName());
			teamForUpdate.setPlayers(updatePlayers(teamDTO, teamForUpdate));
			teamRepository.save(teamForUpdate);
		}
		return createTeamDTOFromTeam(teamForUpdate);
	}

	@Transactional
	public void deleteTeam(Long teamId) throws BadRequestException {
		recordService.deleteRecord(teamId);

		Player player = playerService.getPlayerById(teamId);
		playerService.removePlayer(player.getId());

		List<Match> allMatchesByTeamId = matchService.getAllMatchesByTeamId(teamId);
		for (Match match : allMatchesByTeamId) {
			matchService.archiveMatch(match);
		}

		teamRepository.deleteById(teamId);
	}

	private boolean checkIfPlayersExist(List<Long> players) throws BadRequestException {
		for (Long player : players) {
			playerService.getPlayerDtoById(player);
		}
		return true;
	}

	public boolean checkIfTeamExistsById(Long teamId) {
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

	private void checkIfTeamExistByNameAndId(Team teamFromRepoById, Optional<Team> teamFromRepoByName) throws BadRequestException {
		if (teamFromRepoById == null || teamFromRepoByName.isPresent()) {
			throw new BadRequestException("Team already Exist");
		}
	}

	private List<Player> addPlayers(TeamDTO teamDTO) throws BadRequestException {
		List<Player> players = new ArrayList<>();
		if (checkIfPlayersExist(teamDTO.getPlayers())){
			for (Long playerId : teamDTO.getPlayers()) {
				Player playerFromRepo = playerService.getPlayerById(playerId);
				players.add(playerFromRepo);
			}
		}
		return players;
	}

	private List<Player> updatePlayers(TeamDTO teamDTO, Team team) throws BadRequestException {
		List<Player> players = team.getPlayers();
		List<Player> newPlayersList = team.getPlayers();
		if (checkIfPlayersExist(teamDTO.getPlayers())){
			for (Long playerID : teamDTO.getPlayers()) {
				Player player = playerService.getPlayerById(playerID);
				players.add(player);
			}
		}
		for (Player player : players) {
			if (newPlayersList.contains(player)) continue;
			else newPlayersList.add(player);
		}
		return newPlayersList;
	}
}
