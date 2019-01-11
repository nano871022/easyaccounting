package com.pyt.service.dto;

import org.pyt.common.common.ADto;

import co.com.arquitectura.annotation.proccessor.DelClass;
import co.com.arquitectura.annotation.proccessor.UpdClass;
@DelClass(nombre="com.pyt.service.dto.dels.OficinaDelDTO")
@UpdClass(nombre="com.pyt.service.dto.upds.OficinaUpdDTO")
public class OficinaDTO extends ADto {
	private static final long serialVersionUID = -1103952032633883291L;
	private String nombre;
	private String detalle;
	private String direccion;
	private CiudadDTO ciudad;
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
	public CiudadDTO getCiudad() {
		return ciudad;
	}
	public void setCiudad(CiudadDTO ciudad) {
		this.ciudad = ciudad;
	}
}
