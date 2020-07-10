package principal.control;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public final class Teclado implements KeyListener {

	public Tecla arriba = new Tecla();
	public Tecla abajo = new Tecla();
	public Tecla izquierda = new Tecla();
	public Tecla derecha = new Tecla();
	public boolean correr = false;
	public boolean debug = false;
	public boolean recogiendo = false;
	public boolean inventarioActivo = false;
	public boolean atacando = false;

	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_W:
			arriba.teclaPulsada();
			break;
		case KeyEvent.VK_S:
			abajo.teclaPulsada();
			break;
		case KeyEvent.VK_A:
			izquierda.teclaPulsada();
			break;
		case KeyEvent.VK_D:
			derecha.teclaPulsada();
			break;
		case KeyEvent.VK_SHIFT:
			correr = true;
			break;
		case KeyEvent.VK_ESCAPE:
			System.exit(0);
		case KeyEvent.VK_F1:
			debug = !debug;
			break;
		case KeyEvent.VK_Q:
			recogiendo = true;
			break;
		case KeyEvent.VK_E:
			inventarioActivo = !inventarioActivo;
			break;
		case KeyEvent.VK_ENTER:
			atacando = true;
			break;

		}
	}

	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_W:
			arriba.teclaLiberada();
			break;
		case KeyEvent.VK_S:
			abajo.teclaLiberada();
			break;
		case KeyEvent.VK_A:
			izquierda.teclaLiberada();
			break;
		case KeyEvent.VK_D:
			derecha.teclaLiberada();
			break;
		case KeyEvent.VK_SHIFT:
			correr = false;
			break;
		case KeyEvent.VK_Q:
			recogiendo = false;
			break;
		case KeyEvent.VK_ENTER:
			atacando = false;
			break;
		}
	}

	public void keyTyped(KeyEvent arg0) {
	}
}
