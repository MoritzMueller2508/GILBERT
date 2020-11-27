package de.gilbert.main.modules;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.gilbert.main.Anfrage;
import de.gilbert.main.Verweismodul;

public class VorlesungsplanModul extends Verweismodul {
	private static URL VORLESUNGSPLAN_URL;

	static {
		try {
			VORLESUNGSPLAN_URL = new URL("https://vorlesungsplan.dhbw-mannheim.de/");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public VorlesungsplanModul(String[] schluessel) {
		super(schluessel, VORLESUNGSPLAN_URL, "VorlesungsplanURL");

	}

	@Override
	public void beantworteAnfrage(Anfrage anfrage) {
		Map<String, String> kursdaten;
		try {
			kursdaten = csvData();
			String kursbezeichnung;

			// Keine Datumsangabe in Anfrage
			if (!anfrage.getParameter().containsKey("datumsangabe")) {
				kursbezeichnung = kursbezeichnungInAnfrage(kursdaten, anfrage);
				
				// Kursbezeichnung in Anfrage gefunden
				if (kursbezeichnung != null) {
					anfrage.schreibeVerweis(generiereNeueUrl(kursdaten.get(kursbezeichnung), anfrage), "VorlesungsplanURL");
				
				// Keine Kursbezeichnung in Anfrage gefunden
				} else { // Keine Kursbezeichnung gefunden
					kursbezeichnung = (String) anfrage.frageWert(
							"Um auf den Vorlesunsplan für Ihren Kurs zu verweisen wird die Kursbezeichnung benötigt.\n"
									+ " Geben Sie diese ein oder drücken Sie [Enter], um ohne Kursbezeichnung fortzufahren.");

					//Passende Kursbezeichnung eingegeben
					if (kursbezeichnung.length() != 0 && kursdaten.containsKey(kursbezeichnung.toUpperCase())) {
						anfrage.schreibeVerweis(generiereNeueUrl(kursdaten.get(kursbezeichnung), anfrage), "VorlesungsplanURL");
					}
				}
			
			// Datumsangabe gesetzt
			} else { 
				kursbezeichnung = kursbezeichnungInAnfrage(kursdaten, anfrage);
				// Kursbezeichnung in Anfrage gefunden
				if (kursbezeichnung != null) { 
					anfrage.schreibeVerweis(generiereNeueUrl(kursbezeichnung, anfrage), "VorlesungsplanURL");
				
				// Keine Kursbezeichnung in Anfrage gefunden
				} else {
					kursbezeichnung = (String) anfrage.frageWert(
							"Leider fehlt die Kursbezeichnung zur Bearbeitung der Anfrage. Bitte gebe die Kursbezeichnung ein.");
					if (kursdaten.containsKey(kursbezeichnung)) {
						anfrage.schreibeVerweis(generiereNeueUrl(kursdaten.get(kursbezeichnung), anfrage), "VorlesungsplanURL");
					}
				}
			}
			// Keine Kursbezeichnung gesetzt.
			super.beantworteAnfrage(anfrage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Überprüft, ob die Anfrage eine Kursbezeichnung aus der Kursdaten Map enthält 
	 * @param kursdaten die Map, die alle möglichen Kursbezeichnungen, sowie deren uid enthält
	 * @param anfrage das anfragenobjekt
	 * @return die gefundene Kursbezeichnung oder null, sollte die Anfrage keine Kursbezeichnung enthalten
	 */
	private String kursbezeichnungInAnfrage(Map<String, String> kursdaten, Anfrage anfrage) {
		for (String anfragenWort : anfrage.getWoerter()) {
			if (kursdaten.containsKey(anfragenWort.toUpperCase())) {
				return anfragenWort.toUpperCase();
			}
		}
		return null;
	}
	
	/**
	 * Generiert eine neue Url, die die Basisurl mit Kurs und Datumsangaben parametrisiert. 
	 * @param kurs die uid des jeweiligen Kurses
	 * @param anfrage das Anfragenobjekt
	 * @return die generierte Url oder null, sollte beim parsen ein Fehler auftreten
	 */
	private URL generiereNeueUrl(String kurs, Anfrage anfrage) {
		String neueUrl = "/index.php?action=view&gid=3067001&uid=" + kurs;
		
		if(anfrage.getParameter().containsKey("datumsangabe")) {
			neueUrl += "&date=" + ((Date) anfrage.getParameter().get("datumsangabe")).getTime() / 1000;
		}

		try {
			return new URL(VORLESUNGSPLAN_URL, neueUrl);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Liest eine csv Datei aus und speichert die Daten in einer Map.
	 * @return die mit den Daten aus der CSV gefüllte Map 
	 * @throws IOException, sollte keine Datei mit dem angegebenen Namen exisitieren.
	 */
	private Map<String, String> csvData() throws IOException {
		String csvFile = "Kurszuweisungen.csv";
		String nextLine;
		String cvsSplitBy = ";";
		Map<String, String> kursdaten = new HashMap<>();

		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(csvFile))) {
			bufferedReader.readLine();
			while ((nextLine = bufferedReader.readLine()) != null) {
				if (nextLine.split(cvsSplitBy).length >= 2) {
					String[] zeile = nextLine.split(cvsSplitBy);
					if (zeile.length == 2) {
						kursdaten.put(zeile[0], zeile[1]);
					}

				}

			}
			return kursdaten;
		}
	}
}
