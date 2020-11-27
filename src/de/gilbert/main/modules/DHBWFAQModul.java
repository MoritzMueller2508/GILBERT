package de.gilbert.main.modules;

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

    public DHBWFAQModul() {
        super(FAQ_URL, "FAQ der DHBW");
    }
}
