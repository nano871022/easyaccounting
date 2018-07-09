package com.pyt.service.dto;

import java.math.BigDecimal;

import org.pyt.common.common.ADto;

public class DetalleContableDTO extends ADto {
	private static final long serialVersionUID = -501193270090048865L;
	private ConceptoDTO concepto;
	private CuentaContableDTO cuentaContable;
	private BigDecimal valor;
	private Integer renglon;
	private String observacion;
	public ConceptoDTO getConcepto() {
		return concepto;
	}
	public void setConcepto(ConceptoDTO concepto) {
		this.concepto = concepto;
	}
	public CuentaContableDTO getCuentaContable() {
		return cuentaContable;
	}
	public void setCuentaContable(CuentaContableDTO cuentaContable) {
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
	
}
