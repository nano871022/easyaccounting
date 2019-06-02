package com.japl.ea.query.privates.jpa;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
@Entity(name="TBL_ASSOCIATION_ACHIVE")
@Table(name="TBL_ASSOCIATION_ARCHIVE")
public class AsociacionArchivoJPA extends AJPA {
	@Column(name="sfile")
	private String archivo;
	@Column(name="sname")
	private String nombre;
	private List<MarcadorServicioJPA> marcadorServicios;
	private List<ServicioCampoBusquedaJPA> servicioCampoBusquedas;
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
	public List<MarcadorServicioJPA> getMarcadorServicios() {
		return marcadorServicios;
	}
	public void setMarcadorServicios(List<MarcadorServicioJPA> marcadorServicios) {
		this.marcadorServicios = marcadorServicios;
	}
}
