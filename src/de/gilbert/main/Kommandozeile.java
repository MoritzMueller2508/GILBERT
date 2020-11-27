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
		System.out.println("Hallo! Ich bin GILBERT und bin da, um dir zu helfen.\n" +
				           "Hast du eine Frage?");
		while(true) {
			String userInput = scanner.nextLine();
			if (userInput.equalsIgnoreCase("exit") || userInput.equalsIgnoreCase("nein")) System.exit(0);
			Kommandozeilenanfrage kommandozeilenanfrage = new Kommandozeilenanfrage(scanner, userInput);
			spracherkennung.bearbeiteAnfrage(kommandozeilenanfrage);
			System.out.println("Hast du noch weitere Fragen?");
		}

	}

	public static void main(String[] args) {
		Kommandozeile kommandozeile = new Kommandozeile();
		kommandozeile.beginneLoop();

	}
	
}
