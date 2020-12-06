package de.gilbert.main;

import de.gilbert.main.modules.DHBWFAQModul;

import java.util.Objects;

/**
 * Ein Modul ist für die endgültige Bearbeitung und Beantwortung von Anfragen verantwortlich.
 *
 * @author Lukas Rothenbach
 */
public abstract class Modul {
	/**
	 * Die Schluessel, durch die dieses Modul von der Spracherkennung ausgewaehlt wird.
	 */
	private final String[] schluessel;

	/** Erzeugt ein neues Modul mit den gegebenen Schluesseln. */
	public Modul(String[] schluessel) {
		this.schluessel = Objects.requireNonNullElseGet(schluessel, () -> new String[0]);
	}
	/** Beantwortet die gegebene Anfrage auf Modul-spezifische Weise. */
	public abstract void beantworteAnfrage(Anfrage anfrage);

	public String[] getSchluessel() {
		return schluessel;
	}

	/**
	 * Gibt den Namen des Moduls zurück, um es gegenüber des Benutzers zu identifizieren.
	 * Die Standardimplementierung gibt den Namen der Klasse zurück.
	 */
	public String getName() {
		return getClass().getSimpleName();
	}
}
