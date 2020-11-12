package de.gilbert.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Spracherkennung {
	
	private List<Erkennungsmodul> erkennungsmodule;
	private List<Modul> module;


	public void csvData(){
		String csvFile = "Gilbert_Wortschatz.csv";
		String nextLine;
		String cvsSplitBy = ";";
		ArrayList<String[]> gilbertData = new ArrayList<>();

		try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
			br.readLine();
			while ((nextLine = br.readLine()) != null) {
				if(nextLine.split(cvsSplitBy).length>=3)
					gilbertData.add(nextLine.split(cvsSplitBy));
			}
			for(int gilbertDataRow=0; gilbertDataRow < gilbertData.size(); gilbertDataRow++){
				System.out.println(Arrays.toString(gilbertData.get(gilbertDataRow)));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

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
