package com.pyt.service.dto;

import org.pyt.common.common.ADto;

/**
 * Es el nombre de la empresa
 * @author alejandro parra
 * @since 05/06/2018
 */
public class EmpresaDTO extends ADto{
	private String nombre;
	private String nit;
	private String digitoVerificacion;
	private String direccion;
	private String correoElectronico;
	private String telefono;
	private String pais;
	private ParametroDTO monedaDefecto;
	private String nombreRepresentante;
	private String nombreContador;
	private String tarjetaProfeccionalContador;
	
	public EmpresaDTO() {}
	
	public EmpresaDTO(String codigo, String nombre, String nit, String digitoVerificacion, String direccion,
			String correoElectronico, String telefono, String pais, ParametroDTO monedaDefecto,
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
		this.nombreRepresentante = nombreRepresentante;
		this.nombreContador = nombreContador;
		this.tarjetaProfeccionalContador = tarjetaProfeccionalContador;
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
	public String getPais() {
		return pais;
	}
	public void setPais(String pais) {
		this.pais = pais;
	}
	public ParametroDTO getMonedaDefecto() {
		return monedaDefecto;
	}
	public void setMonedaDefecto(ParametroDTO monedaDefecto) {
		this.monedaDefecto = monedaDefecto;
	}
	public String getNombreRepresentante() {
		return nombreRepresentante;
	}
	public void setNombreRepresentante(String nombreRepresentante) {
		this.nombreRepresentante = nombreRepresentante;
	}
	public String getNombreContador() {
		return nombreContador;
	}
	public void setNombreContador(String nombreContador) {
		this.nombreContador = nombreContador;
	}
	public String getTarjetaProfeccionalContador() {
		return tarjetaProfeccionalContador;
	}
	public void setTarjetaProfeccionalContador(String tarjetaProfeccionalContador) {
		this.tarjetaProfeccionalContador = tarjetaProfeccionalContador;
	}
	
}
