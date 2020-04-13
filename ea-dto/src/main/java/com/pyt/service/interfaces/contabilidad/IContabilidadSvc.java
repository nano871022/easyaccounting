package com.pyt.service.interfaces.contabilidad;

import org.pyt.common.exceptions.accounting.ContabilidadException;

import co.com.japl.ea.dto.contabilidad.GenerarCuotaDTO;
import co.com.japl.ea.dto.contabilidad.PagoDTO;
import co.com.japl.ea.dto.system.UsuarioDTO;

public interface IContabilidadSvc {
	/**
	 * Se encarga de cargar el pago y realizar todas las efectaciones necesarias
	 * @param pago {@link PagoDTO}
	 * @throws {@link ContabilidadException}
	 */
	public void loadPayed(PagoDTO pago,UsuarioDTO user) throws ContabilidadException;
	/**
	 * Se encarga de crear las cuotas a las cuales se realiza el pago de la factura, y se almacena esta info para tener una referencia de como fue el planteamiento inicial de eso ya que las cuotas pueden ser modificadas
	 * @param cuotas {@link GenerarCuotaDTO}
	 * @param user {@link UsuarioDTO}
	 * @throws {@link ContabilidadException}
	 */
	public void generarCuotas(GenerarCuotaDTO cuotas,UsuarioDTO user)throws ContabilidadException;
	
}
