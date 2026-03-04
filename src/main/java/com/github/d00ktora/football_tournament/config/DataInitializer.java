package com.github.d00ktora.football_tournament.config;

import com.github.d00ktora.football_tournament.repository.MatchRepository;
import com.github.d00ktora.football_tournament.repository.PlayerRepository;
import com.github.d00ktora.football_tournament.repository.RecordRepository;
import com.github.d00ktora.football_tournament.repository.TeamRepository;
import com.github.d00ktora.football_tournament.service.csv.CsvManipulator;
import com.github.d00ktora.football_tournament.enums.ImportType;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import java.io.InputStream;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

	private final CsvManipulator csvManipulator;
	private final TeamRepository teamRepository;
	private final PlayerRepository playerRepository;
	private final MatchRepository matchRepository;
	private final RecordRepository recordRepository;
	@Override
	public void run(String... args) throws Exception {

		if (isRepositoryInitialize()) {
			System.out.println("Database already initialized.");
			return;
		}

		System.out.println("Initializing database...");

		importFile("data/teams.csv", ImportType.TEAMS);
		importFile("data/players.csv", ImportType.PLAYERS);
		importFile("data/matches.csv", ImportType.MATCHES);
		importFile("data/records.csv", ImportType.RECORDS);

		System.out.println("Database initialization completed.");
	}

	private void importFile(String path, ImportType type) throws Exception {

		ClassPathResource resource = new ClassPathResource(path);

		try (InputStream inputStream = resource.getInputStream()) {

			switch (type) {
				case TEAMS -> csvManipulator.importTeams(inputStream);
				case PLAYERS -> csvManipulator.importPlayers(inputStream);
				case MATCHES -> csvManipulator.importMatches(inputStream);
				case RECORDS -> csvManipulator.importRecords(inputStream);
			}
		}
	}

	private boolean isRepositoryInitialize() {
		return teamRepository.count() > 0 || playerRepository.count() > 0 || matchRepository.count() > 0 || recordRepository.count() > 0;
	}
}
