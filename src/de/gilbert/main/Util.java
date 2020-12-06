package de.gilbert.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Utility-Klasse, die Methoden bereitstellt, welche an mehreren Stellen im System verwendet werden.
 */
public final class Util {
    /** leerer Konstruktor, um die Initialisierung zu verhindern */
    private Util() {}

    /**
     * Liest die Daten aus der CSV Datei aus und speichert sie als List
     */
    public static List<String[]> csvDataList(String csvName) throws IOException {
        String csvFile = csvName+".csv";
        String nextLine;
        String cvsSplitBy = ";";
        ArrayList<String[]> daten = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(csvFile))) {
            bufferedReader.readLine(); // überschriften ignorieren
            while ((nextLine = bufferedReader.readLine()) != null) {
                String[] split = nextLine.split(cvsSplitBy);
                daten.add(split);
            }

            return daten;
        }
    }
    /**
     * Liest die Daten aus der CSV Datei aus und speichert sie als Map
     */
    public static Map<String, String> csvDataMap(String csvName) throws IOException {
        String csvFile = csvName+".csv";
        String nextLine;
        String cvsSplitBy = ";";
        Map<String, String> daten = new HashMap<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(csvFile))) {
            bufferedReader.readLine(); // überschriften ignorieren
            while ((nextLine = bufferedReader.readLine()) != null) {
                if (nextLine.split(cvsSplitBy).length >= 2) {
                    String[] zeile = nextLine.split(cvsSplitBy);
                    if (zeile.length == 2) daten.put(zeile[0], zeile[1]);
                }
            }

            return daten;
        }
    }

    /**
     * Überprüft ob übergebene Kursbezeichnung in der Kursbezeichnungen.csv Datei existiert.
     * @param kursbezeichnung die zu überprüfende Kursbezeichung
     * @return true, wenn die Kursbezeichnung in der Datei gefunden wurde, sonst false
     */
    public static boolean kursbezeichnungInDatei(String kursbezeichnung) {
        try {
            Map<String, String> kursdaten = csvDataMap("Kurszuweisungen");
            return kursdaten.containsKey(kursbezeichnung);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Um den Levenshtein-Abstand zweier Strings zu berechnen wird der Wagner–Fischer Algorithmus verwendet.
     * Der Levenshtein-Abstand ist die Anzahl an Zeichenänderungen, -entfernungen und -einfügungen, die mindestens
     * benötigt werden, um ein Wort in ein anderes Wort zu überführen.
     *
     * @param a das erste Wort
     * @param b das zweite Wort
     * @return den Levenshtein-Abstand der Parameter
     */
    public static int berechneLevenshteinAbstand(String a, String b) {
        // Die ersten beiden Bedingungen erhöhen die Performance, da keine Matrix erzeugt werden muss
        if (a.isEmpty()) return b.length(); // es muessten alle Zeichen aus b in a eingefuegt werden
        if (b.isEmpty()) return a.length(); // es muessten alle Zeichen aus a in b eingefuegt werden

        // Jedes Feld der Matrix m[i][j] enthaelt den Unterschied zwischen
        //    den ersten i Zeichen von a und
        //    den ersten j Zeichen von b
        int[][] matrix = new int[a.length() + 1][b.length() + 1];

        // Die 1. Zeile / Spalte entspricht `if (a.isEmpty()) return b.length()`
        //   macht das Erzeugen der restlichen Matrix aber einfacher
        for (int i = 0; i < matrix.length; i++) matrix[i][0] = i;
        for (int j = 0; j < matrix[0].length; j++) matrix[0][j] = j;

        // Auffuellen der restlichen Felder
        for (int i = 0; i + 1 < matrix.length; i++) {
            for (int j = 0; j + 1 < matrix[i].length; j++) {
                matrix[i + 1][j + 1] = min(
                    matrix[i][j] + (a.charAt(i) == b.charAt(j)? 0: 1),
                    matrix[i][j+1] + 1,
                    matrix[i+1][j] + 1
                );
            }
        }

        // Das letzte Feld entspricht dem Unterschied der beiden vollen Wörter
        return matrix[a.length()][b.length()];
    }
    /** Gibt den kleinsten Wert der Parameter zurück */
    public static int min(int... a) {
        // Es gibt keine Parameter => Es kann kein Parameter ausgewählt werden
        if (a.length == 0) throw new IllegalArgumentException();

        int min = a[0];
        for (int i = 1; i < a.length; i++) if (a[i] < min) min = a[i];
        return min;
    }

    /**
     * Sucht die besten Treffer zum gegebenen Schluessel aus den Daten heraus.
     * Die besten Treffer sind die Werte, deren Schluessel sich am wenigsten vom
     * gesuchten Schluessel unterscheiden.
     *
     * @param schluessel der gesuchte Schluessel
     * @param daten die zu untersuchenden Daten
     * @param <T> der Typ der Daten
     * @return eine Liste der besten Treffer
     */
    public static <T> List<Treffer<T>> findeBesteTreffer(String schluessel, Map<String, T> daten) {
        schluessel = schluessel.toLowerCase(); // ignore case

        // keine Distanz kann größer sein als Integer.MAX_VALUE,
        // da kein String länger sein kann (String.length() gibt int zurück)
        int minDistance = Integer.MAX_VALUE;
        // eine Liste aller bisher gefundenen Treffer mit `minDistance`
        List<Treffer<T>> besteTreffer = new ArrayList<>();

        for (Map.Entry<String, T> entry : daten.entrySet()) {
            int distance = berechneLevenshteinAbstand(schluessel, entry.getKey().toLowerCase());

            if (distance < minDistance) { // kleinere Distanz als alle anderen bisher gefundenen Treffer

                // lösche alle bisher gefundenen Treffer
                besteTreffer.clear();

                // speicher den neuen Treffer
                besteTreffer.add(new Treffer<>(entry.getKey(), distance, entry.getValue()));
                // speicher die neue kleinste Distanz
                minDistance = distance;
            } else if (distance == minDistance) {
                besteTreffer.add(new Treffer<>(entry.getKey(), distance, entry.getValue()));
            }
        }

        // gibt die besten Treffer zurück
        return Collections.unmodifiableList(besteTreffer);
    }

    /**
     * Ein Treffer, der in {@link #findeBesteTreffer(String, Map)} gefunden wurde.
     * Enthaelt
     * <ul>
     *     <li>den gefundenen Schluessel,</li>
     *     <li>dessen Levenshtein-Distanz zum gesuchten Schluessel und</li>
     *     <li>den zugehörigen Wert</li>
     * </ul>
     *
     * @param <T> der Typ des Wertes
     */
    public static final class Treffer<T> {
        /** der gefundene Schluessel */
        private final String SCHLUESSEL;
        /** die berechnete Distanz */
        private final int DISTANCE;
        /** der zugehörige Wert */
        private final T TREFFER;

        private Treffer(String schluessel, int distance, T treffer) {
            this.SCHLUESSEL = schluessel;
            this.DISTANCE = distance;
            this.TREFFER = treffer;
        }

        public String getSchluessel() {
            return SCHLUESSEL;
        }

        public int getDistance() {
            return DISTANCE;
        }

        public T getTreffer() {
            return TREFFER;
        }
    }

}
