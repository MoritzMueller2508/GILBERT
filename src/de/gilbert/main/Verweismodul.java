package de.gilbert.main;
/*erstellt von Lukas Rothenbach*/
import java.net.URL;

public abstract class Verweismodul extends Modul {
	private URL verweis;
	private String platzhalter;
	
	public Verweismodul(String[] schluessel, URL link, String platzhalter) {
		super(schluessel);
		this.verweis = link;
		this.platzhalter = platzhalter;
	}

	public void beantworteAnfrage(Anfrage anfrage) {
		anfrage.schreibeVerweis(verweis, platzhalter);
	}
	
	public URL getVerweis() {
		return verweis;
	}
	
	public String getPlatzhalter() {
		return platzhalter;
	}
	
	@Override
	public String[] getSchluessel() {
		return super.getSchluessel();
	}
}
