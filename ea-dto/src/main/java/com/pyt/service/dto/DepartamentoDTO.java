package com.pyt.service.dto;

import org.pyt.common.common.ADto;

import co.com.arquitectura.annotation.proccessor.DelClass;
import co.com.arquitectura.annotation.proccessor.UpdClass;
@DelClass(nombre="com.pyt.service.dto.dels.DepartamentoDelDTO")
@UpdClass(nombre="com.pyt.service.dto.upds.DepartamentoUpdDTO")

public class DepartamentoDTO extends ADto {
	private static final long serialVersionUID = 2988034817404294221L;
	private String nombre;
	private PaisDTO pais;
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public PaisDTO getPais() {
		return pais;
	}
	public void setPais(PaisDTO pais) {
		this.pais = pais;
	}
}
