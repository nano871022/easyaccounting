package com.pyt.service.dto;

import java.math.BigDecimal;
/**
 * Indica el concepto del detalle del concepto de un documento
 * @author alejandro parra
 * @since 06/05/2018
 */
public class DetalleConceptoDTO extends ADto{
	private Integer renglon;
	private ConceptoDTO concepto;
	private CentroCostoDTO centroCosto;
	private EmpresaDTO tercero;
	private BigDecimal valor;
	private String observacion;
	public Integer getRenglon() {
		return renglon;
	}
	public void setRenglon(Integer renglon) {
		this.renglon = renglon;
	}
	public ConceptoDTO getConcepto() {
		return concepto;
	}
	public void setConcepto(ConceptoDTO concepto) {
		this.concepto = concepto;
	}
	public CentroCostoDTO getCentroCosto() {
		return centroCosto;
	}
	public void setCentroCosto(CentroCostoDTO centroCosto) {
		this.centroCosto = centroCosto;
	}
	public EmpresaDTO getTercero() {
		return tercero;
	}
	public void setTercero(EmpresaDTO tercero) {
		this.tercero = tercero;
	}
	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	
}
