package com.japl.ea.query.privates.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
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
	//@ManyToOne 
	//@JoinColumn(name="sparameter")
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="sparameter",referencedColumnName="scode")
	private ParametroJPA parametro;
//	private String parametroCode;
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
//	public String getParametroCode() {
//		return parametroCode;
//	}
//	public void setParametroCode(String parametroCode) {
//		this.parametroCode = parametroCode;
//	}
	
}
