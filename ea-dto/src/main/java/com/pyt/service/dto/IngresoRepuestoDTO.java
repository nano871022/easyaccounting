package com.pyt.service.dto;

import org.pyt.common.abstracts.ADto;

import com.pyt.service.dto.inventario.ResumenProductoDTO;

public class IngresoRepuestoDTO extends ADto {
	private static final long serialVersionUID = 1L;
	private IngresoDTO ingreso;
	private ResumenProductoDTO repuesto;

	public IngresoDTO getIngreso() {
		return this.ingreso;
	}

	public void setIngreso(IngresoDTO ingreso) {
		this.ingreso = ingreso;
	}

	public ResumenProductoDTO getRepuesto() {
		return this.repuesto;
	}

	public void setRepuesto(ResumenProductoDTO repuesto) {
		this.repuesto = repuesto;
	}

}
