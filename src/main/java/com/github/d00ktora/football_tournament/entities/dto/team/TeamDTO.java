package com.github.d00ktora.football_tournament.entities.dto.team;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamDTO {
	private Long id;
	private String group;
	private String managerFullName;
	private String name;
	private List<Long> players;
}
