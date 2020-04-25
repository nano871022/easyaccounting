package co.com.japl.ea.dto.contabilidad;

import java.math.BigDecimal;

import com.pyt.service.abstracts.ACajaDTO;
import com.pyt.service.dto.BancoDTO;

public class CajaMayorDTO extends ACajaDTO {
	private BigDecimal valor;
	private BancoDTO banco;
	private String numeroCuenta;
	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	public BancoDTO getBanco() {
		return banco;
	}
	public void setBanco(BancoDTO banco) {
		this.banco = banco;
	}
	public String getNumeroCuenta() {
		return numeroCuenta;
	}
	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}
}
