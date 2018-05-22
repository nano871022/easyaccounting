package com.pyt.service.dto;

import org.pyt.common.common.ADto;

/**
 * Son los bancos que la empresa usa para mover el dinero
 * 
 * @author Alejandro Parra
 * @since 06/05/2018
 */
public class BancoDTO extends ADto{
	private String codigo;
	private String nombre;
	private String descripcion;
	private String numeroCuenta;
	private String fechaApertura;
	private String fechaCierre;
	private ParametroDTO estado;
	private ParametroDTO tipoCuenta;
	private ParametroDTO tipoBanco;
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getNumeroCuenta() {
		return numeroCuenta;
	}
	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}
	public String getFechaApertura() {
		return fechaApertura;
	}
	public void setFechaApertura(String fechaApertura) {
		this.fechaApertura = fechaApertura;
	}
	public String getFechaCierre() {
		return fechaCierre;
	}
	public void setFechaCierre(String fechaCierre) {
		this.fechaCierre = fechaCierre;
	}
	public ParametroDTO getEstado() {
		return estado;
	}
	public void setEstado(ParametroDTO estado) {
		this.estado = estado;
	}
	public ParametroDTO getTipoBanco() {
		return tipoBanco;
	}
	public void setTipoBanco(ParametroDTO tipoBanco) {
		this.tipoBanco = tipoBanco;
	}
	public ParametroDTO getTipoCuenta() {
		return tipoCuenta;
	}
	public void setTipoCuenta(ParametroDTO tipoCuenta) {
		this.tipoCuenta = tipoCuenta;
	}

}
