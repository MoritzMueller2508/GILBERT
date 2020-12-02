package de.gilbert.main;

/**
 * @author Lukas Rothenbach
 */
public abstract class Benutzerschnittstelle {
	
	private Spracherkennung spracherkennung;
		
	public abstract void beginneLoop();

	Benutzerschnittstelle(){

	}
	public Spracherkennung getSpracherkennung() {
		return spracherkennung;
	}
}
