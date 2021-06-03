package co.com.japl.ea.exceptions;
/**
 * Excepcion usada para el objeto de busqueda sobre la lista de servicios
 * @author alejandro parra
 * @since 27/12/2018
 */
public class SearchServicesException extends AExceptions {
	private static final long serialVersionUID = -4449562830017446555L;
	public SearchServicesException(String mensaje,Throwable e) {
		super(mensaje,e);
		setMensage(mensaje);
		setE(e);
	}
	public SearchServicesException(String mensaje) {
		super(mensaje);
		setMensage(mensaje);
	}
}
