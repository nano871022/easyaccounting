package co.com.japl.ea.exceptions;

/**
 * Para manejo de errores de la la clase de carga de aplicaciones fxml
 * 
 * @author Alejandro Parra
 * @since 2018-05-22
 */
public class LoadAppFxmlException extends AExceptions {

	private static final long serialVersionUID = 2105134170297249377L;

	public LoadAppFxmlException(String mensaje) {
		super(mensaje);
	}

	public LoadAppFxmlException(String mensaje, Throwable e) {
		super(mensaje, e);
	}
}
