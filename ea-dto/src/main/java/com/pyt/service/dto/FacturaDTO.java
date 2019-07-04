package com.pyt.service.dto;

import java.util.Date;
import java.util.List;

import org.pyt.common.abstracts.ADto;

import co.com.arquitectura.annotation.proccessor.DelClass;
import co.com.arquitectura.annotation.proccessor.UpdClass;
/**
 * Se encarga de almacenar la factura que se genera por uno o varios trabajos realizados
 * @author alejandro parra
 * @since 06/05/2018
 */
@DelClass(nombre="com.pyt.service.dto.dels.FacturaDelDTO")
@UpdClass(nombre="com.pyt.service.dto.upds.FacturaUpdDTO")

public class FacturaDTO extends ADto{
	private static final long serialVersionUID = -1911681512925350489L;
	private EmpresaDTO tercero;
	private Date fechaFactura;
	private Date fechaRegistro;
	private ParametroDTO moneda;
	private Date fechaVencimiento;
	private String observacion;
	private List<DetalleDTO> detalle;
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
