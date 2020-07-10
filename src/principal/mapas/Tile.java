package principal.mapas;

import java.awt.Rectangle;

import principal.sprites.Sprites;

public class Tile {

	private final Sprites sprite;
	private final int id;
	private boolean solido;

	public Tile(final Sprites sprite, final int id) {
		this.sprite = sprite;
		this.id = id;
		solido = false;
	}

	public Tile(final Sprites sprite, final int id, final boolean solido) {
		this.sprite = sprite;
		this.id = id;
		this.solido = solido;
	}

	public Sprites getSprite() {
		return sprite;
	}

	public int getId() {
		return id;
	}

	public void establecerSolid(final boolean solido) {
		this.solido = solido;
	}

	public Rectangle getLimites(final int x, final int y) {
		return new Rectangle(x, y, sprite.getAncho(), sprite.getAlto());
	}
}
