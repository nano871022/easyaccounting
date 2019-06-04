package com.japl.ea.query.privates.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.pyt.common.abstracts.ADto;

import co.com.arquitectura.annotation.proccessor.DelClass;
import co.com.arquitectura.annotation.proccessor.UpdClass;

/**
 * un concepto contable indica a que se va a contabilidar
 * @author alejandro parra
 * @since 06/05/2018
 */
@Entity(name="TBL_CONCEPT")
@Table(name="TBL_CONCEPT")
public class ConceptoJPA extends AJPA{
	@Column(name="sname")
	private String nombre;
	@Column(name="sdescription")
	private String descripcion;
	@Column(name="sstate")
	private ParametroJPA estado;
	@Column(name="senterprise")
	private EmpresaJPA empresa;
	@Column(name="ssubconcept")
	private String subconcepto;
	@Column(name="sexpenseaccount")
	private CuentaContableJPA cuentaGasto;
	@Column(name="sbilltopay")
	private CuentaContableJPA cuentaXPagar;
	
	public String getSubconcepto() {
		return subconcepto;
	}

	public void setSubconcepto(String subconcepto) {
		this.subconcepto = subconcepto;
	}

	public CuentaContableJPA getCuentaGasto() {
		return cuentaGasto;
	}

	public void setCuentaGasto(CuentaContableJPA cuentaGasto) {
		this.cuentaGasto = cuentaGasto;
	}

	public CuentaContableJPA getCuentaXPagar() {
		return cuentaXPagar;
	}

	public void setCuentaXPagar(CuentaContableJPA cuentaXPagar) {
		this.cuentaXPagar = cuentaXPagar;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public ParametroJPA getEstado() {
		return estado;
	}
	public void setEstado(ParametroJPA estado) {
		this.estado = estado;
	}
	public EmpresaJPA getEmpresa() {
		return empresa;
	}
	public void setEmpresa(EmpresaJPA empresa) {
		this.empresa = empresa;
	}
	
}
