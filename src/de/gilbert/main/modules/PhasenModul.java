package de.gilbert.main.modules;

import de.gilbert.main.Anfrage;
import de.gilbert.main.Modul;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalQueries;
import java.util.*;
import java.util.function.Function;
/**
 * Modul: Gibt an Benutzer Informationen zu den Praxis- und Theoriephasen zurück.
 * Dafür wählt der Benutzer seinen Jahrgang aus und bekommt in zwei CSV-Dateien
 * gespeicherte Informationen zurück (momentan sind nur Informationen für drei
 * Jahrgänge implementiert).
 *
 * @author Yannis Eigenbrodt
 */
public class PhasenModul extends Modul {

    /**
     * Die Theoriephasen.
     * Jedem Jahrgang werden Phasen zugeordnet.
     * Jeder Eintrag im Phasenarray steht für das entsprechende Semester.
     */
    private final Map<String, Phase[]> phasenTheorie;
    /**
     * Die Theoriephasen.
     * Jedem Jahrgang werden Phasen zugeordnet.
     * Jeder Eintrag im Phasenarray steht für das entsprechende Semester.
     */
    private final Map<String, Phase[]> phasenPraxis;

    /**
     * Erzeugt ein neues PhasenModul mit den gegebenen Schluesseln und lädt die Praxis- und Theoriephasen.
     * @param schluessel die Modulschluessel
     */
    public PhasenModul(String[] schluessel) throws IOException {
        super(schluessel);
        phasenPraxis = csvDataPraxis();
        phasenTheorie = csvDataTheorie();
    }

    /**
     * Der Einstiegspunkt in das Modul für eine bestimmte Anfrage.
     * Die Anfrage wird beantwortet, indem der Benutzer seinen Jahrgang auswählt
     * und je nach Anfrage die Praxis- bzw. Theoriephasen bekommt
     *
     * @param anfrage die zu beantwortende Anfrage
     */
    @Override
    public void beantworteAnfrage(Anfrage anfrage) {
        // antworte mit praxisphasen, wenn explizit gefragt, sonst theoriephasen
        boolean praxis = anfrage.getAnfrage().toLowerCase().contains("praxis");

        // frage den Benutzer nach seinem Jahrgang
        Phase[] phasen = anfrage.frageAuswahl("In welchem Jahrgang bist du?", praxis? phasenPraxis: phasenTheorie);
        if (phasen != null) anfrage.schreibeAntwort(String.format("Die %s sind:%n%s", praxis? "Praxisphasen": "Theoriephasen", getString(phasen)));
    }

    /**
     * Wandelt die Phasen in einen String um.
     * Jede einzelne Phase entspricht dabei einer Zeile und wird einem
     * Semester zugeordnet.
     *
     * null-Einträge werden nicht ausgegeben, zählen aber als Semester.
     * Wenn alle Phasen null sind, wird "unbekannt" zurückgegeben.
     *
     * @param phasen die Phasen, die zu einem String umgewandelt werden soll
     * @return eine String-Representation der Phasen
     */
    private String getString(Phase[] phasen) {
        // Benutze StringJoiner, um die Phasen auf Zeilen zu trennen
        StringJoiner joiner = new StringJoiner("\n");
        joiner.setEmptyValue("unbekannt"); // => alle Einträge sind null -> gebe unbekannt zurück

        // füge alle Phasen hinzu
        for (int i = 0; i < phasen.length; i++) {
            Phase phase = phasen[i];
            if (phase != null) joiner.add(
                String.format(
                    Locale.getDefault(Locale.Category.DISPLAY),
                    "%1$d. Semester: %2$te. %2$tB %2$tY bis %3$te. %3$tB %3$tY",
                    // das Semester ist der Phasenindex + 1 (0. Eintrag => 1. Semester, ...)
                    i + 1, phase.getStart(), phase.getEnde()));
        }

        return joiner.toString();
    }

    /**
     * Liest die Theorie-Daten aus einer csv Datei
     * @return die mit den Theorie-Daten aus der CSV gefüllte Map
     * @throws IOException, sollte keine Datei mit dem angegebenen Namen existieren.
     */
    private Map<String, Phase[]> csvDataTheorie() throws IOException {
        return csvData("Phasen_Theorie.csv");
    }

    /**
     * Liest die Praxis-Daten aus einer csv Datei
     * @return die mit den Praxis-Daten aus der CSV gefüllte Map
     * @throws IOException, sollte keine Datei mit dem angegebenen Namen existieren.
     */
    private Map<String, Phase[]> csvDataPraxis() throws IOException {
        return csvData("Phasen_Praxis.csv");
    }

    /**
     * Liest die Daten zu Theorie- oder Praxisphasen aus der gegebenen csv Datei aus
     * @return die mit den Daten aus der CSV gefüllte Map
     * @throws IOException, sollte keine Datei mit dem angegebenen Namen existieren.
     */
    private Map<String, Phase[]> csvData(String csvFile) throws IOException {
        // -- INITIALISIEREN --
        String nextLine; String cvsSplitBy = ";";
        // Die ausgelesenen Daten
        Map<String, Phase[]> phasendaten = new HashMap<>();

        // genutzt, um das Datum zu parsen
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        // zusammenfassen mehrerer Funktionen, die um das Parsen herum passieren
        Function<String, LocalDate> parser = s -> {
            try {
                // prüfe auf passendes Format
                return s.matches("\\d{2}\\.\\d{2}\\.\\d{4}") ?
                        // parse mit DateTimeFormatter und wandel es in ein LocalDate um
                        formatter.parse(s).query(TemporalQueries.localDate()) :
                        // kein passendes Format -> null
                        null;
            } catch (DateTimeParseException e) {
                // Beim Parsen ist etwas schiefgelaufen, evtl. sind Tag, Monat, Jahr invalide
                return null;
            }
        };

        // -- AUSLESEN --
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(csvFile))) {
            bufferedReader.readLine(); // erste Zeile (Titelzeile) ignorieren
            while ((nextLine = bufferedReader.readLine()) != null) { // alle Zeilen durchgehen
                // splitten in einzelne Wertfelder
                String[] zeile = nextLine.split(cvsSplitBy);

                // erstes Feld ist Titel, weitere Felder sind Phasen
                // als erstes werden die Phasen ausgelesen
                Phase[] phasen = new Phase[zeile.length - 1];
                for (int i = 1; i < zeile.length; i++) {
                    // Jede Phase besteht aus zwei Daten, die durch '-' getrennt sind
                    String[] phase = zeile[i].split("-");

                    // Standardwert, falls Phase nicht geparst werden kann
                    phasen[i - 1] = null;
                    // erzeugen und speichern der Phase
                    if (phase.length == 2) {
                        LocalDate start = parser.apply(phase[0]);
                        LocalDate ende = parser.apply(phase[1]);
                        if (start != null && ende != null) phasen[i - 1] = new Phase(start, ende);
                    }
                }

                // speichern der gesammten Phase
                phasendaten.put(zeile[0], phasen);
            }

            // ausgelesenen Daten zurückgeben
            return phasendaten;
        }
    }

    /**
     * Eine Phase ist ein Zeitraum zwischen zwei bestimmten Daten.
     *
     * Es gibt Theorie- und Praxisphasen.
     */
    private static class Phase {
        /** Wann beginnt die Phase */
        private final LocalDate start;
        /** Wann endet die Phase */
        private final LocalDate ende;

        /**
         * Erzeugt eine neue Phase mit den gegebenen Daten
         *
         * @param start Beginn der Phase
         * @param ende Ende der Phase
         */
        private Phase(LocalDate start, LocalDate ende) {
            this.start = start;
            this.ende = ende;
        }

        /** @return den Beginn der Phase */
        public LocalDate getStart() {
            return start;
        }
        /** @return das Ende der Phase */
        public LocalDate getEnde() {
            return ende;
        }

        /** Gibt eine einfache, naive Representation der Phase zurück.
         * v.A. für Debugging, Ausgabe an Benutzer wird spezifischer formatiert.
         */
        @Override
        public String toString() {
            return start + " - " + ende;
        }
    }

}
