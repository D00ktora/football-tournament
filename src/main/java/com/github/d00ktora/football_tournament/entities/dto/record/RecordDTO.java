package com.github.d00ktora.football_tournament.entities.dto.record;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecordDTO {
	private Long id;

	private Long playerId;

	private Long matchId;

	private Integer fromMinutes;

	private Integer toMinutes;
}
