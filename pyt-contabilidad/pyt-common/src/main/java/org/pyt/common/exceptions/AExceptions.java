package org.pyt.common.exceptions;

/**
 * Se encarag de realizar la abstraccion de la excepcion para ser usada en las
 * excepciones a usar
 * 
 * @author alejandro parra
 * @since 06/05/2018
 */
public abstract class AExceptions extends Exception {
	private static final long serialVersionUID = 7911296941097452891L;
	private String mensage;
	private Throwable e;
	
	public AExceptions(String mensaje,Throwable e) {
		super(mensaje,e);
	}
	public AExceptions(String mensaje) {
		super(mensaje);
	}
	public String getMensage() {
		return mensage;
	}
	public void setMensage(String mensage) {
		this.mensage = mensage;
	}
	public Throwable getE() {
		return e;
	}
	public void setE(Throwable e) {
		this.e = e;
	}
}
