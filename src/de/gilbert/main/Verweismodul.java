package de.gilbert.main;

import java.net.URL;
/**
 * Modul, um einen Link und einen zugehörigen Text an den Benutzer zu geben
 *
 * @author Lukas Rothenbach
 */
public abstract class Verweismodul extends Modul {
	/** Der Link an den Benutzer */
	private final URL verweis;
	/** Der Text an den Benutzer */
	private final String platzhalter;

	/** Erzeugt ein Textmodul mit den gegebenen Schluesseln und dem gegebenen Link und Text */
	public Verweismodul(String[] schluessel, URL link, String platzhalter) {
		super(schluessel);
		this.verweis = link;
		this.platzhalter = platzhalter;
	}

	/** Beantwortet die gegebene Anfrage durch schreiben des Verweises an den Benutzer */
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
