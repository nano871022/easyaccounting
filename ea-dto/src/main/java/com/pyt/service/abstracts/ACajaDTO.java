package com.pyt.service.abstracts;

import java.time.LocalDate;

import org.pyt.common.abstracts.ADto;

import co.com.japl.ea.dto.contabilidad.PagoDTO;

public abstract class ACajaDTO extends ADto {
	public static enum CashType  {MAYOR,MENOR};
	private LocalDate fechaIngreso;
	private LocalDate fechaSalida;
	private CashType tipoCaja;
	private PagoDTO pago;
	public LocalDate getFechaIngreso() {
		return fechaIngreso;
	}
	public void setFechaIngreso(LocalDate fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}
	public LocalDate getFechaSalida() {
		return fechaSalida;
	}
	public void setFechaSalida(LocalDate fechaSalida) {
		this.fechaSalida = fechaSalida;
	}
	public CashType getTipoCaja() {
		return tipoCaja;
	}
	public void setTipoCaja(CashType tipoCaja) {
		this.tipoCaja = tipoCaja;
	}
	public PagoDTO getPago() {
		return pago;
	}
	public void setPago(PagoDTO pago) {
		this.pago = pago;
	}
	
}
