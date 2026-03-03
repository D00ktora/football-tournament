package com.github.d00ktora.football_tournament.service.csv;

import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@NoArgsConstructor
public class DateParser {
	private static final List<DateTimeFormatter> FORMATTERS = new ArrayList<>();

	static {
		FORMATTERS.add(DateTimeFormatter.ISO_DATE_TIME);
		FORMATTERS.add(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		FORMATTERS.add(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
		FORMATTERS.add(DateTimeFormatter.ISO_ZONED_DATE_TIME);
		FORMATTERS.add(DateTimeFormatter.ISO_LOCAL_DATE);

		addNumericPattern("yyyy-M-d");
		addNumericPattern("d-M-yyyy");
		addNumericPattern("M-d-yyyy");
		addNumericPattern("yyyy/M/d");
		addNumericPattern("d/M/yyyy");
		addNumericPattern("M/d/yyyy");
		addNumericPattern("yyyy.M.d");
		addNumericPattern("d.M.yyyy");

		addTextPattern("d MMM yyyy", Locale.ENGLISH);
		addTextPattern("d MMMM yyyy", Locale.ENGLISH);
		addTextPattern("MMM d yyyy", Locale.ENGLISH);
		addTextPattern("MMMM d yyyy", Locale.ENGLISH);

		addTextPattern("d MMM yyyy HH:mm", Locale.ENGLISH);
		addTextPattern("d MMMM yyyy HH:mm", Locale.ENGLISH);
		addTextPattern("MMM d yyyy HH:mm", Locale.ENGLISH);
		addTextPattern("MMMM d yyyy HH:mm", Locale.ENGLISH);
	}

	public static LocalDate parseToDate(String input) {
		return parseToDateTime(input).toLocalDate();
	}

	public static LocalDateTime parseToDateTime(String input) {

		String value = input.trim();

		if (value.matches("^\\d{13}$")) {
			return Instant.ofEpochMilli(Long.parseLong(value))
					.atZone(ZoneId.systemDefault())
					.toLocalDateTime();
		}

		if (value.matches("^\\d{10}$")) {
			return Instant.ofEpochSecond(Long.parseLong(value))
					.atZone(ZoneId.systemDefault())
					.toLocalDateTime();
		}

		for (DateTimeFormatter formatter : FORMATTERS) {
			try {
				return LocalDateTime.parse(value, formatter);
			} catch (DateTimeParseException ignored) {
			}

			try {
				return LocalDate.parse(value, formatter).atStartOfDay();
			} catch (DateTimeParseException ignored) {
			}

			try {
				return ZonedDateTime.parse(value, formatter).toLocalDateTime();
			} catch (DateTimeParseException ignored) {
			}

			try {
				return OffsetDateTime.parse(value, formatter).toLocalDateTime();
			} catch (DateTimeParseException ignored) {
			}
		}

		throw new RuntimeException("Unsupported date format: " + input);
	}

	private static void addNumericPattern(String basePattern) {

		DateTimeFormatter formatter = new DateTimeFormatterBuilder()
				.parseCaseInsensitive()
				.appendPattern(basePattern)
				.optionalStart()
				.appendPattern(" HH:mm")
				.optionalStart()
				.appendPattern(":ss")
				.optionalStart()
				.appendPattern(".SSS")
				.optionalEnd()
				.optionalEnd()
				.optionalEnd()
				.parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
				.parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
				.toFormatter();
		FORMATTERS.add(formatter);
	}

	private static void addTextPattern(String pattern, Locale locale) {

		DateTimeFormatter formatter = new DateTimeFormatterBuilder()
				.parseCaseInsensitive()
				.appendPattern(pattern)
				.optionalStart()
				.appendPattern(":ss")
				.optionalEnd()
				.toFormatter(locale);

		FORMATTERS.add(formatter);
	}
}
