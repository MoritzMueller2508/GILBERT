package de.gilbert.main.modules;
/*erstellt von Jonas Knebel*/
import de.gilbert.main.Verweismodul;

import java.net.MalformedURLException;
import java.net.URL;

public class NotenModul extends Verweismodul {
    private static URL NOTEN_URL;
    static {
        try {
            NOTEN_URL = new URL("https://dualis.dhbw.de/scripts/mgrqispi.dll?APPNAME=CampusNet&PRGNAME=EXTERNALPAGES&ARGUMENTS=-N000000000000001,-N000324,-Awelcome");
        }
        catch(MalformedURLException e) {
            e.printStackTrace();
        }
    }
    public NotenModul(String[] schluessel) {
        super(schluessel, NOTEN_URL, "Dualis");
    }
}
