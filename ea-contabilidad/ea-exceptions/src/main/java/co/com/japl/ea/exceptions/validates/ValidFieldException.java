package co.com.japl.ea.exceptions.validates;

import co.com.japl.ea.exceptions.AExceptions;

/**
 * Se encarga del control de errores de la clase de validacion de valores
 * 
 * @author Alejandro Parra
 * @since 22-06-2018
 */
public class ValidFieldException extends AExceptions {

	private static final long serialVersionUID = 6148680238327275683L;

	public ValidFieldException(String mensaje) {
		super(mensaje);
	}

	public ValidFieldException(String mensaje, Throwable e) {
		super(mensaje, e);
	}

}
