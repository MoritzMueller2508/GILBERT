package de.gilbert.main;

public abstract class Modul {
	private String[] schluessel;
	
	public abstract void beantworteAnfrage(Anfrage anfrage);
	
	public String[] getSchluessel() {
		return schluessel;
	}
}
