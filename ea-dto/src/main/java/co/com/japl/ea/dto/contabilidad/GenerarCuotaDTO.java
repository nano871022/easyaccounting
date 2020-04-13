package co.com.japl.ea.dto.contabilidad;

import java.time.LocalDate;

import org.pyt.common.abstracts.ADto;

import com.pyt.service.dto.DocumentoDTO;
import com.pyt.service.dto.ParametroDTO;

public class GenerarCuotaDTO extends ADto {
	private static final long serialVersionUID = 2822172974249414098L;
	
	private Integer numeroCuotas;
	private LocalDate fechaInicio;
	private ParametroDTO periocidad;
	private Double[] porcentaje;
	private DocumentoDTO documento;
	public Integer getNumeroCuotas() {
		return numeroCuotas;
	}
	public void setNumeroCuotas(Integer numeroCuotas) {
		this.numeroCuotas = numeroCuotas;
	}
	public LocalDate getFechaInicio() {
		return fechaInicio;
	}
	public void setFechaInicio(LocalDate fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	public ParametroDTO getPeriocidad() {
		return periocidad;
	}
	public void setPeriocidad(ParametroDTO periocidad) {
		this.periocidad = periocidad;
	}
	public Double[] getPorcentaje() {
		return porcentaje;
	}
	public void setPorcentaje(Double[] porcentaje) {
		this.porcentaje = porcentaje;
	}
	public DocumentoDTO getDocumento() {
		return documento;
	}
	public void setDocumento(DocumentoDTO documento) {
		this.documento = documento;
	}
}
