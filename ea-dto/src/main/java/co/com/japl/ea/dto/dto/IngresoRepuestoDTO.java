package co.com.japl.ea.dto.dto;

import co.com.japl.ea.common.abstracts.ADto;
import co.com.japl.ea.dto.dto.inventario.ResumenProductoDTO;

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
