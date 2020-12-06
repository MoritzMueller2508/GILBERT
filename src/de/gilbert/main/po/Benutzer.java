package de.gilbert.main.po;

import de.gilbert.main.Util;

import java.util.Objects;

/**
 * Ein angemeldeter Benutzer.
 */
public class Benutzer {

    /**
     * Die zuständige Verwaltung =
     * die Verwaltung, in der dieser Benutzer gespeichert ist und die diesen Benutzer erzeugt hat
     */
    private final Benutzerverwaltung VERWALTUNG;
    /** Der Name des Benutzers */
    private String benutzername;
    /** Der Kurs des Benutzers */
    private String kursbezeichnung;

    /**
     * Erzeugt einen neuen Benutzer.
     * Package-Private, damit nur eine Benutzerverwaltung einen Benutzer erzeugen kann,
     * und dieser somit persistent gespeichert wird.
     */
    Benutzer (Benutzerverwaltung verwaltung, String benutzername, String kursbezeichnung) {
        this.VERWALTUNG = Objects.requireNonNull(verwaltung);
        this.benutzername = Objects.requireNonNull(benutzername);
        this.kursbezeichnung = Objects.requireNonNull(kursbezeichnung);
    }

    public String getBenutzername() {
        return Character.toUpperCase(benutzername.charAt(0)) + benutzername.substring(1);
    }
    public String getKursbezeichnung() {
        return kursbezeichnung;
    }

    /**
     * Aendert die Kursbezeichnung und speichert sie in der erzeugenden Verwaltung.
     * @param kursbezeichnung die neue Kursbezeichnung
     */
    public void setKursbezeichnung(String kursbezeichnung) {
        // normalisieren und speichern der Kursbezeichnung in der Verwaltung
        VERWALTUNG.aendereKurs(this, kursbezeichnung = normalisiereKursbezeichnung(kursbezeichnung));
        // speichern des Kurses in diesem Object
        this.kursbezeichnung = kursbezeichnung;
    }
    /**
     * Aendert den Benutzernamen und speichert ihn in der erzeugenden Verwaltung.
     * @param benutzername die neue Kursbezeichnung
     */
    public void setBenutzername(String benutzername) {
        // normalisieren und speichern des neuen Benutzernamens in der Verwaltung
        VERWALTUNG.aendereName(this, benutzername = normalisierenBenutzername(benutzername));
        // speichern des Namens in diesem Object
        this.benutzername = benutzername;
    }

    /**
     * Normalisiert einen Benutzernamen:
     * <ul>
     *     <li>lowercase</li>
     *     <li>keine Leerzeichen am Anfang und am Ende</li>
     *     <li>nur einstellige Leerzeichen im Namen</li>
     * </ul>
     *
     * @param benutzername ein Benutzername
     * @return der normalisierte benutzername
     */
    static String normalisierenBenutzername(String benutzername) {
        return benutzername.replaceAll("\\s+", " ").trim().toLowerCase();
    }
    /**
     * Normalisiert eine Kursbezeichnung:
     * <ul>
     *     <li>uppercase</li>
     *     <li>existierend</li>
     * </ul>
     *
     * @param kursbezeichnung eine Kursbezeichnung
     * @return die normalisierte kursbezeichnung
     * @throws IllegalArgumentException wenn der Kurs nicht bekannt ist
     */
    static String normalisiereKursbezeichnung(String kursbezeichnung) {
        kursbezeichnung = kursbezeichnung.toUpperCase();
        if (!Util.kursbezeichnungInDatei(kursbezeichnung)) throw new IllegalArgumentException("kursbezeichnung muss ein valider Kurs sein");
        return kursbezeichnung;
    }

}
