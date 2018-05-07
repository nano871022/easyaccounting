package com.pyt.service.dto;

import java.util.Date;
/**
 * Se encarga de indicar las personas que trabajan en los servicios
 * @author alejandro parra
 * @since 06/05/2018
 */
public class TrabajadorDTO extends ADto {
	private PersonaDTO persona;
	private Date fechaIngreso;
	private Date fechaRetiro;
	private String estado;
	private ParametroDTO tipoPago;
	private CentroCostoDTO centroCosto;
	public PersonaDTO getPersona() {
		return persona;
	}
	public void setPersona(PersonaDTO persona) {
		this.persona = persona;
	}
	public Date getFechaIngreso() {
		return fechaIngreso;
	}
	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}
	public Date getFechaRetiro() {
		return fechaRetiro;
	}
	public void setFechaRetiro(Date fechaRetiro) {
		this.fechaRetiro = fechaRetiro;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public ParametroDTO getTipoPago() {
		return tipoPago;
	}
	public void setTipoPago(ParametroDTO tipoPago) {
		this.tipoPago = tipoPago;
	}
	public CentroCostoDTO getCentroCosto() {
		return centroCosto;
	}
	public void setCentroCosto(CentroCostoDTO centroCosto) {
		this.centroCosto = centroCosto;
	}
}
