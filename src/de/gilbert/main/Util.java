package de.gilbert.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class Util {
    /**
     * Liest die Daten aus der CSV Datei aus und speichert sie als ArrayList
     */
    public static ArrayList<String[]> csvDataArrayList(String csvName) throws IOException {
        String csvFile = csvName+".csv";
        String nextLine;
        String cvsSplitBy = ";";
        ArrayList<String[]> gilbertData = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(csvFile))) {
            bufferedReader.readLine(); // überschriften ignorieren
            while ((nextLine = bufferedReader.readLine()) != null) {
                String[] split = nextLine.split(cvsSplitBy);
                //
                if(split.length >= 2) gilbertData.add(split);
            }

            return gilbertData;
        }
    }

    public static Map<String, String> csvDataHashMap(String csvName) throws IOException {
        String csvFile = csvName+".csv";
        String nextLine;
        String cvsSplitBy = ";";
        Map<String, String> kursdaten = new HashMap<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(csvFile))) {
            bufferedReader.readLine();
            while ((nextLine = bufferedReader.readLine()) != null) {
                if (nextLine.split(cvsSplitBy).length >= 2) {
                    String[] zeile = nextLine.split(cvsSplitBy);
                    if (zeile.length == 2) {
                        kursdaten.put(zeile[0], zeile[1]);
                    }
                }
            }
            return kursdaten;
        }
    }

    /**
     * Überprüft ob übergebene Kursbezeichnung in der Kursbezeichnungen.csv Datei exisitert.
     * @param kursbezeichung die zu überprüfende Kursbezeichung
     * @return true, wenn die Kursbezeichnung in der Datei gefunden wurde, sonst false
     */
    public static boolean kursbezeichungInDatei(String kursbezeichung) {
        try {
            Map<String, String> kursdaten = csvDataHashMap("Kurszuweisungen");
            return kursdaten.containsKey(kursbezeichung);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
