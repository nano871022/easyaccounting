package org.pyt.common.common;

/**
 * Se encarga de manejar el log de l aaplicacion y es transeversal a toda la
 * aplicacion.
 * 
 * @author Alejandro Parra
 * @since 2018-05-24
 */
public final class Log {
	/**
	 * Se encaga de cargar un mensaje en el log
	 * 
	 * @param mensaje
	 *            {@link String}
	 */
	public final static void logger(String mensaje) {
		System.out.println(mensaje);
	}

	/**
	 * Se encarga de cargar un error en el log
	 * 
	 * @param error
	 *            {@link Exception}
	 */
	public final static <T extends Exception> void logger(T error) {
		System.err.println(error);
	}

	/**
	 * Se encarga de cargar un error en el log con mensaje adicional
	 * 
	 * @param mensaje
	 *            {@link String}
	 * @param error
	 *            {@link Exception}
	 */
	public final static <T extends Exception> void logger(String mensaje, T error) {
		System.err.println(error);
	}
}
