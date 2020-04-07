package co.com.japl.ea.dto.contabilidad;

import java.math.BigDecimal;

import org.pyt.common.abstracts.ADto;

import com.pyt.service.abstracts.ACajaDTO.CashType;

public class ResumenCajaDTO extends ADto {
	private CashType tipoCaja;
	private BigDecimal valor;
	public CashType getTipoCaja() {
		return tipoCaja;
	}
	public void setTipoCaja(CashType tipoCaja) {
		this.tipoCaja = tipoCaja;
	}
	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
}
