package com.github.d00ktora.football_tournament.entities.dto.sweep;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {

	private Integer time;
	private Boolean start;
	private Long playerId;
	private Long teamId;
}
