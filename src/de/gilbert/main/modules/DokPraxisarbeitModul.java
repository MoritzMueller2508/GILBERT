package de.gilbert.main.modules;

import de.gilbert.main.Verweismodul;
import java.net.MalformedURLException;
import java.net.URL;
/**
 * @author Jonas Knebel
 */
public class DokPraxisarbeitModul extends Verweismodul {
    private static URL PRAXISARBEITSURL;

    static {
        try {
            PRAXISARBEITSURL = new URL("https://www.mannheim.dhbw.de/fileadmin/user_upload/Studienangebot/Technik/__Downloads/Leitlinien-fuer-Bearbeitung-und-Dokumentation-FKT-DHBW-201909.pdf");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public DokPraxisarbeitModul(String[] schluessel) {
        super(schluessel, PRAXISARBEITSURL, "Leitlinien Praxisarbeit");
    }
}
