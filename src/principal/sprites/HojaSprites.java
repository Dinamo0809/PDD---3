package principal.sprites;

import java.awt.image.BufferedImage;

import principal.herramientas.CargadorRecursos;

public class HojaSprites {

	final private int anchoPixeles;
	final private int altoPixeles;

	final private int anchoHojaSprites;
	final private int altoHojaSprites;

	final private int anchoSprites;
	final private int altoSprites;

	private Sprites[] sprites;

	public HojaSprites(final String ruta, final int ladoSprites, final boolean hojaOpaca) {
		BufferedImage imagen;

		if (hojaOpaca) {
			imagen = CargadorRecursos.cargarImagenCompatibleOpaca(ruta);
		} else {
			imagen = CargadorRecursos.cargarImagenCompatibleTranslucida(ruta);
		}

		anchoPixeles = imagen.getWidth();
		altoPixeles = imagen.getHeight();

		anchoHojaSprites = anchoPixeles / ladoSprites;
		altoHojaSprites = altoPixeles / ladoSprites;

		anchoSprites = ladoSprites;
		altoSprites = ladoSprites;

		sprites = new Sprites[anchoHojaSprites * altoHojaSprites];

		rellenarSpritesImagen(imagen);
	}

	public HojaSprites(final String ruta, final int anchoSprites, final int altoSprites, final boolean hojaOpaca) {
		BufferedImage imagen;

		if (hojaOpaca) {
			imagen = CargadorRecursos.cargarImagenCompatibleOpaca(ruta);
		} else {
			imagen = CargadorRecursos.cargarImagenCompatibleTranslucida(ruta);
		}

		anchoPixeles = imagen.getWidth();
		altoPixeles = imagen.getHeight();

		anchoHojaSprites = anchoPixeles / anchoSprites;
		altoHojaSprites = altoPixeles / altoSprites;

		this.anchoSprites = anchoSprites;
		this.altoSprites = altoSprites;

		sprites = new Sprites[anchoHojaSprites * altoHojaSprites];

		rellenarSpritesImagen(imagen);
	}

	private void rellenarSpritesImagen(final BufferedImage imagen) {
		for (int y = 0; y < altoHojaSprites; y++) {
			for (int x = 0; x < anchoHojaSprites; x++) {
				final int posicionX = x * anchoSprites;
				final int posicionY = y * altoSprites;

				sprites[x + y * anchoHojaSprites] = new Sprites(
						imagen.getSubimage(posicionX, posicionY, anchoSprites, altoSprites));
			}
		}
	}

	public Sprites getSprite(final int indice) {
		return sprites[indice];
	}

	public Sprites getSprite(final int x, final int y) {
		return sprites[x + y * anchoHojaSprites];
	}
}