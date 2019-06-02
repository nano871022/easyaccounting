package com.japl.ea.query.privates.jpa;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * Datos basicos de una persona
 * @author alejandro parra
 * @since 06/05/2018
 */
@Entity(name="TBL_PERSON")
@Table(name="TBL_PERSON")
public class PersonaJPA extends AJPA{
	@Column(name="sname")
	private String nombre;
	@Column(name="slastname")
	private String apellido;
	@Column(name="sdocumenttype")
	private String tipoDocumento;
	@Column(name="sdocument")
	private String documento;
	@Column(name="saddress")
	private String direccion;
	@Column(name="sphone")
	private String telefono;
	@Column(name="dbirthday")
	private Date fechaNacimiento;
	@Column(name="semail")
	private String email;
	@Column(name="sprofession")
	private ParametroJPA profesion;
	@Column(name="sprofessionalcard")
	private String numeroTarjetaProfesional;
	
	
	public ParametroJPA getProfesion() {
		return profesion;
	}
	public void setProfesion(ParametroJPA profesion) {
		this.profesion = profesion;
	}
	public String getNumeroTarjetaProfesional() {
		return numeroTarjetaProfesional;
	}
	public void setNumeroTarjetaProfesional(String numeroTarjetaProfesional) {
		this.numeroTarjetaProfesional = numeroTarjetaProfesional;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	public String getTipoDocumento() {
		return tipoDocumento;
	}
	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	public String getDocumento() {
		return documento;
	}
	public void setDocumento(String documento) {
		this.documento = documento;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}
	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}
	
}
