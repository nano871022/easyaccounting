package com.pyt.service.interfaces.contabilidad;

import java.util.List;

import org.pyt.common.exceptions.accounting.RecordatorioException;

import co.com.japl.ea.dto.contabilidad.CuotaDTO;
import co.com.japl.ea.dto.contabilidad.RecordatorioPagoDTO;
import co.com.japl.ea.dto.system.UsuarioDTO;

public interface IRecordatorioSvc {
	/**
	 * Se encarga de buscar todas las cuotas siguientes a ser cobradas a partir de la fecha actual
	 * 
	 * @return {@link List} by {@link CuotaDTO}
	 * @throws {@link RecordatorioException}
	 */
	public List<CuotaDTO> findNextQuotes(UsuarioDTO usuario)throws RecordatorioException;
	/**
	 * Se encarga de buscar todas las cuotas siguientes a ser cobradas a partir del numero de factura
	 * @param numeroFactura {@link String}
	 * @return {@link List} by {@link CuotaDTO}
	 * @throws {@link RecordatorioException}
	 */
	public List<CuotaDTO> findNextQuotes(String numeroFactura,UsuarioDTO usuario)throws RecordatorioException;
	/**
	 * Se encarga de genear el recordatorio apartir de la lista de cuotas suministadas
	 * @param cuotas {@link CuotaDTO}
	 * @return {@link RecordatorioPagoDTO}
	 * @throws {@link RecordatorioException}
	 */
	public RecordatorioPagoDTO generarRecordatorio(List<CuotaDTO> cuotas,UsuarioDTO usuario)throws RecordatorioException;
	
}
