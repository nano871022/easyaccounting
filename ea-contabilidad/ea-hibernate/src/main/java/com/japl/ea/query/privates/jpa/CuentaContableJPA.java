package com.japl.ea.query.privates.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
@Entity(name="TBL_ACCOUNTING_ACCOUNT")
@Table(name="TBL_ACCOUNTING_ACCOUNT")
public class CuentaContableJPA extends AJPA{
	@Column(name="scodeaccount")
	private String codigoCuenta;
	@ManyToOne @JoinColumn(name="splantype")
	private ParametroJPA tipoPlanContable;
	@ManyToOne @JoinColumn(name="stype")
	private ParametroJPA tipo;
	@Column(name="sname")
	private String nombre;
	@ManyToOne @JoinColumn(name="snature")
	private ParametroJPA naturaleza;
	@ManyToOne @JoinColumn(name="senterprise")
	private EmpresaJPA empresa;
	@Column(name="sassociated")
	private String asociado;
	
	public ParametroJPA getTipoPlanContable() {
		return tipoPlanContable;
	}
	public void setTipoPlanContable(ParametroJPA tipoPlanContable) {
		this.tipoPlanContable = tipoPlanContable;
	}
	public ParametroJPA getTipo() {
		return tipo;
	}
	public void setTipo(ParametroJPA tipo) {
		this.tipo = tipo;
	}
	public ParametroJPA getNaturaleza() {
		return naturaleza;
	}
	public void setNaturaleza(ParametroJPA naturaleza) {
		this.naturaleza = naturaleza;
	}
	public EmpresaJPA getEmpresa() {
		return empresa;
	}
	public void setEmpresa(EmpresaJPA empresa) {
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
