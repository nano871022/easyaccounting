package com.japl.ea.query.privates.jpa;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.pyt.common.annotation.generics.AssingValue;
import org.pyt.common.constants.ParametroConstants;

import com.pyt.service.dto.ParametroDTO;

/**
 * Son los bancos que la empresa usa para mover el dinero
 * 
 * @author Alejandro Parra
 * @since 06/05/2018
 */
 @Entity(name="TBL_BANK")
 @Table(name="TBL_BANK")
public class BancoJPA extends AJPA{
	 @Column(name="sname")
	private String nombre;
	 @Column(name="sdescription")
	private String descripcion;
	 @Column(name="saccount")
	private BigDecimal numeroCuenta;
	 @Column(name="dopening")
	private LocalDate fechaApertura;
	 @Column(name="dclosing")
	private LocalDate fechaCierre;
	 @Column(name="sstate")
	private ParametroJPA estado;
	 @Column(name="stypeaccount")
	private ParametroJPA tipoCuenta;
	 @Column(name="stypebank")
	private ParametroJPA tipoBanco;
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
