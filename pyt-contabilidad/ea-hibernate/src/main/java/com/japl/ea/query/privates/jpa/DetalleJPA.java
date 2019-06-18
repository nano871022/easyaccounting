package com.japl.ea.query.privates.jpa;

import java.math.BigDecimal;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Contiene el detalle de la factura
 * 
 * @author alejandro parra
 * @since 06/05/2018
 */
@Entity(name="TBL_DETAIL")
@Table(name="TBL_DETAIL")
public class DetalleJPA extends AJPA {
	@Column(name="nrow")
	private Integer renglon;
	@ManyToOne @JoinColumn(name="sconcept")
	private ServicioJPA concepto;
	@ManyToOne @JoinColumn(name="scostcenter")
	private CentroCostoJPA centroCosto;
	@ManyToOne @JoinColumn(name="sspendingcategory")
	private ParametroJPA categoriaGasto;
	@Column(name="ngrossvalue")
	private BigDecimal valorBruto;
	@Column(name="nnetworth")
	private BigDecimal valorNeto;
	@Column(name="nivarate")
	private Long porcentajeIva;
	@Column(name="nivavalue")
	private BigDecimal valorIva;
	@Column(name="nconsumptiontax")
	private Long impuestoConsumo;
	@Column(name="nconsumtionvalue")
	private BigDecimal valorConsumo;
	@ManyToMany(targetEntity=TrabajadorJPA.class)
	private Set<TrabajadorJPA> ejecutadores;
	@ManyToMany(targetEntity=FacturaJPA.class)
	private Set<FacturaJPA> facturas;
	@ManyToOne @JoinColumn(name="sicaactivity")
	private ActividadIcaJPA actividadIca;
	@Column(name="scodedocument")
	private String codigoDocumento;
	@ManyToOne @JoinColumn(name="sentry")
	private IngresoJPA ingreso;
	@Column(name="ndiscountrate")
	private Long porcentajeDescuento;
	@Column(name="ndiscountvalue")
	private BigDecimal valorDescuento;
	@Column(name="sactivityrateica")
	private BigDecimal tarifaActividadIca;
	@ManyToOne @JoinColumn(name="sconcepttype")
	private ParametroJPA tipoConcepto;
	@Column(name="sobservation")
	private String observaciones;
	@Column(name="sdescription")
	private String descripcion;
	@ManyToOne @JoinColumn(name="sthird")
	private EmpresaJPA tercero;
	@ManyToMany
	private Set<TrabajadorJPA> trabajadores; 
	
	public Set<FacturaJPA> getFacturas() {
		return facturas;
	}

	public void setFacturas(Set<FacturaJPA> facturas) {
		this.facturas = facturas;
	}

	public Set<TrabajadorJPA> getTrabajadores() {
		return trabajadores;
	}

	public void setTrabajadores(Set<TrabajadorJPA> trabajadores) {
		this.trabajadores = trabajadores;
	}

	public Long getPorcentajeDescuento() {
		return porcentajeDescuento;
	}

	public void setPorcentajeDescuento(Long porcentajeDescuento) {
		this.porcentajeDescuento = porcentajeDescuento;
	}

	public EmpresaJPA getTercero() {
		return tercero;
	}

	public void setTercero(EmpresaJPA tercero) {
		this.tercero = tercero;
	}

	public BigDecimal getValorDescuento() {
		return valorDescuento;
	}

	public void setValorDescuento(BigDecimal valorDescuento) {
		this.valorDescuento = valorDescuento;
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

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public IngresoJPA getIngreso() {
		return ingreso;
	}

	public void setIngreso(IngresoJPA ingreso) {
		this.ingreso = ingreso;
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

	public ServicioJPA getConcepto() {
		return concepto;
	}

	public void setConcepto(ServicioJPA concepto) {
		this.concepto = concepto;
	}

	public CentroCostoJPA getCentroCosto() {
		return centroCosto;
	}

	public void setCentroCosto(CentroCostoJPA centroCosto) {
		this.centroCosto = centroCosto;
	}

	public ParametroJPA getCategoriaGasto() {
		return categoriaGasto;
	}

	public void setCategoriaGasto(ParametroJPA categoriaGasto) {
		this.categoriaGasto = categoriaGasto;
	}

	public BigDecimal getValorBruto() {
		return valorBruto;
	}

	public void setValorBruto(BigDecimal valorBruto) {
		this.valorBruto = valorBruto;
	}

	public BigDecimal getValorNeto() {
		return valorNeto;
	}

	public void setValorNeto(BigDecimal valorNeto) {
		this.valorNeto = valorNeto;
	}

	public Long getPorcentajeIva() {
		return porcentajeIva;
	}

	public void setPorcentajeIva(Long porcentajeIva) {
		this.porcentajeIva = porcentajeIva;
	}

	public BigDecimal getValorIva() {
		return valorIva;
	}

	public void setValorIva(BigDecimal valorIva) {
		this.valorIva = valorIva;
	}

	public Long getImpuestoConsumo() {
		return impuestoConsumo;
	}

	public void setImpuestoConsumo(Long impuestoConsumo) {
		this.impuestoConsumo = impuestoConsumo;
	}

	public BigDecimal getValorConsumo() {
		return valorConsumo;
	}

	public void setValorConsumo(BigDecimal valorConsumo) {
		this.valorConsumo = valorConsumo;
	}

	public ActividadIcaJPA getActividadIca() {
		return actividadIca;
	}

	public void setActividadIca(ActividadIcaJPA actividadIca) {
		this.actividadIca = actividadIca;
	}

	public Set<TrabajadorJPA> getEjecutadores() {
		return ejecutadores;
	}

	public void setEjecutadores(Set<TrabajadorJPA> ejecutadores) {
		this.ejecutadores = ejecutadores;
	}

}
