package com.github.d00ktora.football_tournament.entities.dto.csv;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalyzedData {
	private List<String> lines = new ArrayList<>();
	private String delimiter;
	private Integer expectedColumns;
}
