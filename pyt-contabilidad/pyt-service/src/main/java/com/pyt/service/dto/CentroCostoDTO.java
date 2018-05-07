package com.pyt.service.dto;
/**
 * areas para contabilizar los costos
 * 
 * @author alejando parra
 * @since 06/05/2018
 */
public class CentroCostoDTO extends ADto{
	private String codigo;
	private String nombre;
	private String descripcion;
	private String estado;
	private Integer orden;
	private EmpresaDTO empresa;
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
	public EmpresaDTO getEmpresa() {
		return empresa;
	}
	public void setEmpresa(EmpresaDTO empresa) {
		this.empresa = empresa;
	}
	
}
