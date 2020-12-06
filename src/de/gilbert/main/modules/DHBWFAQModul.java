package de.gilbert.main.modules;

import de.gilbert.main.Verweismodul;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Verweismodul: Gibt den Link zum FAQ der DHBW an den Benutzer
 *
 * @author Jonas Knebel
 */
public class DHBWFAQModul extends Verweismodul {
    /** Die URL, die zurückgegeben werden soll */
    private static URL FAQ_URL;

    // Initialisierung der URL (ausgelagert, wegen MalformedURLException)
    static {
        try {
            FAQ_URL = new URL("https://www.mannheim.dhbw.de/service/itservice-center-1/haeufige-fragen-faq");
        } catch(MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Erzeugt ein neues DHBWFAQModul mit den gegebenen Schluesseln.
     * @param schluessel die Modulschluessel
     */
    public DHBWFAQModul(String[] schluessel) {
        super(schluessel, FAQ_URL, "FAQ der DHBW");
    }
}
