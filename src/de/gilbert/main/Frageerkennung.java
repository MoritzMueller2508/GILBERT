package de.gilbert.main;


public class Frageerkennung extends Erkennungsmodul {

	/**
	 * Extrahiert das erste auftretende Fragewort des Anfrage Strings, sollte dies eines beinhalten, und fügt dies der Parameter Map der Anfrage hinzu.
	 * @param anfrage : das Anfrageobjekt, welches alle nötigen Information der Anfrage verwaltet. 
	 */
	@Override
	public void untersucheAnfrage(Anfrage anfrage) {
		String[] frageWoerter = new String[] {"wo","wie","was","woher","welche","wer","an welchem","wann","ab wann","bis wann", "gibt es"};
		//anpassen damit die Wörter der Anfrage mit den kleingeschriebenen fragewoertern verglichen werden können.
		String angepassteAnfrage = anfrage.getAnfrage().toLowerCase();
		String bestesFrageWort = "";
		int bestesFrageWortIndex = Integer.MAX_VALUE;

		for (String frageWort : frageWoerter) {
			if (angepassteAnfrage.contains(frageWort.toLowerCase())) {
				int neuerIndex = angepassteAnfrage.indexOf(frageWort.toLowerCase());
				
				if (neuerIndex < bestesFrageWortIndex) {
					//besser passendes Fragewort gefunden
					bestesFrageWortIndex = neuerIndex;
					bestesFrageWort = frageWort;
				}
			}
		}
		//Es soll nur das am besten passende Fragewort abgespeichert werden. 
		if(bestesFrageWortIndex >= 0) {
			anfrage.getParamter().put("fragewort", bestesFrageWort);
		}
	}

}
