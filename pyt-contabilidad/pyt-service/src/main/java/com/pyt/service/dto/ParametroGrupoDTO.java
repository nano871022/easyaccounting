package com.pyt.service.dto;

import org.pyt.common.common.ADto;

/**
 * Almacena el codigo de un grupo de parametro a un mapa de grupo para tratar de
 * realizar los valores que sean lo mas parametrizable posible
 * 
 * @author Alejandro Parra
 * @since 30-06-2018
 */
public class ParametroGrupoDTO extends ADto {
	private static final long serialVersionUID = -8003746118505697276L;
	private String parametro;
	private String grupo;
	public String getGrupo() {
		return grupo;
	}
	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}
	public String getParametro() {
		return parametro;
	}
	public void setParametro(String parametro) {
		this.parametro = parametro;
	}
}
