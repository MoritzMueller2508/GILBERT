package de.gilbert.main;

import java.net.URL;
import java.util.Map;

public class Kommandozeilenanfrage extends Anfrage {
	
	//TODO: implement
	public Kommandozeilenanfrage(String anfrage) {
		super(anfrage);
	}
	
	//TODO: implement
	@Override
	public void schreibeAntwort(String text) {
	}
	
	//TODO: implement
	@Override
	public void schreibeVerweis(URL link, String platzhalter) {
	}
	
	//TODO: implement
	@Override
	public Object frageAuswahl(String frage, Map<String, Object> auswahl) {
		return null;
	}

	//TODO: implement
	@Override
	public boolean frageBestaetigung(String frage) {
		return false;
	}

}
