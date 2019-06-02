package com.japl.ea.query.privates.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Es el nombre de la empresa
 * @author alejandro parra
 * @since 05/06/2018
 */
@Entity(name="TBL_ENTERPRISE")
@Table(name="TBL_ENTERPRISE")
public class EmpresaJPA extends com.japl.ea.query.privates.jpa.AJPA{
	@Column(name="sname")
	private String nombre;
	@Column(name="snit")
	private String nit;
	@Column(name="sdigitvalid")
	private String digitoVerificacion;
	@Column(name="saddress")
	private String direccion;
	@Column(name="semail")
	private String correoElectronico;
	@Column(name="sphone")
	private String telefono;
	@Column(name="scountry")
	private PaisJPA pais;
	@Column(name="smoneydefect")
	private ParametroJPA monedaDefecto;
	@Column(name="srepresentative")
	private String nombreRepresentante;
	@Column(name="scounter")
	private PersonaJPA contador;
	
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
	public ParametroJPA getMonedaDefecto() {
		return monedaDefecto;
	}
	public void setMonedaDefecto(ParametroJPA monedaDefecto) {
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
