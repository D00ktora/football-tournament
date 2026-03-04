package com.github.d00ktora.football_tournament.entities.dto.sweep;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlayerPair {
	
	private Long player1;
	private Long player2;

	public PlayerPair(Long p1, Long p2) {
		sortPlayersByIdAsc(p1, p2);
	}

	private void sortPlayersByIdAsc(Long p1, Long p2) {
		if (p1 < p2) {
			this.player1 = p1;
			this.player2 = p2;
		} else {
			this.player1 = p2;
			this.player2 = p1;
		}
	}
}
