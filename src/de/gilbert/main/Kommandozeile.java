package de.gilbert.main;

import de.gilbert.main.modules.GILBERTHilfeModul;

import java.io.IOException;
import java.util.Scanner;

public class Kommandozeile extends Benutzerschnittstelle {

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
		System.out.println(GILBERTHilfeModul.getAnleitung());
		System.out.println();
		System.out.println("Wie kann ich dir heute helfen?");
		while(true) {
			String userInput = scanner.nextLine();
			if (userInput.equalsIgnoreCase("exit") || userInput.equalsIgnoreCase("nein")) {
				System.out.println("Ich hoffe ich, konnte helfen. Bis zum nächsten Mal!");
				System.exit(0);
			}
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
