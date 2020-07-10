package principal.inventario.armas;

import java.awt.Rectangle;
import java.util.ArrayList;

import principal.entes.Jugador;

public class Desarmar extends Arma {

	public Desarmar(int id, String nombre, String descripcion, int atqMinimo, int atqMaximo, boolean automatica,
			boolean penetrante, double ataquesPorSegundo) {
		super(id, nombre, descripcion, atqMinimo, atqMaximo, automatica, penetrante, ataquesPorSegundo,
				"/sonidos/Gunshot.wav");
	}

	public ArrayList<Rectangle> getAlcance(Jugador jugador) {
		return null;
	}

}
