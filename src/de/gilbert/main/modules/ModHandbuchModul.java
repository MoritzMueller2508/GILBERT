package de.gilbert.main.modules;

import de.gilbert.main.Anfrage;
import de.gilbert.main.Modul;
import de.gilbert.main.Util;
import de.gilbert.main.Verweismodul;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Verweismodul: Gibt den Link zur PDF "Modulhandbuch" an Benutzer
 *
 * @author Moritz Mueller
 */
public class ModHandbuchModul extends Modul {

    private final Map<String, URL> handbuecher;

    /**
     * Erzeugt ein neues ModHandbuchModul mit den gegebenen Schluesseln.
     * @param schluessel die Modulschluessel
     */
    public ModHandbuchModul(String[] schluessel) throws IOException {
        super(schluessel);
        this.handbuecher = new HashMap<>();
        initHandbuecher();
    }

    private void initHandbuecher() throws IOException {
        for (String[] eintrag : Util.csvDataList("Modulhandbuecher"))
            if (eintrag.length == 2) handbuecher.put(eintrag[0], new URL(eintrag[1]));
    }

    @Override
    public void beantworteAnfrage(Anfrage anfrage) {
        anfrage.schreibeVerweis(
                anfrage.frageAuswahl("Was ist dein Studiengang?", handbuecher),
                "Modulhandbuch"
        );
    }
}
