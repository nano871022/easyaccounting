package com.pyt.service.dto;

import org.pyt.common.common.ADto;

/**
 * Nombre de los repuestos que se usaran para resolver problemas de los
 * vehiculos
 * 
 * @author alejandro parra
 * @since 06/05/2018
 */
public class RepuestoDTO extends ADto{
	private String nombre;
	private Long valorMercado;
	private Long valorVenta;
	private Long porcengajeIva;
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
	public Long getPorcengajeIva() {
		return porcengajeIva;
	}
	public void setPorcengajeIva(Long porcengajeIva) {
		this.porcengajeIva = porcengajeIva;
	}
	
}
