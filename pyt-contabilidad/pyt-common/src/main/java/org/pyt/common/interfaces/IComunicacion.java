package org.pyt.common.interfaces;

/**
 * Se encarga de comunicar los objetos que implemente esta interfaz
 * 
 * @author Alejandro Parra
 * @since 2018-05-25
 */
public interface IComunicacion {
	/**
	 * Recibe el comando y el valor al que el suscriptor desea informarse.
	 * 
	 * @param comando {@link String}
	 * @param valor {@link Object}
	 */
	public <T extends Object> void get(String comando, T valor);
}
