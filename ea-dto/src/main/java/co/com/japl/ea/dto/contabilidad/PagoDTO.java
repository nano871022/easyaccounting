package co.com.japl.ea.dto.contabilidad;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.pyt.common.abstracts.ADto;

import com.pyt.service.dto.BancoDTO;
import com.pyt.service.dto.DocumentoDTO;
import com.pyt.service.dto.ParametroDTO;

public class PagoDTO extends ADto{
	private static final long serialVersionUID = 876227054318142421L;
	private LocalDate fechaPago;
	private LocalDate fechaPagado;
	private String numeroCuenta;
	private String numeroComprobante;
	private BancoDTO banco;
	private CuotaDTO cuota;
	private BigDecimal valorImpuestos;
	private BigDecimal valorPagadoNeto;
	private BigDecimal valorAPAgar;
	private Boolean pagoTotal;
	private Boolean pagoParcial;
	private Boolean pagoCuota;
	public LocalDate getFechaPago() {
		return fechaPago;
	}
	public void setFechaPago(LocalDate fechaPago) {
		this.fechaPago = fechaPago;
	}
	public LocalDate getFechaPagado() {
		return fechaPagado;
	}
	public void setFechaPagado(LocalDate fechaPagado) {
		this.fechaPagado = fechaPagado;
	}
	public String getNumeroCuenta() {
		return numeroCuenta;
	}
	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}
	public String getNumeroComprobante() {
		return numeroComprobante;
	}
	public void setNumeroComprobante(String numeroComprobante) {
		this.numeroComprobante = numeroComprobante;
	}
	public BancoDTO getBanco() {
		return banco;
	}
	public void setBanco(BancoDTO banco) {
		this.banco = banco;
	}
	public CuotaDTO getCuota() {
		return cuota;
	}
	public void setCuota(CuotaDTO cuota) {
		this.cuota = cuota;
	}
	public BigDecimal getValorImpuestos() {
		return valorImpuestos;
	}
	public void setValorImpuestos(BigDecimal valorImpuestos) {
		this.valorImpuestos = valorImpuestos;
	}
	public BigDecimal getValorPagadoNeto() {
		return valorPagadoNeto;
	}
	public void setValorPagadoNeto(BigDecimal valorPagadoNeto) {
		this.valorPagadoNeto = valorPagadoNeto;
	}
	public BigDecimal getValorAPAgar() {
		return valorAPAgar;
	}
	public void setValorAPAgar(BigDecimal valorAPAgar) {
		this.valorAPAgar = valorAPAgar;
	}
	public Boolean getPagoTotal() {
		return pagoTotal;
	}
	public void setPagoTotal(Boolean pagoTotal) {
		this.pagoTotal = pagoTotal;
	}
	public Boolean getPagoParcial() {
		return pagoParcial;
	}
	public void setPagoParcial(Boolean pagoParcial) {
		this.pagoParcial = pagoParcial;
	}
	public Boolean getPagoCuota() {
		return pagoCuota;
	}
	public void setPagoCuota(Boolean pagoCuota) {
		this.pagoCuota = pagoCuota;
	}
}
	
	

