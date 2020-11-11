package de.gilbert.main;

import java.util.List;

public class Spracherkennung {
	
	private List<Erkennungsmodul> erkennungsmodule;
	private List<Modul> module;
	
	//TODO: implement
	public void importiereModul() {
		
	}
	
	public void bearbeiteAnfrage(Anfrage anfrage) {
		rufeErkennungsModuleAuf(anfrage);
		findeModul(anfrage);
	}
	
	//TODO: implement
	private void rufeErkennungsModuleAuf(Anfrage anfrage) {
		
	}
	
	//TODO: implement
	private Modul findeModul(Anfrage anfrage) {
		return null;
	}
	
	public List<Erkennungsmodul> getErkennungsmodule() {
		return erkennungsmodule;
	}
	
	public List<Modul> getModule() {
		return module;
	}

}
