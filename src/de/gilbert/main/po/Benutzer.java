package de.gilbert.main.po;

import de.gilbert.main.Util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class Benutzer {

    private String benutzername;
    private String kursbezeichnung;
    private Map<String, String> benutzernamen;

    public Benutzer () {
        try {
            benutzernamen = Util.csvDataHashMap("Gilbert_Benutzer");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Überprüft ob der Nutzer mit dem angegebenen Benutzernamen bereits in der Gilbert_Benutzer.csv exisitert.
     * @param benutzername : der eingegebene Benutzername
     * @return true, wenn der Nutzername in der Gilbert_Benutzer.csv Datei vorhanden ist, sonst false
     */
    public boolean nutzerVorhanden(String benutzername) {
        if(benutzernamen.containsKey(benutzername.toLowerCase())) {
            this.benutzername = benutzername;
            this.kursbezeichnung = benutzernamen.get(benutzername);
            return true;
        }
        return false;
    }

    /**
     * Fügt einen neuen Benutzer hinzu und speichert die Daten in der Gilbert_Benutzer.csv Datei
     * @param benutzername der Name des neuen Benutzers
     * @param kursbezeichnung die Kursbezeichung des neuen Nutzers.
     */
    public void nutzerHinzufuegen(String benutzername, String kursbezeichnung) {
        this.benutzername = benutzername;
        this.kursbezeichnung = kursbezeichnung;
        this.benutzernamen.put(benutzername, kursbezeichnung);
        serialisiereBenutzer();
    }

    /**
     * Speichert die Daten aus der Hashmap in die Gilbert_Benutzer.csv Datei
     */
    private void serialisiereBenutzer() {
        try {
            FileWriter csvWriter = new FileWriter("Gilbert_Benutzer.csv");
            csvWriter.append("Benutzername; Kursbezeichung\n");

            for(String schluessel : benutzernamen.keySet()) {
                csvWriter.append(schluessel + ";" + benutzernamen.get(schluessel) + "\n");
            }
            csvWriter.flush();
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getBenutzername() {
        return benutzername;
    }

    public String getKursbezeichnung() {
        return kursbezeichnung;
    }

    public boolean valide() {
        return benutzername != null && kursbezeichnung != null;
    }
}
