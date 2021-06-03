package co.com.japl.ea.dto.dto.inventario;

import co.com.japl.ea.common.abstracts.ADto;

/**
 * Este objeto se ecarga de restar la cantidad de de unidades vendidas a la cantidad de unidades por cada movimiento
 * @author Alejandro Parra
 * @since 18-12-2018 
 */
public class RestarCantidadDTO extends ADto {
	private static final long serialVersionUID = -4038179761217195064L;
	private MovimientoDTO movimiento;
	private Integer cantidad;
	public MovimientoDTO getMovimiento() {
		return movimiento;
	}
	public void setMovimiento(MovimientoDTO movimiento) {
		this.movimiento = movimiento;
	}
	public Integer getCantidad() {
		return cantidad;
	}
	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
}
