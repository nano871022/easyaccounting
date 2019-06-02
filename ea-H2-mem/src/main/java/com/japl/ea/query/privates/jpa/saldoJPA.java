package com.japl.ea.query.privates.jpa;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
@Entity(name="TBL_BALANCE")
@Table(name="TBL_BALANCE")
public class saldoJPA extends AJPA {
	@Column(name="smovement")
	private MovimientoJPA movimiento;
	@Column(name="nquantity")
	private Integer cantidad;
	@Column(name="ntotal")
	private BigDecimal total;
	public MovimientoJPA getMovimiento() {
		return movimiento;
	}
	public void setMovimiento(MovimientoJPA movimiento) {
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
