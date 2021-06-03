package co.com.japl.ea.exceptions;
/**
 * Se encarga de controlar las excepciones del servicio de empleados
 * @author alejandro parra
 * @since 06/05/2018
 */
public class ProccesConfigServiceException extends AExceptions {
	private static final long serialVersionUID = 5682846993312424378L;

	public ProccesConfigServiceException(String mensaje, Throwable e) {
		super(mensaje, e);
		setMensage(mensaje);
		setE(e);
	}
	public ProccesConfigServiceException(String mensaje) {
		super(mensaje);
		setMensage(mensaje);
	}

}
