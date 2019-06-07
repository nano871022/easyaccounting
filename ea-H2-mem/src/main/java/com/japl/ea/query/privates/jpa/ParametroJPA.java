package com.japl.ea.query.privates.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Se almacenan todos los parametros que se van a utilizar en la aplicacion y se
 * separan por grupos.
 * 
 * @author alejandro parra
 * @since 06/05/2018
 */
@Entity(name = "TBL_PARAMETER")
@Table(name = "TBL_PARAMETER")
public class ParametroJPA extends AJPA {
	@Column(name="norder")
	private Long orden;
	@Column(name="sname")
	private String nombre;
	@Column(name="sdescription")
	private String descripcion;
	@Column(name="svalue")
	private String valor;
	@Column(name="svalue2")
	private String valor2;
	@Column(name="sgroup")
	private String grupo;
	@Column(name="nstate")
	private String estado;
	
	public Long getOrden() {
		return orden;
	}

	public void setOrden(Long orden) {
		this.orden = orden;
	}

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

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getValor2() {
		return valor2;
	}

	public void setValor2(String valor2) {
		this.valor2 = valor2;
	}

	public String getGrupo() {
		return grupo;
	}

	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
}
