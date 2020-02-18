package com.pyt.service.dto.inventario;

import org.pyt.common.abstracts.ADto;

public class UbicacionDTO extends ADto {
	private static final long serialVersionUID = -922036576175025989L;
	private AlmacenDTO almacen;
	private String nombre;
	private String descripcion;
	public AlmacenDTO getAlmacen() {
		return almacen;
	}
	public void setAlmacen(AlmacenDTO almacen) {
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
