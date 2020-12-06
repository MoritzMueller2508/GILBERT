package de.gilbert.main.modules;

import de.gilbert.main.Verweismodul;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Verweismodul: Gibt den Link zur PDF "Modulhandbuch" an Benutzer
 *
 * @author Moritz Mueller
 */
public class ModHandbuchModul extends Verweismodul {
    /** Die URL, die zurückgegeben werden soll */
    private static URL MODHANDBUCH_URL;

    // Initialisierung der URL (ausgelagert, wegen MalformedURLException)
    static {
        try {
            MODHANDBUCH_URL = new URL("https://www.dhbw.de/fileadmin/user/public/SP/MA/Informatik/Angewandte_Informatik.pdf");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Erzeugt ein neues ModHandbuchModul mit den gegebenen Schluesseln.
     * @param schluessel die Modulschluessel
     */
    public ModHandbuchModul(String[] schluessel) {
        super(schluessel, MODHANDBUCH_URL,"Modulhandbuch");
    }
}
