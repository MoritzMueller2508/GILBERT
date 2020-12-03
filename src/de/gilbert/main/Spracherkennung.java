package de.gilbert.main;

import de.gilbert.main.modules.*;

import java.io.IOException;
import java.util.*;

/**
 * @author Jonas Knebel, Lukas Rothenbach und Yannis Eigenbrodt
 */
public class Spracherkennung {
	
	private List<Erkennungsmodul> erkennungsmodule;
	private List<Modul> module;

	/**
	 * Wird verwendet, wenn kein anderes Modul ausgewählt werden kann, da kein Modul
	 * mehr passende Schlüsselwörter hat, als ein anderes.
	 */
	private final Modul fallbackModul = new Textmodul(new String[0], "Ich konnte die Frage leider nicht verstehen.") {	};

	public Spracherkennung() throws IOException {
		importiereModul();
	}


	/**
	 * Läd die CSV Datei und ordnet die Daten den Modulen zu
	 */
	public Map<Integer, String[]> ladeSchluessel() throws IOException {

		ArrayList<String[]> csvData = Util.csvDataArrayList("Gilbert_Wortschatz");
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
		module.add(new MoodleModul(getOrDefault(schluessel, 1)));          // Modul  1 - Moodle / Blackboard
		module.add(new NotenModul(getOrDefault(schluessel, 2)));           // Modul  2 - Dualis / Noten
		module.add(new GILBERTHilfeModul(getOrDefault(schluessel, 3)));    // Modul  3 - Gilbert Intro
		module.add(new MensaplanModul(getOrDefault(schluessel, 4)));       // Modul  4 - Mensaplan
		module.add(new VorlesungsplanModul(getOrDefault(schluessel, 5)));  // Modul  5 - Vorlesungsplan
		module.add(new PruefOrdnungModul(getOrDefault(schluessel, 6)));    // Modul  6 - Prüfungsordnung
		module.add(new TerminplanModul(getOrDefault(schluessel, 7)));      // Modul  7 - Terminplan
		module.add(new ModHandbuchModul(getOrDefault(schluessel, 8)));     // Modul  8 - Modul Handbuch
		module.add(new DokPraxisarbeitModul(getOrDefault(schluessel, 9))); // Modul  9 - Praxisarbeit
		module.add(new DHBWFAQModul(getOrDefault(schluessel, 10)));        // Modul 10 - FAQ
		module.add(new PhasenModul(getOrDefault(schluessel, 11)));         // Modul 11 - Theorie-/Praxisphasen

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
	 * @return das Modul, das am besten zur Anfrage passt
	 */
	private Modul findeModul(Anfrage anfrage) {
		int maxModulCounter = 0;
		// Anzahl der Wörter, die nur passen mit levenshtein distance <= 2
		int maxModulCounterNichtGleich = 0;
		List<Modul> maxModule = new LinkedList<>();

		// zähle die Schluessel, von jedem Modul, die in der Anfrage auftauchen
		String[] woerter = anfrage.getWoerter();
		for (Modul modul: module) {
			int modulCounter = 0;
			// Anzahl der Wörter, die nur passen mit levenshtein distance <= 2
			int modulCounterNichtGleich = 0;

			// nacheinander werden jetzt alle Schluessel des aktuellen Moduls untersucht
			String[] modulSchluessel = modul.getSchluessel();
			for (String schluessel : modulSchluessel) {
				// Ein Schluessel kann aus mehreren Woertern bestehen, die durch Whitespace getrennt sind
				// Die Wörter werden getrennt, um einfacher mit der Anfrage verglichen zu werden
				String[] split = schluessel.toLowerCase().split("\\s+");

				int anzahlWoerter = 0; // die Anzahl der Wörter des Schluessel, die schon gefunden wurden
				for (String wort : woerter) {
					if (sindWoerterWahrscheinlichGleich(wort, split[anzahlWoerter])) {
						// das Modul ist nur ähnlich
						if (!wort.equalsIgnoreCase(split[anzahlWoerter])) modulCounterNichtGleich++;

						// das nächste für den Schluessel erwartete Wort wird gefunden
						anzahlWoerter++; // es wurde ein weiteres Wort des Schluessels gefunden
						if (anzahlWoerter == split.length) {
							// wenn alle Wörter des Schluessels gefunden wurden
							// erhöhe den Counter des Moduls und breche die Suche ab
							modulCounter++;
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

			if (modulCounter > maxModulCounter) {
				// das Modul hat mehr Treffer, als jedes andere vorherige Modul
				maxModulCounter = modulCounter;
				maxModulCounterNichtGleich = modulCounterNichtGleich;

				maxModule.clear();
				maxModule.add(modul);
				// maxModul = modul;
			} else if (modulCounter == maxModulCounter) {
				if (modulCounterNichtGleich < maxModulCounterNichtGleich) {
					// diese Modul passt besser als alle anderen, weil es mehr wirklich passende Schlüssel hat
					maxModule.clear();
					maxModule.add(modul); maxModulCounterNichtGleich = modulCounterNichtGleich;
				} else if (modulCounterNichtGleich == maxModulCounterNichtGleich)
					// das Modul ist nicht eindeutig, es gibt mindestens 2 Module mit maxModulCounter Treffern
					maxModule.add(modul);
			}
		}

		if (maxModule.size() == 1) return maxModule.get(0);
		if (maxModule.size() == 2)
			anfrage.frageAuswahl("Was meinst du?", Map.of(
					maxModule.get(0).toString(), maxModule.get(0),
					maxModule.get(1).toString(), maxModule.get(1)));

		return null;
	}

	private boolean sindWoerterWahrscheinlichGleich(String a, String b) {
		// maximal zwei fehler => zwei falsche Buchstaben. z.B. auch Buchstabendreher
		return berechneLevenshteinAbstand(a, b) <= 2;
	}

	/**
	 * Um den Levenshtein-Abstand zweier Strings zu berechnen wird der Wagner–Fischer Algorithmus verwendet.
	 *
	 * @param a das erste Wort
	 * @param b das zweite Wort
	 * @return den Levenshtein-Abstand der Parameter
	 */
	private int berechneLevenshteinAbstand(String a, String b) {
		if (a.isEmpty()) return b.length();
		if (b.isEmpty()) return a.length();

		int[][] matrix = new int[a.length() + 1][b.length() + 1];

		for (int i = 0; i < matrix.length; i++) matrix[i][0] = i;
		for (int j = 0; j < matrix[0].length; j++) matrix[0][j] = j;

		for (int i = 0; i + 1 < matrix.length; i++) {
			for (int j = 0; j + 1 < matrix[i].length; j++) {
				matrix[i + 1][j + 1] = min(
					matrix[i][j] + (a.charAt(i) == b.charAt(j)? 0: 1),
					matrix[i][j+1] + 1,
					matrix[i+1][j] + 1
				);
			}
		}

		return matrix[a.length()][b.length()];
	}

	private int min(int... a) {
		if (a.length == 0) throw new IllegalArgumentException();

		int min = a[0];
		for (int i = 1; i < a.length; i++) if (a[i] < min) min = a[i];
		return min;
	}

	public List<Erkennungsmodul> getErkennungsmodule() {
		return erkennungsmodule;
	}
	
	public List<Modul> getModule() {
		return module;
	}

}
