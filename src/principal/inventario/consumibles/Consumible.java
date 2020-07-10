package principal.inventario.consumibles;

import principal.Constantes;
import principal.inventario.Objeto;
import principal.sprites.HojaSprites;
import principal.sprites.Sprites;

public class Consumible extends Objeto {

	public static HojaSprites hojaConsumibles = new HojaSprites(Constantes.RUTA_OBJETOS, Constantes.LADO_SPRITE, false);

	public Consumible(int id, String nombre, String descripcion, int cantidad) {
		super(id, nombre, descripcion, cantidad);
	}

	public Consumible(int id, String nombre, String descripcion) {
		super(id, nombre, descripcion);
	}

	public Sprites getSprites() {
		return hojaConsumibles.getSprite(id);
	}

}
