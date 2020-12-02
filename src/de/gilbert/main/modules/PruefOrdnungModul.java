package de.gilbert.main.modules;
import de.gilbert.main.Verweismodul;

import java.net.MalformedURLException;
import java.net.URL;
/**
 * @author Rosa Kern
 */
public class PruefOrdnungModul extends Verweismodul {
    private static URL PRUEFORDNUNG_URL;

    static {
        try {
            PRUEFORDNUNG_URL = new URL("https://www.mannheim.dhbw.de/fileadmin/user_upload/Studienangebot/Technik/__Downloads/Pruefungsordnung-2020-Fakultaet-Technik-Praesidium-DHBW-S-200727_Nr._14.pdf");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    public PruefOrdnungModul(String[] schluessel) {
        super(schluessel, PRUEFORDNUNG_URL, "Prüfungsordnung");
    }
}
