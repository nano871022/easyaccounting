package com.pyt.service.dto;

import org.pyt.common.common.ADto;

public class ServicioCampoBusquedaDTO extends ADto {
	private static final long serialVersionUID = 1584913745551304294L;
	private String servicio;
	private String campo;
	public String getServicio() {
		return servicio;
	}
	public void setServicio(String servicio) {
		this.servicio = servicio;
	}
	public String getCampo() {
		return campo;
	}
	public void setCampo(String campo) {
		this.campo = campo;
	}
}
