package com.japl.ea.query.privates.jpa;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
@Entity(name="TBL_ACCOUNTING_DETAIL")
@Table(name="TBL_ACCOUNTING_DETAIL")
public class DetalleContableJPA extends AJPA {
	@Column(name="sconcept")
	private ConceptoJPA concepto;
	@Column(name="saccountingaccount")
	private CuentaContableJPA cuentaContable;
	@Column(name="nvalue")
	private BigDecimal valor;
	@Column(name="nrow")
	private Integer renglon;
	@Column(name="sobservation")
	private String observacion;
	@Column(name="scodedocument")
	private String codigoDocumento;
	public ConceptoJPA getConcepto() {
		return concepto;
	}
	public void setConcepto(ConceptoJPA concepto) {
		this.concepto = concepto;
	}
	public CuentaContableJPA getCuentaContable() {
		return cuentaContable;
	}
	public void setCuentaContable(CuentaContableJPA cuentaContable) {
		this.cuentaContable = cuentaContable;
	}
	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	public Integer getRenglon() {
		return renglon;
	}
	public void setRenglon(Integer renglon) {
		this.renglon = renglon;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public String getCodigoDocumento() {
		return codigoDocumento;
	}
	public void setCodigoDocumento(String codigoDocumento) {
		this.codigoDocumento = codigoDocumento;
	}
}
