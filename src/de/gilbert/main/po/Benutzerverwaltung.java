package de.gilbert.main.po;

import de.gilbert.main.Util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Eine Benutzerverwaltung, die Benutzer persistent speichert und abrufbar macht.
 */
public class Benutzerverwaltung {

    /** Alle Benutzer, die diese Verwaltung kennt */
    private final Map<String, String> ALLE_BENUTZER = new HashMap<>();

    /**
     * Erzeugt eine neue Benutzerverwaltung und lädt gespeicherte Benutzer.
     */
    public Benutzerverwaltung() {
        try {
            Map<String, String> gilbert_benutzer = Util.csvDataMap("Gilbert_Benutzer");
            gilbert_benutzer.forEach((name, kurs) -> ALLE_BENUTZER.put(Benutzer.normalisierenBenutzername(name), kurs));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gibt den Nutzer mit dem angegebenen Benutzernamen zurück, falls dieser in der Gilbert_Benutzer.csv existiert.
     * @param benutzername : der eingegebene Benutzername
     * @return der passende Benutzer, wenn der Nutzername in der Gilbert_Benutzer.csv Datei vorhanden ist, sonst null
     */
    public Benutzer getBenutzer(String benutzername) {
        return ALLE_BENUTZER.containsKey(Benutzer.normalisierenBenutzername(benutzername))?
                new Benutzer(this, benutzername, ALLE_BENUTZER.get(Benutzer.normalisierenBenutzername(benutzername))):
                null;
    }

    void aendereKurs(Benutzer benutzer, String neuerKurs) {
        ALLE_BENUTZER.put(benutzer.getBenutzername(), neuerKurs);
    }
    void aendereName(Benutzer benutzer, String neuerName) {
        ALLE_BENUTZER.remove(benutzer.getBenutzername());
        ALLE_BENUTZER.put(neuerName, benutzer.getKursbezeichnung());
    }

    /**
     * Fügt einen neuen Benutzer hinzu und speichert die Daten in der Gilbert_Benutzer.csv Datei
     * @param benutzername der Name des neuen Benutzers
     * @param kursbezeichnung die Kursbezeichnung des neuen Nutzers.
     */
    public Benutzer nutzerHinzufuegen(String benutzername, String kursbezeichnung) {
        benutzername = Benutzer.normalisierenBenutzername(benutzername);
        kursbezeichnung = Benutzer.normalisiereKursbezeichnung(kursbezeichnung);

        ALLE_BENUTZER.put(benutzername, kursbezeichnung);
        serialisiereBenutzer(); // speichern nach Erzeugen

        return new Benutzer(this, benutzername, kursbezeichnung);
    }

    /**
     * Speichert die Daten aus der Map in die Gilbert_Benutzer.csv Datei
     */
    private void serialisiereBenutzer() {
        try {
            FileWriter csvWriter = new FileWriter("Gilbert_Benutzer.csv");
            csvWriter.append("Benutzername; Kursbezeichnung\n");

            for (String schluessel : ALLE_BENUTZER.keySet()) {
                csvWriter.append(schluessel)
                        .append(";")
                        .append(ALLE_BENUTZER.get(schluessel))
                        .append("\n");
            }

            csvWriter.flush();
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
