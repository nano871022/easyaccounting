package co.com.japl.ea.exceptions;

/**
 * Ser encarga de controlar la excepciones del serivicos de servicios
 * 
 * @author alejandro parra
 * @since 06/05/2018
 */
public class GenericServiceException extends AExceptions {

	private static final long serialVersionUID = 5962743388991994735L;

	public GenericServiceException(String mensaje, Throwable e) {
		super(mensaje, e);
		setMensage(mensaje);
		setE(e);
	}

	public GenericServiceException(String mensaje) {
		super(mensaje);
		setMensage(mensaje);
	}
}
