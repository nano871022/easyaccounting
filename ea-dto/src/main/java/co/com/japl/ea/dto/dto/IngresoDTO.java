package co.com.japl.ea.dto.dto;

import java.util.Date;

import co.com.arquitectura.annotation.proccessor.DelClass;
import co.com.arquitectura.annotation.proccessor.UpdClass;
import co.com.japl.ea.common.abstracts.ADto;

/**
 * Se encarga de tener en almacenamiento del ingreso de un vehiculo, en el cual
 * se realiza lo que se pretende realizar sobre el vehiculo
 * 
 * @author alejandro parra
 * @since 06/05/2018
 */
@DelClass(nombre = "co.com.japl.ea.dto.dto.dels.IngresoDelDTO")
@UpdClass(nombre = "co.com.japl.ea.dto.dto.upds.IngresoUpdDTO")

public class IngresoDTO extends ADto {
	private static final long serialVersionUID = -199744420238673033L;
	private String placaVehiculo;
	private String descripcion;
	private PersonaDTO propietario;
	private PersonaDTO conductorEntrada;
	private PersonaDTO conductorSalida;
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

	public PersonaDTO getPropietario() {
		return this.propietario;
	}

	public void setPropietario(PersonaDTO propietario) {
		this.propietario = propietario;
	}

	public PersonaDTO getConductorEntrada() {
		return this.conductorEntrada;
	}

	public void setConductorEntrada(PersonaDTO conductorEntrada) {
		this.conductorEntrada = conductorEntrada;
	}

	public PersonaDTO getConductorSalida() {
		return this.conductorSalida;
	}

	public void setConductorSalida(PersonaDTO conductorSalida) {
		this.conductorSalida = conductorSalida;
	}

}
