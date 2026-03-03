package com.github.d00ktora.football_tournament.service.csv.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class CsvData {
	private final String delimiter;
	private final List<String[]> rows;
}
