package de.gilbert.main.modules;

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


    public ModHandbuchModul() {
        super(MODHANDBUCH_URL,"Modulhandbuch");
    }
}
