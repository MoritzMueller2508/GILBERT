package de.gilbert.main.modules;
/*erstellt von Jonas Knebel*/
import de.gilbert.main.Verweismodul;

import java.net.MalformedURLException;
import java.net.URL;

public class DHBWFAQModul extends Verweismodul {
    private static URL FAQ_URL;
    static {
        try {
            FAQ_URL = new URL("https://www.mannheim.dhbw.de/service/itservice-center-1/haeufige-fragen-faq");
        }
        catch(MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public DHBWFAQModul(String[] schluessel) {
        super(schluessel, FAQ_URL, "FAQ der DHBW");
    }
}
