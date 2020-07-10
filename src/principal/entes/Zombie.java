package principal.entes;

import java.awt.Graphics;

import principal.Constantes;
import principal.herramientas.DibujoDebug;
import principal.sprites.HojaSprites;

public class Zombie extends Enemigo {

	private static HojaSprites hojaZombie;

	public Zombie(int idEnemigo, String nombre, int vidaMaxima, final String rutaLamento) {
		super(idEnemigo, nombre, vidaMaxima, rutaLamento);

		if (hojaZombie == null) {
			hojaZombie = new HojaSprites(Constantes.RUTA_ENEMIGOS + idEnemigo + ".png", Constantes.LADO_SPRITE, false);
		}
	}

	public void dibujar(final Graphics g, final int puntoX, final int puntoY) {
		DibujoDebug.dibujarImagen(g, hojaZombie.getSprite(0).getImagen(), puntoX, puntoY);
		super.dibujar(g, puntoX, puntoY);
	}
}
