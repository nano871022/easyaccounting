package org.pyt.common.exceptions;

public class FXMLFileNotFoundException extends AExceptions {

	private static final long serialVersionUID = 7483696997385115352L;

	public FXMLFileNotFoundException(String mensaje) {
		super(mensaje);
	}

	public FXMLFileNotFoundException(String mensaje, Throwable e) {
		super(mensaje, e);
	}
}
