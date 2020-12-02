package de.gilbert.main.modules;
/*erstellt von Moritz Mueller*/
import de.gilbert.main.Verweismodul;

import java.net.MalformedURLException;
import java.net.URL;

public class ModHandbuchModul extends Verweismodul {
    private static URL MODHANDBUCH_URL;

    static {
        try {
            MODHANDBUCH_URL = new URL("https://www.dhbw.de/fileadmin/user/public/SP/MA/Informatik/Angewandte_Informatik.pdf");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    public ModHandbuchModul(String[] schluessel) {
        super(schluessel, MODHANDBUCH_URL,"Modulhandbuch");
    }
}
