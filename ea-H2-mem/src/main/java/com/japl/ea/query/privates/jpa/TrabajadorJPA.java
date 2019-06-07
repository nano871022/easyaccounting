package com.japl.ea.query.privates.jpa;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
/**
 * Se encarga de indicar las personas que trabajan en los servicios
 * @author alejandro parra
 * @since 06/05/2018
 */
@Entity(name="TBL_EMPLOYEE")
@Table(name="TBL_EMPLOYEE")
public class TrabajadorJPA extends AJPA {
	@ManyToOne @JoinColumn(name="sperson")
	private PersonaJPA persona;
	@Column(name="dentry")
	private Date fechaIngreso;
	@Column(name="dretirement")
	private Date fechaRetiro;
	@Column(name="sstate")
	private String estado;
	@ManyToOne @JoinColumn(name="spaytype")
	private ParametroJPA tipoPago;
	@ManyToOne @JoinColumn(name="scentercost")
	private CentroCostoJPA centroCosto;
	@Column(name="semail")
	private String correo;
	@ManyToMany(targetEntity=DetalleJPA.class)
	private Set<DetalleJPA> detalles; 
	
	public Set<DetalleJPA> getDetalles() {
		return detalles;
	}
	public void setDetalles(Set<DetalleJPA> detalles) {
		this.detalles = detalles;
	}
	public String getCorreo() {
		return correo;
	}
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	public PersonaJPA getPersona() {
		return persona;
	}
	public void setPersona(PersonaJPA persona) {
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
	public ParametroJPA getTipoPago() {
		return tipoPago;
	}
	public void setTipoPago(ParametroJPA tipoPago) {
		this.tipoPago = tipoPago;
	}
	public CentroCostoJPA getCentroCosto() {
		return centroCosto;
	}
	public void setCentroCosto(CentroCostoJPA centroCosto) {
		this.centroCosto = centroCosto;
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
		TrabajadorJPA other = (TrabajadorJPA) obj;
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
