package de.gilbert.main.modules;
import de.gilbert.main.Verweismodul;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Verweismodul: Gibt den Link zur PDF "StuPrO DHBW Technik" an den Benutzer
 *
 * @author Rosa Kern
 */
public class PruefOrdnungModul extends Verweismodul {
    /** Die URL, die zurückgegeben werden soll */
    private static URL PRUEFORDNUNG_URL;

    // Initialisierung der URL (ausgelagert, wegen MalformedURLException)
    static {
        try {
            PRUEFORDNUNG_URL = new URL("https://www.mannheim.dhbw.de/fileadmin/user_upload/Studienangebot/Technik/__Downloads/Pruefungsordnung-2020-Fakultaet-Technik-Praesidium-DHBW-S-200727_Nr._14.pdf");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Erzeugt ein neues PruefOrdnungModul mit den gegebenen Schluesseln.
     * @param schluessel die Modulschluessel
     */
    public PruefOrdnungModul(String[] schluessel) {
        super(schluessel, PRUEFORDNUNG_URL, "Prüfungsordnung");
    }
}
