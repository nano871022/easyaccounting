package com.pyt.service.dto;

import org.pyt.common.abstracts.ADto;

import co.com.arquitectura.annotation.proccessor.DelClass;
import co.com.arquitectura.annotation.proccessor.UpdClass;

/**
 * Nombre de los servicios que ofrece la empresa
 * @author alejandro parra 
 * @since 06/05/2018
 */
@DelClass(nombre="com.pyt.service.dto.dels.ServicioDelDTO")
@UpdClass(nombre="com.pyt.service.dto.upds.ServicioUpdDTO")

public class ServicioDTO extends ADto{
	private static final long serialVersionUID = -1416881593237077200L;
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
