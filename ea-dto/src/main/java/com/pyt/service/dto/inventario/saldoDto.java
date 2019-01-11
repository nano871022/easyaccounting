package com.pyt.service.dto.inventario;

import java.math.BigDecimal;

import org.pyt.common.common.ADto;

public class saldoDto extends ADto {
	private static final long serialVersionUID = -4582108584069764218L;
	private MovimientoDto movimiento;
	private Integer cantidad;
	private BigDecimal total;
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
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
}
