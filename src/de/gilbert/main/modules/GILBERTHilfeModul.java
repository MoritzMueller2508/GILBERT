package de.gilbert.main.modules;

import de.gilbert.main.Textmodul;

/**
 * Textmodul: Gibt einen Hilfetext über GILBERT an den Benutzer
 *
 * @author Zusibell Jimenez
 */
public class GILBERTHilfeModul extends Textmodul {
    /** Der zurückgegebene Hilfetext */
    private static final String ANLEITUNG = "Hallo, ich bin GILBERT.\n" +
            "Ich bin hier um dir mit deiner Anfrage bezüglich der DHBW Mannheim zu helfen.\n" +
            "Du findest irgendwelche Information oder einen wichtigen Link nicht? Keine Sorge, ich bin hier.\n" +
            "Ich besitze Informationen über folgende Themen:\n" +
            "- Moodle\n" +
            "- Dualis\n" +
            "- Mensaplan\n" +
            "- Vorlesungsplan\n" +
            "- Prüfungsordnung\n" +
            "- Terminpläne\n" +
            "- Modulhandbuch\n" +
            "- Praxisarbeit\n" +
            "- DHBWs allgemeines FAQ Dokument\n\n" +
            "Wenn du noch keine Idee hast, wie du mit mir reden kannst, hier ein paar Anfragebeispiele:\n" +
            "- Welche Vorlesungen habe ich heute?\n" +
            "- Was gibt es heute zu essen?\n" +
            "- Wie sind die Leitlinien für die Praxisarbeit?";

    /**
     * Erzeugt ein neues DokPraxisarbeitModul mit den gegebenen Schluesseln.
     * @param schluessel die Modulschluessel
     */
    public GILBERTHilfeModul(String[] schluessel) {
        super(schluessel, ANLEITUNG);
    }

    /** Gibt die Anleitung zur weiteren Verwendung zurück (ins Besondere nach der Anmeldung) */
    public static String getAnleitung() {
        return ANLEITUNG;
    }

}
