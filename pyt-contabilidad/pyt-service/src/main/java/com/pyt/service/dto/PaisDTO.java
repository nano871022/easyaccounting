package com.pyt.service.dto;

import org.pyt.common.common.ADto;

public class PaisDTO extends ADto {
	private static final long serialVersionUID = -7715514480948023886L;
	private String nombre;
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}
