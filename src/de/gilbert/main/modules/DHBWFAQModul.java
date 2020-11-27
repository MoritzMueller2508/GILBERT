package de.gilbert.main.modules;

import de.gilbert.main.Verweismodul;

import java.net.MalformedURLException;
import java.net.URL;

public class DHBWFAQModul extends Verweismodul {
    private static URL FAQ_url;
    static {
        try {
            FAQ_url = new URL("https://www.mannheim.dhbw.de/service/itservice-center-1/haeufige-fragen-faq");
        }
        catch(MalformedURLException e) {

        }
    }

    public DHBWFAQModul() {
        super(FAQ_url, "FAQ der DHBW");
    }
}
