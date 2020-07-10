package principal.herramientas;

import java.awt.FontMetrics;
import java.awt.Graphics;

public class MedidorString {

	public static int medirAnchoPixeles(final Graphics g, final String s) {

		FontMetrics fm = g.getFontMetrics();

		return fm.stringWidth(s);
	}

	public static int medirAltoPixeles(final Graphics g, final String s) {

		FontMetrics fm = g.getFontMetrics();

		return (int) fm.getLineMetrics(s, g).getHeight();
	}
}