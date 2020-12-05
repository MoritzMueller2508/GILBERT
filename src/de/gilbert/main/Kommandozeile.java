package de.gilbert.main;

import de.gilbert.main.modules.GILBERTHilfeModul;
import de.gilbert.main.po.Benutzer;

import java.io.IOException;
import java.util.Scanner;

/**
 * @author Jonas Knebel
 */
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
		Benutzer benutzer = new Benutzer();
		System.out.println("Anmeldung");
		System.out.println("--------------------------------------");
		System.out.println(	  " - Du kannst dich mit deinem Benutzernamen anmelden" +
							"\n - Einen neuen Benutzer anlegen, indem du einen neuen Benutzernamen eingibst" +
							"\n - Die Anmeldung überspringen in dem du Enter drückst\n" +
							"\n - Bei Nicht-Überspringen der Anmeldung wird der Name und die Kursbezeichnung gespeichert \n\n");
		System.out.print("Benutzername : ");
		String benutzername = scanner.nextLine();

		if(benutzername.length() != 0) {
			if(!benutzer.nutzerVorhanden(benutzername)) {
				do {
					System.out.print("Kursbezeichung : ");
					String kursbezeichung  = scanner.nextLine();
					kursbezeichung = kursbezeichung.replaceAll("\\s+", "").toUpperCase();

					if(kursbezeichung.length() != 0 && Util.kursbezeichungInDatei(kursbezeichung)) {
							benutzer.nutzerHinzufuegen(benutzername, kursbezeichung);
							break;
					}
					System.out.println("Das hat nicht geklappt. Versuche es mit einer validen Kursbezeichung erneut.");
				} while(true);
			}else{
				System.out.println(" \nHallo " + benutzername + "\n" +
						"Du bist in diesem Kurs: " + benutzer.getKursbezeichnung());
				Kommandozeilenanfrage bestaetigung = new Kommandozeilenanfrage(scanner, "");
				if(!bestaetigung.frageBestaetigung("Ist das noch korrekt?"))
				{
					do {
						System.out.print("Kursbezeichung : ");
						String kursbezeichung  = scanner.nextLine();
						kursbezeichung = kursbezeichung.replaceAll("\\s+", "").toUpperCase();

						if(kursbezeichung.length() != 0 && Util.kursbezeichungInDatei(kursbezeichung)) {
							benutzer.nutzerHinzufuegen(benutzername, kursbezeichung);
							break;
						}
						System.out.println("Das hat nicht geklappt. Versuche es mit einer validen Kursbezeichung erneut.");
					} while(true);
				}
			}
		}


		System.out.println(GILBERTHilfeModul.getAnleitung());
		System.out.println();
		System.out.println("Wie kann ich dir heute helfen?");
		while(true) {
			String userInput = scanner.nextLine();
			if (userInput.equalsIgnoreCase("exit") || userInput.equalsIgnoreCase("nein")) {
				System.out.println("Ich hoffe, ich konnte helfen. Bis zum nächsten Mal!");
				System.exit(0);
			}
			Kommandozeilenanfrage kommandozeilenanfrage = new Kommandozeilenanfrage(scanner, userInput);
			if(benutzer.valide()) {
				kommandozeilenanfrage.getParameter().put("benutzer", benutzer);
			}
			spracherkennung.bearbeiteAnfrage(kommandozeilenanfrage);
			System.out.println("Hast du noch weitere Fragen?");
		}

	}

	public static void main(String[] args) {
		Kommandozeile kommandozeile = new Kommandozeile();
		kommandozeile.beginneLoop();

	}
	
}
