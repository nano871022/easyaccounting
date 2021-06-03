package com.japl.ea.query.privates.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
@Entity(name="TBL_CITY")
@Table(name="TBL_CITY")
public class CiudadJPA extends AJPA{
	@Column(name="sname")
	private String nombre;
	@ManyToOne @JoinColumn(name="sdepartament")
	private DepartamentoJPA departamento;
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public DepartamentoJPA getDepartamento() {
		return departamento;
	}
	public void setDepartamento(DepartamentoJPA departamento) {
		this.departamento = departamento;
	}
}
