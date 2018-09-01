package com.pyt.service.dto;

import org.pyt.common.common.ADto;

public class CuentaContableDTO extends ADto{
	private static final long serialVersionUID = -8350913912942141963L;
	private String codigoCuenta;
	private ParametroDTO tipoPlanContable;
	private ParametroDTO tipo;
	private String nombre;
	private ParametroDTO naturaleza;
	private EmpresaDTO empresa;
	private String asociado;
	
	public ParametroDTO getTipoPlanContable() {
		return tipoPlanContable;
	}
	public void setTipoPlanContable(ParametroDTO tipoPlanContable) {
		this.tipoPlanContable = tipoPlanContable;
	}
	public ParametroDTO getTipo() {
		return tipo;
	}
	public void setTipo(ParametroDTO tipo) {
		this.tipo = tipo;
	}
	public ParametroDTO getNaturaleza() {
		return naturaleza;
	}
	public void setNaturaleza(ParametroDTO naturaleza) {
		this.naturaleza = naturaleza;
	}
	public EmpresaDTO getEmpresa() {
		return empresa;
	}
	public void setEmpresa(EmpresaDTO empresa) {
		this.empresa = empresa;
	}
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
