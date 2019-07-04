package com.pyt.service.dto;

import org.pyt.common.abstracts.ADto;

import co.com.arquitectura.annotation.proccessor.DelClass;
import co.com.arquitectura.annotation.proccessor.UpdClass;

/**
 * Son las actividades indicadas por el govierno
 * @author Alejandro Parra
 * @since 06/05/2018
 */
@DelClass(nombre="com.pyt.service.dto.dels.ActividadIcaDelDTO")
@UpdClass(nombre="com.pyt.service.dto.upds.ActividadIcaUpdDTO")

public class ActividadIcaDTO extends ADto{
	private static final long serialVersionUID = 5006959473201225898L;
	private String codigoIca;
	private String nombre;
	private String descripcion;
	private String base;
	private String tarifa;
	
	public String getCodigoIca() {
		return codigoIca;
	}
	public void setCodigoIca(String codigoIca) {
		this.codigoIca = codigoIca;
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
	public String getBase() {
		return base;
	}
	public void setBase(String base) {
		this.base = base;
	}
	public String getTarifa() {
		return tarifa;
	}
	public void setTarifa(String tarifa) {
		this.tarifa = tarifa;
	}
	
}
