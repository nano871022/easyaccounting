package com.japl.ea.query.privates.jpa;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.pyt.common.reflection.ReflectionDto;

@MappedSuperclass
public abstract class AJPA extends ReflectionDto{
	@Id
	@Column(name="CODIGO")
	protected String codigo;
	@Column
	protected Date fechaCreacion;
	@Column
	protected Date fechaActualizacion;
	@Column
	protected Date fechaEliminacion;
	@Column
	protected String creador;
	@Column
	protected String actualizador;
	@Column
	protected String eliminador;
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
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
