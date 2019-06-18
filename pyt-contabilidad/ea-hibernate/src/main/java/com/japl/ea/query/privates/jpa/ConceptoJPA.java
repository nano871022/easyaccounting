package com.japl.ea.query.privates.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
	@ManyToOne @JoinColumn(name="sstate")
	private ParametroJPA estado;
	@ManyToOne @JoinColumn(name="senterprise")
	private EmpresaJPA empresa;
	@Column(name="ssubconcept")
	private String subconcepto;
	@ManyToOne @JoinColumn(name="sexpenseaccount")
	private CuentaContableJPA cuentaGasto;
	@ManyToOne @JoinColumn(name="sbilltopay")
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
