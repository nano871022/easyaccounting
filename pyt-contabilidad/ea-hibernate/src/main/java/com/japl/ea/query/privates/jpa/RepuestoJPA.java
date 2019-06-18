package com.japl.ea.query.privates.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Nombre de los repuestos que se usaran para resolver problemas de los
 * vehiculos
 * 
 * @author alejandro parra
 * @since 06/05/2018
 */
@Entity(name="TBL_REPLACEMENT")
@Table(name="TBL_REPLACEMENT")
public class RepuestoJPA extends AJPA {
	@Column(name="sname")
	private String nombre;
	@Column(name="sreference")
	private String referencia;
	@Column(name="nvaluemarket")
	private Long valorMercado;
	@Column(name="nvaluesale")
	private Long valorVenta;
	@Column(name="nivapercent")
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
