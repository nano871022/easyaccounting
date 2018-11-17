package com.pyt.service.dto;

import org.pyt.common.common.ADto;
/**
 * Se encarga de almacenar las configuraciones
 * @author Alejandro Parra
 * @since 16/09/2018
 */
public class ConfiguracionDTO extends ADto {
	private static final long serialVersionUID = -5794475592464268409L;
	private String configuracion;
	private String archivo;
	private String archivoSalida;
	private String descripcion;
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
