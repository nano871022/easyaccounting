package co.com.japl.ea.dto.contabilidad;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.pyt.common.abstracts.ADto;

import com.pyt.service.dto.DocumentoDTO;
import com.pyt.service.dto.ParametroDTO;

public class CuotaDTO extends ADto {
	private static final long serialVersionUID = -8826393056184202509L;
	private LocalDate fechaPago;
	private BigDecimal valorNeto;
	private BigDecimal valorImpuestos;
	private BigDecimal valor;
	private BigDecimal valorPagadoNeto;
	private Integer numeroCuota;
	private DocumentoDTO documento;
	private ParametroDTO estado;
	private Double iva;
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
	public BigDecimal getValorImpuestos() {
		return valorImpuestos;
	}
	public void setValorImpuestos(BigDecimal valorImpuestos) {
		this.valorImpuestos = valorImpuestos;
	}
	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	public BigDecimal getValorPagadoNeto() {
		return valorPagadoNeto;
	}
	public void setValorPagadoNeto(BigDecimal valorPagadoNeto) {
		this.valorPagadoNeto = valorPagadoNeto;
	}
	public Integer getNumeroCuota() {
		return numeroCuota;
	}
	public void setNumeroCuota(Integer numeroCuota) {
		this.numeroCuota = numeroCuota;
	}
	public DocumentoDTO getDocumento() {
		return documento;
	}
	public void setDocumento(DocumentoDTO documento) {
		this.documento = documento;
	}
	public ParametroDTO getEstado() {
		return estado;
	}
	public void setEstado(ParametroDTO estado) {
		this.estado = estado;
	}
	public Double getIva() {
		return iva;
	}
	public void setIva(Double iva) {
		this.iva = iva;
	}
}
