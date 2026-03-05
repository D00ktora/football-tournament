package com.github.d00ktora.football_tournament.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArchiveMatch {
	private Long id;
	private Long teamAId;
	private Long teamBId;
	private LocalDateTime date;
	private String score;
}
