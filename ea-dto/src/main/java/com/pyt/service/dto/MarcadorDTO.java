package com.pyt.service.dto;

import org.pyt.common.abstracts.ADto;

/**
 * Se encarga de almacenar los marcadores asociados a la configuracion
 * 
 * @author Alejandro Parra
 * @since 16/09/2018
 */
public class MarcadorDTO extends ADto {
	private static final long serialVersionUID = 4555138682411497766L;
	private Integer orden;
	private String marcador;
	private String tipoInOut;
	private ConfiguracionDTO configuracion;

	public String getMarcador() {
		return marcador;
	}

	public void setMarcador(String marcador) {
		this.marcador = marcador;
	}

	public ConfiguracionDTO getConfiguracion() {
		return configuracion;
	}

	public void setConfiguracion(ConfiguracionDTO configuracion) {
		this.configuracion = configuracion;
	}

	public String getTipoInOut() {
		return tipoInOut;
	}

	public void setTipoInOut(String tipoInOut) {
		this.tipoInOut = tipoInOut;
	}

	public Integer getOrden() {
		return orden;
	}

	public void setOrden(Integer orden) {
		this.orden = orden;
	}
}
