package de.gilbert.main.modules;

import de.gilbert.main.Verweismodul;

import java.net.MalformedURLException;
import java.net.URL;

public class MoodleModul extends Verweismodul {
    private static URL moodleURL;
    static {
        try {
            moodleURL = new URL("https://moodle.dhbw-mannheim.de/login/index.php");
        }
        catch(MalformedURLException e) {

        }
    }
    public MoodleModul() {
        super(moodleURL, "MoodleURL");
    }
}
