package de.gilbert.main.modules;

import de.gilbert.main.Verweismodul;

import java.net.MalformedURLException;
import java.net.URL;
/**
 * @author Zusibell Jimenez
 */
public class TerminplanModul extends Verweismodul {
    private static URL TERMINPLAN_URL;

    static{
        try {
            TERMINPLAN_URL = new URL("https://www.mannheim.dhbw.de/fileadmin/user_upload/Studienangebot/Technik/Informatik/Angewandte-Informatik/Termine-2019-20-SG-AI-FAKT-DHBW-MA-202002.pdf");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    public TerminplanModul(String[] schluessel) {
        super(schluessel, TERMINPLAN_URL, "Terminplan");
    }
}
