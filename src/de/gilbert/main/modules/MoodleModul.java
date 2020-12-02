package de.gilbert.main.modules;
/*erstellt von Rosa Kern*/
import de.gilbert.main.Verweismodul;

import java.net.MalformedURLException;
import java.net.URL;

public class MoodleModul extends Verweismodul {
    private static URL MOODLE_URL;
    static {
        try {
            MOODLE_URL = new URL("https://moodle.dhbw-mannheim.de/login/index.php");
        }
        catch(MalformedURLException e) {
            e.printStackTrace();
        }
    }
    public MoodleModul(String[] schluessel) {
        super(schluessel, MOODLE_URL, "MoodleURL");
    }
}
