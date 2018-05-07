package com.pyt.service.dto;

import java.util.Date;
import java.util.List;
/**
 * Se encarga de almacenar la factura que se genera por uno o varios trabajos realizados
 * @author alejandro parra
 * @since 06/05/2018
 */
public class FacturaDTO extends ADto{
	private String codigo;
	private EmpresaDTO tercero;
	private Date fechaFactura;
	private Date fechaRegistro;
	private ParametroDTO moneda;
	private Date fechaVencimiento;
	private String observacion;
	private List<DetalleDTO> detalle;
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public EmpresaDTO getTercero() {
		return tercero;
	}
	public void setTercero(EmpresaDTO tercero) {
		this.tercero = tercero;
	}
	public Date getFechaFactura() {
		return fechaFactura;
	}
	public void setFechaFactura(Date fechaFactura) {
		this.fechaFactura = fechaFactura;
	}
	public Date getFechaRegistro() {
		return fechaRegistro;
	}
	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	public ParametroDTO getMoneda() {
		return moneda;
	}
	public void setMoneda(ParametroDTO moneda) {
		this.moneda = moneda;
	}
	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}
	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public List<DetalleDTO> getDetalle() {
		return detalle;
	}
	public void setDetalle(List<DetalleDTO> detalle) {
		this.detalle = detalle;
	}
	
	
}
