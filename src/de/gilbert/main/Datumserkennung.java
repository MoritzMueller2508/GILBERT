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
 * Ein Erkennungsmodul, das versucht eine Datumsangabe in einer Anfrage zu finden
 *
 * @author Lukas Rothenbach, Yannis Eigenbrodt
 */
public class Datumserkennung extends Erkennungsmodul {

	/** Eine Liste der verwendeten Parser. Je kleiner der Index, desto früher wird er ausgewertet */
	private final List<Datumsparser> parser = new ArrayList<>();

	/** Erzeugt eine Datumserkennung und initialisiert die Standardparser */
	public Datumserkennung() {
		initialisiereParser();
	}

	/**
	 * Untersucht die Anfrage auf eine Datumsangabe
	 * @param anfrage die zu untersuchende Anfrage
	 */
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
		parser.add(new StringFeldDifferenzParser("heute", Period.ofDays(0)));
		parser.add(new StringFeldDifferenzParser("übermorgen", Period.ofDays(2)));
		parser.add(new StringFeldDifferenzParser("uebermorgen", Period.ofDays(2)));
		parser.add(new StringFeldDifferenzParser("morgen", Period.ofDays(1)));
		parser.add(new StringFeldDifferenzParser("vorgestern", Period.ofDays(-2)));
		parser.add(new StringFeldDifferenzParser("gestern", Period.ofDays(-1)));

		// Woche
		parser.add(new StringFeldDifferenzParser("diese woche", Period.ofWeeks(0)));
		parser.add(new StringFeldDifferenzParser("übernächste woche", Period.ofWeeks(2)));
		parser.add(new StringFeldDifferenzParser("uebernaechste woche", Period.ofWeeks(2)));
		parser.add(new StringFeldDifferenzParser("nächste woche", Period.ofWeeks(1)));
		parser.add(new StringFeldDifferenzParser("naechste woche", Period.ofWeeks(1)));
		parser.add(new StringFeldDifferenzParser("vorletzte woche", Period.ofWeeks(-2)));
		parser.add(new StringFeldDifferenzParser("letzte woche", Period.ofWeeks(-1)));

		// Datum (dd.MM.yyyy) oder (dd.MM.)
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

		/**
		 * Sucht in der gegebenen Anfrage nach einer Datumsangabe in einem bestimmten Format.
		 *
		 * @param anfrage die zu untersuchende Anfrage
		 * @return ein Datum oder null, wenn kein Datum angegeben war
		 */
		LocalDate getDatumsangabeInString(Anfrage anfrage);
	}

	/**
	 * Sucht nach einer Zeichenkette und erzeugt dann ein Datum mit einem bestimmten
	 * Offset, z.B. StringFeldDifferenzParser("morgen", Period.ofDays(1)) bedeutet,
	 * dass die Phrase "morgen" interpretiert wird als das Datum, das einen Tag später ist.
	 */
	private static class StringFeldDifferenzParser implements Datumsparser {
		/** die zu suchende Phrase */
		private final String phrase;
		/** die Differenz zu heute, die die Phrase impliziert */
		private final TemporalAmount differenz;

		/** Erzeugt einen neuen Parser mit den gegebenen Werten */
		StringFeldDifferenzParser(String phrase, TemporalAmount differenz) {
			this.phrase = phrase.toLowerCase(); // ignore case
			this.differenz = differenz;
		}

		/** erzeugt ein Datum mit der gegebenen Differenz */
		LocalDate erzeugeDatum() {
			return LocalDate.now().plus(differenz);
		}

		/**
		 * Sucht in der Anfrage nach der gegebenen Phrase.
		 * Wenn die Phase gefunden wird, wird ein Datum erzeugt und zurückgegeben.
		 *
		 * @param anfrage die zu untersuchende Anfrage
		 * @return ein Datum mit einem festen Offset oder null, wenn die Phrase nicht gefunden wird
		 */
		@Override
		public LocalDate getDatumsangabeInString(Anfrage anfrage) {
			return anfrage.getAnfrage().toLowerCase().contains(phrase)? erzeugeDatum(): null;
		}
	}

	/**
	 * Sucht nach einem Pattern und versucht aus dem gefundenen Teilstring ein Datum zu erzeugen.
	 */
	private static class PatternParser implements Datumsparser {
		/** Das Pattern, nach dem gesucht wird */
		private final Pattern pattern;
		/** Ein Parser, der zum Pattern passt */
		private final DateTimeFormatter parser;

		/**
		 * Erzeugt einen neuen Parser mit dem gegebenen Pattern und Parser.
		 * <b>Achtung!</b> Wenn Pattern und Parser nicht zusammenpassen,
		 * kann kein Datum gefunden werden.
		 */
		private PatternParser(String pattern, DateTimeFormatter parser) {
			this.pattern = Pattern.compile(pattern);
			this.parser = parser;
		}

		/**
		 * Sucht in der Anfrage nach dem gegebenen Muster.
		 * Wenn das Muster gefunden wird, wird versucht daraus ein Datum zu erzeugt.<br>
		 * Wenn nicht alle Angaben für ein Datum in dem erzeugten Datum gefunden werden,
		 * werden die fehlenden Angaben mit Angaben aus dem heutigen Tag aufgefüllt.
		 *
		 * @param anfrage die zu untersuchende Anfrage
		 * @return das erzeugte Datum oder null, wenn kein Datum erzeugt werden konnte
		 */
		@Override
		public LocalDate getDatumsangabeInString(Anfrage anfrage) {
			// ein Matcher untersucht eine Zeichenkette nach bestimmten Mustern
			Matcher matcher = pattern.matcher(anfrage.getAnfrage());

			while (matcher.find()) { // es gibt einen zum Muster passende Substring
				try {
					// heute zum Auffuellen von Daten
					LocalDate now = LocalDate.now();
					// versuche aus der Zeichenkette eine Zeitangabe zu erzeugen
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
					// Das Muster wird gefunden, ist aber kein valides Datum
					//    z.B. (pattern="\d{1,2}\.\d{1,2}\.", parser="dd.MM.") -> "50.5." passt zum pattern,
					//          ist aber kein echtes Datum, weil kein Monat 50 Tage hat
				}
			}

			// kein Datum konnte erzeugt werden
			return null;
		}
	}

}
