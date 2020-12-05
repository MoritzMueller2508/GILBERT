package de.gilbert.main.modules;

import de.gilbert.main.Textmodul;

/**
 * @author Zusibell Jimenez
 */
public class GILBERTHilfeModul extends Textmodul {
    private static String ANLEITUNG = "Hallo, ich bin GILBERT.\n" +
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

    public GILBERTHilfeModul(String[] schluessel) {
        super(schluessel, ANLEITUNG);
    }

    public static String getAnleitung() {
        return ANLEITUNG;
    }

}
