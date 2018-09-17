package com.pyt.service.dto;

import org.pyt.common.common.ADto;
/**
 * Se encarga de almacenar los marcadores asociados a la configuracion
 * @author Alejandro Parra
 * @since 16/09/2018
 */
public class MarcadorDTO extends ADto {
	private static final long serialVersionUID = 4555138682411497766L;
	private String marcador;
	private String configuracion;
	public String getMarcador() {
		return marcador;
	}
	public void setMarcador(String marcador) {
		this.marcador = marcador;
	}
	public String getConfiguracion() {
		return configuracion;
	}
	public void setConfiguracion(String configuracion) {
		this.configuracion = configuracion;
	}
}
