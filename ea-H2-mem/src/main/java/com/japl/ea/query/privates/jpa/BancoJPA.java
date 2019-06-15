package com.japl.ea.query.privates.jpa;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
	 @Column(name="snumberaccount")
	private String numeroCuenta;
	 @Column(name="dopening")
	private LocalDate fechaApertura;
	 @Column(name="dclosing")
	private LocalDate fechaCierre;
	 @ManyToOne @JoinColumn(name="sstate")
	private ParametroJPA estado;
	 @ManyToOne @JoinColumn(name="stypeaccount")
	private ParametroJPA tipoCuenta;
	 @ManyToOne @JoinColumn(name="stypebank")
	private ParametroJPA tipoBanco;
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
	public ParametroJPA getEstado() {
		return estado;
	}
	public void setEstado(ParametroJPA estado) {
		this.estado = estado;
	}
	public ParametroJPA getTipoBanco() {
		return tipoBanco;
	}
	public void setTipoBanco(ParametroJPA tipoBanco) {
		this.tipoBanco = tipoBanco;
	}
	public ParametroJPA getTipoCuenta() {
		return tipoCuenta;
	}
	public void setTipoCuenta(ParametroJPA tipoCuenta) {
		this.tipoCuenta = tipoCuenta;
	}

}
