package com.github.d00ktora.football_tournament.controller;

import com.github.d00ktora.football_tournament.entities.dto.player.PlayerDTO;
import com.github.d00ktora.football_tournament.service.player.PlayerService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/players/")
public class PlayerController {
	private final PlayerService playerService;

	@PostMapping("create")
	public ResponseEntity<PlayerDTO> createPlayer(@RequestBody PlayerDTO playerDTO) throws BadRequestException {
		return ResponseEntity.ok(playerService.createPlayer(playerDTO));
	}

	@GetMapping("{id}")
	public ResponseEntity<PlayerDTO> getPlayer(@PathVariable Long id) throws BadRequestException {
		return ResponseEntity.ok(playerService.getPlayerDtoById(id));
	}

	@PostMapping("update")
	public ResponseEntity<PlayerDTO> updatePlayer(@RequestBody PlayerDTO playerDTO) throws BadRequestException {
		return ResponseEntity.ok(playerService.updatePlayer(playerDTO));
	}

	@PostMapping("delete/{id}")
	public ResponseEntity.BodyBuilder deletePlayer(@PathVariable Long id) throws BadRequestException {
		playerService.removePlayer(id);
		return ResponseEntity.ok();
	}


}
