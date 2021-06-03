package co.com.japl.ea.exceptions;

/**
 * Se encarga de manejar las excepciones generadas por el servicio de bancos
 * 
 * @author alejandro parra
 * @since 06/05/2018
 */
public class BancoException extends AExceptions {
	private static final long serialVersionUID = -6166722235534165307L;

	public BancoException(String mensaje) {
		super(mensaje);
		setMensage(mensaje);
	}
	public BancoException(String mensaje,Throwable e) {
		super(mensaje);
		setMensage(mensaje);
		setE(e);
	}

}
