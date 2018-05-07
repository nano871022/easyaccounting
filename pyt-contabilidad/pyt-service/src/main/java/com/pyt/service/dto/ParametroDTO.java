package com.pyt.service.dto;

/**
 * Se almacenan todos los parametros que se van a utilizar en la aplicacion y se
 * separan por grupos.
 * 
 * @author alejandro parra 
 * @since 06/05/2018
 */
public class ParametroDTO extends ADto{
	private String codigo;
	private String nombre;
	private String descripcion;
	private String valor;
	private String valor2;
	private String grupo;
	private String estado;
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
