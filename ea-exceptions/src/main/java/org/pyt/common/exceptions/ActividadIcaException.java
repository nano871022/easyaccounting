package org.pyt.common.exceptions;

/**
 * Se genera una excepcion para el servicio de actividad ica
 * 
 * @author alejandro parra
 * @since 06/05/2018
 */
public class ActividadIcaException extends AExceptions {
	private static final long serialVersionUID = -1484725946678861602L;

	public ActividadIcaException(String mensaje, Throwable e) {
		super(mensaje, e);
		this.setMensage(mensaje);
		this.setE(e);
	}

	public ActividadIcaException(String mensaje) {
		super(mensaje);
		this.setMensage(mensaje);
	}
}
