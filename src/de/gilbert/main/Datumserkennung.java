package de.gilbert.main;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.spi.CalendarDataProvider;
/**
 * @author Lukas Rothenbach, Yannis Eigenbrodt
 */
public class Datumserkennung extends Erkennungsmodul{

	private List<Datumsparser> parser = new ArrayList<>();

	public Datumserkennung() {
		initialisiereParser();
	}

	@Override
	public void untersucheAnfrage(Anfrage anfrage) {
		// Die Parser sind gewichtet, indem die Reihenfolge in der Liste angepasst wird
		//  -> Der erste Parser, der ein Datum findet, entscheidet das Datum
		for (Datumsparser parser: parser) {
			Date date = parser.getDatumsangabeInString(anfrage);
			if (date != null) anfrage.getParameter().put("datumsangabe", date);
		}
	
	}

	/**
	 * Initialisiere die Parser
	 */
	private void initialisiereParser() {
		// Tag
		parser.add(new StringFeldDifferenzParser("vorgestern", Calendar.DATE, -2));
		parser.add(new StringFeldDifferenzParser("gestern", Calendar.DATE, -1));
		parser.add(new StringFeldDifferenzParser("heute", Calendar.DATE, 0));
		parser.add(new StringFeldDifferenzParser("morgen", Calendar.DATE, 1));
		parser.add(new StringFeldDifferenzParser("übermorgen", Calendar.DATE, 2));

		// Woche
		parser.add(new StringFeldDifferenzParser("vorletzte woche", Calendar.WEEK_OF_YEAR, -2));
		parser.add(new StringFeldDifferenzParser("letzte woche", Calendar.WEEK_OF_YEAR, -1));
		parser.add(new StringFeldDifferenzParser("diese woche", Calendar.WEEK_OF_YEAR, 0));
		parser.add(new StringFeldDifferenzParser("nächste woche", Calendar.WEEK_OF_YEAR, 1));
		parser.add(new StringFeldDifferenzParser("übernächste woche", Calendar.WEEK_OF_YEAR, 2));

		// Datum
		parser.add(new PatternParser("\\d{1,2}\\.\\d{1,2}\\.\\d{4}", new SimpleDateFormat("dd.MM.yyyy"), true));
		parser.add(new PatternParser("\\d{1,2}\\.\\d{1,2}", new SimpleDateFormat("dd.MM"), false));
	}

	/**
	 * Erstellt ein Calenderobjekt, dass auf den Beginn des aktuellen Tages zeigt.  
	 * @return das erstellte Kalenderobjekt
	 */
	private static Calendar setupKalender() {
		Calendar kalender = Calendar.getInstance();
		kalender.set(Calendar.HOUR_OF_DAY, 0);
		kalender.set(Calendar.MINUTE, 0);
		kalender.set(Calendar.SECOND, 0);
		return kalender;
	}

	/**
	 * Sucht in einer Anfrage nach einem bestimmten Datumsformat
	 */
	private interface Datumsparser {
		Date getDatumsangabeInString(Anfrage anfrage);
	}

	/**
	 * Sucht nach einer Zeichenkette und erzeugt dann ein Datum mit einem bestimmten
	 * Offset, z.B. StringFeldDifferenzParser("morgen", Calendar.DATE, 1) bedeutet,
	 * dass die Phrase "morgen" interpretiert wird als das Datum, das einen Tag später ist.
	 */
	private static class StringFeldDifferenzParser implements Datumsparser {
		private final String phrase;

		private final int differenz;
		private final int feld;

		StringFeldDifferenzParser(String phrase, int feld, int differenz) {
			this.phrase = phrase.toLowerCase();
			this.differenz = differenz;
			this.feld = feld;
		}

		Calendar erzeugeDatum() {
			Calendar kalendar = setupKalender();
			kalendar.add(feld, differenz);

			return kalendar;
		}

		@Override
		public Date getDatumsangabeInString(Anfrage anfrage) {
			return anfrage.getAnfrage().contains(phrase)? erzeugeDatum().getTime(): null;
		}
	}

	private static class PatternParser implements Datumsparser {
		private Pattern pattern;
		private DateFormat parser;
		private boolean includesYear;

		private PatternParser(String pattern, DateFormat parser, boolean includesYear) {
			this.pattern = Pattern.compile(pattern);
			this.parser = parser;
			this.includesYear = includesYear;
		}


		@Override
		public Date getDatumsangabeInString(Anfrage anfrage) {
			Matcher matcher = pattern.matcher(anfrage.getAnfrage());

			while (matcher.find()) {
				try {
					Date date = parser.parse(matcher.group());

					// wenn Tag/Monat ohne Jahr angegeben ist, wird erst dieses Jahr gesetzt
					// wenn das Datum dann in der Vergangenheit liegt,
					// ist wahrscheinlich der gleiche Tag nächstes Jahr gemeint
					if (!includesYear) {
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(date);
						calendar.set(Calendar.YEAR, setupKalender().get(Calendar.YEAR));

						if (calendar.before(setupKalender())) calendar.add(Calendar.YEAR, 1);

						date = calendar.getTime();
					}

					return date;
				} catch (ParseException ignored) {}
			}

			return null;
		}
	}

}
