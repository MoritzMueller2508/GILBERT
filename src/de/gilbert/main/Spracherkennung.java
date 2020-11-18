package de.gilbert.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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

	/*
	Auslesen der CSV Datei
	 */
	public void csvData(){
		String csvFile = "Gilbert_Wortschatz.csv";
		String nextLine;
		String cvsSplitBy = ";";
		ArrayList<String[]> gilbertData = new ArrayList<>();

		try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
			br.readLine();
			while ((nextLine = br.readLine()) != null) {
				if(nextLine.split(cvsSplitBy).length>=2)
					gilbertData.add(nextLine.split(cvsSplitBy));
			}
			for(int gilbertDataRow=0; gilbertDataRow < gilbertData.size(); gilbertDataRow++){
				System.out.println(Arrays.toString(gilbertData.get(gilbertDataRow)));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//TODO: implement
	public void importiereModul() {
		// Testcode: keine Erkennungsmodule oder Module werden geladen,
		//           es werden aber schon die Listen angelegt, um diese iterieren zu können
		erkennungsmodule = Collections.emptyList();
		module = Collections.emptyList();
	}
	
	public void bearbeiteAnfrage(Anfrage anfrage) {
		// rufe die Erkennungsmodule für eine allgemeine vorbereitende Untersuchung der Anfrage auf
		rufeErkennungsModuleAuf(anfrage);

		// suche das passende Modul oder benutze das Fallbackmodul, wenn kein Modul ausgewählt werden konnte
		Modul m = findeModul(anfrage);
		(m != null? m: fallbackModul).beantworteAnfrage(anfrage);
	}

	private void rufeErkennungsModuleAuf(Anfrage anfrage) {
		// rufe jedes Erkennungsmodul für die gegebene Anfrage auf
		erkennungsmodule.forEach(m -> m.untersucheAnfrage(anfrage));
	}

	private Modul findeModul(Anfrage anfrage) {
		int[] modulCounter = new int[module.size()];

		// zähle die Schluessel, von jedem Modul, die in der Anfrage auftauchen
		String[] woerter = anfrage.getWoerter();
		for (int i = 0; i < module.size(); i++) {
			Modul modul = module.get(i);

			// nacheinander werden jetzt alle Schluessel des aktuellen Moduls untersucht
			String[] modulSchluessel = modul.getSchluessel();
			for (String schluessel : modulSchluessel) {
				// Ein Schluessel kann aus mehreren Woertern bestehen, die durch Whitespace getrennt sind
				// Die Wörter werden getrennt, um einfacher mit der Anfrage verglichen zu werden
				String[] split = schluessel.split("\\s+");

				int w = 0; // die Anzahl der Wörter des Schluessel, die schon gefunden wurden
				for (String wort : woerter) {
					if (wort.equals(split[w])) {
						// das nächste für den Schluessel erwartete Wort wird gefunden
						w++; // es wurde ein weiteres Wort des Schluessels gefunden
						if (w == split.length) {
							// wenn alle Wörter des Schluessels gefunden wurden
							// erhöhe den Counter des Moduls und breche die Suche ab
							modulCounter[i]++;
							break;
						}
					} else {
						// das naechste erwartete Wort wurde nicht gefunden:
						//   Falls schon Wörter des Schluessels gefunden wurden,
						//   können diese nicht Teil einer Schluesselphrase in der Anfrage sein, es wurden also 0 Wörter
						//   gefunden
						w = 0;
					}
				}
			}
		}

		// suche das Modul mit den meisten passenden Schluesseln
		// beachte dabei, dass es mindestens einen passenden Schluessel hat
		int index = -1, max = 0;
		for (int i = 0; i < modulCounter.length; i++) {
			if (modulCounter[i] > max) {
				index = i; max = modulCounter[i];
			}
		}

		if (max > 0) { // wenn es mindestens einen Treffer gab: prüfe, ob das Maximum eindeutig ist
			// man kann ab index + 1 anfangen, da modulCounter[index] == max immer wahr sein muss
			// und jeder Counter vorher kleiner sein musste als max
			for (int i = index + 1; i < modulCounter.length; i++) {
				if (modulCounter[i] == max) {
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
