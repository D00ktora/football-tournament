package com.github.d00ktora.football_tournament.entities.dto.sweep;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchParticipation {
	private Long playerId;
	private Long teamId;
	private Integer fromMinute;
	private Integer toMinute;
}
