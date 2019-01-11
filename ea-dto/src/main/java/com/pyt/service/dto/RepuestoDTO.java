package com.pyt.service.dto;

import org.pyt.common.common.ADto;

import co.com.arquitectura.annotation.proccessor.DelClass;
import co.com.arquitectura.annotation.proccessor.UpdClass;

/**
 * Nombre de los repuestos que se usaran para resolver problemas de los
 * vehiculos
 * 
 * @author alejandro parra
 * @since 06/05/2018
 */
@DelClass(nombre="com.pyt.service.dto.dels.RepuestoDelDTO")
@UpdClass(nombre="com.pyt.service.dto.upds.RepuestoUpdDTO")

public class RepuestoDTO extends ADto {
	private static final long serialVersionUID = -105566083091576666L;
	private String nombre;
	private String referencia;
	private Long valorMercado;
	private Long valorVenta;
	private Long porcentajeIva;

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Long getValorMercado() {
		return valorMercado;
	}

	public void setValorMercado(Long valorMercado) {
		this.valorMercado = valorMercado;
	}

	public Long getValorVenta() {
		return valorVenta;
	}

	public void setValorVenta(Long valorVenta) {
		this.valorVenta = valorVenta;
	}

	public Long getPorcentajeIva() {
		return porcentajeIva;
	}

	public void setPorcentajeIva(Long porcentajeIva) {
		this.porcentajeIva = porcentajeIva;
	}

}
