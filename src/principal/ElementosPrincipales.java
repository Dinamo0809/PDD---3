package principal;

import principal.entes.Jugador;
import principal.inventario.Inventario;
import principal.mapas.MapaTiled;

public class ElementosPrincipales {

	public static MapaTiled mapa = new MapaTiled("/mapas/mapa-apocaliptico.json");
	public static Jugador jugador = new Jugador();
	public static Inventario inventario = new Inventario();
}
