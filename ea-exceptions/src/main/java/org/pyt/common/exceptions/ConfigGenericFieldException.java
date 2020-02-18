package org.pyt.common.exceptions;
/**
 * Se encarga de manejar los errores del servicio de cuenta contables
 * @author Alejandro Parra
 * @since 012/07/2018
 */
public class ConfigGenericFieldException extends AExceptions {
	private static final long serialVersionUID = 75036388547637118L;
	public ConfigGenericFieldException(String mensaje) {
		super(mensaje);
	}
	public ConfigGenericFieldException(String mensaje,Throwable e) {
		super(mensaje,e);
	}
	
	public ConfigGenericFieldException(Throwable e) {
		super(e);
	}

}
