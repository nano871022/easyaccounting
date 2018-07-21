package com.pyt.service.dto;

import org.pyt.common.common.ADto;

/**
 * Son las actividades indicadas por el govierno
 * @author Alejandro Parra
 * @since 06/05/2018
 */
public class ActividadIcaDTO extends ADto{
	private String codigo;
	private String nombre;
	private String descripcion;
	private String base;
	private String tarifa;
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
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
