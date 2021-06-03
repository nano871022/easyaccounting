package co.com.japl.ea.dto.system;

import java.time.LocalDate;

import co.com.japl.ea.common.abstracts.ADto;

public class LoginDTO extends ADto {
	private LocalDate fechaInicio;
	private LocalDate fechaFin;
	private String ipMaquina;
	private String ipPublica;
	private Boolean recordar;
	private UsuarioDTO usuario;
	public LocalDate getFechaInicio() {
		return fechaInicio;
	}
	public void setFechaInicio(LocalDate fechaIncio) {
		this.fechaInicio = fechaIncio;
	}
	public LocalDate getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(LocalDate fechaFin) {
		this.fechaFin = fechaFin;
	}
	public String getIpMaquina() {
		return ipMaquina;
	}
	public void setIpMaquina(String ipMaquina) {
		this.ipMaquina = ipMaquina;
	}
	public String getIpPublica() {
		return ipPublica;
	}
	public void setIpPublica(String ipPublica) {
		this.ipPublica = ipPublica;
	}
	public Boolean getRecordar() {
		return recordar;
	}
	public void setRecordar(Boolean recordad) {
		this.recordar = recordad;
	}
	public UsuarioDTO getUsuario() {
		return usuario;
	}
	public void setUsuario(UsuarioDTO usuario) {
		this.usuario = usuario;
	}
	
}
