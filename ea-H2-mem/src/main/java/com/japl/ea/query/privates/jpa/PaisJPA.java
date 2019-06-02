package com.japl.ea.query.privates.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
@Entity(name="TBL_COUNTRY")
@Table(name="TBL_COUNTRY")
public class PaisJPA extends AJPA {
	@Column(name="sname")
	private String nombre;
	@Column(name="sstate")
	private ParametroJPA estado;
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}
