package com.japl.ea.query.privates.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Almacena el codigo de un grupo de parametro a un mapa de grupo para tratar de
 * realizar los valores que sean lo mas parametrizable posible
 * 
 * @author Alejandro Parra
 * @since 30-06-2018
 */
@Entity(name="TBL_PARAMETER_GROUP")
@Table(name="TBL_PARAMETER_GROUP")
public class ParametroGrupoJPA extends AJPA {
	@Column(name="sparameter")
	private ParametroJPA parametro;
	@Column(name="sgroup")
	private String grupo;
	public String getGrupo() {
		return grupo;
	}
	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}
	public ParametroJPA getParametro() {
		return parametro;
	}
	public void setParametro(ParametroJPA parametro) {
		this.parametro = parametro;
	}
}
