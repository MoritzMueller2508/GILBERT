package de.gilbert.main;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Eine Anfrage entspricht einer vom Benutzer gestellten Frage.
 * Sie enthält
 * <ul>
 *     <li>die ursprüngliche Anfrage, </li>
 *     <li>die Wörter der Anfrage (lowercase und ohne Satzzeichen), </li>
 *     <li>die Parameter, die der Anfrage im Lauf der Bearbeitung zugewiesen werden</li>
 * </ul>
 *
 * @author Lukas Rothenbach
 */
public abstract class Anfrage {

	/** Die ursprüngliche Anfrage */
	private final String anfrage;
	/** Die Wörter der Anfrage (lowercase) */
	private final String[] woerter;
	/** Die Parameter der Anfrage */
	private final Map<String, Object> parameter;

	/**
	 * Erzeugt eine neue Anfrage mit
	 * der gegebenen Frage,
	 * den darin gefundenen Wörtern und
	 * noch keinen Parametern.
	 */
	public Anfrage(String anfrage) {
		this.anfrage = anfrage;
		this.woerter = anfrage
				.toLowerCase()
				.replaceAll("\\p{Punct}", " ")
				.split("\\s+");
		parameter = new HashMap<>();
	}

	/** Schreibt die gegebene Antwort an den Benutzer */
	public abstract void schreibeAntwort(String text);

	/**
	 * Schreibt den gegebenen Link - in an die Benutzerschnittstelle angepasste Weise - an den Benutzer.
	 * @param link der Link
	 * @param platzhalter ein Text, der den Link beschreibt
	 */
	public abstract void schreibeVerweis(URL link, String platzhalter);

	/**
	 * Lässt den Benutzer einen Eintrag aus {@code auswahl} auswaehlen.
	 * Die Schluessel sind fuer den Benutzer sichtbar,
	 * die zugeordneten Werte werden intern weitergegeben.
	 *
	 * @param frage die Frage, die der Benutzer beantworten soll
	 * @param auswahl die Schluessel und Auswahlmoeglichkeiten fuer den Benutzer
	 * @param <T> der Typ der Auswahlmöglichkeiten
	 * @return die Wahl des Nutzers
	 */
	public abstract <T> T frageAuswahl(String frage, Map<String, T> auswahl);

	/**
	 * Lässt den Benutzer eine Frage bestätigen oder ablehnen.
	 *
	 * @param frage die Frage, die der Benutzer beantworten soll
	 * @return true, wenn der Benutzer die Frage bestätigt, false sonst
	 */
	public abstract boolean frageBestaetigung(String frage);
	
	public String getAnfrage() {
		return anfrage;
	}
	
	public String[] getWoerter() {
		return woerter;
	}
	
	public Map<String, Object> getParameter() {
		return parameter;
	}
}
