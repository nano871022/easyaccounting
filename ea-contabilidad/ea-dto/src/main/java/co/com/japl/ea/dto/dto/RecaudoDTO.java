package co.com.japl.ea.dto.dto;

import java.math.BigDecimal;

import co.com.japl.ea.common.abstracts.ADto;

public class RecaudoDTO extends ADto {

	private String numeroFactura;
	private String tipoDocumento;
	private String descripcion;
	private String codigoDocumento;
	private String acreditaa;
	private String debitaa;
	private BigDecimal valueIva;
	private BigDecimal valorPagado;
	public String getNumeroFactura() {
		return numeroFactura;
	}
	public void setNumeroFactura(String numeroFactura) {
		this.numeroFactura = numeroFactura;
	}
	public String getTipoDocumento() {
		return tipoDocumento;
	}
	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getCodigoDocumento() {
		return codigoDocumento;
	}
	public void setCodigoDocumento(String codigoDocumento) {
		this.codigoDocumento = codigoDocumento;
	}
	public String getAcreditaa() {
		return acreditaa;
	}
	public void setAcreditaa(String acreditaa) {
		this.acreditaa = acreditaa;
	}
	public String getDebitaa() {
		return debitaa;
	}
	public void setDebitaa(String debitaa) {
		this.debitaa = debitaa;
	}
	public BigDecimal getValueIva() {
		return valueIva;
	}
	public void setValueIva(BigDecimal valueIva) {
		this.valueIva = valueIva;
	}
	public BigDecimal getValorPagado() {
		return valorPagado;
	}
	public void setValorPagado(BigDecimal valorPagado) {
		this.valorPagado = valorPagado;
	}
}
