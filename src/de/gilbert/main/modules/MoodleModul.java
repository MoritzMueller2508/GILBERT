package de.gilbert.main.modules;

import de.gilbert.main.Verweismodul;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Verweismodul: Gibt den Link zu Moodle der DHBW Mannheim an den Benutzer
 *
 * @author Rosa Kern
 */
public class MoodleModul extends Verweismodul {
    /** Die URL, die zurückgegeben werden soll */
    private static URL MOODLE_URL;

    // Initialisierung der URL (ausgelagert, wegen MalformedURLException)
    static {
        try {
            MOODLE_URL = new URL("https://moodle.dhbw-mannheim.de/login/index.php");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Erzeugt ein neues MoodleModul mit den gegebenen Schluesseln.
     * @param schluessel die Modulschluessel
     */
    public MoodleModul(String[] schluessel) {
        super(schluessel, MOODLE_URL, "MoodleURL");
    }
}
