package de.gilbert.main;

import de.gilbert.main.modules.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Spracherkennung {
	
	private List<Erkennungsmodul> erkennungsmodule;
	private List<Modul> module;

	/**
	 * Wird verwendet, wenn kein anderes Modul ausgewählt werden kann, da kein Modul
	 * mehr passende Schlüsselwörter hat, als ein anderes.
	 */
	private final Modul fallbackModul = new Textmodul("Ich konnte die Frage leider nicht verstehen.") {	};

	public Spracherkennung() {
		// TODO: implement
		importiereModul();
	}

	/**
	 * Liest die Daten aus der CSV Datei aus und speichert sie als ArrayList
	 */
	public  ArrayList<String[]> csvData() throws IOException{
		String csvFile = "Gilbert_Wortschatz.csv";
		String nextLine;
		String cvsSplitBy = ";";
		ArrayList<String[]> gilbertData = new ArrayList<>();

		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(csvFile))) {
			bufferedReader.readLine();
			while ((nextLine = bufferedReader.readLine()) != null) {
				if(nextLine.split(cvsSplitBy).length>=2)
					gilbertData.add(nextLine.split(cvsSplitBy));
			}
			return gilbertData;
		}
	}

	public void importiereModul(){
		module = new ArrayList<>();
		erkennungsmodule = new ArrayList<>();
		module.add(new DHBWFAQModul());
		module.add(new DokPraxisarbeitModul());
		module.add(new GILBERTHilfeModul());
		module.add(new MensaplanModul());
		module.add(new ModHandbuchModul());
		module.add(new MoodleModul());
		module.add(new NotenModul());
		module.add(new PruefOrdnungModul());
		module.add(new TerminplanModul());
		module.add(new VorlesungsplanModul());
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

			// nacheinander werden jetzt alle Schluessel des a#ktuellen Moduls untersucht
			String[] modulSchluessel = modul.getSchluessel();
			for (String schluessel : modulSchluessel) {
				// Ein Schluessel kann aus mehreren Woertern bestehen, die durch Whitespace getrennt sind
				// Die Wörter werden getrennt, um einfacher mit der Anfrage verglichen zu werden
				String[] split = schluessel.split("\\s+");

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
