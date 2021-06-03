package co.com.japl.ea.exceptions;
/**
 * Se encarga de usar la excepcion de consultas
 * @author alejandro parra
 * @since 06/05/2018
 */
public class IngresoException extends AExceptions {
	private static final long serialVersionUID = -4449562830017446555L;
	public IngresoException(String mensaje,Throwable e) {
		super(mensaje,e);
		setMensage(mensaje);
		setE(e);
	}
	public IngresoException(String mensaje) {
		super(mensaje);
		setMensage(mensaje);
	}
}
