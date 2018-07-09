package com.pyt.service.dto;

import org.pyt.common.common.ADto;

public class CuentaContableDTO extends ADto{
	private static final long serialVersionUID = -8350913912942141963L;
	private String codigoCuenta;
	private String nombre;
	private ParametroDTO tipoCuenta;
	private String asociado;
	public String getCodigoCuenta() {
		return codigoCuenta;
	}
	public void setCodigoCuenta(String codigoCuenta) {
		this.codigoCuenta = codigoCuenta;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getAsociado() {
		return asociado;
	}
	public void setAsociado(String asociado) {
		this.asociado = asociado;
	}
	
	
	

}
