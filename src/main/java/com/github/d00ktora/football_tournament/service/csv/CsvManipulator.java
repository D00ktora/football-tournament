package com.github.d00ktora.football_tournament.service.csv;

import com.github.d00ktora.football_tournament.entities.Match;
import com.github.d00ktora.football_tournament.entities.Player;
import com.github.d00ktora.football_tournament.entities.Record;
import com.github.d00ktora.football_tournament.entities.Team;
import com.github.d00ktora.football_tournament.repository.MatchRepository;
import com.github.d00ktora.football_tournament.repository.PlayerRepository;
import com.github.d00ktora.football_tournament.repository.RecordRepository;
import com.github.d00ktora.football_tournament.repository.TeamRepository;
import com.github.d00ktora.football_tournament.service.csv.dto.AnalyzedData;
import com.github.d00ktora.football_tournament.service.csv.dto.CsvData;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Transactional
public class CsvManipulator {

	private final PlayerRepository playerRepository;
	private final TeamRepository teamRepository;
	private final MatchRepository matchRepository;
	private final RecordRepository recordRepository;
	private final CsvAnalyzer csvAnalyzer;

	public void importPlayers(InputStream inputStream) {
		CsvData csvData = getCsvData(inputStream);
		List<Player> players = extractPlayers(csvData);
		playerRepository.saveAll(players);
	}

	public void importTeams(InputStream inputStream) {
		CsvData csvData = getCsvData(inputStream);
		List<Team> teams = extractTeams(csvData);
		teamRepository.saveAll(teams);

	}

	public void importMatches(InputStream inputStream) {
		CsvData csvData = getCsvData(inputStream);
		List<Match> matches = extractMatches(csvData);
		matchRepository.saveAll(matches);
	}

	public void importRecords(InputStream inputStream) {
		CsvData csvData = getCsvData(inputStream);
		List<Record> records = extractRecords(csvData);
		recordRepository.saveAll(records);
	}

	private CsvData getCsvData(InputStream inputStream) {
		AnalyzedData analyzedData = csvAnalyzer.analyze(inputStream);
		return csvAnalyzer.extractData(analyzedData);
	}

	private List<Player> extractPlayers(CsvData csvData) {
		List<Player> players = new ArrayList<>();
		for (String[] row : csvData.getRows()) {
			players.add(createPlayer(row));
		}
		return players;
	}

	private Player createPlayer(String[] row) {
		Player player = new Player();
		player.setTeamNumber(Integer.parseInt(row[1].trim()));
		player.setPosition(row[2].trim());
		player.setFullName(row[3].trim());
		Long teamId = Long.parseLong(row[4].trim());
		Team team = teamRepository.findById(teamId).orElseThrow(() -> new RuntimeException("Team not found with id: " + teamId));
		player.setTeam(team);
		return player;
	}

	private List<Team> extractTeams(CsvData csvData) {
		List<Team> teams = new ArrayList<>();
		for (String[] row : csvData.getRows()) {
			Team team = new Team();
			team.setName(row[1].trim());
			team.setManagerFullName(row[2].trim());
			team.setGroup(row[3].trim());
			teams.add(team);
		}
		return teams;
	}

	private List<Match> extractMatches(CsvData csvData) {
		List<Match> matches = new ArrayList<>();
		for (String[] row : csvData.getRows()) {
			Match match = new Match();
			match.setTeamA(getTeamIfExist(Long.parseLong(row[1].trim())));
			match.setTeamB(getTeamIfExist(Long.parseLong(row[2].trim())));
			match.setDate(DateParser.parseToDateTime(row[3].trim()));
			match.setScore(extractResult(row[4]));
			matches.add(match);
		}
		return matches;
	}

	private Team getTeamIfExist(Long teamAId) {
		return teamRepository.findById(teamAId).orElseThrow(() -> new EntityNotFoundException("Team A not found with id: " + teamAId));
	}

	private String extractResult(String row) {
		if (row.contains("(")) {
			StringBuilder outputResult = new StringBuilder();
			Pattern pattern = Pattern.compile("\\((.*?)\\)");
			Matcher matcher = pattern.matcher(row);
			int counter = 0;
			while (matcher.find()) {
				outputResult.append(matcher.group(1));
				if (counter < 1) {
					outputResult.append("-");
					counter++;
				}
			}
			return outputResult.toString().trim();
		} else {
			return row.trim();
		}
	}

	private List<Record> extractRecords(CsvData csvData) {
		List<Record> records = new ArrayList<>();
		for (String[] row : csvData.getRows()) {
			Record record = new Record();
			record.setPlayer(getPlayerIfExist(Long.parseLong(row[1].trim())));
			record.setMatch(getMatchIfExist(Long.parseLong(row[2].trim())));
			record.setFromMinutes(Integer.parseInt(row[3].trim()));
			record.setToMinutes(row[4].equals("NULL") ? 90 : Integer.parseInt(row[4].trim()));
			records.add(record);
		}
		return records;
	}

	private Match getMatchIfExist(Long matchId) {
		return matchRepository.findById(matchId).orElseThrow(() -> new RuntimeException("Match not found with id: " + matchId));
	}

	private Player getPlayerIfExist(Long playerId) {
		return playerRepository.findById(playerId).orElseThrow(() -> new RuntimeException("Player not found with id: " + playerId));
	}
}
