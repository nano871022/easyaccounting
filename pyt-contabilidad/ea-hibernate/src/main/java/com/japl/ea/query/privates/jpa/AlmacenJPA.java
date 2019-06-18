package com.japl.ea.query.privates.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name="TBL_STORE")
@Table(name="TBL_STORE")
public class AlmacenJPA extends AJPA {
	@Column(name="sname")
	private String nombre;
	@Column(name="sdescription")
	private String descripcion;
	@Column(name="saddress")
	private String direccion;
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}
