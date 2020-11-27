package de.gilbert.main;

import java.net.URL;
import java.util.Map;
import java.util.Scanner;

public class Kommandozeilenanfrage extends Anfrage {
	
	private Scanner scanner;

	public Kommandozeilenanfrage(Scanner sc, String anfrage) {
		super(anfrage);
		this.scanner = sc;
	}
	

	@Override
	public void schreibeAntwort(String text) {
		System.out.println(text);
	}
	

	@Override
	public void schreibeVerweis(URL link, String platzhalter) {
		System.out.println(platzhalter+": "+ link);
	}


	//TODO überarbeiten vielleicht
	@Override
	public <T> T frageAuswahl(String frage, Map<String, T> auswahl) {
		System.out.println(frage);

		for(String keyElement : auswahl.keySet()) {
			System.out.println(keyElement);
		}
		String antwort = scanner.nextLine();
		return auswahl.get(antwort);
	}
	
	public Object frageWert(String frage) {
		System.out.println(frage);
		String antwort = scanner.nextLine();
		return antwort;
	}


	@Override
	public boolean frageBestaetigung(String frage) {
		System.out.println(frage);
		String antwort = scanner.nextLine();

		if(antwort.toUpperCase().startsWith("J")){
			return true;
		}
		else if(antwort.toUpperCase().startsWith("N")) {
			return false;
		}
		//Falls nichts zutrifft
		else {
			return false;
		}
	}

}
