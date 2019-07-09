package com.pyt.service.dto;

import java.util.Date;
import java.util.List;

import org.pyt.common.abstracts.ADto;

import com.pyt.service.dto.inventario.ResumenProductoDTO;

import co.com.arquitectura.annotation.proccessor.DelClass;
import co.com.arquitectura.annotation.proccessor.UpdClass;

/**
 * Se encarga de tener en almacenamiento del ingreso de un vehiculo, en el cual se
 * realiza lo que se pretende realizar sobre el vehiculo 
 * 
 * @author alejandro parra
 * @since 06/05/2018
 */
@DelClass(nombre="com.pyt.service.dto.dels.IngresoDelDTO")
@UpdClass(nombre="com.pyt.service.dto.upds.IngresoUpdDTO")

public class IngresoDTO extends ADto{
	private static final long serialVersionUID = -199744420238673033L;
	private String placaVehiculo;
	private String descripcion;
	private List<ServicioDTO> servicios;
	private List<ResumenProductoDTO> respuestos;
	private String propietario;
	private String conductorEntrada;
	private String conductorSalida;
	private String documentoConductorEntrada;
	private String documentoConductorSalida;
	private String telefonoContacto;
	private EmpresaDTO empresa;
	private TrabajadorDTO trabajador;
	private Date fechaEntrada;
	private Date fechaSalida;
	private Long tiempoEstimado;
	private Long tiempoTrabajado;
	
	public TrabajadorDTO getTrabajador() {
		return trabajador;
	}
	public void setTrabajador(TrabajadorDTO trabajador) {
		this.trabajador = trabajador;
	}
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
	public List<ResumenProductoDTO> getRespuestos() {
		return respuestos;
	}
	public void setRespuestos(List<ResumenProductoDTO> respuestos) {
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
	public String getDocumentoConductorSalida() {
		return documentoConductorSalida;
	}
	public void setDocumentoConductorSalida(String documentoConductorSaldia) {
		this.documentoConductorSalida = documentoConductorSaldia;
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
