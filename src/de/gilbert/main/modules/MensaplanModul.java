package de.gilbert.main.modules;

import de.gilbert.main.Verweismodul;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Verweismodul: Gibt den Link zur Seite über die Mensen des Studierendenwerkes Mannheim an den Benutzer
 *
 * @author Moritz Mueller
 */
public class MensaplanModul extends Verweismodul {
    /** Die URL, die zurückgegeben werden soll */
    private static URL MENSAURL;

    // Initialisierung der URL (ausgelagert, wegen MalformedURLException)
    static {
        try {
            MENSAURL = new URL("https://www.stw-ma.de/speisepl%C3%A4ne.html");
        } catch(MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Erzeugt ein neues MensaplanModul mit den gegebenen Schluesseln.
     * @param schluessel die Modulschluessel
     */
    public MensaplanModul(String[] schluessel) {
        super(schluessel, MENSAURL, "Mensa");
    }
}
