package com.japl.ea.query.privates.jpa;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
@Entity(name="TBL_ASSOCIATION_ACHIVE")
@Table(name="TBL_ASSOCIATION_ARCHIVE")
public class AsociacionArchivoJPA extends AJPA {
	@Column(name="sfile")
	private String archivo;
	@Column(name="sname")
	private String nombre;
	@ManyToMany(targetEntity=MarcadorServicioJPA.class)
	private Set<MarcadorServicioJPA> marcadorServicios;
	@ManyToMany(targetEntity=ServicioCampoBusquedaJPA.class)
	private Set<ServicioCampoBusquedaJPA> servicioCampoBusquedas;
	public String getArchivo() {
		return archivo;
	}
	public void setArchivo(String archivo) {
		this.archivo = archivo;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Set<MarcadorServicioJPA> getMarcadorServicios() {
		return marcadorServicios;
	}
	public void setMarcadorServicios(Set<MarcadorServicioJPA> marcadorServicios) {
		this.marcadorServicios = marcadorServicios;
	}
	public Set<ServicioCampoBusquedaJPA> getServicioCampoBusquedas() {
		return servicioCampoBusquedas;
	}
	public void setServicioCampoBusquedas(Set<ServicioCampoBusquedaJPA> servicioCampoBusquedas) {
		this.servicioCampoBusquedas = servicioCampoBusquedas;
	}
	
}
