package com.japl.ea.query.privates.jpa;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * Se encarga de almacenar la factura que se genera por uno o varios trabajos realizados
 * @author alejandro parra
 * @since 06/05/2018
 */
@Entity(name="TBL_BILL")
@Table(name="TBL_BILL")
public class FacturaJPA extends AJPA{
	@Column(name="senterprise")
	private EmpresaJPA tercero;
	@Column(name="dbill")
	private Date fechaFactura;
	@Column(name="dregistre")
	private Date fechaRegistro;
	@Column(name="smoney")
	private ParametroJPA moneda;
	@Column(name="dexpiration")
	private Date fechaVencimiento;
	@Column(name="sobservation")
	private String observacion;
	private List<DetalleJPA> detalle;
	public EmpresaJPA getTercero() {
		return tercero;
	}
	public void setTercero(EmpresaJPA tercero) {
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
	public ParametroJPA getMoneda() {
		return moneda;
	}
	public void setMoneda(ParametroJPA moneda) {
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
	public List<DetalleJPA> getDetalle() {
		return detalle;
	}
	public void setDetalle(List<DetalleJPA> detalle) {
		this.detalle = detalle;
	}
	
	
}
