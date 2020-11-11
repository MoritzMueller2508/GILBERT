package de.gilbert.main;

public abstract class Textmodul extends Modul{
	private String text;
	
	public Textmodul(String text) {
		this.text = text;
	}
	
	//TODO: implement
	public void beantworten(Anfrage anfrage) {
		
	}
	
	public String getText() {
		return text;
	}
	
	@Override
	public String[] getSchluessel() {
		return super.getSchluessel();
	}
}
