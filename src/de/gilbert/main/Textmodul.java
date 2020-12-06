package de.gilbert.main;

/**
 * Modul, um einen einfachen Text an den Benutzer zu geben
 *
 * @author Lukas Rothenbach
 */
public abstract class Textmodul extends Modul {
	/** Der Text an den Benutzer */
	private final String text;

	/** Erzeugt ein Textmodul mit den gegebenen Schluesseln und dem gegebenen Text */
	public Textmodul(String[] schluessel, String text) {
		super(schluessel);
		this.text = text;
	}

	/** Beantwortet die gegebene Anfrage durch schreiben des Textes an den Benutzer */
	public void beantworteAnfrage(Anfrage anfrage) {
		anfrage.schreibeAntwort(getText());
	}
	
	public String getText() {
		return text;
	}

}
