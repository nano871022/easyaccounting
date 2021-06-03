package co.com.japl.ea.exceptions;

/**
 * Se encarga de controlar las excepciones de empresas
 * 
 * @author alejandro parra
 * @since 06/05/2018
 */
public class MarcadorServicioException extends AExceptions {

	private static final long serialVersionUID = 2767550412983144964L;

	public MarcadorServicioException(String mensaje, Throwable e) {
		super(mensaje, e);
		setMensage(mensaje);
		setE(e);
	}
	
	public MarcadorServicioException( Throwable e) {
		super(e);
		setE(e);
	}

	public MarcadorServicioException(String mensaje) {
		super(mensaje);
		setMensage(mensaje);
	}
}
