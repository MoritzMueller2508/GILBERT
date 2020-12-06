package de.gilbert.main;

/**
 * Ein Erkennungsmodul untersucht eine Anfrage auf verschiedene universell
 * verwendbare Informationen und speichert diese in den Parametern der Anfrage.
 *
 * @author Lukas Rothenbach
 */
public abstract class Erkennungsmodul {

	/**
	 * Untersucht die Anfrage auf Erkennungsmodul-spezifische Daten und speichert diese
	 * in den Parametern der Anfrage
	 * @param anfrage die zu untersuchende Anfrage
	 */
	public abstract void untersucheAnfrage(Anfrage anfrage);

}
