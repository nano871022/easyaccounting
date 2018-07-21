package org.pyt.common.exceptions;

/**
 * Se encarga o de manejar las excepciones de la lectura y escritura de archivos
 * binarios
 * 
 * @author Alejandro Parra
 * @since 22-06-2018
 *
 */
public class FileBinException extends AExceptions {
	private static final long serialVersionUID = -4347723679916437549L;

	public FileBinException(String mensaje) {
		super(mensaje);
	}

	public FileBinException(String mensaje, Throwable e) {
		super(mensaje, e);
	}
}
