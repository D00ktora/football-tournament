package com.github.d00ktora.football_tournament.service.csv;

import com.github.d00ktora.football_tournament.entities.dto.csv.AnalyzedData;
import com.github.d00ktora.football_tournament.entities.dto.csv.CsvData;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class CsvAnalyzer {

	public AnalyzedData analyze(InputStream inputStream) {
		AnalyzedData analyzedData = new AnalyzedData();
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

		List<String> lines = reader.lines().toList();
		checkIfCsvIsEmpty(lines);
		analyzedData.setLines(lines);

		String header = lines.getFirst();
		String delimiter = detectDelimiter(header);
		analyzedData.setDelimiter(delimiter);

		int expectedColumns = header.split(Pattern.quote(delimiter)).length;
		analyzedData.setExpectedColumns(expectedColumns);

		return analyzedData;
	}

	public CsvData extractData(AnalyzedData analyzedData) {
		List<String[]> parsedRows = new ArrayList<>();

		for (int i = 1; i < analyzedData.getLines().size(); i++) {
			String[] row = analyzedData.getLines().get(i).split(Pattern.quote(analyzedData.getDelimiter()));
			checkIfRowIsComplete(row, analyzedData.getExpectedColumns(), i);
			parsedRows.add(row);
		}
		return new CsvData(analyzedData.getDelimiter(), parsedRows);
	}

	private void checkIfRowIsComplete(String[] row, int expectedColumns, int i) {
		if (row.length != expectedColumns) {
			throw new RuntimeException("Invalid CSV format at line " + (i + 1) + ". Expected " + expectedColumns + " columns but found " + row.length);
		}
	}

	private void checkIfCsvIsEmpty(List<String> lines) {
		if (lines.isEmpty()) {
			throw new RuntimeException("Empty CSV file");
		}
	}

	private String detectDelimiter(String headerLine) {

		String[] possibleDelimiters = {",", ";", "\t", "|", ":"};
		int maxCount = 0;
		String detectedDelimiter = ",";

		for (String delimiter : possibleDelimiters) {
			int count = headerLine.split(Pattern.quote(delimiter), -1).length;
			if (count > maxCount) {
				maxCount = count;
				detectedDelimiter = delimiter;
			}
		}
		return detectedDelimiter;
	}

}
