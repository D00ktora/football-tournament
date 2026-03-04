package com.github.d00ktora.football_tournament.entities.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerDTO {
	private Long id;
	@NotNull
	@NotBlank
	private String fullName;
	@NotNull
	@NotBlank
	private String position;
	@NotNull
	@NotBlank
	private Long teamNumber;
	@NotNull
	@NotBlank
	private Long teamId;
}
