package de.gilbert.main.modules;

import de.gilbert.main.Anfrage;
import de.gilbert.main.Util;
import de.gilbert.main.Verweismodul;
import de.gilbert.main.po.Benutzer;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Map;

/**
 * @author Lukas Rothenbach
 */
public class VorlesungsplanModul extends Verweismodul {
    private static URL VORLESUNGSPLAN_URL;

    static {
        try {
            VORLESUNGSPLAN_URL = new URL("https://vorlesungsplan.dhbw-mannheim.de/");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public VorlesungsplanModul(String[] schluessel) {
        super(schluessel, VORLESUNGSPLAN_URL, "Vorlesungsplan");
    }

    @Override
    public void beantworteAnfrage(Anfrage anfrage) {
        Map<String, String> kursdaten;
        try {
            kursdaten = Util.csvDataHashMap("Kurszuweisungen");
            String kursbezeichnung;
            boolean beantwortet = false;
            Map<String, Object> parameter = anfrage.getParameter();
            Benutzer benutzer = (Benutzer) parameter.get("benutzer");
            String kursbezeichung = kursbezeichnungInAnfrage(kursdaten, anfrage);

            //angemeldeter Benutzer
            if (benutzer != null) {
                anfrage.schreibeVerweis(generiereNeueUrl(kursdaten.get(benutzer.getKursbezeichnung()), anfrage), "Vorlesungsplan");
                beantwortet = true;
            }
            //kurszuweisung in Anfrage
            else if (kursbezeichung != null) {
                anfrage.schreibeVerweis(generiereNeueUrl(kursdaten.get(kursbezeichung), anfrage), "Vorlesungsplan");
                beantwortet = true;

            }
            //keine Kurszuweisung gefunden
            if (!beantwortet) {
                super.beantworteAnfrage(anfrage);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Überprüft, ob die Anfrage eine Kursbezeichnung aus der Kursdaten Map enthält
     *
     * @param kursdaten die Map, die alle möglichen Kursbezeichnungen, sowie deren uid enthält
     * @param anfrage   das anfragenobjekt
     * @return die gefundene Kursbezeichnung oder null, sollte die Anfrage keine Kursbezeichnung enthalten
     */
    private String kursbezeichnungInAnfrage(Map<String, String> kursdaten, Anfrage anfrage) {
        for (String anfragenWort : anfrage.getWoerter()) {
            if (kursdaten.containsKey(anfragenWort.toUpperCase())) {
                return anfragenWort.toUpperCase();
            }
        }
        return null;
    }

    /**
     * Generiert eine neue Url, die die Basisurl mit Kurs und Datumsangaben parametrisiert.
     *
     * @param kurs    die uid des jeweiligen Kurses
     * @param anfrage das Anfragenobjekt
     * @return die generierte Url oder null, sollte beim parsen ein Fehler auftreten
     */
    private URL generiereNeueUrl(String kurs, Anfrage anfrage) {
        String neueUrl = "/index.php?action=view&gid=3067001&uid=" + kurs;

        if (anfrage.getParameter().containsKey("datumsangabe")) {
            neueUrl += "&date=" + ((Date) anfrage.getParameter().get("datumsangabe")).getTime() / 1000;
        }

        try {
            return new URL(VORLESUNGSPLAN_URL, neueUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
