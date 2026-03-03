package com.github.d00ktora.football_tournament.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TournamentResultDTO {
	private Long player1Id;
	private Long player2Id;
	private Integer playedTimeTogether;
}
