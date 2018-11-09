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
}
