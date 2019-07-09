package com.pyt.service.dto.inventario;

import java.math.BigDecimal;

import org.pyt.common.abstracts.ADto;

public class SaldoDTO extends ADto {
	private static final long serialVersionUID = -4582108584069764218L;
	private MovimientoDTO movimiento;
	private Integer cantidad;
	private BigDecimal total;
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
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
}
