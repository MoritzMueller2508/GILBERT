package de.gilbert.main;

/**
 * @author Lukas Rothenbach
 */
public abstract class Textmodul extends Modul{
	private final String text;
	
	public Textmodul(String[] schluessel, String text) {
		super(schluessel);
		this.text = text;
	}

	public void beantworteAnfrage(Anfrage anfrage) {
		anfrage.schreibeAntwort(getText());
	}
	
	public String getText() {
		return text;
	}

}
