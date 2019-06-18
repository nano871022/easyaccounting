package com.japl.ea.query.privates.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
@Entity(name="TBL_FIELD_SERVICE_SEARCH")
@Table(name="TBL_FIELD_SERVICE_SEARCH")
public class ServicioCampoBusquedaJPA extends AJPA {
	@Column(name="sservice")
	private String servicio;
	@Column(name="sfield")
	private String campo;
	@Column(name="scolumn")
	private Integer columna;
	@Column(name="sconfiguration")
	private String configuracion;
	@Column(name="smarker")
	private String marcador;
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
	public Integer getColumna() {
		return columna;
	}
	public void setColumna(Integer columna) {
		this.columna = columna;
	}
	public String getConfiguracion() {
		return configuracion;
	}
	public void setConfiguracion(String configuracion) {
		this.configuracion = configuracion;
	}
	public String getMarcador() {
		return marcador;
	}
	public void setMarcador(String marcador) {
		this.marcador = marcador;
	}
}
