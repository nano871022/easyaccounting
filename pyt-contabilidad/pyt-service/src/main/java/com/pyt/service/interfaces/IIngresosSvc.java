package com.pyt.service.interfaces;

import java.util.List;

import org.pyt.common.common.UsuarioDTO;
import org.pyt.common.exceptions.IngresoException;

import com.pyt.service.dto.IngresoDTO;

/**
 * Se encarga de realizar crud sobre los ingresos de vehiculos
 * 
 * @author alejanro parra
 * @since 06/05/2018
 */
public interface IIngresosSvc {
	/**
	 * Se encarga de obtener todos los registros de {@link IngresoDTO} 
	 * @param dto {@link IngresoDTO}
	 * @return {@link List} og {@link IngresoDTO}
	 * @throws {@link IngresoException}
	 */
	public List<IngresoDTO> getAllIngresos(IngresoDTO dto) throws IngresoException;
	/**
	 * Se encarga de obtener todos los registros paginados de {@link IngresoDTO}
	 * @param dto {@link IngresoDTO}
	 * @param init {@link Integer}
	 * @param end {@link Integer}
	 * @return {@link List} of {@link IngresoDTO}
	 * @throws {@link IngresoException}
	 */
	public List<IngresoDTO> getIngresos(IngresoDTO dto, Integer init, Integer end) throws IngresoException;
	/**
	 * Se encarga de obtener un solo registro de {@link IngresoDTO} 
	 * @param dto {@link IngresoDTO}
	 * @return {@link IngresoDTO}
	 * @throws {@link IngresoException}
	 */
	public IngresoDTO getIngreso(IngresoDTO dto) throws IngresoException;
	/**
	 * Se encarga de actualizar un registro en la tabla de {@link IngresoDTO}
	 * @param dto {@link IngresoDTO}
	 * @param user {@link UsuarioDTO}
	 * @throws {@link IngresoException}
	 */
	public void update(IngresoDTO dto, UsuarioDTO user) throws IngresoException;
	/**
	 * Se encarga de realizar la insercion de un registro en {@link IngresoDTO}
	 * @param dto {@link IngresoDTO}
	 * @param user {@link UsuarioDTO}
	 * @return {@link IngresoDTO}
	 * @throws IngresoException
	 */
	public IngresoDTO insert(IngresoDTO dto, UsuarioDTO user) throws IngresoException;
	/**
	 * Se encarga de realizar la eliminacion de un registro de {@link IngresoDTO}
	 * @param dto {@link IngresoDTO}
	 * @param user {@link UsuarioDTO}
	 * @throws {@link IngresoException}
	 */
	public void delete(IngresoDTO dto, UsuarioDTO user) throws IngresoException;
}
