package org.pyt.common.exceptions;
/**
 * Se encarga de manejar los errores del servicio de cuenta contables
 * @author Alejandro Parra
 * @since 012/07/2018
 */
public class CuentaContableException extends AExceptions {
	private static final long serialVersionUID = 75036388547637118L;
	public CuentaContableException(String mensaje) {
		super(mensaje);
	}
	public CuentaContableException(String mensaje,Throwable e) {
		super(mensaje,e);
	}
	
	public CuentaContableException(Throwable e) {
		super(e);
	}

}
