package com.github.d00ktora.football_tournament.service.player;

import com.github.d00ktora.football_tournament.entities.Player;
import com.github.d00ktora.football_tournament.entities.Team;
import com.github.d00ktora.football_tournament.entities.dto.PlayerDTO;
import com.github.d00ktora.football_tournament.repository.PlayerRepository;
import com.github.d00ktora.football_tournament.service.team.TeamService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlayerService {
	private final PlayerRepository playerRepository;
	private final TeamService teamService;

	public PlayerDTO createPlayer(PlayerDTO player) throws BadRequestException {
		checkIfItsPlayerDataValid(player);
		Player newPlayer = createPlayerFromDTO(player);
		Player createdPayer = playerRepository.save(newPlayer);
		return createPlayerDtoFromPlayer(createdPayer);
	}

	public PlayerDTO updatePlayer(PlayerDTO player) throws BadRequestException {
		if (isPlayerAlreadyExist(player.getFullName())) {
			Player playerForUpdate = playerRepository.findByFullName(player.getFullName()).get();
			updatePlayer(player, playerForUpdate);
			playerRepository.save(playerForUpdate);
		}
		throw new BadRequestException("Player do not exist");
	}

	public void removePlayer(Long player) throws BadRequestException {
		Optional<Player> playerFromRepo = playerRepository.findById(player);
		if (playerFromRepo.isPresent()) {
			playerRepository.delete(playerFromRepo.get());
			return;
		}
		throw new BadRequestException("Player do not exist.");
	}

	public PlayerDTO getPlayerById(Long id) throws BadRequestException {
		Optional<Player> playerFromRepo = playerRepository.findById(id);
		if (playerFromRepo.isPresent()) {
			return createPlayerDtoFromPlayer(playerFromRepo.get());
		}
		throw new BadRequestException("Player is not exist.");
	}

	private void checkIfItsPlayerDataValid(PlayerDTO player) throws BadRequestException {
		if (isPlayerAlreadyExist(player.getFullName())) {
			throw new BadRequestException("Player Already Exist");
		}
		if (!isTeamExist(player.getTeamId())) {
			throw new BadRequestException("Team Not Exist");
		}
		if (!isPlayerNumberValid(player.getTeamNumber(), player.getTeamId())) {
			throw new BadRequestException("Team Number Not Valid");
		}
		if (isPlayerPositionAvailable(player.getPosition(), player.getTeamId())) {
			throw new BadRequestException("Player Position Is Not Available");
		}
	}

	private boolean isPlayerAlreadyExist(String playerName) {
		return playerRepository.findByFullName(playerName).isPresent();
	}

	private boolean isTeamExist(Long teamId) {
		return teamService.checkIfTeamExists(teamId);
	}

	private boolean isPlayerNumberValid(Long number, Long teamId) {
		List<Player> allByTeamId = playerRepository.findAllByTeamId(teamId);
		for (Player player : allByTeamId) {
			if (player.getId().equals((long) number)) {
				return false;
			}
		}
		return true;
	}

	private boolean isPlayerPositionAvailable(String position, Long teamId) {
		List<Player> allByTeamId = playerRepository.findAllByTeamId(teamId);
		for (Player player : allByTeamId) {
			if (player.getPosition().equals(position) && player.getPosition().equals("GK")) {
				return false;
			}
		}
		return true;
	}

	private Player createPlayerFromDTO(PlayerDTO playerDTO) {
		Player newPlayer = new Player();
		newPlayer.setFullName(playerDTO.getFullName());
		newPlayer.setTeamNumber(playerDTO.getTeamNumber().intValue());
		newPlayer.setPosition(playerDTO.getPosition());
		Team teamById = teamService.getTeamById(playerDTO.getTeamNumber());
		if (teamById != null) {
			newPlayer.setTeam(teamById);
		}
		return newPlayer;
	}

	private PlayerDTO createPlayerDtoFromPlayer(Player player) {
		PlayerDTO playerDTO = new PlayerDTO();
		playerDTO.setFullName(player.getFullName());
		playerDTO.setPosition(player.getPosition());
		playerDTO.setTeamId(player.getTeam().getId());
		playerDTO.setId(player.getId());
		return playerDTO;
	}

	private void updatePlayer(PlayerDTO player, Player playerForUpdate) throws BadRequestException {
		playerForUpdate.setFullName(player.getFullName());
		playerForUpdate.setPosition(player.getPosition());
		playerForUpdate.setTeamNumber(player.getTeamNumber().intValue());
		if (isTeamExist(player.getTeamId())) {
			playerForUpdate.setTeam(teamService.getTeamById(player.getTeamId()));
		} else {
			throw new BadRequestException("Team do not exist");
		}
	}
}
