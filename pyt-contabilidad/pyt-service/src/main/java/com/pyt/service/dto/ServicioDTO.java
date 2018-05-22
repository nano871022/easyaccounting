package com.pyt.service.dto;

import org.pyt.common.common.ADto;

/**
 * Nombre de los servicios que ofrece la empresa
 * @author alejandro parra 
 * @since 06/05/2018
 */
public class ServicioDTO extends ADto{
	private String nombre;
	private Long  valorManoObra;
	private String descripcion;
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
	public Long getValorManoObra() {
		return valorManoObra;
	}
	public void setValorManoObra(Long valorManoObra) {
		this.valorManoObra = valorManoObra;
	}
}
