package de.gilbert.main.modules;

import de.gilbert.main.Verweismodul;

import java.net.MalformedURLException;
import java.net.URL;

public class PruefOrdnungModul extends Verweismodul {
    private static URL PruefOrdnung_URL;

    static {
        try {
            PruefOrdnung_URL = new URL("https://www.mannheim.dhbw.de/fileadmin/user_upload/Studienangebot/Technik/__Downloads/Pruefungsordnung-2020-Fakultaet-Technik-Praesidium-DHBW-S-200727_Nr._14.pdf");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    public PruefOrdnungModul(URL link, String platzhalter) {
        super(link, platzhalter);
    }
}
