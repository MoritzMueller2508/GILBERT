package de.gilbert.main.modules;

import de.gilbert.main.Verweismodul;

import java.net.MalformedURLException;
import java.net.URL;

public class TerminplanModul extends Verweismodul {
    private static URL TERMINPLAN_URL;

    static{
        try {
            TERMINPLAN_URL = new URL("https://vorlesungsplan.dhbw-mannheim.de/index.php");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    public TerminplanModul() {
        super(TERMINPLAN_URL, "Terminplan");
    }
}
