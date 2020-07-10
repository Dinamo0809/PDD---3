package principal.mapas;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import principal.Constantes;
import principal.ElementosPrincipales;
import principal.control.GestorControles;
import principal.dijkstra.Dijkstra;
import principal.entes.Enemigo;
import principal.entes.RegistroEnemigos;
import principal.herramientas.CalculadoraDistancia;
import principal.herramientas.CargadorRecursos;
import principal.herramientas.DibujoDebug;
import principal.inventario.Objeto;
import principal.inventario.ObjetoUnicoTiled;
import principal.inventario.RegistroObjetos;
import principal.inventario.armas.Desarmar;
import principal.sprites.HojaSprites;
import principal.sprites.Sprites;

public class MapaTiled {

	private int anchoMapaEnTiles;
	private int altoMapaEnTiles;

	private Point puntoInicial;

	private ArrayList<CapaSprites> capasSprites;
	private ArrayList<CapaColisiones> capasColisiones;

	private ArrayList<Rectangle> areasColisionOriginales;
	public ArrayList<Rectangle> areasColisionPorActualizacion;

	private Sprites[] paletaSprites;
	private Dijkstra d;

	private ArrayList<ObjetoUnicoTiled> objetosMapa;
	private ArrayList<Enemigo> enemigosMapa;

	public MapaTiled(final String ruta) {
		String contenido = CargadorRecursos.leerArchivoTexto(ruta);

		// Ancho, alto
		JSONObject globalJSON = getObjetoJSON(contenido);
		anchoMapaEnTiles = getIntDesdeJSON(globalJSON, "width");
		altoMapaEnTiles = getIntDesdeJSON(globalJSON, "height");

		// punto de inicio
		JSONObject puntoInicial = getObjetoJSON(globalJSON.get("start").toString());
		this.puntoInicial = new Point(getIntDesdeJSON(puntoInicial, "x"), getIntDesdeJSON(puntoInicial, "y"));
//		System.out.println(this.puntoInicial.toString());

		// CAPAS
		JSONArray capas = getArrayJSON(globalJSON.get("layers").toString());
		this.capasSprites = new ArrayList<>();
		this.capasColisiones = new ArrayList<>();

		// Iniciar capas
		for (int i = 0; i < capas.size(); i++) {
			JSONObject datosCapa = getObjetoJSON(capas.get(i).toString());

			int anchoCapa = getIntDesdeJSON(datosCapa, "width");
			int altoCapa = getIntDesdeJSON(datosCapa, "height");
			int xCapa = getIntDesdeJSON(datosCapa, "x");
			int yCapa = getIntDesdeJSON(datosCapa, "y");
			String tipo = datosCapa.get("type").toString();

			switch (tipo) {
			case "tilelayer":
				JSONArray sprites = getArrayJSON(datosCapa.get("data").toString());
				int[] spritesCapa = new int[sprites.size()];
				for (int j = 0; j < sprites.size(); j++) {
					int codigoSprite = Integer.parseInt(sprites.get(j).toString());
					spritesCapa[j] = codigoSprite - 1;
				}
				this.capasSprites.add(new CapaSprites(anchoCapa, altoCapa, xCapa, yCapa, spritesCapa));
				break;
			case "objectgroup":
				JSONArray rectangulos = getArrayJSON(datosCapa.get("objects").toString());
				Rectangle[] rectangulosCapa = new Rectangle[rectangulos.size()];
				for (int j = 0; j < rectangulos.size(); j++) {
					JSONObject datosRectangulo = getObjetoJSON(rectangulos.get(j).toString());

					int x = getIntDesdeJSON(datosRectangulo, "x");
					int y = getIntDesdeJSON(datosRectangulo, "y");
					int ancho = getIntDesdeJSON(datosRectangulo, "width");
					int alto = getIntDesdeJSON(datosRectangulo, "height");

					if (x == 0)
						x = 1;
					if (y == 0)
						y = 1;
					if (ancho == 0)
						ancho = 1;
					if (alto == 0)
						alto = 1;

					Rectangle rectangulo = new Rectangle(x, y, ancho, alto);
					rectangulosCapa[j] = rectangulo;
				}
				this.capasColisiones.add(new CapaColisiones(anchoCapa, altoCapa, xCapa, yCapa, rectangulosCapa));
				break;
			}

		}
		// COMBINAR COLISIONES EN UN SOLO ARRAYLIST POR EFICIENCIA
		areasColisionOriginales = new ArrayList<>();
		for (int i = 0; i < capasColisiones.size(); i++) {
			Rectangle[] rectangulos = capasColisiones.get(i).getColisionables();

			for (int j = 0; j < rectangulos.length; j++) {
				areasColisionOriginales.add(rectangulos[j]);
			}
		}

		// dijkstra
		d = new Dijkstra(new Point(10, 10), anchoMapaEnTiles, altoMapaEnTiles, areasColisionOriginales);

		// AVERIGUAR TOTAL DE SPRITES EXITSTENTES EN TODAS LAS CAPAS
		JSONArray coleccionesSprites = getArrayJSON(globalJSON.get("tilesets").toString());
		int totalSprites = 0;
		for (int i = 0; i < coleccionesSprites.size(); i++) {
			JSONObject datosGrupo = getObjetoJSON(coleccionesSprites.get(i).toString());
			totalSprites += getIntDesdeJSON(datosGrupo, "tilecount");
		}
		paletaSprites = new Sprites[totalSprites];

		// ASIGNAR SPRITES NECESARIOS A LA PALETA A PARTIR DE LAS CAPAS
		for (int i = 0; i < coleccionesSprites.size(); i++) {
			JSONObject datosGrupo = getObjetoJSON(coleccionesSprites.get(i).toString());

			String nombreImagen = datosGrupo.get("image").toString();
			int anchoTiles = getIntDesdeJSON(datosGrupo, "tilewidth");
			int altoTiles = getIntDesdeJSON(datosGrupo, "tileheight");
			HojaSprites hoja = new HojaSprites("/imagenes/hojaTexturas/" + nombreImagen, anchoTiles, altoTiles, false);

			int primerSpriteColeccion = getIntDesdeJSON(datosGrupo, "firstgid") - 1;
			int ultimoSpriteColeccion = primerSpriteColeccion + getIntDesdeJSON(datosGrupo, "tilecount") - 1;

			for (int j = 0; j < this.capasSprites.size(); j++) {
				CapaSprites capaActual = this.capasSprites.get(j);
				int[] spritesCapa = capaActual.getArraySprites();

				for (int k = 0; k < spritesCapa.length; k++) {
					int idSpriteActual = spritesCapa[k];
					if (idSpriteActual >= primerSpriteColeccion && idSpriteActual <= ultimoSpriteColeccion) {
						if (paletaSprites[idSpriteActual] == null) {
							paletaSprites[idSpriteActual] = hoja.getSprite(idSpriteActual - primerSpriteColeccion);
						}
					}
				}
			}
		}

		// OBTENER OBJETOS
		objetosMapa = new ArrayList<>();
		JSONArray coleccionObjetos = getArrayJSON(globalJSON.get("objetos").toString());
		for (int i = 0; i < coleccionObjetos.size(); i++) {
			JSONObject datosObjeto = getObjetoJSON(coleccionObjetos.get(i).toString());

			int idObjeto = getIntDesdeJSON(datosObjeto, "id");
			int cantidadObjeto = getIntDesdeJSON(datosObjeto, "cantidad");
			int xObjeto = getIntDesdeJSON(datosObjeto, "x");
			int yObjeto = getIntDesdeJSON(datosObjeto, "y");

			Point posicionObjeto = new Point(xObjeto, yObjeto);
			Objeto objeto = RegistroObjetos.getObjeto(idObjeto);
			ObjetoUnicoTiled objetoUnico = new ObjetoUnicoTiled(posicionObjeto, objeto);
			objetosMapa.add(objetoUnico);
		}

		// Obtener Enemigos
		enemigosMapa = new ArrayList<>();
		JSONArray coleccionEnemigos = getArrayJSON(globalJSON.get("enemigos").toString());
		for (int i = 0; i < coleccionEnemigos.size(); i++) {
			JSONObject datosEnemigo = getObjetoJSON(coleccionEnemigos.get(i).toString());

			int idEnemigo = getIntDesdeJSON(datosEnemigo, "id");
			int xEnemigo = getIntDesdeJSON(datosEnemigo, "x");
			int yEnemigo = getIntDesdeJSON(datosEnemigo, "y");

			Point posicionEnemigo = new Point(xEnemigo, yEnemigo);
			Enemigo enemigo = RegistroEnemigos.getEnemigo(idEnemigo);
			enemigo.establecerPosicion(posicionEnemigo.x, posicionEnemigo.y);

			enemigosMapa.add(enemigo);
		}

		areasColisionPorActualizacion = new ArrayList<>();

	}

	public void actualizar() {
		actualizarAreasColision();
		actualizarRecogidaObjetos();
		actualizarEnemigos();
		actualizarAtaques();

		Point punto = new Point(ElementosPrincipales.jugador.getPosicionXInt(),
				ElementosPrincipales.jugador.getPosicionYInt());
		Point puntoCoincidente = d.getCoordenadasNodoCoincidente(punto);
		d.reiniciarYEvaluar(puntoCoincidente);
	}

	private void actualizarAtaques() {
		if (enemigosMapa.isEmpty() || ElementosPrincipales.jugador.getAlcanceActual().isEmpty()
				|| ElementosPrincipales.jugador.getAlmacenEquipo().obtenerArma1() instanceof Desarmar) {
			return;
		}

		if (GestorControles.teclado.atacando) {
			ArrayList<Enemigo> enemigosAlcanzados = new ArrayList<>();

			if (ElementosPrincipales.jugador.getAlmacenEquipo().obtenerArma1().esPenetrante()) {
				for (Enemigo enemigo : enemigosMapa) {
					if (ElementosPrincipales.jugador.getAlcanceActual().get(0).intersects(enemigo.getArea())) {
						enemigosAlcanzados.add(enemigo);
					}
				}
			} else {
				Enemigo enemigoMasCercano = null;
				Double distanciaMasCercana = null;

				for (Enemigo enemigo : enemigosMapa) {
					if (ElementosPrincipales.jugador.getAlcanceActual().get(0).intersects(enemigo.getArea())) {
						Point puntoJugador = new Point(
								ElementosPrincipales.jugador.getPosicionXInt() / Constantes.LADO_SPRITE,
								ElementosPrincipales.jugador.getPosicionYInt() / Constantes.LADO_SPRITE);

						Point puntoEnemigo = new Point((int) enemigo.getPosicionX(), (int) enemigo.getPosicionY());

						Double distanciaActual = CalculadoraDistancia.getDistanciaEntrePuntos(puntoJugador,
								puntoEnemigo);

						if (enemigoMasCercano == null) {
							enemigoMasCercano = enemigo;
							distanciaMasCercana = distanciaActual;
						} else if (distanciaActual < distanciaMasCercana) {
							enemigoMasCercano = enemigo;
							distanciaMasCercana = distanciaActual;
						}
					}
				}
				enemigosAlcanzados.add(enemigoMasCercano);
			}
			ElementosPrincipales.jugador.getAlmacenEquipo().obtenerArma1().atacar(enemigosAlcanzados);
		}

		Iterator<Enemigo> iterador = enemigosMapa.iterator();

		while (iterador.hasNext()) {
			Enemigo enemigo = iterador.next();

			if (enemigo.getVidaActual() <= 0) {
				iterador.remove();
			}
		}
	}

	private void actualizarRecogidaObjetos() {
		if (!objetosMapa.isEmpty()) {
			final Rectangle areaJugador = new Rectangle(ElementosPrincipales.jugador.getPosicionXInt(),
					ElementosPrincipales.jugador.getPosicionYInt(), Constantes.LADO_SPRITE, Constantes.LADO_SPRITE);
			for (int i = 0; i < objetosMapa.size(); i++) {
				final ObjetoUnicoTiled objetoActual = objetosMapa.get(i);

				final Rectangle posicionObjetoActual = new Rectangle(objetoActual.getPosicion().x,
						objetoActual.getPosicion().y, Constantes.LADO_SPRITE, Constantes.LADO_SPRITE);

				if (areaJugador.intersects(posicionObjetoActual) && GestorControles.teclado.recogiendo) {
					ElementosPrincipales.inventario.recogerObjetos(objetoActual);
					objetosMapa.remove(i);
				}
			}
		}
	}

	private void actualizarAreasColision() {
		if (!areasColisionPorActualizacion.isEmpty()) {
			areasColisionPorActualizacion.clear();
		}

		for (int i = 0; i < areasColisionOriginales.size(); i++) {
			Rectangle rInicial = areasColisionOriginales.get(i);

			int puntoX = rInicial.x - (int) ElementosPrincipales.jugador.getPosicionX() + Constantes.MARGEN_X;
			int puntoY = rInicial.y - (int) ElementosPrincipales.jugador.getPosicionY() + Constantes.MARGEN_Y;

			final Rectangle rFinal = new Rectangle(puntoX, puntoY, rInicial.width, rInicial.height);
			areasColisionPorActualizacion.add(rFinal);
		}
	}

	private void actualizarEnemigos() {
		if (!enemigosMapa.isEmpty()) {
			for (Enemigo enemigo : enemigosMapa) {
				enemigo.cambiarSiguienteNodo(d.encontrarSiguienteNodoParaEnemigo(enemigo));
				enemigo.actualizar(enemigosMapa);
			}
		}
	}

	public void dibujar(Graphics g) {
		for (int i = 0; i < capasSprites.size(); i++) {
			int[] spritesCapa = capasSprites.get(i).getArraySprites();

			for (int y = 0; y < altoMapaEnTiles; y++) {
				for (int x = 0; x < anchoMapaEnTiles; x++) {
					int idSpriteActual = spritesCapa[x + y * anchoMapaEnTiles];
					if (idSpriteActual != -1) {
						int puntoX = x * Constantes.LADO_SPRITE - (int) ElementosPrincipales.jugador.getPosicionX()
								+ Constantes.MARGEN_X;
						int puntoY = y * Constantes.LADO_SPRITE - (int) ElementosPrincipales.jugador.getPosicionY()
								+ Constantes.MARGEN_Y;
						if (puntoX < 0 - Constantes.LADO_SPRITE || puntoX > Constantes.ANCHO_JUEGO
								|| puntoY < 0 - Constantes.LADO_SPRITE || puntoY > Constantes.ALTO_JUEGO - 65) {
							continue;
						}
						DibujoDebug.dibujarImagen(g, paletaSprites[idSpriteActual].getImagen(), puntoX, puntoY);
					}
				}
			}
		}

		for (int i = 0; i < objetosMapa.size(); i++) {
			ObjetoUnicoTiled objetoActual = objetosMapa.get(i);
			int puntoX = objetoActual.getPosicion().x - (int) ElementosPrincipales.jugador.getPosicionX()
					+ Constantes.MARGEN_X;
			int puntoY = objetoActual.getPosicion().y - (int) ElementosPrincipales.jugador.getPosicionY()
					+ Constantes.MARGEN_Y;
			DibujoDebug.dibujarImagen(g, objetoActual.getObjeto().getSprites().getImagen(), puntoX, puntoY);
		}

		for (int i = 0; i < enemigosMapa.size(); i++) {
			Enemigo enemigo = enemigosMapa.get(i);
			int puntoX = (int) enemigo.getPosicionX() - (int) ElementosPrincipales.jugador.getPosicionX()
					+ Constantes.MARGEN_X;
			int puntoY = (int) enemigo.getPosicionY() - (int) ElementosPrincipales.jugador.getPosicionY()
					+ Constantes.MARGEN_Y;
			enemigo.dibujar(g, puntoX, puntoY);
		}
	}

	private JSONObject getObjetoJSON(final String codigoJSON) {
		JSONParser lector = new JSONParser();
		JSONObject objetoJSON = null;

		try {
			Object recuperado = lector.parse(codigoJSON);
			objetoJSON = (JSONObject) recuperado;
		} catch (ParseException e) {
			System.out.println("Posicion: " + e.getPosition());
			System.out.println(e);
		}

		return objetoJSON;
	}

	private JSONArray getArrayJSON(final String codigoJSON) {
		JSONParser lector = new JSONParser();
		JSONArray arrayJSON = null;

		try {
			Object recuperado = lector.parse(codigoJSON);
			arrayJSON = (JSONArray) recuperado;
		} catch (ParseException e) {
			System.out.println("Posicion: " + e.getPosition());
			System.out.println(e);
		}

		return arrayJSON;
	}

	private int getIntDesdeJSON(final JSONObject objetoJSON, final String clave) {
		return Integer.parseInt(objetoJSON.get(clave).toString());
	}

	public Point getPosicionInicial() {
		return puntoInicial;
	}

	public Rectangle getBordes(final int posicionX, final int posicionY) {
		int x = Constantes.MARGEN_X - posicionX + ElementosPrincipales.jugador.getAncho();
		int y = Constantes.MARGEN_Y - posicionY + ElementosPrincipales.jugador.getAlto();
		int ancho = this.anchoMapaEnTiles * Constantes.LADO_SPRITE - ElementosPrincipales.jugador.getAncho() * 2;
		int alto = this.altoMapaEnTiles * Constantes.LADO_SPRITE - ElementosPrincipales.jugador.getAlto() * 2;
		return new Rectangle(x, y, ancho, alto);
	}
}