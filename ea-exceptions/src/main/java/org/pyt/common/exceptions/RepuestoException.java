package org.pyt.common.exceptions;
/**
 * Se encarga de usar la excepcion de consultas
 * @author alejandro parra
 * @since 06/05/2018
 */
public class RepuestoException extends AExceptions {
	private static final long serialVersionUID = -4449562830017446555L;
	public RepuestoException(String mensaje,Throwable e) {
		super(mensaje,e);
		setMensage(mensaje);
		setE(e);
	}
	public RepuestoException(String mensaje) {
		super(mensaje);
		setMensage(mensaje);
	}
}
