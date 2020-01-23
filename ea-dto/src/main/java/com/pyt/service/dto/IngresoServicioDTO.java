package com.pyt.service.dto;

import org.pyt.common.abstracts.ADto;

public class IngresoServicioDTO extends ADto {
	private static final long serialVersionUID = 1L;
	private IngresoDTO ingreso;
	private ServicioDTO servicio;

	public IngresoDTO getIngreso() {
		return this.ingreso;
	}

	public void setIngreso(IngresoDTO ingreso) {
		this.ingreso = ingreso;
	}

	public ServicioDTO getServicio() {
		return this.servicio;
	}

	public void setServicio(ServicioDTO servicio) {
		this.servicio = servicio;
	}

}
