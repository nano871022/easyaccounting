package com.japl.ea.query.privates.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity(name = "TBL_LOCATION")
@Table(name = "TBL_LOCATION")
public class UbicacionJPA extends AJPA {
	@ManyToOne @JoinColumn(name = "sstore")
	private AlmacenJPA almacen;
	@Column(name = "sname")
	private String nombre;
	@Column(name = "sdescription")
	private String descripcion;

	public AlmacenJPA getAlmacen() {
		return almacen;
	}

	public void setAlmacen(AlmacenJPA almacen) {
		this.almacen = almacen;
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
}
