package com.japl.ea.query.privates.jpa;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

import co.com.japl.ea.common.reflection.ReflectionDto;

@MappedSuperclass
public class JPA extends ReflectionDto implements Serializable{
	private static final long serialVersionUID = 1L;
	@Column(name="dcreate")
	protected Date fechaCreacion;
	@Column(name="dupdate")
	protected Date fechaActualizacion;
	@Column(name="ddelete")
	protected Date fechaEliminacion;
	@Column(name="screater")
	protected String creador;
	@Column(name="supdater")
	protected String actualizador;
	@Column(name="sdeleter")
	protected String eliminador;
	public Date getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	public Date getFechaActualizacion() {
		return fechaActualizacion;
	}
	public void setFechaActualizacion(Date fechaActualizacion) {
		this.fechaActualizacion = fechaActualizacion;
	}
	public Date getFechaEliminacion() {
		return fechaEliminacion;
	}
	public void setFechaEliminacion(Date fechaEliminacion) {
		this.fechaEliminacion = fechaEliminacion;
	}
	public String getCreador() {
		return creador;
	}
	public void setCreador(String creador) {
		this.creador = creador;
	}
	public String getActualizador() {
		return actualizador;
	}
	public void setActualizador(String actualizador) {
		this.actualizador = actualizador;
	}
	public String getEliminador() {
		return eliminador;
	}
	public void setEliminador(String eliminador) {
		this.eliminador = eliminador;
	}
}
