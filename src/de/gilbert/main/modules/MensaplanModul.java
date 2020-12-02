package de.gilbert.main.modules;
/*erstellt von Moritz Mueller*/
import de.gilbert.main.Verweismodul;

import java.net.MalformedURLException;
import java.net.URL;

public class MensaplanModul extends Verweismodul {
    private static URL MENSAURL;
    static {
        try {
            MENSAURL = new URL("https://www.stw-ma.de/speisepl%C3%A4ne.html");
        }
        catch(MalformedURLException e) {
            e.printStackTrace();
        }
    }
    public MensaplanModul(String[] schluessel) {
        super(schluessel, MENSAURL, "Mensa");
    }
}
