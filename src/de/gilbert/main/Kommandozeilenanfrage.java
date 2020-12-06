package de.gilbert.main;

import java.net.URL;
import java.util.Map;

/**
 * Eine Anfrage, die von einer {@link Kommandozeile} erzeugt wird und mit dem Benutzer über
 * die Kommandozeile kommuniziert.
 *
 * @author Yannis Eigenbrodt, Lukas Rothenbach
 */
public class Kommandozeilenanfrage extends Anfrage {

	/** Die erzeugende Kommandozeile */
	private final Kommandozeile kommandozeile;

	/** Erzeugt eine neue Anfrage, die über die gegebene Kommandozeile mit dem Benutzer interagiert */
	public Kommandozeilenanfrage(Kommandozeile kommandozeile, String anfrage) {
		super(anfrage);
		this.kommandozeile = kommandozeile;
	}

	/** leitet den Text an die Kommandozeile weiter */
	@Override
	public void schreibeAntwort(String text) {
		kommandozeile.schreibeAntwort(text);
	}

	/** leitet den Verweis an die Kommandozeile weiter */
	@Override
	public void schreibeVerweis(URL link, String platzhalter) {
		kommandozeile.schreibeVerweis(link, platzhalter);
	}

	/** leitet die Frage an die Kommandozeile weiter */
	@Override
	public <T> T frageAuswahl(String frage, Map<String, T> auswahl) {
		return kommandozeile.frageAuswahl(frage, auswahl);
	}

	/** leitet die Frage an die Kommandozeile weiter */
	@Override
	public boolean frageBestaetigung(String frage) {
		return kommandozeile.frageBestaetigung(frage);
	}
}
