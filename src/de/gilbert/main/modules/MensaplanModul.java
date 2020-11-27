package de.gilbert.main.modules;

import de.gilbert.main.Verweismodul;

import java.net.MalformedURLException;
import java.net.URL;

public class MensaplanModul extends Verweismodul {
    private static URL mensaURL;
    static {
        try {
            mensaURL = new URL("https://www.stw-ma.de/speisepl%C3%A4ne.html");
        }
        catch(MalformedURLException e) {

        }
    }
    public MensaplanModul() {
        super(mensaURL, "Mensa");
    }
}
