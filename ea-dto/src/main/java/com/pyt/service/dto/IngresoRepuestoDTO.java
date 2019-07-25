package com.pyt.service.dto;

import org.pyt.common.abstracts.ADto;

public class IngresoRepuestoDTO extends ADto {
	private static final long serialVersionUID = 1L;
	private IngresoDTO ingreso;
	private RepuestoDTO repuesto;

	public IngresoDTO getIngreso() {
		return this.ingreso;
	}

	public void setIngreso(IngresoDTO ingreso) {
		this.ingreso = ingreso;
	}

	public RepuestoDTO getRepuesto() {
		return this.repuesto;
	}

	public void setRepuesto(RepuestoDTO repuesto) {
		this.repuesto = repuesto;
	}

}
