package com.japl.ea.query.privates.jpa;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.pyt.common.abstracts.ADto;

import com.pyt.service.dto.inventario.ResumenProductoDto;

import co.com.arquitectura.annotation.proccessor.DelClass;
import co.com.arquitectura.annotation.proccessor.UpdClass;

/**
 * Se encarga de tener en almacenamiento del ingreso de un vehiculo, en el cual se
 * realiza lo que se pretende realizar sobre el vehiculo 
 * 
 * @author alejandro parra
 * @since 06/05/2018
 */
@Entity(name="TBL_ENTRY")
@Table(name="TBL_ENTRY")
public class IngresoJPA extends AJPA{
	@Column(name="splate")
	private String placaVehiculo;
	@Column(name="sdescriiption")
	private String descripcion;
	private List<ServicioJPA> servicios;
	private List<ResumenProductoDto> respuestos;
	@Column(name="sowner")
	private PersonaJPA propietario;
	@Column(name="sdriverin")
	private PersonaJPA conductorEntrada;
	@Column(name="sdriverout")
	private PersonaJPA conductorSalida;
	@Column(name="sphonecontact")
	private String telefonoContacto;
	@Column(name="senterprise")
	private EmpresaJPA empresa;
	@Column(name="sworker")
	private TrabajadorJPA trabajador;
	@Column(name="din")
	private Date fechaEntrada;
	@Column(name="dout")
	private Date fechaSalida;
	@Column(name="nstimatedtime")
	private Long tiempoEstimado;
	@Column(name="nworktime")
	private Long tiempoTrabajado;
	
	public TrabajadorJPA getTrabajador() {
		return trabajador;
	}
	public void setTrabajador(TrabajadorJPA trabajador) {
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
	public List<ServicioJPA> getServicios() {
		return servicios;
	}
	public void setServicios(List<ServicioJPA> servicios) {
		this.servicios = servicios;
	}
	public List<ResumenProductoDto> getRespuestos() {
		return respuestos;
	}
	public void setRespuestos(List<ResumenProductoDto> respuestos) {
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
	public EmpresaJPA getEmpresa() {
		return empresa;
	}
	public void setEmpresa(EmpresaJPA empresa) {
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
