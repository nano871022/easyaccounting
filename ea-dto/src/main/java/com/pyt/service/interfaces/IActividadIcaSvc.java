package com.pyt.service.interfaces;

import java.util.List;

import org.pyt.common.exceptions.ActividadIcaException;

import com.pyt.service.dto.ActividadIcaDTO;

import co.com.japl.ea.dto.system.UsuarioDTO;

/**
 * Se realiza un servicio para realizar crud de actividades iva
 * 
 * @author alejandro parra
 * @since 06/05/2018
 */
public interface IActividadIcaSvc {
	/**
	 * Se encargad ede obtener las actividades en una paginacion
	 * 
	 * @param dto
	 *            {@link ActividadIcaDTO}
	 * @param init
	 *            {@link Integer}
	 * @param end
	 *            {@link Integer}
	 * @return {@link List} of {@link ActividadIcaDTO}
	 * @throws {@link
	 *             ActividadIcaException}
	 */
	public List<ActividadIcaDTO> getActividadesIca(ActividadIcaDTO dto, Integer init, Integer end)
			throws ActividadIcaException;

	/**
	 * Se encarga de obtener todas las actividades ica
	 * 
	 * @param dto
	 *            {@link ActividadIcaDTO}
	 * @return {@link List} of {@link ActividadIcaDTO}
	 * @throws {@link
	 *             ActividadIcaException}
	 */
	public List<ActividadIcaDTO> getAllActividadesIca(ActividadIcaDTO dto) throws ActividadIcaException;

	/**
	 * Se encarga de obtener un solo regisro de la actividad ica
	 * 
	 * @param dto
	 *            {@link ActividadIcaDTO}
	 * @return {@link ActividadIcaDTO}
	 * @throws {@link
	 *             ActividadIcaException}
	 */
	public ActividadIcaDTO getActividadesIca(ActividadIcaDTO dto) throws ActividadIcaException;

	/**
	 * Se encarga de realizar la actualizacion de un registro
	 * 
	 * @param dto
	 *            {@link ActividadIcaDTO}
	 * @param user
	 *            {@link UsuarioDTO}
	 * @throws {@link
	 *             ActividadIcaException}
	 */
	public void update(ActividadIcaDTO dto, UsuarioDTO user) throws ActividadIcaException;

	/**
	 * Se e carga de realizar el ingreso de un registro
	 * 
	 * @param dto
	 *            {@link ActividadIcaDTO}
	 * @param user
	 *            {@link UsuarioDTO}
	 * @return {@link ActividadIcaDTO}
	 * @throws {@link
	 *             ActividadIcaException}
	 */
	public ActividadIcaDTO insert(ActividadIcaDTO dto, UsuarioDTO user) throws ActividadIcaException;

	/**
	 * Se encarga de realizar la eliminacion de un registro
	 * 
	 * @param dto
	 *            {@link ActividadIcaDTO}
	 * @param user
	 *            {@link UsuarioDTO}
	 * @throws {@link
	 *             ActividadIcaDTO}
	 */
	public void delete(ActividadIcaDTO dto, UsuarioDTO user) throws ActividadIcaException;
	/**
	 * Se encarga de obtner la cantidad de registros aplicado el filtro
	 * @param filter {@link ActividadIcaDTO}
	 * @return {@link Integer}
	 * @throws {@link ActividadIcaException}
	 */
	public Integer getTotalRows(ActividadIcaDTO filter)throws ActividadIcaException;
}
