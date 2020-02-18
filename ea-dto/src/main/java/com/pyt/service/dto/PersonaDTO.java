package com.pyt.service.dto;

import java.util.Date;
import java.util.Optional;

import org.pyt.common.abstracts.ADto;
import org.pyt.common.annotation.generics.DefaultFieldToGeneric;

import co.com.arquitectura.annotation.proccessor.DelClass;
import co.com.arquitectura.annotation.proccessor.UpdClass;

/**
 * Datos basicos de una persona
 * 
 * @author alejandro parra
 * @since 06/05/2018
 */
@DelClass(nombre = "com.pyt.service.dto.dels.PersonaDelDTO")
@UpdClass(nombre = "com.pyt.service.dto.upds.PersonaUpdDTO")

public class PersonaDTO extends ADto {
	private static final long serialVersionUID = 5798223959158769375L;
	@DefaultFieldToGeneric(simpleNameClazzBean = "PopupGenBean", use = DefaultFieldToGeneric.Uses.COLUMN)
	@DefaultFieldToGeneric(simpleNameClazzBean = "PopupGenBean", use = DefaultFieldToGeneric.Uses.FILTER)
	private String nombre;
	@DefaultFieldToGeneric(simpleNameClazzBean = "PopupGenBean", use = DefaultFieldToGeneric.Uses.COLUMN)
	@DefaultFieldToGeneric(simpleNameClazzBean = "PopupGenBean", use = DefaultFieldToGeneric.Uses.FILTER)

	private String apellido;
	private ParametroDTO tipoDocumento;
	@DefaultFieldToGeneric(simpleNameClazzBean = "PopupGenBean", use = DefaultFieldToGeneric.Uses.COLUMN)
	@DefaultFieldToGeneric(simpleNameClazzBean = "PopupGenBean", use = DefaultFieldToGeneric.Uses.FILTER)
	private String documento;
	private String direccion;
	private String telefono;
	private Date fechaNacimiento;
	private String email;
	private ParametroDTO profesion;
	private String numeroTarjetaProfesional;

	public ParametroDTO getProfesion() {
		return profesion;
	}

	public void setProfesion(ParametroDTO profesion) {
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

	public ParametroDTO getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(ParametroDTO tipoDocumento) {
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

	public String getNombres() {
		return String.format("%s %s", Optional.ofNullable(getNombre()).orElse(""),
				Optional.ofNullable(getApellido()).orElse("")).trim();
	}

}
