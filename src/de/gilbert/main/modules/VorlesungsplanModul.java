package de.gilbert.main.modules;

import de.gilbert.main.Anfrage;
import de.gilbert.main.Util;
import de.gilbert.main.Verweismodul;
import de.gilbert.main.po.Benutzer;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Map;

/**
 * Verweismodul: Gibt den Link zum Vorlesungsplan der DHBW Mannheim an den Benutzer
 * Wenn der Benutzer angemeldet ist, wird der Vorlesungsplan des Kurses des Benutzers geöffnet
 *
 * @author Lukas Rothenbach
 */
public class VorlesungsplanModul extends Verweismodul {
    /** Die URL, die zurückgegeben werden soll */
    private static URL VORLESUNGSPLAN_URL;

    // Initialisierung der URL (ausgelagert, wegen MalformedURLException)
    static {
        try {
            VORLESUNGSPLAN_URL = new URL("https://vorlesungsplan.dhbw-mannheim.de/");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Erzeugt ein neues VorlesungsplanModul mit den gegebenen Schluesseln.
     * @param schluessel die Modulschluessel
     */
    public VorlesungsplanModul(String[] schluessel) {
        super(schluessel, VORLESUNGSPLAN_URL, "Vorlesungsplan");
    }

    /**
     * Beantwortet die gegebene Anfrage mit einem Verweis auf den Vorlesungsplan der DHBW Mannheim.
     * Wenn der Benutzer einen bestimmten Kurs in der Anfrage anfragt oder
     * der Benutzer angemeldet ist und ihm damit ein bestimmter Kurs zugeordnet ist,
     * wird der entsprechende Kurs geöffnet, sonst nur die Startseite des Vorlesungsplans.
     */
    @Override
    public void beantworteAnfrage(Anfrage anfrage) {
        // wenn bis zum Schluss nicht beantwortet werden konnte -> VORLESUNGSPLAN_URL an Benutzer
        boolean beantwortet = false;

        try {

            Map<String, String> kursdaten = Util.csvDataMap("Kurszuweisungen");
            Map<String, Object> parameter = anfrage.getParameter();

            // suche nach Kursbezeichnung in bekannten Informationen
            String kursbezeichnung = kursbezeichnungInAnfrage(kursdaten, anfrage); // hat Benutzer bestimmten Kurs angefragt?

            if (kursbezeichnung == null) { // es wurde kein Kurs gefragt
                Benutzer benutzer = (Benutzer) parameter.get("benutzer"); // ist Benutzer angemeldet?
                if (benutzer != null) kursbezeichnung = benutzer.getKursbezeichnung();
            }

            if (kursbezeichnung != null) { // Kurszuweisung gefunden
                URL url = generiereNeueUrl(kursdaten.get(kursbezeichnung), anfrage);
                if (url != null) {
                    anfrage.schreibeVerweis(url, "Vorlesungsplan");
                    beantwortet = true;
                } // sonst ist etwas beim erzeugen der URL schiefgelaufen
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Keine Kurszuweisung gefunden oder ein Fehler ist aufgetreten
        if (!beantwortet) super.beantworteAnfrage(anfrage);
    }

    /**
     * Überprüft, ob die Anfrage eine Kursbezeichnung aus der Kursdaten Map enthält
     *
     * @param kursdaten die Map, die alle möglichen Kursbezeichnungen, sowie deren uid enthält
     * @param anfrage   das anfragenobjekt
     * @return die gefundene Kursbezeichnung oder null, sollte die Anfrage keine Kursbezeichnung enthalten
     */
    private String kursbezeichnungInAnfrage(Map<String, String> kursdaten, Anfrage anfrage) {
        // prüfe jedes Wort, ob es einem Kurs entspricht
        for (String anfragenWort : anfrage.getWoerter()) {
            // jeder Kurs ist UpperCase gespeichert, um nicht auf Groß-/Kleinschreibung zu achten
            if (kursdaten.containsKey(anfragenWort.toUpperCase())) {
                return anfragenWort.toUpperCase();
            }
        }

        // es wurde kein Kurs gefunden
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
        // öffnen des Kurses
        String neueUrl = "/index.php?action=view&uid=" + kurs;

        // wenn der Benutzer ein Datum genannt hat, öffne diesen Tag
        Object date = anfrage.getParameter().get("datumsangabe");
        if (date != null) {
            if (date instanceof LocalDate) // sollte immer wahr sein (nur für Cast Safety)
                neueUrl += "&date=" + ((LocalDate) date).toEpochSecond(LocalTime.MIDNIGHT, ZoneOffset.UTC);
        }

        // neue URL erzeugen
        try {
            return new URL(VORLESUNGSPLAN_URL, neueUrl);
        } catch (MalformedURLException e) {
            // sollte eigentlich nie eintreten
            e.printStackTrace();
            return null;
        }
    }
}
