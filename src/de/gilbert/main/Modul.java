package de.gilbert.main;

import java.util.Objects;

/**
 * @author Lukas Rothenbach
 */
public abstract class Modul {
	private final String[] schluessel;

	public Modul(String[] schluessel) {
		this.schluessel = Objects.requireNonNullElseGet(schluessel, () -> new String[0]);
	}

	public abstract void beantworteAnfrage(Anfrage anfrage);

	public String[] getSchluessel() {
		return schluessel;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}
}
