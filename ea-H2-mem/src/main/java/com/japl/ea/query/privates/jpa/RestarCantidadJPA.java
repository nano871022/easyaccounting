package com.japl.ea.query.privates.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Este objeto se ecarga de restar la cantidad de de unidades vendidas a la cantidad de unidades por cada movimiento
 * @author Alejandro Parra
 * @since 18-12-2018 
 */
@Entity(name="TBL_REMAIN_QUANTITY")
@Table(name="TBL_REMAIN_QUANTITY")
public class RestarCantidadJPA extends AJPA {
	@Column(name="smovement")
	private MovimientoJPA movimiento;
	@Column(name="nquantity")
	private Integer cantidad;
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
}
