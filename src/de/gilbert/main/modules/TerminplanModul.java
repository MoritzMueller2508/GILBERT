package de.gilbert.main.modules;

import de.gilbert.main.Verweismodul;

import java.net.MalformedURLException;
import java.net.URL;

public class TerminplanModul extends Verweismodul {
    private static URL Terminplan_URL;

    static{
        try {
            Terminplan_URL = new URL("https://vorlesungsplan.dhbw-mannheim.de/index.php");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    public TerminplanModul() {
        super(Terminplan_URL, "Terminplan");
    }
}
