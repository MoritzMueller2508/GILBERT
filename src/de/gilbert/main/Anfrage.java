package de.gilbert.main;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public abstract class Anfrage {
	private String anfrage;
	private String[] woerter;
	private Map<String, Object> parameter;
	
	public Anfrage(String anfrage) {
		this.anfrage = anfrage;
		this.woerter = anfrage.split("\\s+");
		parameter = new HashMap<>();
	}
	
	public abstract void schreibeAntwort(String text);
	
	public abstract void schreibeVerweis(URL link, String platzhalter);
	
	public abstract Object frageAuswahl(String frage, Map<String, Object> auswahl);
	
	public abstract Object frageWert(String frage);
	
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
