package de.gilbert.main;

import de.gilbert.main.modules.*;

import java.io.IOException;
import java.util.*;

/**
 * Untersucht Anfragen mit Hilfe von {@link Erkennungsmodul Erkennungsmodulen}
 * und versucht ein passendes {@link Modul} zu finden, das die Anfrage dann beantwortet.
 *
 * @author Jonas Knebel, Lukas Rothenbach und Yannis Eigenbrodt
 */
public class Spracherkennung {

	/** Erkennungsmodule, die Anfragen vorbearbeiten */
	private List<Erkennungsmodul> erkennungsmodule;
	/** Module, die Anfragen beantworten können und über Schluessel gefunden werden koennen */
	private List<Modul> module;

	/**
	 * Wird verwendet, wenn kein anderes Modul ausgewählt werden kann, da kein Modul
	 * mehr passende Schlüsselwörter hat, als ein anderes.
	 */
	private final Modul fallbackModul = new Textmodul(new String[0], "Ich konnte die Frage leider nicht verstehen.") {	};

	/** Erzeugt eine Spracherkennung und importiert Module */
	public Spracherkennung() throws IOException {
		importiereModul();
	}

	/**
	 * Läd die CSV Datei und ordnet die Daten den Modulen zu
	 */
	public Map<Integer, String[]> ladeSchluessel() throws IOException {

		List<String[]> csvData = Util.csvDataList("Gilbert_Wortschatz");
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

	/**
	 * Sucht in der gegebenen Map nach dem gegebenen id.
	 * Wenn die id nicht gefunden wird, wird ein leeres Array zurückgegeben.
	 */
	private String[] getOrDefault(Map<Integer, String[]> schluessel, int modulId) {
		return Objects.requireNonNullElseGet(schluessel.get(modulId), () -> new String[0]);
	}

	/**
	 * Importiert alle Module und Erkennungsmodule
	 */
	public void importiereModul() throws IOException {
		module = new ArrayList<>();
		erkennungsmodule = new ArrayList<>();

		// lade Module
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

		// lade Erkennungsmodule
		erkennungsmodule.add(new Frageerkennung());
		erkennungsmodule.add(new Datumserkennung());
	}

	/**
	 * Bearbeitet die Anfrage:
	 * Laesst die Erkennungsmodule die Anfrage vorbereiten, sucht ein passendes Modul
	 * und laesst dieses die Anfrage beantworten.
	 */
	public void bearbeiteAnfrage(Anfrage anfrage) {
		// rufe die Erkennungsmodule für eine allgemeine vorbereitende Untersuchung der Anfrage auf
		rufeErkennungsModuleAuf(anfrage);

		// suche das passende Modul
		findeModul(anfrage).beantworteAnfrage(anfrage);
	}

	/** ruft alle Erkennungsmodule mit der gegebenen Anfrage auf */
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
		// es ist eindeutig
		if (maxModule.size() == 1) return maxModule.get(0);
		// es ist zweideutig -> lass den Benutzer auswaehlen
		if (maxModule.size() == 2)
			return anfrage.frageAuswahl("Was meinst du?", Map.of(
					maxModule.get(0).getName(), maxModule.get(0),
					maxModule.get(1).getName(), maxModule.get(1)));

		// es ist unverstaendlich -> fallback zum fallbackModul
		return fallbackModul;
	}

	/**
	 * Testet, ob zwei Woerter wahrscheinlich gleich sind.
	 * Das ist der Fall, wenn sie sich an maximal zwei Stellen unterscheiden.
	 *
	 * @see Util#berechneLevenshteinAbstand(String, String)
	 */
	private boolean sindWoerterWahrscheinlichGleich(String a, String b) {
		// maximal zwei fehler => zwei falsche Buchstaben. z.B. auch Buchstabendreher
		return Util.berechneLevenshteinAbstand(a, b) <= 2;
	}

	public List<Erkennungsmodul> getErkennungsmodule() {
		return erkennungsmodule;
	}
	
	public List<Modul> getModule() {
		return module;
	}

}
