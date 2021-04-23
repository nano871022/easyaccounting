package com.pyt.service.dto;

import java.math.BigDecimal;

import org.pyt.common.abstracts.ADto;
import org.pyt.common.annotations.Increment;
import org.pyt.common.annotations.NoEdit;
import org.pyt.common.annotations.Operacion;
import org.pyt.common.annotations.Operar;

import co.com.arquitectura.annotation.proccessor.DelClass;
import co.com.arquitectura.annotation.proccessor.UpdClass;

/**
 * Contiene el detalle de la factura
 * 
 * @author alejandro parra
 * @since 06/05/2018
 */
@DelClass(nombre = "com.pyt.service.dto.dels.DetalleDelDTO")
@UpdClass(nombre = "com.pyt.service.dto.upds.DetalleUpdDTO")

public class DetalleDTO extends ADto {
	private static final long serialVersionUID = -3773810908869511818L;
	@Increment
	private Integer renglon;
	private ServicioDTO concepto;
	private CentroCostoDTO centroCosto;
	private ParametroDTO categoriaGasto;
	@Operacion(valor1="valorUnidad", valor2="cantidad", operacion=Operar.MULTIPLICAR)
	private BigDecimal valorBruto;
	@NoEdit
	@Operacion(valor1 = "valorBruto", valor2 = "valorIva", operacion = Operar.SUMA)
	@Operacion(valor1 = "valorConsumo", operacion = Operar.SUMA)
	private BigDecimal valorNeto;
	private Long porcentajeIva;
	@Operacion(valor1 = "porcentajeIva", valor2 = "valorBruto", operacion = Operar.MULTIPLICAR)
	private BigDecimal valorIva;
	private Long impuestoConsumo;
	@Operacion(valor1 = "impuestoConsumo", valor2 = "valorBruto", operacion = Operar.MULTIPLICAR)
	private BigDecimal valorConsumo;
	private ActividadIcaDTO actividadIca;
	private String codigoDocumento;
	private IngresoDTO ingreso;
	private Long porcentajeDescuento;
	private BigDecimal valorDescuento;
	private BigDecimal tarifaActividadIca;
	private ParametroDTO tipoConcepto;
	private String observaciones;
	private String descripcion;
	private EmpresaDTO tercero;
	@Operacion(valor1="concepto.valorManoObra",operacion=Operar.IGUAL)
	private BigDecimal valorUnidad;
	private Integer cantidad;

	public Long getPorcentajeDescuento() {
		return porcentajeDescuento;
	}

	public void setPorcentajeDescuento(Long porcentajeDescuento) {
		this.porcentajeDescuento = porcentajeDescuento;
	}

	public EmpresaDTO getTercero() {
		return tercero;
	}

	public void setTercero(EmpresaDTO tercero) {
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

	public ParametroDTO getTipoConcepto() {
		return tipoConcepto;
	}

	public void setTipoConcepto(ParametroDTO tipoConcepto) {
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

	public IngresoDTO getIngreso() {
		return ingreso;
	}

	public void setIngreso(IngresoDTO ingreso) {
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

	public ServicioDTO getConcepto() {
		return concepto;
	}

	public void setConcepto(ServicioDTO concepto) {
		this.concepto = concepto;
	}

	public CentroCostoDTO getCentroCosto() {
		return centroCosto;
	}

	public void setCentroCosto(CentroCostoDTO centroCosto) {
		this.centroCosto = centroCosto;
	}

	public ParametroDTO getCategoriaGasto() {
		return categoriaGasto;
	}

	public void setCategoriaGasto(ParametroDTO categoriaGasto) {
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

	public ActividadIcaDTO getActividadIca() {
		return actividadIca;
	}

	public void setActividadIca(ActividadIcaDTO actividadIca) {
		this.actividadIca = actividadIca;
	}

	public BigDecimal getValorUnidad() {
		return valorUnidad;
	}

	public void setValorUnidad(BigDecimal valorUnidad) {
		this.valorUnidad = valorUnidad;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

}
