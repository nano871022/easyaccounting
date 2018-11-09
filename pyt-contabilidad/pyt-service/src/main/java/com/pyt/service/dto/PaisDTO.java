package com.pyt.service.dto;

import org.pyt.common.annotations.DelClass;
import org.pyt.common.annotations.UpdClass;
import org.pyt.common.common.ADto;
@DelClass(nombre="com.pyt.service.dto.dels.PaisDelDTO")
@UpdClass(nombre="com.pyt.service.dto.upds.PaisUpdDTO")

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
