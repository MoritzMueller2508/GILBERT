package de.gilbert.main;

import java.io.IOException;
import java.util.Scanner;

public class Kommandozeile extends Benutzerschnittstelle {
	
	//TODO: Implement 
	public void beginneLoop() {
		Spracherkennung spracherkennung = new Spracherkennung();
		try {
			spracherkennung.csvData();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scanner scanner = new Scanner(System.in);
		while(true) {
			String userInput = scanner.nextLine();
			Kommandozeilenanfrage kommandozeilenanfrage = new Kommandozeilenanfrage(userInput);
			spracherkennung.bearbeiteAnfrage(kommandozeilenanfrage);
		if(userInput.equals("exit")){
			System.exit(0);
		}
		scanner.close();
		System.out.println("Hast du noch weitere Fragen?");
		}

	}

	public static void main(String[] args) {
		Kommandozeile kommandozeile = new Kommandozeile();
		kommandozeile.beginneLoop();

	}
	
}
