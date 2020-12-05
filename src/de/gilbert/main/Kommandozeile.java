package de.gilbert.main;

import de.gilbert.main.modules.GILBERTHilfeModul;
import de.gilbert.main.po.Benutzer;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * @author Jonas Knebel
 */
public class Kommandozeile extends Benutzerschnittstelle {

	private static final Set<String> EINGABE_BESTAETIGUNG = Set.of("ja", "okay", "doch", "klar", "jup", "yes", "j", "y");
	private static final Set<String> EINGABE_ABLEHNUNG = Set.of("nein", "nö", "nicht", "abbrechen", "exit", "stop", "n");

	private final Scanner scanner;

	public Kommandozeile() throws IOException {
		this.scanner = new Scanner(System.in);
	}

	public void beginneLoop() {
		Benutzer benutzer = null;
		System.out.println("Anmeldung");
		System.out.println("--------------------------------------");
		System.out.println(	  " - Du kannst dich mit deinem Benutzernamen anmelden" +
							"\n - Einen neuen Benutzer anlegen, indem du einen neuen Benutzernamen eingibst" +
							"\n - Die Anmeldung überspringen in dem du Enter drückst\n" +
							"\n - Bei Nicht-Überspringen der Anmeldung wird der Name und die Kursbezeichnung gespeichert \n\n");
		System.out.print("Benutzername : ");
		String benutzername = scanner.nextLine();

		if (benutzername.length() != 0) {
			benutzer = Benutzer.getBenutzer(benutzername);
			if (benutzer == null) benutzer = Benutzer.nutzerHinzufuegen(benutzername, frageNachKurs());
			else {
				System.out.println("\nHallo " + benutzer.getBenutzername());
				System.out.println("Du bist in dem Kurs " + benutzer.getKursbezeichnung() + " angemeldet.");
				if (!frageBestaetigung("Stimmt das noch?")) benutzer.setKursbezeichnung(frageNachKurs());
			}
		}

		System.out.println(GILBERTHilfeModul.getAnleitung());
		System.out.println();
		System.out.println("Wie kann ich dir heute helfen?");
		while(true) {
			String userInput = scanner.nextLine();
			if (EINGABE_ABLEHNUNG.contains(userInput.toLowerCase())) {
				System.out.println("Ich hoffe, ich konnte helfen. Bis zum nächsten Mal!");
				System.exit(0);
			}

			Kommandozeilenanfrage kommandozeilenanfrage = new Kommandozeilenanfrage(this, userInput);
			if(benutzer != null) kommandozeilenanfrage.getParameter().put("benutzer", benutzer);
			getSpracherkennung().bearbeiteAnfrage(kommandozeilenanfrage);
			System.out.println("Hast du noch weitere Fragen?");
		}

	}

	private String frageNachKurs() {
		do {
			System.out.print("Kursbezeichnung : ");
			String kursbezeichnung = scanner.nextLine();
			kursbezeichnung = kursbezeichnung.replaceAll("\\s+", "").toUpperCase();

			if(kursbezeichnung.length() != 0 && Util.kursbezeichnungInDatei(kursbezeichnung)) {
				return kursbezeichnung;
			}

			System.out.println("Das hat nicht geklappt. Versuche es mit einer validen Kursbezeichnung erneut.");
		} while(true);
	}

	// Standard Benutzerinteraktionen

	public void schreibeAntwort(String text) {
		System.out.println(text);
	}
	public void schreibeVerweis(URL link, String platzhalter) {
		System.out.println(platzhalter + ": " + link);

		if (frageBestaetigung("Soll ich den Link in Deinem Standardbrowser öffnen?")) {
			try {
				Runtime.getRuntime().exec(
						"rundll32 url.dll,FileProtocolHandler "
								+ link);
			} catch (IOException e) {
				System.out.println("Das hat nicht geklappt... ");
			}
		}
	}

	public <T> T frageAuswahl(String frage, Map<String, T> auswahl) {
		String antwort;
		do {
			System.out.println(frage);
			for (String keyElement : auswahl.keySet()) {
				System.out.println(keyElement);
			}
			antwort = scanner.nextLine();

			if (!auswahl.containsKey(antwort)) System.out.println("Ich bin mir nicht sicher, was du meinst.");
		} while (!auswahl.containsKey(antwort));

		return auswahl.get(antwort);
	}

	public boolean frageBestaetigung(String frage) {
		do {
			System.out.println(frage);
			String antwort = scanner.nextLine().toLowerCase();

			if (EINGABE_BESTAETIGUNG.contains(antwort)) return true;
			if (EINGABE_ABLEHNUNG.contains(antwort)) return false;

			System.out.println("Ich hab das nicht verstanden. Versuche es mit \"Ja\" oder \"Nein\".");
		} while (true);
	}

	// Anwendungseinstieg

	public static void main(String[] args) throws IOException {
		Benutzerschnittstelle schnittstelle = new Kommandozeile();
		schnittstelle.beginneLoop();
	}
	
}
