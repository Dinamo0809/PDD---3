package principal.maquinaestado.estados.menujuego;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import principal.Constantes;
import principal.ElementosPrincipales;
import principal.GestorPrincipal;
import principal.graficos.SuperficieDibujo;
import principal.herramientas.DibujoDebug;
import principal.herramientas.EscaladorElementos;
import principal.herramientas.MedidorString;
import principal.inventario.Objeto;
import principal.inventario.armas.Arma;
import principal.inventario.armas.Desarmar;

public class MenuEquipo extends SeccionMenu {

	final Rectangle panelObjetos = new Rectangle(em.FONDO.x + margenGeneral,
			barraPeso.y + barraPeso.height + margenGeneral, 248,
			Constantes.ALTO_JUEGO - barraPeso.y - barraPeso.height - margenGeneral * 2);
	final Rectangle tituloO = new Rectangle(panelObjetos.x, panelObjetos.y, panelObjetos.width, 24);

	final Rectangle panelEquipo = new Rectangle(panelObjetos.x + panelObjetos.width + margenGeneral, panelObjetos.y, 88,
			panelObjetos.height);
	final Rectangle tituloE = new Rectangle(panelEquipo.x, panelEquipo.y, panelEquipo.width, 24);

	final Rectangle panelAtributos = new Rectangle(panelEquipo.x + panelEquipo.width + margenGeneral, panelEquipo.y,
			132, panelEquipo.height);
	final Rectangle tituloA = new Rectangle(panelAtributos.x, panelAtributos.y, panelAtributos.width, 24);

	final Rectangle ArmaTag = new Rectangle(tituloE.x + margenGeneral, tituloE.y + tituloE.height + margenGeneral,
			tituloE.width - margenGeneral * 2,
			margenGeneral * 2 + MedidorString.medirAltoPixeles(GestorPrincipal.getSd().getGraphics(), "Arma"));
	final Rectangle contenedorArma = new Rectangle(ArmaTag.x + 1, ArmaTag.y + ArmaTag.height, ArmaTag.width - 2,
			Constantes.LADO_SPRITE);

	Objeto objetoSeleccionado = null;

	public MenuEquipo(String nombreSeccion, Rectangle etiquetaMenu, EstructuraMenu em) {
		super(nombreSeccion, etiquetaMenu, em);
	}

	public void actualizar() {
		actualizarPosicionesMenu();
		actualizarSeleccionRaton();
		actualizarObjetoSeleccionado();
	}

	private void actualizarPosicionesMenu() {
		if (ElementosPrincipales.inventario.getArmas().isEmpty()) {
			return;
		}

		for (int i = 0; i < ElementosPrincipales.inventario.getArmas().size(); i++) {
			final Point puntoInicial = new Point(tituloO.x + margenGeneral, tituloO.y + tituloO.height + margenGeneral);

			final int lado = Constantes.LADO_SPRITE;

			int idActual = ElementosPrincipales.inventario.getArmas().get(i).getId();

			ElementosPrincipales.inventario.getObjeto(idActual).establecerPosicionMenu(
					new Rectangle(puntoInicial.x + i * (lado + margenGeneral), puntoInicial.y, lado, lado));
		}
	}

	private void actualizarSeleccionRaton() {
		Rectangle posicionRaton = GestorPrincipal.getSd().getRaton().getRectanguloPosicion();

		if (posicionRaton.intersects(EscaladorElementos.escalarRectanguloArriba(panelObjetos))) {
			if (ElementosPrincipales.inventario.getArmas().isEmpty()) {
				return;
			}

			for (Objeto objeto : ElementosPrincipales.inventario.getArmas()) {
				if (GestorPrincipal.getSd().getRaton().getClick() && posicionRaton
						.intersects(EscaladorElementos.escalarRectanguloArriba(objeto.getPosicionMenu()))) {
					objetoSeleccionado = objeto;
				}
			}
		} else if (posicionRaton.intersects(EscaladorElementos.escalarRectanguloArriba(panelEquipo))) {
			if (objetoSeleccionado != null && objetoSeleccionado instanceof Arma
					&& GestorPrincipal.getSd().getRaton().getClick()
					&& posicionRaton.intersects(EscaladorElementos.escalarRectanguloArriba(contenedorArma))) {
				ElementosPrincipales.jugador.getAlmacenEquipo().cambiarArma1((Arma) objetoSeleccionado);
				objetoSeleccionado = null;
			}
		} else if (posicionRaton.intersects(EscaladorElementos.escalarRectanguloArriba(panelAtributos))) {

		}
	}

	private void actualizarObjetoSeleccionado() {
		if (objetoSeleccionado != null) {
			if (GestorPrincipal.getSd().getRaton().getClick2()) {
				objetoSeleccionado = null;
				return;
			}
			Point posicionRaton = EscaladorElementos
					.escalarPuntoAbajo(GestorPrincipal.getSd().getRaton().getPuntoPosicion());
			objetoSeleccionado.establecerPosicionFlotante(
					new Rectangle(posicionRaton.x, posicionRaton.y, Constantes.LADO_SPRITE, Constantes.LADO_SPRITE));
		}
	}

	public void dibujar(Graphics g, SuperficieDibujo sd, EstructuraMenu em) {
		dibujarLimitePeso(g);
		dibujarPaneles(g);

		if (sd.getRaton().getRectanguloPosicion().intersects(EscaladorElementos.escalarRectanguloArriba(barraPeso))) {
			dibujarTooltipPeso(g, sd, em);
		}
	}

	private void dibujarPaneles(final Graphics g) {
		dibujarPanelObjetos(g, panelObjetos, tituloO, "Objetos");
		dibujarPanelEquipo(g, panelEquipo, tituloE, "Equipar");
		dibujarPanelAtributos(g, panelAtributos, tituloA, "Atributos");
	}

	private void dibujarPanelObjetos(final Graphics g, final Rectangle panel, final Rectangle PanelTitulo,
			final String nombre) {
		dibujarPanel(g, panel, PanelTitulo, nombre);
		// dibujar objetos
		dibujarElementosEquipables(g, panel, PanelTitulo);
	}

	private void dibujarElementosEquipables(final Graphics g, final Rectangle panelObjetos,
			final Rectangle PanelTitulo) {
		if (ElementosPrincipales.inventario.getArmas().isEmpty()) {
			return;
		}

		final Point puntoInicial = new Point(PanelTitulo.x + margenGeneral,
				PanelTitulo.y + PanelTitulo.height + margenGeneral);

		final int lado = Constantes.LADO_SPRITE;

		for (int i = 0; i < ElementosPrincipales.inventario.getArmas().size(); i++) {

			int idActual = ElementosPrincipales.inventario.getArmas().get(i).getId();
			Objeto objetoActual = ElementosPrincipales.inventario.getObjeto(idActual);
			DibujoDebug.dibujarImagen(g, objetoActual.getSprites().getImagen(), objetoActual.getPosicionMenu().x,
					objetoActual.getPosicionMenu().y);

			g.setColor(Color.black);
			DibujoDebug.dibujarRectanguloRelleno(g, puntoInicial.x + i * (lado + margenGeneral) + lado - 12,
					puntoInicial.y + 32 - 8, 12, 8);

			String texto = "";
			g.setColor(Color.white);
			if (objetoActual.getCantidad() < 10) {
				texto = "0" + objetoActual.getCantidad();
			} else {
				texto = "" + objetoActual.getCantidad();
			}

			g.setFont(g.getFont().deriveFont(10f));
			DibujoDebug.dibujarString(g, texto,
					puntoInicial.x + i * (lado + margenGeneral) + lado - MedidorString.medirAnchoPixeles(g, texto),
					puntoInicial.y + 31);
		}

		g.setFont(g.getFont().deriveFont(12f));

		if (objetoSeleccionado != null) {
			DibujoDebug.dibujarImagen(g, objetoSeleccionado.getSprites().getImagen(),
					new Point(objetoSeleccionado.getPosicionFlotante().x, objetoSeleccionado.getPosicionFlotante().y));
		}
	}

	private void dibujarPanelEquipo(final Graphics g, final Rectangle panel, final Rectangle PanelTitulo,
			final String nombre) {
		dibujarPanel(g, panel, PanelTitulo, nombre);
		// dibujar ranuras equipables
		g.setColor(Color.black);
		DibujoDebug.dibujarRectanguloRelleno(g, ArmaTag);
		DibujoDebug.dibujarRectanguloContorno(g, contenedorArma);

		if (!(ElementosPrincipales.jugador.getAlmacenEquipo().obtenerArma1() instanceof Desarmar)) {
			Point coordenadaImagen = new Point(contenedorArma.x + contenedorArma.width / 2 - Constantes.LADO_SPRITE / 2,
					contenedorArma.y);

			DibujoDebug.dibujarImagen(g,
					ElementosPrincipales.jugador.getAlmacenEquipo().obtenerArma1().getSprites().getImagen(),
					coordenadaImagen);
		}

		g.setColor(Color.white);
		DibujoDebug.dibujarString(g, "Arma",
				new Point(ArmaTag.x + ArmaTag.width / 2 - MedidorString.medirAnchoPixeles(g, "Arma") / 2,
						ArmaTag.y + ArmaTag.height / 2 + MedidorString.medirAltoPixeles(g, "Arma") / 2));
		// dibujar arma equipada (si la hay)
	}

	private void dibujarPanelAtributos(final Graphics g, final Rectangle panel, final Rectangle PanelTitulo,
			final String nombre) {
		dibujarPanel(g, panel, PanelTitulo, nombre);
		// dibujar atributos
	}

	private void dibujarPanel(final Graphics g, final Rectangle panel, final Rectangle PanelTitulo,
			final String nombre) {
		g.setColor(new Color(0xffff0000));
		DibujoDebug.dibujarRectanguloContorno(g, panel);
		DibujoDebug.dibujarRectanguloRelleno(g, PanelTitulo);
		g.setColor(Color.white);
		DibujoDebug.dibujarString(g, nombre,
				new Point(PanelTitulo.x + PanelTitulo.width / 2 - MedidorString.medirAnchoPixeles(g, nombre) / 2,
						PanelTitulo.y + PanelTitulo.height - MedidorString.medirAltoPixeles(g, nombre) / 2 - 4));
	}

	public Objeto getObjetoSeleccionado() {
		return objetoSeleccionado;
	}

	public void eliminarObjetoSeleccionado() {
		objetoSeleccionado = null;
	}
}
