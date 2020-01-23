package com.japl.ea.query.privates.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * areas para contabilizar los costos
 * 
 * @author alejando parra
 * @since 06/05/2018
 */
@Entity(name="TBL_CENTER_COST")
@Table(name="TBL_CENTER_COST")
public class CentroCostoJPA extends AJPA{
	@Column(name="sname")
	private String nombre;
	@Column(name="sdescription")
	private String descripcion;
	@Column(name="sstate")
	private String estado;
	@Column(name="norder")
	private Integer orden;
	@ManyToOne @JoinColumn(name="senterprise")
	private EmpresaJPA empresa;
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
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public Integer getOrden() {
		return orden;
	}
	public void setOrden(Integer orden) {
		this.orden = orden;
	}
	public EmpresaJPA getEmpresa() {
		return empresa;
	}
	public void setEmpresa(EmpresaJPA empresa) {
		this.empresa = empresa;
	}
	
}
