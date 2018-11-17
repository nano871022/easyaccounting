package org.pyt.common.exceptions;
/**
 * Se encarga de controlar las excepciones del cervicio de centro de costos
 * @author alejandro parra
 * @since 06/05/2018
 */
public class CentroCostosException extends AExceptions {
	private static final long serialVersionUID = 3296613334965425872L;

	public CentroCostosException(String mensaje, Throwable e) {
		super(mensaje, e);
		setMensage(mensaje);
		setE(e);
	}

	public CentroCostosException(String mensaje) {
		super(mensaje);
		setMensage(mensaje);
	}

}
