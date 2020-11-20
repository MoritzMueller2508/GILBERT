package de.gilbert.main.modules;

import de.gilbert.main.Verweismodul;

import java.net.MalformedURLException;
import java.net.URL;

public class VorlesungsplanModul extends Verweismodul {
    private static URL vorlesungsplanURL;

    static {
        try {
            vorlesungsplanURL = new URL("https://vorlesungsplan.dhbw-mannheim.de/");
        } catch (MalformedURLException e) {

        }
    }

    public VorlesungsplanModul() {
        super(vorlesungsplanURL, "VorlesungsplanURL");

    }
}
