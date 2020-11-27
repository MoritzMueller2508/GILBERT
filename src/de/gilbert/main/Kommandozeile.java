package de.gilbert.main;

import java.io.IOException;
import java.util.Scanner;

public class Kommandozeile extends Benutzerschnittstelle {
	
	//TODO: Implement 
	public void beginneLoop() {
		Spracherkennung spracherkennung;
		try {
			spracherkennung = new Spracherkennung();
		} catch (IOException e) {
			System.out.println("Fehler beim initialisieren");
			e.printStackTrace();
			return;
		}

		Scanner scanner = new Scanner(System.in);
		while(true) {
			String userInput = scanner.nextLine();
			Kommandozeilenanfrage kommandozeilenanfrage = new Kommandozeilenanfrage(scanner, userInput);
			spracherkennung.bearbeiteAnfrage(kommandozeilenanfrage);
			if(userInput.equals("exit")) System.exit(0);
			System.out.println("Hast du noch weitere Fragen?");
		}

	}

	public static void main(String[] args) {
		Kommandozeile kommandozeile = new Kommandozeile();
		kommandozeile.beginneLoop();

	}
	
}
