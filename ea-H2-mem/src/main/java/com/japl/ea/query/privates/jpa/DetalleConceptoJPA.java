package com.japl.ea.query.privates.jpa;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.pyt.common.abstracts.AJPA;
/**
 * Indica el concepto del detalle del concepto de un documento
 * @author alejandro parra
 * @since 06/05/2018
 */
@Entity(name="TBL_CONCEPT_DETAIL")
@Table(name="TBL_CONCEPT_DETAIL")
public class DetalleConceptoJPA extends AJPA{
	@Column(name="nrow")
	private Integer renglon;
	@Column(name="sconcept")
	private ConceptoJPA concepto;
	@Column(name="scostcenter")
	private CentroCostoJPA centroCosto;
	@Column(name="sthird")
	private EmpresaJPA tercero;
	@Column(name="ngrossvalue")
	private BigDecimal valorBruto;
	@Column(name="sobservation")
	private String observacion;
	@Column(name="scodedocument")
	private String codigoDocumento;
	@Column(name="ndiscountvalue")
	private BigDecimal valorDescuento;
	@Column(name="nnetworth")
	private BigDecimal valorNeto;
	@Column(name="nivapercent")
	private Integer porcentajeIVa;
	@Column(name="nivavalue")
	private BigDecimal valorIva;
	@Column(name="nbasecosumptiontax")
	private BigDecimal baseImpoConsumo;
	@Column(name="nconsumptiontaxvalue")
	private BigDecimal valorIpoConsumo;
	@Column(name="sicaactivity")
	private ActividadIcaJPA actividadIca;
	@Column(name="nrateicaactivity")
	private BigDecimal tarifaActividadIca;
	@Column(name="sconcepttype")
	private ParametroJPA tipoConcepto;
	@Column(name="sdescriiption")
	private String descripcion;
	
	public BigDecimal getValorBruto() {
		return valorBruto;
	}
	public void setValorBruto(BigDecimal valorBruto) {
		this.valorBruto = valorBruto;
	}
	public BigDecimal getValorDescuento() {
		return valorDescuento;
	}
	public void setValorDescuento(BigDecimal valorDescuento) {
		this.valorDescuento = valorDescuento;
	}
	public BigDecimal getValorNeto() {
		return valorNeto;
	}
	public void setValorNeto(BigDecimal valorNeto) {
		this.valorNeto = valorNeto;
	}
	public Integer getPorcentajeIVa() {
		return porcentajeIVa;
	}
	public void setPorcentajeIVa(Integer porcentajeIVa) {
		this.porcentajeIVa = porcentajeIVa;
	}
	public BigDecimal getValorIva() {
		return valorIva;
	}
	public void setValorIva(BigDecimal valorIva) {
		this.valorIva = valorIva;
	}
	public BigDecimal getBaseImpoConsumo() {
		return baseImpoConsumo;
	}
	public void setBaseImpoConsumo(BigDecimal baseImpoConsumo) {
		this.baseImpoConsumo = baseImpoConsumo;
	}
	public BigDecimal getValorIpoConsumo() {
		return valorIpoConsumo;
	}
	public void setValorIpoConsumo(BigDecimal valorIpoConsumo) {
		this.valorIpoConsumo = valorIpoConsumo;
	}
	public ActividadIcaJPA getActividadIca() {
		return actividadIca;
	}
	public void setActividadIca(ActividadIcaJPA actividadIca) {
		this.actividadIca = actividadIca;
	}
	public BigDecimal getTarifaActividadIca() {
		return tarifaActividadIca;
	}
	public void setTarifaActividadIca(BigDecimal tarifaActividadIca) {
		this.tarifaActividadIca = tarifaActividadIca;
	}
	public ParametroJPA getTipoConcepto() {
		return tipoConcepto;
	}
	public void setTipoConcepto(ParametroJPA tipoConcepto) {
		this.tipoConcepto = tipoConcepto;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
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
	public ConceptoJPA getConcepto() {
		return concepto;
	}
	public void setConcepto(ConceptoJPA concepto) {
		this.concepto = concepto;
	}
	public CentroCostoJPA getCentroCosto() {
		return centroCosto;
	}
	public void setCentroCosto(CentroCostoJPA centroCosto) {
		this.centroCosto = centroCosto;
	}
	public EmpresaJPA getTercero() {
		return tercero;
	}
	public void setTercero(EmpresaJPA tercero) {
		this.tercero = tercero;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	
}
