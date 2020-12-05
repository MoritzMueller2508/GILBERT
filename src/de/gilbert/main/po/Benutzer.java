package de.gilbert.main.po;

import de.gilbert.main.Util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Benutzer {

    private String benutzername;
    private String kursbezeichnung;
    private static final Map<String, String> ALLE_BENUTZER = new HashMap<>();

    static {
        try {
            Map<String, String> gilbert_benutzer = Util.csvDataHashMap("Gilbert_Benutzer");
            gilbert_benutzer.forEach((name, kurs) -> ALLE_BENUTZER.put(normalisierenBenutzername(name), kurs));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Benutzer (String benutzername, String kursbezeichnung) {
        this.benutzername = benutzername;
        this.kursbezeichnung = kursbezeichnung;
    }

    public String getBenutzername() {
        return Character.toUpperCase(benutzername.charAt(0)) + benutzername.substring(1);
    }
    public String getKursbezeichnung() {
        return kursbezeichnung;
    }

    public void setKursbezeichnung(String kursbezeichnung) {
        ALLE_BENUTZER.put(benutzername, this.kursbezeichnung = normalisiereKursbezeichnung(kursbezeichnung));
    }
    public void setBenutzername(String benutzername) {
        String kursbezeichnung = ALLE_BENUTZER.remove(this.benutzername);
        ALLE_BENUTZER.put(this.benutzername = normalisierenBenutzername(benutzername), kursbezeichnung);
    }

    /**
     * Überprüft ob der Nutzer mit dem angegebenen Benutzernamen bereits in der Gilbert_Benutzer.csv existiert.
     * @param benutzername : der eingegebene Benutzername
     * @return true, wenn der Nutzername in der Gilbert_Benutzer.csv Datei vorhanden ist, sonst false
     */
    public static Benutzer getBenutzer(String benutzername) {
        return ALLE_BENUTZER.containsKey(normalisierenBenutzername(benutzername))?
                new Benutzer(benutzername, ALLE_BENUTZER.get(benutzername.toLowerCase())):
                null;
    }

    /**
     * Fügt einen neuen Benutzer hinzu und speichert die Daten in der Gilbert_Benutzer.csv Datei
     * @param benutzername der Name des neuen Benutzers
     * @param kursbezeichnung die Kursbezeichnung des neuen Nutzers.
     */
    public static Benutzer nutzerHinzufuegen(String benutzername, String kursbezeichnung) {
        benutzername = normalisierenBenutzername(benutzername);
        kursbezeichnung = normalisiereKursbezeichnung(kursbezeichnung);

        ALLE_BENUTZER.put(benutzername, kursbezeichnung);
        serialisiereBenutzer();

        return new Benutzer(benutzername, kursbezeichnung);
    }

    /**
     * Speichert die Daten aus der Hashmap in die Gilbert_Benutzer.csv Datei
     */
    private static void serialisiereBenutzer() {
        try {
            FileWriter csvWriter = new FileWriter("Gilbert_Benutzer.csv");
            csvWriter.append("Benutzername; Kursbezeichnung\n");

            for(String schluessel : ALLE_BENUTZER.keySet()) {
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

    private static String normalisierenBenutzername(String benutzername) {
        return benutzername.replaceAll("\\s+", " ").trim().toLowerCase();
    }
    private static String normalisiereKursbezeichnung(String kursbezeichnung) {
        kursbezeichnung = kursbezeichnung.toUpperCase();
        if (!Util.kursbezeichnungInDatei(kursbezeichnung)) throw new IllegalArgumentException("kursbezeichnung muss ein valider Kurs sein");
        return kursbezeichnung;
    }

}
