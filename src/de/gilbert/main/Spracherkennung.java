package de.gilbert.main;

import de.gilbert.main.modules.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Spracherkennung {
	
	private List<Erkennungsmodul> erkennungsmodule;
	private List<Modul> module;

	/**
	 * Wird verwendet, wenn kein anderes Modul ausgewählt werden kann, da kein Modul
	 * mehr passende Schlüsselwörter hat, als ein anderes.
	 */
	private final Modul fallbackModul = new Textmodul(new String[0], "Ich konnte die Frage leider nicht verstehen.") {	};

	public Spracherkennung() throws IOException {
		// TODO: implement
		importiereModul();
	}

	/**
	 * Liest die Daten aus der CSV Datei aus und speichert sie als ArrayList
	 */
	public  ArrayList<String[]> csvData() throws IOException {
		String csvFile = "Gilbert_Wortschatz.csv";
		String nextLine;
		String cvsSplitBy = ";";
		ArrayList<String[]> gilbertData = new ArrayList<>();

		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(csvFile))) {
			bufferedReader.readLine(); // ignore title line
			while ((nextLine = bufferedReader.readLine()) != null) {
				String[] split = nextLine.split(cvsSplitBy);
				//
				if(split.length >= 2) gilbertData.add(split);
			}

			return gilbertData;
		}
	}

	/**
	 * Läd die CSV Datei und ordnet die Daten den Modulen zu
	 */
	public Map<Integer, String[]> ladeSchluessel() throws IOException {
		ArrayList<String[]> csvData = csvData();
		Map<Integer, List<String>> schluessel = new HashMap<>();

		for (String[] data: csvData) {
			try {
				int modulID = Integer.parseInt(data[1]);
				schluessel.computeIfAbsent(modulID, i -> new ArrayList<>()).add(data[0]);
			} catch (NumberFormatException ignored) {}
		}

		Map<Integer, String[]> schluesselArrayMap = new HashMap<>();
		schluessel.forEach((i, s) -> schluesselArrayMap.put(i, s.toArray(new String[0])));
		return schluesselArrayMap;
	}

	private String[] getOrDefault(Map<Integer, String[]> schluessel, int modulId) {
		return Objects.requireNonNullElseGet(schluessel.get(modulId), () -> new String[0]);
	}

	public void importiereModul() throws IOException {
		module = new ArrayList<>();
		erkennungsmodule = new ArrayList<>();

		Map<Integer, String[]> schluessel = ladeSchluessel();
		module.add(new MoodleModul(getOrDefault(schluessel, 1)));          // Modul 1
		module.add(new NotenModul(getOrDefault(schluessel, 2)));           // Modul 2
		module.add(new GILBERTHilfeModul(getOrDefault(schluessel, 3)));    // Modul 3
		module.add(new MensaplanModul(getOrDefault(schluessel, 4)));       // Modul 4
		module.add(new VorlesungsplanModul(getOrDefault(schluessel, 5)));  // Modul 5
		module.add(new PruefOrdnungModul(getOrDefault(schluessel, 6)));    // Modul 6
		module.add(new TerminplanModul(getOrDefault(schluessel, 7)));      // Modul 7
		module.add(new ModHandbuchModul(getOrDefault(schluessel, 8)));     // Modul 8
		module.add(new DokPraxisarbeitModul(getOrDefault(schluessel, 9))); // Modul 9
		module.add(new DHBWFAQModul(getOrDefault(schluessel, 10)));        // Modul 10

		erkennungsmodule.add(new Frageerkennung());
		erkennungsmodule.add(new Datumserkennung());
	}
	
	public void bearbeiteAnfrage(Anfrage anfrage) {
		// rufe die Erkennungsmodule für eine allgemeine vorbereitende Untersuchung der Anfrage auf
		rufeErkennungsModuleAuf(anfrage);

		// suche das passende Modul oder benutze das Fallbackmodul, wenn kein Modul ausgewählt werden konnte
		Modul modul = findeModul(anfrage);
		(modul != null? modul: fallbackModul).beantworteAnfrage(anfrage);
	}


	private void rufeErkennungsModuleAuf(Anfrage anfrage) {
		// rufe jedes Erkennungsmodul für die gegebene Anfrage auf
		erkennungsmodule.forEach(modul -> modul.untersucheAnfrage(anfrage));
	}

	/**
	 * findet das richtige Modul um die Anfrage zu bearbeiten
	 * @param anfrage
	 * @return das Modul, das am besten zur Anfrage passt
	 */
	private Modul findeModul(Anfrage anfrage) {
		int[] modulCounter = new int[module.size()];

		// zähle die Schluessel, von jedem Modul, die in der Anfrage auftauchen
		String[] woerter = anfrage.getWoerter();
		for (int modulIndex = 0; modulIndex < module.size(); modulIndex++) {
			Modul modul = module.get(modulIndex);

			// nacheinander werden jetzt alle Schluessel des aktuellen Moduls untersucht
			String[] modulSchluessel = modul.getSchluessel();
			for (String schluessel : modulSchluessel) {
				// Ein Schluessel kann aus mehreren Woertern bestehen, die durch Whitespace getrennt sind
				// Die Wörter werden getrennt, um einfacher mit der Anfrage verglichen zu werden
				String[] split = schluessel.toLowerCase().split("\\s+");

				int anzahlWoerter = 0; // die Anzahl der Wörter des Schluessel, die schon gefunden wurden
				for (String wort : woerter) {
					if (wort.equals(split[anzahlWoerter])) {
						// das nächste für den Schluessel erwartete Wort wird gefunden
						anzahlWoerter++; // es wurde ein weiteres Wort des Schluessels gefunden
						if (anzahlWoerter == split.length) {
							// wenn alle Wörter des Schluessels gefunden wurden
							// erhöhe den Counter des Moduls und breche die Suche ab
							modulCounter[modulIndex]++;
							break;
						}
					} else {
						//  das naechste erwartete Wort wurde nicht gefunden:
						//   Falls schon Wörter des Schluessels gefunden wurden,
						//   können diese nicht Teil einer Schluesselphrase in der Anfrage sein, es wurden also 0 Wörter
						//   gefunden
						anzahlWoerter = 0;
					}
				}
			}
		}

		// suche das Modul mit den meisten passenden Schluesseln
		// beachte dabei, dass es mindestens einen passenden Schluessel hat
		int index = -1, max = 0;
		for (int modulIndex = 0; modulIndex < modulCounter.length; modulIndex++) {
			if (modulCounter[modulIndex] > max) {
				index = modulIndex; max = modulCounter[modulIndex];
			}
		}

		if (max > 0) { // wenn es mindestens einen Treffer gab: prüfe, ob das Maximum eindeutig ist
			// man kann ab index + 1 anfangen, da modulCounter[index] == max immer wahr sein muss
			// und jeder Counter vorher kleiner sein musste als max
			for (int modulIndex = index + 1; modulIndex < modulCounter.length; modulIndex++) {
				if (modulCounter[modulIndex] == max) {
					// markiere die Nichteindeutigkeit: Es kann kein Element ausgewählt werden
					index = -1; break;
				}
			}
		}

		// gebe ein Modul mit mindestens einem Treffer zurück, wenn kein anderes Modul
		// mindestens genauso viele Treffer hat
		return index >= 0? module.get(index): null;
	}
	
	public List<Erkennungsmodul> getErkennungsmodule() {
		return erkennungsmodule;
	}
	
	public List<Modul> getModule() {
		return module;
	}

}
