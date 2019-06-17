package appAdictive.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Estadisticas {

	/**
	 * Cantidad de rutinas oficiales creadas esta semana
	 */
	private int oficiales;

	/**
	 * Cantidad de rutinas publicas creadas esta semana
	 */
	private int publicas;

	/**
	 * Cantidad de rutinas privadas creadas esta semana
	 */
	private int privadas;
	
	/**
	 * Cantidad de usuarios totales sin baneo
	 */
	private int baneado;
	
	/**
	 * Cantidad de usuarios totales baneados
	 */
	private int noBaneado;
}
