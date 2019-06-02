package com.japl.ea.query.privates.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.pyt.common.abstracts.ADto;
@Entity(name="TBL_MARKER_SERVICE")
@Table(name="TBL_MARKER_SERVICE")
public class MarcadorServicioJPA extends ADto {
	private static final long serialVersionUID = 4348093849805335955L;
	@Column(name="smark")
	private String marcador;
	@Column(name="sservice")
	private String servicio;
	@Column(name="snamefield")
	private String nombreCampo;
	@Column(name="sconfiguration")
	private String configuracion;

	public String getMarcador() {
		return marcador;
	}

	public void setMarcador(String marcador) {
		this.marcador = marcador;
	}

	public String getServicio() {
		return servicio;
	}

	public void setServicio(String servicio) {
		this.servicio = servicio;
	}

	public String getNombreCampo() {
		return nombreCampo;
	}

	public void setNombreCampo(String nombreCampo) {
		this.nombreCampo = nombreCampo;
	}

	public String getConfiguracion() {
		return configuracion;
	}

	public void setConfiguracion(String configuracion) {
		this.configuracion = configuracion;
	}
}
