package de.gilbert.main;

import java.net.URL;
/**
 * @author Lukas Rothenbach
 */
public abstract class Verweismodul extends Modul {
	private final URL verweis;
	private final String platzhalter;
	
	public Verweismodul(String[] schluessel, URL link, String platzhalter) {
		super(schluessel);
		this.verweis = link;
		this.platzhalter = platzhalter;
	}

	public void beantworteAnfrage(Anfrage anfrage) {
		anfrage.schreibeVerweis(getVerweis(), getPlatzhalter());
	}
	
	public URL getVerweis() {
		return verweis;
	}
	
	public String getPlatzhalter() {
		return platzhalter;
	}

}
