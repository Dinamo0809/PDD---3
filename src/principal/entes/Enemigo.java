package principal.entes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

import principal.Constantes;
import principal.ElementosPrincipales;
import principal.dijkstra.Nodo;
import principal.herramientas.DibujoDebug;
import principal.sonidos.Sonido;

public class Enemigo {

	private Sonido lamento;
	private long duracionLamento;
	private long lamentoSiguiente = 0;

	private int idEnemigo;
	private double posicionX;
	private double posicionY;

	private String nombre;
	private int vidaMaxima;
	private float vidaActual;

	private Nodo siguienteNodo;

	public Enemigo(final int idEnemigo, final String nombre, final int vidaMaxima, final String rutaLamento) {
		this.idEnemigo = idEnemigo;
		this.posicionX = 0;
		this.posicionY = 0;

		this.nombre = nombre;
		this.vidaMaxima = vidaMaxima;
		this.vidaActual = vidaMaxima;

		this.lamento = new Sonido(rutaLamento);
		this.duracionLamento = lamento.getDuracion();
	}

	public void actualizar(ArrayList<Enemigo> enemigos) {
		if (lamentoSiguiente > 0) {
			lamentoSiguiente -= 1000000 / 60;
		}
		moverHaciaSiguienteNodo(enemigos);
	}

	private void moverHaciaSiguienteNodo(ArrayList<Enemigo> enemigos) {
		if (siguienteNodo == null) {
			return;
		}

//		gestionar atascos 
		for (Enemigo enemigo : enemigos) {
			if (enemigo.getAreaPosicional().equals(this.getAreaPosicional())) {
				continue;
			}

			if (enemigo.getAreaPosicional().intersects(siguienteNodo.getAreaPixeles())) {
				return;
			}
		}
		double velocidad = 0.5;

		int xSiguienteNodo = siguienteNodo.getPosicion().x * Constantes.LADO_SPRITE;
		int ySiguienteNodo = siguienteNodo.getPosicion().y * Constantes.LADO_SPRITE;

		if (posicionX < xSiguienteNodo) {
			posicionX += velocidad;
		}

		if (posicionX > xSiguienteNodo) {
			posicionX -= velocidad;
		}

		if (posicionY < ySiguienteNodo) {
			posicionY += velocidad;
		}

		if (posicionY > ySiguienteNodo) {
			posicionY -= velocidad;
		}
	}

	public void dibujar(final Graphics g, final int puntoX, final int puntoY) {
		if (vidaActual <= 0) {
			return;
		}

		dibujarBarraVida(g, puntoX, puntoY);
//		DibujoDebug.dibujarRectanguloContorno(g, getArea());
//		dibujarDistancia(g, puntoX, puntoY);
	}

	private void dibujarBarraVida(final Graphics g, final int puntoX, final int puntoY) {
		g.setColor(Color.green);
		DibujoDebug.dibujarRectanguloRelleno(g, puntoX, puntoY - 5,
				Constantes.LADO_SPRITE * (int) vidaActual / vidaMaxima, 2);
	}

//	private void dibujarDistancia(final Graphics g, final int puntoX, final int puntoY) {
//		Point puntoJugador = new Point(ElementosPrincipales.jugador.getPosicionXInt(),
//				ElementosPrincipales.jugador.getPosicionYInt());
//
//		Point puntoEnemigo = new Point((int) posicionX, (int) posicionY);
//
//		Double distancia = CalculadoraDistancia.getDistanciaEntrePuntos(puntoJugador, puntoEnemigo);
//
//		DibujoDebug.dibujarString(g, String.format("%.2f", distancia), puntoX, puntoY - 8);
//	}

	public void establecerPosicion(final double posicionX, final double posicionY) {
		this.posicionX = posicionX;
		this.posicionY = posicionY;
	}

	public double getPosicionX() {
		return posicionX;
	}

	public double getPosicionY() {
		return posicionY;
	}

	public int getIdEnemigo() {
		return idEnemigo;
	}

	public float getVidaActual() {
		return vidaActual;
	}

	public void perderVida(float ataqueRecibido) {
		// gestionar sonido
		if (lamentoSiguiente <= 0) {
			lamento.reproducir();
			lamentoSiguiente = duracionLamento;
		}

		if (vidaActual - ataqueRecibido < 0) {
			vidaActual = 0;
		} else {
			vidaActual -= ataqueRecibido;
		}
	}

	public Rectangle getArea() {
		final int puntoX = (int) posicionX - ElementosPrincipales.jugador.getPosicionXInt() + Constantes.MARGEN_X;
		final int puntoY = (int) posicionY - ElementosPrincipales.jugador.getPosicionYInt() + Constantes.MARGEN_Y;
		return new Rectangle(puntoX, puntoY, Constantes.LADO_SPRITE, Constantes.LADO_SPRITE);
	}

	public Rectangle getAreaPosicional() {
		return new Rectangle((int) posicionX, (int) posicionY, Constantes.LADO_SPRITE, Constantes.LADO_SPRITE);
	}

	public void cambiarSiguienteNodo(Nodo nodo) {
		// cuidado, posible bug (si es que esto llega a funcionar)
		siguienteNodo = nodo;
	}

	public Nodo getSiguienteNodo() {
		return siguienteNodo;
	}
}
