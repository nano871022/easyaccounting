package com.pyt.service.interfaces;

import java.util.List;

import org.pyt.common.common.UsuarioDTO;
import org.pyt.common.exceptions.RepuestoException;

import com.pyt.service.dto.RepuestoDTO;

/**
 * Se encarga de realizar crud sobre los registros de repuestos
 * 
 * @author alejandro parra
 * @since 06/05/2018
 */
public interface IRepuestosSvc {
	/**
	 * Se encarga de obtener todos los repuestos
	 * 
	 * @param dto
	 *            {@link RepuestoDTO}
	 * @return {@link List} of {@link RepuestoDTO}
	 * @throws {@link
	 *             RepuestoException}
	 */
	public List<RepuestoDTO> getAllRepuestos(RepuestoDTO dto) throws RepuestoException;

	/**
	 * Se encarga de obtener los registros paginados
	 * 
	 * @param dto
	 *            {@link RepuestoDTO}
	 * @param init
	 *            {@link Integer}
	 * @param end
	 *            {@link Integer}
	 * @return {@link List} of {@link RepuestoDTO}
	 * @throws {@link
	 *             RepuestoException}
	 */
	public List<RepuestoDTO> getRepuestos(RepuestoDTO dto, Integer init, Integer end) throws RepuestoException;

	/**
	 * Se encarga de obtner un registro
	 * 
	 * @param dto
	 *            {@link RepuestoDTO}
	 * @return {@link RepuestoDTO}
	 * @throws {@link
	 *             RepuestoException}
	 */
	public RepuestoDTO getRepuesto(RepuestoDTO dto) throws RepuestoException;

	/**
	 * Se encarga de actualizar un registro de repuesto
	 * 
	 * @param dto
	 *            {@link RepuestoDTO}
	 * @param user
	 *            {@link UsuarioDTO}
	 * @throws {@link
	 *             RepuestoException}
	 */
	public void update(RepuestoDTO dto, UsuarioDTO user) throws RepuestoException;

	/**
	 * Se encarga de insertar un registro en la tabla de repuestos
	 * 
	 * @param dto
	 *            {@link RepuestoDTO}
	 * @param user
	 *            {@link UsuarioDTO}
	 * @return {@link RepuestoDTO}
	 * @throws {@link
	 *             RepuestoException}
	 */
	public RepuestoDTO insert(RepuestoDTO dto, UsuarioDTO user) throws RepuestoException;

	/**
	 * Se encarga de eliminar un registro de {@link RepuestoException}
	 * 
	 * @param dto
	 *            {@link RepuestoDTO}
	 * @param user
	 *            {@link UsuarioDTO}
	 * @throws {@link
	 *             RepuestoException}
	 */
	public void delete(RepuestoDTO dto, UsuarioDTO user) throws RepuestoException;

	/**
	 * Se encarga de obtener la cantidad de registros segun el filtro aplicado
	 * @param dto {@link RepuestoDTO}
	 * @return {@link Integer}
	 * @throws {@link RepuestoException}
	 */
	public Integer getTotalRows(RepuestoDTO dto) throws RepuestoException;
}
