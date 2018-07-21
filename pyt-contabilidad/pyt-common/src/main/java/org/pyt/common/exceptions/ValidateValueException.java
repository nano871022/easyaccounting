package org.pyt.common.exceptions;

/**
 * Se encarga del control de errores de la clase de validacion de valores
 * 
 * @author Alejandro Parra
 * @since 22-06-2018
 */
public class ValidateValueException extends AExceptions {

	private static final long serialVersionUID = 6148680238327275683L;

	public ValidateValueException(String mensaje) {
		super(mensaje);
	}

	public ValidateValueException(String mensaje, Throwable e) {
		super(mensaje, e);
	}

}
