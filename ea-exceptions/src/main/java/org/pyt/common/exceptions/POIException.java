package org.pyt.common.exceptions;
/**
 * Se encarga de manejar los errores de POI
 * @author Alejandro Parra
 * @since 09/11/2018
 */
public class POIException extends AExceptions {
	private static final long serialVersionUID = 75036388547637118L;
	public POIException(String mensaje) {
		super(mensaje);
	}
	public POIException(String mensaje,Throwable e) {
		super(mensaje,e);
	}
	
	public POIException(Throwable e) {
		super(e);
	}

}
