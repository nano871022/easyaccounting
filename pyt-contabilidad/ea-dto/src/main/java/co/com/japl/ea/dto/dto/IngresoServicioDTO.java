package co.com.japl.ea.dto.dto;

import co.com.japl.ea.common.abstracts.ADto;

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
