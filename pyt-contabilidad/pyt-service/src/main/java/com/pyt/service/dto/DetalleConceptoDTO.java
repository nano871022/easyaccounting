package com.pyt.service.dto;

import java.math.BigDecimal;

import org.pyt.common.common.ADto;
/**
 * Indica el concepto del detalle del concepto de un documento
 * @author alejandro parra
 * @since 06/05/2018
 */
public class DetalleConceptoDTO extends ADto{
	private static final long serialVersionUID = -2863760998675204289L;
	private Integer renglon;
	private ConceptoDTO concepto;
	private CentroCostoDTO centroCosto;
	private EmpresaDTO tercero;
	private BigDecimal valor;
	private String observacion;
	private String codigoDocumento;
	
	public String getCodigoDocumento() {
		return codigoDocumento;
	}
	public void setCodigoDocumento(String codigoDocumento) {
		this.codigoDocumento = codigoDocumento;
	}
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
