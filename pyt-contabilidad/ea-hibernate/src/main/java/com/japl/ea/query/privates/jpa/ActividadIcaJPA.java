package com.japl.ea.query.privates.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Son las actividades indicadas por el govierno
 * 
 * @author Alejandro Parra
 * @since 06/05/2018
 */
@Entity(name = "TBL_ICA_ACTIVITY")
@Table(name = "TBL_ICA_ACTIVITY")
public class ActividadIcaJPA extends AJPA {
	@Column(name = "sicacode")
	private String codigoIca;
	@Column(name = "sname")
	private String nombre;
	@Column(name = "sdescription")
	private String descripcion;
	@Column(name = "sbase")
	private String base;
	@Column(name = "srate")
	private String tarifa;

	public String getCodigoIca() {
		return codigoIca;
	}

	public void setCodigoIca(String codigoIca) {
		this.codigoIca = codigoIca;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public String getTarifa() {
		return tarifa;
	}

	public void setTarifa(String tarifa) {
		this.tarifa = tarifa;
	}

}
