package com.japl.ea.query.privates.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "TBL_PRODUCT")
@Table(name = "TBL_PRODUCT")
public class ProductoJPA extends AJPA {
	@Column(name = "sname")
	private String nombre;
	@Column(name = "sdescription")
	private String descripcion;
	@Column(name = "sreference")
	private String referencia;

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}
