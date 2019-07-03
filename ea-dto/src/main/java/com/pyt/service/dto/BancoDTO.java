package com.pyt.service.dto;

import java.time.LocalDate;

import org.pyt.common.abstracts.ADto;
import org.pyt.common.annotation.generics.AssingValue;
import org.pyt.common.constants.ParametroConstants;

import co.com.arquitectura.annotation.proccessor.DelClass;
import co.com.arquitectura.annotation.proccessor.UpdClass;

/**
 * Son los bancos que la empresa usa para mover el dinero
 * 
 * @author Alejandro Parra
 * @since 06/05/2018
 */
@DelClass(nombre="com.pyt.service.dto.dels.BancoDelDTO")
@UpdClass(nombre="com.pyt.service.dto.upds.BancoUpdDTO")
public class BancoDTO extends ADto{
	private static final long serialVersionUID = -5471821713015787406L;
	private String nombre;
	private String descripcion;
	private String numeroCuenta;
	private LocalDate fechaApertura;
	private LocalDate fechaCierre;
	@AssingValue(nameField=ParametroConstants.FIELD_NAME_GROUP,value=ParametroConstants.GRUPO_ESTADO_BANCO)
	@AssingValue(nameField=ParametroConstants.FIELD_NAME_STATE,value=ParametroConstants.COD_ESTADO_PARAMETRO_ACTIVO)
	private ParametroDTO estado;
	@AssingValue(nameField=ParametroConstants.FIELD_NAME_GROUP,value=ParametroConstants.GRUPO_TIPO_CUENTA)
	@AssingValue(nameField=ParametroConstants.FIELD_NAME_STATE,value=ParametroConstants.COD_ESTADO_PARAMETRO_ACTIVO)
	private ParametroDTO tipoCuenta;
	@AssingValue(nameField=ParametroConstants.FIELD_NAME_GROUP,value=ParametroConstants.GRUPO_TIPO_BANCO)
	@AssingValue(nameField=ParametroConstants.FIELD_NAME_STATE,value=ParametroConstants.COD_ESTADO_PARAMETRO_ACTIVO)
	private ParametroDTO tipoBanco;
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
	public LocalDate getFechaApertura() {
		return fechaApertura;
	}
	public void setFechaApertura(LocalDate fechaApertura) {
		this.fechaApertura = fechaApertura;
	}
	public LocalDate getFechaCierre() {
		return fechaCierre;
	}
	public void setFechaCierre(LocalDate fechaCierre) {
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
