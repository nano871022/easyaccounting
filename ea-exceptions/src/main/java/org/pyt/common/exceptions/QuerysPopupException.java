package org.pyt.common.exceptions;
/**
 * Excepcion para ser usada sobre servicio de querys popup
 * @author Ingeneo
 *
 */
public class QuerysPopupException extends AExceptions {

	private static final long serialVersionUID = 5313575734193846514L;

	public QuerysPopupException(String mensaje) {
		super(mensaje);
	}

	public QuerysPopupException(String mensaje, Throwable e) {
		super(mensaje, e);
	}

	public QuerysPopupException(Throwable e) {
		super(e);
	}

}
