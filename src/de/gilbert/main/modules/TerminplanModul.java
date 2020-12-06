package de.gilbert.main.modules;

import de.gilbert.main.Verweismodul;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Verweismodul: Gibt den Link zum Terminplan der Fakultät Technik der DHBW Mannheim an den Benutzer
 *
 * @author Zusibell Jimenez
 */
public class TerminplanModul extends Verweismodul {
    /** Die URL, die zurückgegeben werden soll */
    private static URL TERMINPLAN_URL;

    // Initialisierung der URL (ausgelagert, wegen MalformedURLException)
    static {
        try {
            TERMINPLAN_URL = new URL("https://www.mannheim.dhbw.de/fileadmin/user_upload/Studienangebot/Technik/Informatik/Angewandte-Informatik/Termine-2019-20-SG-AI-FAKT-DHBW-MA-202002.pdf");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Erzeugt ein neues TerminplanModul mit den gegebenen Schluesseln.
     * @param schluessel die Modulschluessel
     */
    public TerminplanModul(String[] schluessel) {
        super(schluessel, TERMINPLAN_URL, "Terminplan");
    }
}
