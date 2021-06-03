package co.com.japl.ea.dto.dto;

import java.math.BigDecimal;

import co.com.japl.ea.common.abstracts.ADto;
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
	private BigDecimal valorBruto;
	private String observacion;
	private String codigoDocumento;
	private BigDecimal valorDescuento;
	private BigDecimal valorNeto;
	private Integer porcentajeIVa;
	private BigDecimal valorIva;
	private BigDecimal baseImpoConsumo;
	private BigDecimal valorIpoConsumo;
	private ActividadIcaDTO actividadIca;
	private BigDecimal tarifaActividadIca;
	private ParametroDTO tipoConcepto;
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
	public ActividadIcaDTO getActividadIca() {
		return actividadIca;
	}
	public void setActividadIca(ActividadIcaDTO actividadIca) {
		this.actividadIca = actividadIca;
	}
	public BigDecimal getTarifaActividadIca() {
		return tarifaActividadIca;
	}
	public void setTarifaActividadIca(BigDecimal tarifaActividadIca) {
		this.tarifaActividadIca = tarifaActividadIca;
	}
	public ParametroDTO getTipoConcepto() {
		return tipoConcepto;
	}
	public void setTipoConcepto(ParametroDTO tipoConcepto) {
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
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	
}
