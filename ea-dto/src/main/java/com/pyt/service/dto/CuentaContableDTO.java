package com.pyt.service.dto;

import org.pyt.common.abstracts.ADto;
import org.pyt.common.annotation.generics.DefaultFieldToGeneric;

import co.com.arquitectura.annotation.proccessor.DelClass;
import co.com.arquitectura.annotation.proccessor.UpdClass;

@DelClass(nombre = "com.pyt.service.dto.dels.CuentaContableDelDTO")
@UpdClass(nombre = "com.pyt.service.dto.upds.CuentaContableUpdDTO")

public class CuentaContableDTO extends ADto {
	private static final long serialVersionUID = -8350913912942141963L;
	@DefaultFieldToGeneric(simpleNameClazzBean = "PopupGenBean", use = DefaultFieldToGeneric.Uses.COLUMN)
	@DefaultFieldToGeneric(simpleNameClazzBean = "PopupGenBean", use = DefaultFieldToGeneric.Uses.FILTER)
	private String codigoCuenta;
	private ParametroDTO tipoPlanContable;
	private ParametroDTO tipo;
	@DefaultFieldToGeneric(simpleNameClazzBean = "PopupGenBean", use = DefaultFieldToGeneric.Uses.FILTER)
	@DefaultFieldToGeneric(simpleNameClazzBean = "PopupGenBean", use = DefaultFieldToGeneric.Uses.COLUMN)
	private String nombre;
	private ParametroDTO naturaleza;
	private EmpresaDTO empresa;
	private CuentaContableDTO asociado;

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

	public CuentaContableDTO getAsociado() {
		return this.asociado;
	}

	public void setAsociado(CuentaContableDTO asociado) {
		this.asociado = asociado;
	}

}
