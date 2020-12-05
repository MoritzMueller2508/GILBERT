package de.gilbert.main;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.SignStyle;
import java.time.temporal.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * @author Lukas Rothenbach, Yannis Eigenbrodt
 */
public class Datumserkennung extends Erkennungsmodul{

	private final List<Datumsparser> parser = new ArrayList<>();

	public Datumserkennung() {
		initialisiereParser();
	}

	@Override
	public void untersucheAnfrage(Anfrage anfrage) {
		// Die Parser sind gewichtet, indem die Reihenfolge in der Liste angepasst wird
		//  -> Der erste Parser, der ein Datum findet, entscheidet das Datum
		for (Datumsparser parser: parser) {
			LocalDate date = parser.getDatumsangabeInString(anfrage);
			if (date != null) {
				anfrage.getParameter().put("datumsangabe", date);
				break;
			}
		}
	
	}

	/**
	 * Initialisiere die Parser
	 */
	private void initialisiereParser() {
		// Tag
		parser.add(new StringFeldDifferenzParser("vorgestern", Period.ofDays(-2)));
		parser.add(new StringFeldDifferenzParser("gestern", Period.ofDays(-1)));
		parser.add(new StringFeldDifferenzParser("heute", Period.ofDays(0)));
		parser.add(new StringFeldDifferenzParser("morgen", Period.ofDays(1)));
		parser.add(new StringFeldDifferenzParser("übermorgen", Period.ofDays(2)));

		// Woche
		parser.add(new StringFeldDifferenzParser("vorletzte woche", Period.ofWeeks(-2)));
		parser.add(new StringFeldDifferenzParser("letzte woche", Period.ofWeeks(-1)));
		parser.add(new StringFeldDifferenzParser("diese woche", Period.ofWeeks(0)));
		parser.add(new StringFeldDifferenzParser("nächste woche", Period.ofWeeks(1)));
		parser.add(new StringFeldDifferenzParser("übernächste woche", Period.ofWeeks(2)));

		// Datum
		parser.add(new PatternParser("\\d{1,2}\\.\\d{1,2}\\.(\\d{4})?",
				new DateTimeFormatterBuilder()
				.appendValue(ChronoField.DAY_OF_MONTH, 1, 2, SignStyle.NEVER).appendLiteral('.')
				.appendValue(ChronoField.MONTH_OF_YEAR, 1, 2, SignStyle.NEVER).appendLiteral('.')
				.optionalStart().appendValue(ChronoField.YEAR, 1, 4, SignStyle.NORMAL).optionalEnd()
				.toFormatter()));

	}

	/**
	 * Sucht in einer Anfrage nach einem bestimmten Datumsformat
	 */
	private interface Datumsparser {
		LocalDate getDatumsangabeInString(Anfrage anfrage);
	}

	/**
	 * Sucht nach einer Zeichenkette und erzeugt dann ein Datum mit einem bestimmten
	 * Offset, z.B. StringFeldDifferenzParser("morgen", Calendar.DATE, 1) bedeutet,
	 * dass die Phrase "morgen" interpretiert wird als das Datum, das einen Tag später ist.
	 */
	private static class StringFeldDifferenzParser implements Datumsparser {
		private final String phrase;

		private final TemporalAmount differenz;

		StringFeldDifferenzParser(String phrase, TemporalAmount differenz) {
			this.phrase = phrase.toLowerCase();
			this.differenz = differenz;
		}

		LocalDate erzeugeDatum() {
			return LocalDate.now().plus(differenz);
		}

		@Override
		public LocalDate getDatumsangabeInString(Anfrage anfrage) {
			return anfrage.getAnfrage().contains(phrase)? erzeugeDatum(): null;
		}
	}

	private static class PatternParser implements Datumsparser {
		private final Pattern pattern;
		private final DateTimeFormatter parser;

		private PatternParser(String pattern, DateTimeFormatter parser) {
			this.pattern = Pattern.compile(pattern);
			this.parser = parser;
		}


		@Override
		public LocalDate getDatumsangabeInString(Anfrage anfrage) {
			Matcher matcher = pattern.matcher(anfrage.getAnfrage());

			while (matcher.find()) {
				try {
					LocalDate now = LocalDate.now();
					TemporalAccessor result = parser.parse(matcher.group());

					int dayOfMonth, month, year;

					// wenn ein benötigtes Feld nicht gesetzt ist, setzte es auf den entsprechenden Wert des heutigen Tages
					//     besonders das Feld ChronoField.YEAR kann oft nicht gesetzt sein
					if (result.isSupported(ChronoField.DAY_OF_MONTH)) dayOfMonth = result.get(ChronoField.DAY_OF_MONTH);
					else dayOfMonth = now.getDayOfMonth();
					if (result.isSupported(ChronoField.MONTH_OF_YEAR)) month = result.get(ChronoField.MONTH_OF_YEAR);
					else month = now.getMonthValue();
					if (result.isSupported(ChronoField.YEAR)) year = result.get(ChronoField.YEAR);
					else year = now.getYear();

					return LocalDate.of(year, month, dayOfMonth);
				} catch (DateTimeException ignored) {
					// Das pattern wird gefunden, ist aber kein valides Datum
					//    z.B. (pattern="\d{1,2}\.\d{1,2}\.", parser="dd.MM.") -> "50.5." passt zum pattern,
					//          ist aber kein echtes Datum, weil kein Monat 50 Tage hat
				}
			}

			return null;
		}
	}

}
