package com.japl.ea.query.privates.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity(name="TBL_OFFICE")
@Table(name="TBL_OFFICE")
public class OficinaJPA extends AJPA {
	@Column(name="sname")
	private String nombre;
	@Column(name="sdetail")
	private String detalle;
	@Column(name="saddress")
	private String direccion;
	@ManyToOne @JoinColumn(name="scity")
	private CiudadJPA ciudad;
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDetalle() {
		return detalle;
	}
	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public CiudadJPA getCiudad() {
		return ciudad;
	}
	public void setCiudad(CiudadJPA ciudad) {
		this.ciudad = ciudad;
	}
}
