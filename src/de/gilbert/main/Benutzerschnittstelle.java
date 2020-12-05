package de.gilbert.main;

import java.io.IOException;

/**
 * @author Lukas Rothenbach
 */
public abstract class Benutzerschnittstelle {
	
	private final Spracherkennung spracherkennung;
		
	public abstract void beginneLoop();

	Benutzerschnittstelle() throws IOException {
		try {
			spracherkennung = new Spracherkennung();
		} catch (IOException e) {
			System.out.println("Es ist ein Fehler beim Start aufgetreten. Du kannst versuchen,\n");
			System.out.println(" - mich nochmal zu starten (z.B. vertrage ich mich manchmal nicht mit dem Betriebssystem)");
			System.out.println(" - mich neu zu installieren (vielleicht habe ich einen Teil meiner Arbeitsmaterialien verloren)");
			System.out.println(" - bei meinen Entwicklern nachfragen (die wissen schließlich am Besten, wie ich funktioniere)");
			System.out.println("Vielleicht hilft die folgende Fehlermeldung ja auch: ");

			throw e;
		}
	}
	public Spracherkennung getSpracherkennung() {
		return spracherkennung;
	}
}
