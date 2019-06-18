package com.japl.ea.query.privates.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * Se encarga de almacenar las configuraciones
 * @author Alejandro Parra
 * @since 16/09/2018
 */
@Entity(name="TBL_CONFIGURATION")
@Table(name="TBL_CONFIGURATION")
public class ConfiguracionJPA extends AJPA {
	@Column(name="sconfiguration")
	private String configuracion;
	@Column(name="sfile")
	private String archivo;
	@Column(name="soutputfile")
	private String archivoSalida;
	@Column(name="sdescription")
	private String descripcion;
	@Column(name="nreport")
	private Boolean report;
	public String getArchivo() {
		return archivo;
	}
	public void setArchivo(String archivo) {
		this.archivo = archivo;
	}
	public String getConfiguracion() {
		return configuracion;
	}
	public void setConfiguracion(String configuracion) {
		this.configuracion = configuracion;
	}
	public String getArchivoSalida() {
		return archivoSalida;
	}
	public void setArchivoSalida(String archivoSalida) {
		this.archivoSalida = archivoSalida;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Boolean getReport() {
		return report;
	}
	public void setReport(Boolean report) {
		this.report = report;
	}
}
