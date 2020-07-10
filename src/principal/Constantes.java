package principal;

import java.awt.Color;
import java.awt.Font;

import principal.herramientas.CargadorRecursos;

public class Constantes {
	public static final int LADO_SPRITE = 32;
	public static int LADO_CURSOR = 0;

	public static int ANCHO_JUEGO = 640;
	public static int ALTO_JUEGO = 360;

	public static int CENTRO_VENTANA_X = ANCHO_JUEGO / 2;
	public static int CENTRO_VENTANA_Y = ALTO_JUEGO / 2;

	public static int MARGEN_X = ANCHO_JUEGO / 2 - LADO_SPRITE / 2;
	public static int MARGEN_Y = ALTO_JUEGO / 2 - LADO_SPRITE / 2;

	public static int ANCHO_FULL_SCREEN = 1280;
	public static int ALTO_FULL_SCREEN = 720;

	public static double FACTOR_ESCALADO_X = (double) ANCHO_FULL_SCREEN / (double) ANCHO_JUEGO;
	public static double FACTOR_ESCALADO_Y = (double) ALTO_FULL_SCREEN / (double) ALTO_JUEGO;

	public static String RUTA_MAPA = "/mapas/01.pddm";
	public static Font FUENTE_PIXEL = CargadorRecursos.cargarFuente("/fuentes/px10.ttf");
	public static String RUTA_PERSONAJE = "/imagenes/hojaPersonajes/1.png";
	public static String RUTA_PISTOLA = "/imagenes/hojaPersonajes/pistola.png";
	public static String RUTA_OBJETOS = "/imagenes/hojaObjetos/1.png";
	public static String RUTA_OBJETOS_ARMAS = "/imagenes/hojaObjetos/armas.png";
	public static String RUTA_ENEMIGOS = "/imagenes/hojaEnemigos/";
	public static final Color COLOR_ROJO_RARO = new Color(0xffff0000);
}
