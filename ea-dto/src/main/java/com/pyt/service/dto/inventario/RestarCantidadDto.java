package com.pyt.service.dto.inventario;

import org.pyt.common.common.ADto;

/**
 * Este objeto se ecarga de restar la cantidad de de unidades vendidas a la cantidad de unidades por cada movimiento
 * @author Alejandro Parra
 * @since 18-12-2018 
 */
public class RestarCantidadDto extends ADto {
	private static final long serialVersionUID = -4038179761217195064L;
	private MovimientoDto movimiento;
	private Integer cantidad;
	public MovimientoDto getMovimiento() {
		return movimiento;
	}
	public void setMovimiento(MovimientoDto movimiento) {
		this.movimiento = movimiento;
	}
	public Integer getCantidad() {
		return cantidad;
	}
	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
}
