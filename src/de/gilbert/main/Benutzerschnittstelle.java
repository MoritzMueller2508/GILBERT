package de.gilbert.main;

public abstract class Benutzerschnittstelle {
	
	private Spracherkennung spracherkennung;
		
	public abstract void beginneLoop();

	Benutzerschnittstelle(){

	}
	public Spracherkennung getSpracherkennung() {
		return spracherkennung;
	}
}
