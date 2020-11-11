package de.gilbert.main;

public abstract class Benutzerschnittstelle {
	
	private Spracherkennung spracherkennung;
		
	public abstract void beginneLoop();
	
	public Spracherkennung getSpracherkennung() {
		return spracherkennung;
	}
}
