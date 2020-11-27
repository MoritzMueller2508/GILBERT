package de.gilbert.main;

import java.net.URL;
import java.util.Map;
import java.util.Scanner;

public class Kommandozeilenanfrage extends Anfrage {
	

	public Kommandozeilenanfrage(String anfrage) {
		super(anfrage);
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
	public Object frageAuswahl(String frage, Map<String, Object> auswahl) {
		System.out.println(frage);

		for(String keyElement : auswahl.keySet()){
			System.out.println(keyElement);
		}
		Scanner scanner = new Scanner(System.in);
		String antwort = scanner.nextLine();

		scanner.close();
		return antwort;
	}
	
	public Object frageWert(String frage) {
		System.out.println(frage);
		Scanner scanner = new Scanner(System.in); 
		String antwort = scanner.nextLine();
		scanner.close();
		return antwort;
	}


	@Override
	public boolean frageBestaetigung(String frage) {

		if(frage.toUpperCase().startsWith("J")){
			return true;
		}
		else if(frage.toUpperCase().startsWith("N")) {
			return false;
		}
		//Falls nichts zutrifft
		else {
			return false;
		}
	}

}
