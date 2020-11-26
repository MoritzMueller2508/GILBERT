package de.gilbert.main;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Datumserkennung extends Erkennungsmodul{

	@Override
	public void untersucheAnfrage(Anfrage anfrage) {
		Map<String, Long> datumsKeyWoerter = initialisiereDateMap();

		String angepassteAnfrage = anfrage.getAnfrage().toLowerCase();
		String besteDatumsangabe = "";
		int besteDatumsangabeIndex = Integer.MAX_VALUE;

		for (String datumsangabe : datumsKeyWoerter.keySet()) {
			if (angepassteAnfrage.contains(datumsangabe.toLowerCase())) {
				int neuerIndex = angepassteAnfrage.indexOf(datumsangabe.toLowerCase());

				if (neuerIndex < besteDatumsangabeIndex) {
					besteDatumsangabeIndex = neuerIndex;
					besteDatumsangabe = datumsangabe;
				}
			}
		}
		if (besteDatumsangabeIndex >= 0) {
			anfrage.getParamter().put("datumsangabe", new Date(datumsKeyWoerter.get(besteDatumsangabe)));
		}
	
	}
	
	private Map<String, Long> initialisiereDateMap() {
		Map<String, Long> dateMap = new HashMap<>();
		Calendar kalender = setupKalender();
		int stundenProTag = 86400000;

		Long heute = kalender.getTimeInMillis();

		kalender.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		Long wochenStart = kalender.getTimeInMillis();

		// Tage
		dateMap.put("heute", heute);

		dateMap.put("morgen", heute + stundenProTag);
		dateMap.put("übermorgen", heute + (2 * stundenProTag));

		dateMap.put("gestern", heute - stundenProTag);
		dateMap.put("vorgestern", heute - (2 * stundenProTag));

		// Wochen
		dateMap.put("diese woche", wochenStart);

		dateMap.put("nächste woche", wochenStart + (7 * stundenProTag));
		dateMap.put("übernächste woche", wochenStart + (14 * stundenProTag));

		dateMap.put("letze woche", wochenStart - (7 * stundenProTag));
		dateMap.put("vorletze woche", wochenStart - (14 * stundenProTag));

		return dateMap;
	}

	private Calendar setupKalender() {
		Calendar kalender = Calendar.getInstance();
		kalender.set(Calendar.HOUR_OF_DAY, 0);
		kalender.set(Calendar.MINUTE, 0);
		kalender.set(Calendar.SECOND, 0);
		return kalender;
	}
}
