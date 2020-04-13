package com.pyt.service.interfaces.contabilidad;

import org.pyt.common.exceptions.accounting.ContabilidadException;

import co.com.japl.ea.dto.contabilidad.PagoDTO;
import co.com.japl.ea.dto.system.UsuarioDTO;

public interface IContabilidadSvc {
	/**
	 * Se encarga de cargar el pago y realizar todas las efectaciones necesarias
	 * @param pago {@link PagoDTO}
	 * @throws {@link ContabilidadException}
	 */
	public void loadPayed(PagoDTO pago,UsuarioDTO user) throws ContabilidadException;
	
}
