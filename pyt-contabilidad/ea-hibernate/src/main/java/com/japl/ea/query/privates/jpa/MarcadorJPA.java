package com.japl.ea.query.privates.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * Se encarga de almacenar los marcadores asociados a la configuracion
 * @author Alejandro Parra
 * @since 16/09/2018
 */
@Entity(name="TBL_MARKER")
@Table(name="TBL_MARKER")
public class MarcadorJPA extends AJPA {
	@Column(name="norder")
	private Integer orden;
	@Column(name="smark")
	private String marcador;
	@Column(name="siotype")
	private String tipoInOut;
	@Column(name="sconfiguration")
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
