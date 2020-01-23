package com.japl.ea.query.privates.jpa;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
	@ManyToMany(targetEntity=ServicioJPA.class)
	private Set<ServicioJPA> servicios;
	@ManyToMany(targetEntity=ResumenProductoJPA.class)
	private Set<ResumenProductoJPA> respuestos;
	@ManyToOne @JoinColumn(name="sowner")
	private PersonaJPA propietario;
	@ManyToOne @JoinColumn(name="sdriverin")
	private PersonaJPA conductorEntrada;
	@ManyToOne @JoinColumn(name="sdriverout")
	private PersonaJPA conductorSalida;
	@Column(name="sphonecontact")
	private String telefonoContacto;
	@ManyToOne @JoinColumn(name="senterprise")
	private EmpresaJPA empresa;
	@ManyToOne @JoinColumn(name="sworker")
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
	public Set<ServicioJPA> getServicios() {
		return servicios;
	}
	public void setServicios(Set<ServicioJPA> servicios) {
		this.servicios = servicios;
	}
	public Set<ResumenProductoJPA> getRespuestos() {
		return respuestos;
	}
	public void setRespuestos(Set<ResumenProductoJPA> respuestos) {
		this.respuestos = respuestos;
	}
	public PersonaJPA getPropietario() {
		return propietario;
	}
	public void setPropietario(PersonaJPA propietario) {
		this.propietario = propietario;
	}
	public PersonaJPA getConductorEntrada() {
		return conductorEntrada;
	}
	public void setConductorEntrada(PersonaJPA conductorEntrada) {
		this.conductorEntrada = conductorEntrada;
	}
	public PersonaJPA getConductorSalida() {
		return conductorSalida;
	}
	public void setConductorSalida(PersonaJPA conductorSalida) {
		this.conductorSalida = conductorSalida;
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
