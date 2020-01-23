package com.pyt.service.dto;

import java.util.Date;

import org.pyt.common.abstracts.ADto;

import co.com.arquitectura.annotation.proccessor.DelClass;
import co.com.arquitectura.annotation.proccessor.UpdClass;

/**
 * Se encarga de indicar las personas que trabajan en los servicios
 * 
 * @author alejandro parra
 * @since 06/05/2018
 */
@DelClass(nombre = "com.pyt.service.dto.dels.TrabajadorDelDTO")
@UpdClass(nombre = "com.pyt.service.dto.upds.TrabajadorUpdDTO")
public class TrabajadorDTO extends ADto {
	private static final long serialVersionUID = 8423665201076663703L;
	private PersonaDTO persona;
	private Date fechaIngreso;
	private Date fechaRetiro;
	private ParametroDTO estado;
	private ParametroDTO tipoPago;
	private CentroCostoDTO centroCosto;
	private String correo;

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

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

	public ParametroDTO getEstado() {
		return this.estado;
	}

	public void setEstado(ParametroDTO estado) {
		this.estado = estado;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((centroCosto == null) ? 0 : centroCosto.hashCode());
		result = prime * result + ((correo == null) ? 0 : correo.hashCode());
		result = prime * result + ((estado == null) ? 0 : estado.hashCode());
		result = prime * result + ((fechaIngreso == null) ? 0 : fechaIngreso.hashCode());
		result = prime * result + ((fechaRetiro == null) ? 0 : fechaRetiro.hashCode());
		result = prime * result + ((persona == null) ? 0 : persona.hashCode());
		result = prime * result + ((tipoPago == null) ? 0 : tipoPago.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TrabajadorDTO other = (TrabajadorDTO) obj;
		if (centroCosto == null) {
			if (other.centroCosto != null)
				return false;
		} else if (!centroCosto.equals(other.centroCosto))
			return false;
		if (correo == null) {
			if (other.correo != null)
				return false;
		} else if (!correo.equals(other.correo))
			return false;
		if (estado == null) {
			if (other.estado != null)
				return false;
		} else if (!estado.equals(other.estado))
			return false;
		if (fechaIngreso == null) {
			if (other.fechaIngreso != null)
				return false;
		} else if (!fechaIngreso.equals(other.fechaIngreso))
			return false;
		if (fechaRetiro == null) {
			if (other.fechaRetiro != null)
				return false;
		} else if (!fechaRetiro.equals(other.fechaRetiro))
			return false;
		if (persona == null) {
			if (other.persona != null)
				return false;
		} else if (!persona.equals(other.persona))
			return false;
		if (tipoPago == null) {
			if (other.tipoPago != null)
				return false;
		} else if (!tipoPago.equals(other.tipoPago))
			return false;
		return true;
	}

}
