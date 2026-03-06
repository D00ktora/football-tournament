package com.github.d00ktora.football_tournament.service.records;

import com.github.d00ktora.football_tournament.entities.Match;
import com.github.d00ktora.football_tournament.entities.Player;
import com.github.d00ktora.football_tournament.entities.Record;
import com.github.d00ktora.football_tournament.entities.dto.record.RecordDTO;
import com.github.d00ktora.football_tournament.repository.RecordRepository;
import com.github.d00ktora.football_tournament.service.match.MatchService;
import com.github.d00ktora.football_tournament.service.player.PlayerService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class RecordService {
	private final RecordRepository recordRepository;
	private final PlayerService playerService;
	private final MatchService matchService;

	public RecordDTO createRecord(RecordDTO recordDTO) throws BadRequestException {
		Record record = createRecordFromDTO(recordDTO);
		Record savedRecord = recordRepository.save(record);
		return createRecordDTOFromRecord(savedRecord);
	}

	public RecordDTO getRecordById(Long recordId) throws BadRequestException {
		Record record = recordRepository.findById(recordId).orElse(null);
		if (record != null) {
			return createRecordDTOFromRecord(record);
		}
		throw new BadRequestException("Record do not exist.");
	}

	public RecordDTO updateRecord(RecordDTO recordDTO) throws BadRequestException {
		Record record = recordRepository.findById(recordDTO.getId()).orElse(null);

		if (record == null) {
			throw new BadRequestException("Record already exists.");
		}

		Record recordFromDTO = createRecordFromDTO(recordDTO);
		record.setToMinutes(recordFromDTO.getToMinutes());
		record.setFromMinutes(recordFromDTO.getFromMinutes());
		record.setMatch(recordFromDTO.getMatch());
		record.setPlayer(recordFromDTO.getPlayer());
		recordRepository.save(record);
		return createRecordDTOFromRecord(record);
	}

	public void deleteRecord(Long id) {
		recordRepository.deleteById(id);
	}

	private Record createRecordFromDTO(RecordDTO recordDTO) throws BadRequestException {
		Player player = playerService.getPlayerById(recordDTO.getPlayerId());
		Match match = matchService.getMatchById(recordDTO.getMatchId());
		if (player == null) {
			throw new BadRequestException("Player do not exist");
		}
		if (match == null) {
			throw new BadRequestException("Match do not exist");
		}
		Record record = new Record();
		record.setMatch(match);
		record.setPlayer(player);
		record.setFromMinutes(recordDTO.getFromMinutes());
		record.setToMinutes(recordDTO.getToMinutes());
		return record;
	}

	private RecordDTO createRecordDTOFromRecord(Record record) {
		RecordDTO dto = new RecordDTO();
		dto.setFromMinutes(record.getFromMinutes());
		dto.setToMinutes(record.getToMinutes());
		dto.setId(record.getId());
		dto.setPlayerId(record.getPlayer().getId());
		dto.setMatchId(record.getMatch().getId());
		return dto;
	}
}
