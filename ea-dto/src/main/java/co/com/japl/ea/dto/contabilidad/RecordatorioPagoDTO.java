package co.com.japl.ea.dto.contabilidad;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.pyt.common.abstracts.ADto;

import com.pyt.service.dto.DocumentoDTO;

public class RecordatorioPagoDTO extends ADto {
	private DocumentoDTO documento;
	private LocalDate fechaPago;
	private BigDecimal valorNeto;
	private BigDecimal valorImpuesto;
	public DocumentoDTO getDocumento() {
		return documento;
	}
	public void setDocumento(DocumentoDTO documento) {
		this.documento = documento;
	}
	public LocalDate getFechaPago() {
		return fechaPago;
	}
	public void setFechaPago(LocalDate fechaPago) {
		this.fechaPago = fechaPago;
	}
	public BigDecimal getValorNeto() {
		return valorNeto;
	}
	public void setValorNeto(BigDecimal valorNeto) {
		this.valorNeto = valorNeto;
	}
	public BigDecimal getValorImpuesto() {
		return valorImpuesto;
	}
	public void setValorImpuesto(BigDecimal valorImpuesto) {
		
		this.valorImpuesto = valorImpuesto;
	}
}
