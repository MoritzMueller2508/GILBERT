package de.gilbert.main.modules;

import de.gilbert.main.Verweismodul;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Verweismodul: Gibt den Link zu Dualis an den Benutzer
 *
 * @author Jonas Knebel
 */
public class NotenModul extends Verweismodul {
    /** Die URL, die zurückgegeben werden soll */
    private static URL NOTEN_URL;

    // Initialisierung der URL (ausgelagert, wegen MalformedURLException)
    static {
        try {
            NOTEN_URL = new URL("https://dualis.dhbw.de/scripts/mgrqispi.dll?APPNAME=CampusNet&PRGNAME=EXTERNALPAGES&ARGUMENTS=-N000000000000001,-N000324,-Awelcome");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Erzeugt ein neues NotenModul mit den gegebenen Schluesseln.
     * @param schluessel die Modulschluessel
     */
    public NotenModul(String[] schluessel) {
        super(schluessel, NOTEN_URL, "Dualis");
    }
}
