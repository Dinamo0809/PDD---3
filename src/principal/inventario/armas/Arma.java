package principal.inventario.armas;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

import principal.Constantes;
import principal.entes.Enemigo;
import principal.entes.Jugador;
import principal.inventario.Objeto;
import principal.sonidos.Sonido;
import principal.sprites.HojaSprites;
import principal.sprites.Sprites;

public abstract class Arma extends Objeto {

	public Sonido sonidoDisparo;

	public static HojaSprites hojaArmas = new HojaSprites(Constantes.RUTA_OBJETOS_ARMAS, Constantes.LADO_SPRITE, false);

	protected int atqMinimo;
	protected int atqMaximo;
	protected boolean automatica;
	protected boolean penetrante;
	protected double ataquesPorSegundo;
	protected int actualizacionesParaSiguienteAtaque;

	public Arma(int id, String nombre, String descripcion, int atqMinimo, int atqMaximo, final boolean automatica,
			final boolean penetrante, final double ataquesPorSegundo, final String rutaSonidoDisparo) {
		super(id, nombre, descripcion);
		this.atqMinimo = atqMinimo;
		this.atqMaximo = atqMaximo;
		this.automatica = automatica;
		this.penetrante = penetrante;
		this.ataquesPorSegundo = ataquesPorSegundo;
		this.actualizacionesParaSiguienteAtaque = 0;
		this.sonidoDisparo = new Sonido(rutaSonidoDisparo);

	}

	public abstract ArrayList<Rectangle> getAlcance(final Jugador jugador);

	public void actualizar() {
		if (actualizacionesParaSiguienteAtaque > 0) {
			actualizacionesParaSiguienteAtaque--;
		}
	}

	public void atacar(final ArrayList<Enemigo> enemigos) {
		if (actualizacionesParaSiguienteAtaque > 0) {
			return;
		}

		actualizacionesParaSiguienteAtaque = (int) (ataquesPorSegundo * 60);

		// reproducir sonido
		sonidoDisparo.reproducir();

		if (enemigos.isEmpty()) {
			return;
		}

		float ataqueActual = getAtqPromedio();

		for (Enemigo enemigo : enemigos) {
			enemigo.perderVida(ataqueActual);
		}
	}

	public Sprites getSprites() {
		return hojaArmas.getSprite(id - 500);
	}

	protected int getAtqPromedio() {
		Random r = new Random();

		return r.nextInt(atqMaximo - atqMinimo) + atqMinimo;
	}

	public boolean esAutomatica() {
		return automatica;
	}

	public boolean esPenetrante() {
		return penetrante;
	}

}
