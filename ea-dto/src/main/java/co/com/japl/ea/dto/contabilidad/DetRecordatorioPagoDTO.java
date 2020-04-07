package co.com.japl.ea.dto.contabilidad;

import java.math.BigDecimal;

import org.pyt.common.abstracts.ADto;

public class DetRecordatorioPagoDTO extends ADto {
	private RecordatorioPagoDTO recordatorio;
	private CuotaDTO cuota;
	private BigDecimal valorImpuesto;
	private BigDecimal valorNeto;
	public RecordatorioPagoDTO getRecordatorio() {
		return recordatorio;
	}
	public void setRecordatorio(RecordatorioPagoDTO recordatorio) {
		this.recordatorio = recordatorio;
	}
	public CuotaDTO getCuota() {
		return cuota;
	}
	public void setCuota(CuotaDTO cuota) {
		this.cuota = cuota;
	}
	public BigDecimal getValorImpuesto() {
		return valorImpuesto;
	}
	public void setValorImpuesto(BigDecimal valorImpuesto) {
		this.valorImpuesto = valorImpuesto;
	}
	public BigDecimal getValorNeto() {
		return valorNeto;
	}
	public void setValorNeto(BigDecimal valorNeto) {
		this.valorNeto = valorNeto;
	}
}
