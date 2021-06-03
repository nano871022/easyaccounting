package co.com.japl.ea.dto.dto;

import co.com.japl.ea.common.abstracts.ADto;

public class MarcadorServicioDTO extends ADto {
	private static final long serialVersionUID = 4348093849805335955L;
	private String marcador;
	private String servicio;
	private String nombreCampo;
	private ConfiguracionDTO configuracion;

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

	public ConfiguracionDTO getConfiguracion() {
		return configuracion;
	}

	public void setConfiguracion(ConfiguracionDTO configuracion) {
		this.configuracion = configuracion;
	}
}
