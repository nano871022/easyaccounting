package co.com.japl.ea.dto.contabilidad;

import java.math.BigDecimal;

import com.pyt.service.abstracts.ACajaDTO;

public class CajaMenorDTO extends ACajaDTO {
	private static final long serialVersionUID = -8571148181044296402L;
	private BigDecimal valor;

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	
}
