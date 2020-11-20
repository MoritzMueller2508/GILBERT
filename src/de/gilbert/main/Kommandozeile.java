package de.gilbert.main;

import java.util.Scanner;

public class Kommandozeile extends Benutzerschnittstelle {
	
	//TODO: Implement 
	public void beginneLoop() {
		Spracherkennung spracherkennung = new Spracherkennung();
		spracherkennung.csvData();
		Scanner scanner = new Scanner(System.in);
		while(true) {
			String userInput = scanner.nextLine();
			Kommandozeilenanfrage kommandozeilenanfrage = new Kommandozeilenanfrage(userInput);
			spracherkennung.bearbeiteAnfrage(kommandozeilenanfrage);
		if(userInput.equals("exit")){
			System.exit(0);
		}


		System.out.println("Hast du noch weitere Fragen?");
		}

	}

	public static void main(String[] args) {
		Kommandozeile kommandozeile = new Kommandozeile();
		kommandozeile.beginneLoop();

	}
	
}
