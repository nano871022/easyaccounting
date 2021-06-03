package co.com.japl.ea.exceptions.inventario;

import co.com.japl.ea.exceptions.AExceptions;

/**
 * Ser encarga de controlar la excepciones del serivicos de servicios
 * 
 * @author alejandro parra
 * @since 06/05/2018
 */
public class AlmacenException extends AExceptions {

	private static final long serialVersionUID = 5962743388991994735L;

	public AlmacenException(String mensaje, Throwable e) {
		super(mensaje, e);
		setMensage(mensaje);
		setE(e);
	}

	public AlmacenException(String mensaje) {
		super(mensaje);
		setMensage(mensaje);
	}
}
