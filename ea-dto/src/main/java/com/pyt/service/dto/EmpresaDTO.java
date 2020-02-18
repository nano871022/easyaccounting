package com.pyt.service.dto;

import org.pyt.common.abstracts.ADto;

import co.com.arquitectura.annotation.proccessor.DelClass;
import co.com.arquitectura.annotation.proccessor.UpdClass;

/**
 * Es el nombre de la empresa
 * 
 * @author alejandro parra
 * @since 05/06/2018
 */
@DelClass(nombre = "com.pyt.service.dto.dels.EmpresaDelDTO")
@UpdClass(nombre = "com.pyt.service.dto.upds.EmpresaUpdDTO")

public class EmpresaDTO extends ADto {
	private static final long serialVersionUID = -332395796361732931L;
	private String nombre;
	private String nit;
	private String digitoVerificacion;
	private String direccion;
	private String correoElectronico;
	private String telefono;
	private PaisDTO pais;
	private ParametroDTO monedaDefecto;
	private PersonaDTO representante;
	private PersonaDTO contador;

	public EmpresaDTO() {
	}

	public EmpresaDTO(String codigo, String nombre, String nit, String digitoVerificacion, String direccion,
			String correoElectronico, String telefono, PaisDTO pais, ParametroDTO monedaDefecto,
			String nombreRepresentante, String nombreContador, String tarjetaProfeccionalContador) {
		super();
		this.nombre = nombre;
		this.nit = nit;
		this.digitoVerificacion = digitoVerificacion;
		this.direccion = direccion;
		this.correoElectronico = correoElectronico;
		this.telefono = telefono;
		this.pais = pais;
		this.monedaDefecto = monedaDefecto;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNit() {
		return nit;
	}

	public void setNit(String nit) {
		this.nit = nit;
	}

	public String getDigitoVerificacion() {
		return digitoVerificacion;
	}

	public void setDigitoVerificacion(String digitoVerificacion) {
		this.digitoVerificacion = digitoVerificacion;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getCorreoElectronico() {
		return correoElectronico;
	}

	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public PaisDTO getPais() {
		return pais;
	}

	public void setPais(PaisDTO pais) {
		this.pais = pais;
	}

	public ParametroDTO getMonedaDefecto() {
		return monedaDefecto;
	}

	public void setMonedaDefecto(ParametroDTO monedaDefecto) {
		this.monedaDefecto = monedaDefecto;
	}

	public PersonaDTO getRepresentante() {
		return this.representante;
	}

	public void setRepresentante(PersonaDTO representante) {
		this.representante = representante;
	}

	public PersonaDTO getContador() {
		return this.contador;
	}

	public void setContador(PersonaDTO contador) {
		this.contador = contador;
	}

}
