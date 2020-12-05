package de.gilbert.main;

import java.net.URL;
import java.util.Map;

/**
 * @author Yannis Eigenbrodt, Lukas Rothenbach
 */
public class Kommandozeilenanfrage extends Anfrage {
	
	private final Kommandozeile kommandozeile;

	public Kommandozeilenanfrage(Kommandozeile kommandozeile, String anfrage) {
		super(anfrage);
		this.kommandozeile = kommandozeile;
	}

	@Override
	public void schreibeAntwort(String text) {
		kommandozeile.schreibeAntwort(text);
	}

	@Override
	public void schreibeVerweis(URL link, String platzhalter) {
		kommandozeile.schreibeVerweis(link, platzhalter);
	}

	@Override
	public <T> T frageAuswahl(String frage, Map<String, T> auswahl) {
		return kommandozeile.frageAuswahl(frage, auswahl);
	}

	@Override
	public boolean frageBestaetigung(String frage) {
		return kommandozeile.frageBestaetigung(frage);
	}
}
