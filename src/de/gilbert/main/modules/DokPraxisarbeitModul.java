package de.gilbert.main.modules;

import de.gilbert.main.Verweismodul;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Verweismodul: Gibt den Link zur PDF
 * "Leitlinien für die Bearbeitung und Dokumentation der Module Praxisprojekt I bis III, Studienarbeit I / II, Bachelorarbeit"
 * an den Benutzer
 *
 * @author Jonas Knebel
 */
public class DokPraxisarbeitModul extends Verweismodul {
    /** Die URL, die zurückgegeben werden soll */
    private static URL PRAXISARBEITSURL;

    // Initialisierung der URL (ausgelagert, wegen MalformedURLException)
    static {
        try {
            PRAXISARBEITSURL = new URL("https://www.mannheim.dhbw.de/fileadmin/user_upload/Studienangebot/Technik/__Downloads/Leitlinien-fuer-Bearbeitung-und-Dokumentation-FKT-DHBW-201909.pdf");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Erzeugt ein neues DokPraxisarbeitModul mit den gegebenen Schluesseln.
     * @param schluessel die Modulschluessel
     */
    public DokPraxisarbeitModul(String[] schluessel) {
        super(schluessel, PRAXISARBEITSURL, "Leitlinien Praxisarbeit");
    }
}
