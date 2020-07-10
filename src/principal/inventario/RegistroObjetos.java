package principal.inventario;

import principal.inventario.armas.Desarmar;
import principal.inventario.armas.Pistola;
import principal.inventario.consumibles.Consumible;

public class RegistroObjetos {

	public static Objeto getObjeto(final int idObjeto) {

		Objeto objeto = null;

		switch (idObjeto) {
		// 0-499: objeots consumibles
		case 0:
			objeto = new Consumible(idObjeto, "Medkit", "");
			break;
		case 1:
			objeto = new Consumible(idObjeto, "Balas", "");
			break;
		case 2:
			objeto = new Consumible(idObjeto, "Pill", "");
			break;
		case 3:
			objeto = new Consumible(idObjeto, "Super Medkit", "");
			break;
		case 4:
			objeto = new Consumible(idObjeto, "Super Balas", "");
			break;
		case 5:
			objeto = new Consumible(idObjeto, "Super Pill", "");
			break;

		// 500-599: armas
		case 500:
			objeto = new Pistola(idObjeto, "Handgun", "", 2, 4, false, true, 1);
			break;
//		case 501:
//			objeto = new Pistola(idObjeto, "Escopeta", "", 3, 5, true, true, 0.4);
//			break;
//		case 502:
//			objeto = new Pistola(idObjeto, "Rifle", "", 5, 7, true, false, 1);
//			break;
//		case 503:
//			objeto = new Pistola(idObjeto, "Rifle Pesado", "", 7, 9, true, true, 0.4);
//			break;
		case 599:
			objeto = new Desarmar(idObjeto, "Desarmado", "", 0, 0, false, false, 0);
		}

		return objeto;
	}
}
