package principal.interfaz_usuario;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import principal.Constantes;
import principal.ElementosPrincipales;
import principal.entes.Jugador;
import principal.herramientas.DibujoDebug;

public class MenuInferior {

	private Rectangle areaInventario;
	private Rectangle bordeAreaInventario;

	private Color grisRaro;
	private Color verdeOscuro;
	private Color rojoOscuro;
	private Color azulOscuro;
	private Color amarilloOscuro;

	public MenuInferior() {
		int altoMenu = 64;
		areaInventario = new Rectangle(0, Constantes.ALTO_JUEGO - altoMenu, Constantes.ANCHO_JUEGO, altoMenu);
		bordeAreaInventario = new Rectangle(areaInventario.x, areaInventario.y - 1, areaInventario.width, 1);

		grisRaro = new Color(35, 35, 38);
		verdeOscuro = new Color(11, 99, 5);
		rojoOscuro = new Color(145, 4, 4);
		azulOscuro = new Color(0, 7, 105);
		amarilloOscuro = new Color(189, 139, 4);
	}

	public void dibujar(final Graphics g) {
		dibujarAreaInventario(g);
		dibujarBarraHP(g);
		dibujarBarraPoder(g);
		dibujarBarraStamina(g);
		dibujarBarraExp(g, 25);
		dibujarRanurasObjetos(g);
	}

	private void dibujarAreaInventario(final Graphics g) {
		DibujoDebug.dibujarRectanguloRelleno(g, areaInventario, grisRaro);
		DibujoDebug.dibujarRectanguloRelleno(g, bordeAreaInventario, Color.WHITE);
	}

	private void dibujarBarraHP(final Graphics g) {
		final int medidaVertical = 4;
		final int anchoTotal = 100;

		DibujoDebug.dibujarRectanguloRelleno(g, areaInventario.x + 35, areaInventario.y + medidaVertical, anchoTotal,
				medidaVertical, Color.green);
		DibujoDebug.dibujarRectanguloRelleno(g, areaInventario.x + 35, areaInventario.y + medidaVertical * 2,
				anchoTotal, medidaVertical, verdeOscuro);

		g.setColor(Color.WHITE);
		DibujoDebug.dibujarString(g, "HP", areaInventario.x + 10, areaInventario.y + medidaVertical * 3);
		DibujoDebug.dibujarString(g, "500", anchoTotal + 45, areaInventario.y + medidaVertical * 3);
	}

	private void dibujarBarraPoder(final Graphics g) {
		final int medidaVertical = 4;
		final int anchoTotal = 100;

		DibujoDebug.dibujarRectanguloRelleno(g, areaInventario.x + 35, areaInventario.y + medidaVertical * 4,
				anchoTotal, medidaVertical, Color.red);
		DibujoDebug.dibujarRectanguloRelleno(g, areaInventario.x + 35, areaInventario.y + medidaVertical * 5,
				anchoTotal, medidaVertical, rojoOscuro);

		g.setColor(Color.WHITE);
		DibujoDebug.dibujarString(g, "POW", areaInventario.x + 5, areaInventario.y + medidaVertical * 6);
		DibujoDebug.dibujarString(g, "700", anchoTotal + 45, areaInventario.y + medidaVertical * 6);
	}

	private void dibujarBarraStamina(final Graphics g) {
		final int medidaVertical = 4;
		final int anchoTotal = 100;
		final int ancho = anchoTotal * ElementosPrincipales.jugador.getStamina() / Jugador.STAMINA_TOTAL;

		DibujoDebug.dibujarRectanguloRelleno(g, areaInventario.x + 35, areaInventario.y + medidaVertical * 7, ancho,
				medidaVertical, Color.blue);
		DibujoDebug.dibujarRectanguloRelleno(g, areaInventario.x + 35, areaInventario.y + medidaVertical * 8, ancho,
				medidaVertical, azulOscuro);

		g.setColor(Color.WHITE);
		DibujoDebug.dibujarString(g, "STM", areaInventario.x + 5, areaInventario.y + medidaVertical * 9);
		DibujoDebug.dibujarString(g, "" + ElementosPrincipales.jugador.getStamina(), anchoTotal + 45,
				areaInventario.y + medidaVertical * 9);
	}

	private void dibujarBarraExp(final Graphics g, final int experiencia) {
		final int medidaVertical = 4;
		final int anchoTotal = 100;
		final int ancho = anchoTotal * experiencia / anchoTotal;

		DibujoDebug.dibujarRectanguloRelleno(g, areaInventario.x + 35, areaInventario.y + medidaVertical * 10, ancho,
				medidaVertical, Color.yellow);
		DibujoDebug.dibujarRectanguloRelleno(g, areaInventario.x + 35, areaInventario.y + medidaVertical * 11, ancho,
				medidaVertical, amarilloOscuro);

		g.setColor(Color.WHITE);
		DibujoDebug.dibujarString(g, "EXP", areaInventario.x + 5, areaInventario.y + medidaVertical * 12);
		DibujoDebug.dibujarString(g, experiencia + "%", anchoTotal + 45, areaInventario.y + medidaVertical * 12);
	}

	private void dibujarRanurasObjetos(final Graphics g) {
		final int anchoRanura = 32;
		final int numeroRanuras = 10;
		final int espaciadoRanura = 10;
		final int anchoTotal = anchoRanura * numeroRanuras + espaciadoRanura * numeroRanuras;
		final int xInicial = Constantes.ANCHO_JUEGO - anchoTotal;
		final int anchoRanuraYEspacio = anchoRanura + espaciadoRanura;

		g.setColor(Color.white);

		for (int i = 0; i < numeroRanuras; i++) {
			int xActual = xInicial + anchoRanuraYEspacio * i;

			Rectangle ranura = new Rectangle(xActual, areaInventario.y + 4, anchoRanura, anchoRanura);
			DibujoDebug.dibujarRectanguloRelleno(g, ranura);
			DibujoDebug.dibujarString(g, "" + i, xActual + 13, areaInventario.y + 54);
		}
	}
}
