package com.japl.ea.query.privates.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
@Entity(name="TBL_DEPARTAMENT")
@Table(name="TBL_DEPARTAMENT")
public class DepartamentoJPA extends AJPA {
	@Column(name="sname")
	private String nombre;
	@Column(name="scountry")
	private PaisJPA pais;
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public PaisJPA getPais() {
		return pais;
	}
	public void setPais(PaisJPA pais) {
		this.pais = pais;
	}
}
