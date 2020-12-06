package de.gilbert.main;

import java.io.IOException;

/**
 * Eine Schnittstelle, mit der der Benutzer interagiert.
 *
 * @author Lukas Rothenbach
 */
public abstract class Benutzerschnittstelle {
	
	private final Spracherkennung spracherkennung;

	/**
	 * Erzeugt eine Schnittstelle und initialisiert
	 * die zugehörige Spracherkennung.
	 *
	 * @throws IOException wenn beim initialisieren ein Fehler auftritt
	 */
	public Benutzerschnittstelle() throws IOException {
		spracherkennung = new Spracherkennung();
	}

	/**
	 * Startet die Benutzerschnittstelle zur Interaktion mit dem Benutzer.
	 * Die Interaktion laeuft in einer Schleife, in der nacheinander Anfragen erstellt und
	 * von der Spracherkennung bearbeitet werden.
	 */
	public abstract void beginneLoop();

	public Spracherkennung getSpracherkennung() {
		return spracherkennung;
	}
}
