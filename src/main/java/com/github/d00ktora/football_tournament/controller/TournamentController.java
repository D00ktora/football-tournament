package com.github.d00ktora.football_tournament.controller;

import com.github.d00ktora.football_tournament.entities.dto.TournamentResultDTO;
import com.github.d00ktora.football_tournament.service.tournament.TournamentService;
import com.github.d00ktora.football_tournament.enums.Mode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
public class TournamentController {

	private final TournamentService tournamentService;

	@GetMapping("results")
	public ResponseEntity<List<TournamentResultDTO>> results (@RequestParam Mode mode) {
		List<TournamentResultDTO> resultDTOS = tournamentService.processTournament(mode);
		return ResponseEntity.ok(resultDTOS);
	}

}
