package principal;

import principal.control.GestorControles;
import principal.graficos.SuperficieDibujo;
import principal.graficos.Ventana;
import principal.maquinaestado.GestorEstados;

public class GestorPrincipal {
	private boolean enFuncionamiento = false;
	private String titulo;
	private int ancho;
	private int alto;

	private static SuperficieDibujo sd;
	private Ventana ventana;
	private GestorEstados ge;

	private static int fps = 0;
	private static int aps = 0;

//	private Sonido musica = new Sonido("/sonidos/insertar.wav");

	private GestorPrincipal(final String titulo, final int ancho, final int alto) {
		this.titulo = titulo;
		this.ancho = ancho;
		this.alto = alto;
	}

	public static void main(String[] args) {
//      MacOs, Linux y probablemente Ubuntu?
//		System.setProperty("sun.java2d.opengl", "True");

//		Windows
		System.setProperty("sun.java2d.d3d", "True");
		System.setProperty("sun.java2d.ddforcevram", "True");

//		Creo que en las dos?
		System.setProperty("sun.java.transaccel", "True");

		GestorPrincipal gp = new GestorPrincipal("Post - D Day", Constantes.ANCHO_FULL_SCREEN,
				Constantes.ALTO_FULL_SCREEN);
		gp.iniciarJuego();
		gp.iniciarbuclePrincipal();
	}

	private void iniciarJuego() {
		enFuncionamiento = true;
		inicializar();
//     musica.repetir();
	}

	private void inicializar() {
		sd = new SuperficieDibujo(ancho, alto);
		ventana = new Ventana(titulo, sd);
		ge = new GestorEstados(sd);
	}

	private void iniciarbuclePrincipal() {
		int actualizacionesAcumuladas = 0;
		int framesAcumulados = 0;

		final int NS_POR_SEGUNDO = 1000000000;
		final byte APS_OBJETIVO = 60;
		final double NS_POR_ACTUALIZACION = NS_POR_SEGUNDO / APS_OBJETIVO;

		long referenciaActualizacion = System.nanoTime();
		long referenciaCounter = System.nanoTime();

		double tiempoTranscurrido;
		double delta = 0;

		while (enFuncionamiento) {
			final long inicioBucle = System.nanoTime();

			tiempoTranscurrido = inicioBucle - referenciaActualizacion;
			referenciaActualizacion = inicioBucle;

			delta += tiempoTranscurrido / NS_POR_ACTUALIZACION;

			while (delta >= 1) {
				actualizar();
				actualizacionesAcumuladas++;
				delta--;

			}

			dibujar();
			framesAcumulados++;

			if (System.nanoTime() - referenciaCounter > NS_POR_SEGUNDO) {

				aps = actualizacionesAcumuladas;
				fps = framesAcumulados;

				actualizacionesAcumuladas = 0;
				framesAcumulados = 0;
				referenciaCounter = System.nanoTime();

			}
		}
	}

	private void actualizar() {
		if (GestorControles.teclado.inventarioActivo) {
			ge.cambiarEstadoActual(1);
		} else {
			ge.cambiarEstadoActual(0);
		}
		ge.actualizar();
		sd.actualizar();
	}

	private void dibujar() {
		sd.dibujar(ge);
	}

	public static int getFPS() {
		return fps;
	}

	public static int getAPS() {
		return aps;
	}

	public static SuperficieDibujo getSd() {
		return sd;
	}
}
