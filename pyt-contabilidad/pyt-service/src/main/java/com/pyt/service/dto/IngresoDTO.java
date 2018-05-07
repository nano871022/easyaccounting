package com.pyt.service.dto;

import java.util.Date;
import java.util.List;

/**
 * Se encarga de tener en almacenamiento del ingreso de un vehiculo, en el cual se
 * realiza lo que se pretende realizar sobre el vehiculo 
 * 
 * @author alejandro parra
 * @since 06/05/2018
 */
public class IngresoDTO extends ADto{
	private String placaVehiculo;
	private String descripcion;
	private List<ServicioDTO> servicios;
	private List<RepuestoDTO> respuestos;
	private String propietario;
	private String conductorEntrada;
	private String conductorSalida;
	private String documentoConductorEntrada;
	private String documentoConductorSaldia;
	private String telefonoContacto;
	private EmpresaDTO empresa;
	private Date fechaEntrada;
	private Date fechaSalida;
	private Long tiempoEstimado;
	private Long tiempoTrabajado;
	public String getPlacaVehiculo() {
		return placaVehiculo;
	}
	public void setPlacaVehiculo(String placaVehiculo) {
		this.placaVehiculo = placaVehiculo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public List<ServicioDTO> getServicios() {
		return servicios;
	}
	public void setServicios(List<ServicioDTO> servicios) {
		this.servicios = servicios;
	}
	public List<RepuestoDTO> getRespuestos() {
		return respuestos;
	}
	public void setRespuestos(List<RepuestoDTO> respuestos) {
		this.respuestos = respuestos;
	}
	public String getPropietario() {
		return propietario;
	}
	public void setPropietario(String propietario) {
		this.propietario = propietario;
	}
	public String getConductorEntrada() {
		return conductorEntrada;
	}
	public void setConductorEntrada(String conductorEntrada) {
		this.conductorEntrada = conductorEntrada;
	}
	public String getConductorSalida() {
		return conductorSalida;
	}
	public void setConductorSalida(String conductorSalida) {
		this.conductorSalida = conductorSalida;
	}
	public String getDocumentoConductorEntrada() {
		return documentoConductorEntrada;
	}
	public void setDocumentoConductorEntrada(String documentoConductorEntrada) {
		this.documentoConductorEntrada = documentoConductorEntrada;
	}
	public String getDocumentoConductorSaldia() {
		return documentoConductorSaldia;
	}
	public void setDocumentoConductorSaldia(String documentoConductorSaldia) {
		this.documentoConductorSaldia = documentoConductorSaldia;
	}
	public String getTelefonoContacto() {
		return telefonoContacto;
	}
	public void setTelefonoContacto(String telefonoContacto) {
		this.telefonoContacto = telefonoContacto;
	}
	public EmpresaDTO getEmpresa() {
		return empresa;
	}
	public void setEmpresa(EmpresaDTO empresa) {
		this.empresa = empresa;
	}
	public Date getFechaEntrada() {
		return fechaEntrada;
	}
	public void setFechaEntrada(Date fechaEntrada) {
		this.fechaEntrada = fechaEntrada;
	}
	public Date getFechaSalida() {
		return fechaSalida;
	}
	public void setFechaSalida(Date fechaSalida) {
		this.fechaSalida = fechaSalida;
	}
	public Long getTiempoEstimado() {
		return tiempoEstimado;
	}
	public void setTiempoEstimado(Long tiempoEstimado) {
		this.tiempoEstimado = tiempoEstimado;
	}
	public Long getTiempoTrabajado() {
		return tiempoTrabajado;
	}
	public void setTiempoTrabajado(Long tiempoTrabajado) {
		this.tiempoTrabajado = tiempoTrabajado;
	}
	
}
