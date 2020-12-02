package de.gilbert.main.modules;
/*erstellt von Yannis Eigenbrodt*/
import de.gilbert.main.Anfrage;
import de.gilbert.main.Kommandozeilenanfrage;
import de.gilbert.main.Modul;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalQueries;
import java.util.*;
import java.util.function.Function;

public class PhasenModul extends Modul {

    private Map<String, Phase[]> phasenTheorie;
    private Map<String, Phase[]> phasenPraxis;

    public PhasenModul(String[] schluessel) throws IOException {
        super(schluessel);
        phasenPraxis = csvDataPraxis();
        phasenTheorie = csvDataTheorie();
    }

    @Override
    public void beantworteAnfrage(Anfrage anfrage) {
        // antworte mit praxisphasen, wenn explizit gefragt, sonst theoriephasen
        boolean praxis = anfrage.getAnfrage().toLowerCase().contains("praxis");

        Phase[] phasen = anfrage.frageAuswahl("In welchem Jahrgang bist du?", praxis? phasenPraxis: phasenTheorie);
        if (phasen != null) anfrage.schreibeAntwort(String.format("Die %sphasen sind:%n%s", praxis? "Praxis": "Theorie", getString(phasen)));
    }

    public String getString(Phase[] phasen) {
        StringJoiner joiner = new StringJoiner("\n");
        joiner.setEmptyValue("unbekannt");
        for (int i = 0; i < phasen.length; i++) {
            Phase phase = phasen[i];
            if (phase != null) joiner.add(String.format(Locale.getDefault(Locale.Category.DISPLAY),"%d. Semester: %2$te. %2$tB %2$tY bis %3$te. %3$tB %3$tY", i + 1, phase.getStart(), phase.getEnde()));
        }
        return joiner.toString();
    }

    /**
     * Liest eine csv Datei aus und speichert die Daten in einer Map.
     * @return die mit den Daten aus der CSV gefüllte Map
     * @throws IOException, sollte keine Datei mit dem angegebenen Namen exisitieren.
     */
    private Map<String, Phase[]> csvDataTheorie() throws IOException {
        return csvData("Phasen_Theorie.csv");
    }

    /**
     * Liest eine csv Datei aus und speichert die Daten in einer Map.
     * @return die mit den Daten aus der CSV gefüllte Map
     * @throws IOException, sollte keine Datei mit dem angegebenen Namen exisitieren.
     */
    private Map<String, Phase[]> csvDataPraxis() throws IOException {
        return csvData("Phasen_Praxis.csv");
    }

    private Map<String, Phase[]> csvData(String csvFile) throws IOException {
        String nextLine;
        String cvsSplitBy = ";";
        Map<String, Phase[]> phasendaten = new HashMap<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        Function<String, LocalDate> parser = s -> {
            try {
                return s.matches("\\d{2}\\.\\d{2}\\.\\d{4}") ?
                        formatter.parse(s).query(TemporalQueries.localDate()) :
                        null;
            } catch (DateTimeParseException e) {
                return null;
            }
        };

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(csvFile))) {
            bufferedReader.readLine();
            while ((nextLine = bufferedReader.readLine()) != null) {
                String[] zeile = nextLine.split(cvsSplitBy);
                if (zeile.length == 7) {
                    Phase[] phasen = new Phase[zeile.length - 1];
                    for (int i = 1; i < zeile.length; i++) {
                        String[] phase = zeile[i].split("-");

                        phasen[i - 1] = null;
                        if (phase.length == 2) {
                            LocalDate start = parser.apply(phase[0]);
                            LocalDate ende = parser.apply(phase[1]);
                            if (start != null && ende != null) phasen[i - 1] = new Phase(start, ende);
                        }
                    }

                    phasendaten.put(zeile[0], phasen);
                }

            }
            return phasendaten;
        }
    }

    private static class Phase {

        private final LocalDate start;
        private final LocalDate ende;

        private Phase(LocalDate start, LocalDate ende) {
            this.start = start;
            this.ende = ende;
        }

        public LocalDate getStart() {
            return start;
        }

        public LocalDate getEnde() {
            return ende;
        }

        @Override
        public String toString() {
            return start + " - " + ende;
        }
    }

}
