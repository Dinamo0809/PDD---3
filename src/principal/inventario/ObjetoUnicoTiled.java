package principal.inventario;

import java.awt.Point;

public class ObjetoUnicoTiled {

	private Point posicion;
	private Objeto objeto;

	public ObjetoUnicoTiled(Point posicion, Objeto objeto) {
		this.posicion = posicion;
		this.objeto = objeto;
	}

	public Point getPosicion() {
		return posicion;
	}

	public Objeto getObjeto() {
		return objeto;
	}
}
