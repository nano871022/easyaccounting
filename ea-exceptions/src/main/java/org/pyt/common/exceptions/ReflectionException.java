package org.pyt.common.exceptions;

/**
 * Se encarga de manejar lass excepciones para los errores generrados por
 * refleccion.
 * 
 * @author Alejandro Parra
 * @since 2018-05-19
 */
public class ReflectionException extends RuntimeException {

	private static final long serialVersionUID = -1647410075727585247L;

	public ReflectionException(String mensaje, Throwable e) {
		super(mensaje, e);
	}

	public ReflectionException(String mensaje) {
		super(mensaje);
	}

}
