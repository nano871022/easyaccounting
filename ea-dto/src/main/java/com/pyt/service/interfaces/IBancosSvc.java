package com.pyt.service.interfaces;
/**
 * Se encarga de realizar crud sobre los registro de la tabla de bancos
 * @author alejandro parra
 * @since 06/05/2018
 */

import java.util.List;

import org.pyt.common.exceptions.BancoException;

import com.pyt.service.dto.BancoDTO;

import co.com.japl.ea.dto.system.UsuarioDTO;

/**
 * Se encarga de realizar crud sobre sobre los registros
 * 
 * @author alejandro parra
 * @since 06/05/2018
 */
public interface IBancosSvc {
	/**
	 * Se encarga de obtener todos los registros de la bd
	 * 
	 * @param dto
	 *            {@link BancoDTO}
	 * @return {@link List} of {@link BancoDTO}
	 * @throws {@link
	 *             BancoException}
	 */
	public List<BancoDTO> getAllBancos(BancoDTO dto) throws BancoException;

	/**
	 * Se encarga de obtener los registros paginados
	 * 
	 * @param dto
	 *            {@link BancoDTO}
	 * @param init
	 *            {@link Integer}
	 * @param end
	 *            {@link Integer}
	 * @return {@link List} of {@link BancoDTO}
	 * @throws BancoException
	 */
	public List<BancoDTO> getBancos(BancoDTO dto, Integer init, Integer end) throws BancoException;

	/**
	 * Se encarga de obtner un registro
	 * 
	 * @param dto
	 *            {@link BancoDTO}
	 * @return {@link BancoDTO}
	 * @throws {@link
	 *             BancoException}
	 */
	public BancoDTO getBancos(BancoDTO dto) throws BancoException;

	/**
	 * Se encarga de actualizar el registro
	 * 
	 * @param dto
	 *            {@link BancoDTO}
	 * @param user
	 *            {@link UsuarioDTO}
	 * @throws {@link
	 *             BancoException}
	 */
	public void update(BancoDTO dto, UsuarioDTO user) throws BancoException;

	/**
	 * Se encarga de insertar un registro
	 * 
	 * @param dto
	 *            {@link BancoDTO}
	 * @param user
	 *            {@link UsuarioDTO}
	 * @return {@link BancoDTO}
	 * @throws {@link
	 *             BancoException}
	 */
	public BancoDTO insert(BancoDTO dto, UsuarioDTO user) throws BancoException;

	/**
	 * Se encarga de eliminar el registro
	 * 
	 * @param dto
	 *            {@link BancoDTO}
	 * @param user
	 *            {@link UsuarioDTO}
	 * @throws {@link
	 *             BancoException}
	 */
	public void delete(BancoDTO dto, UsuarioDTO user) throws BancoException;

	/**
	 * Se encarrga de conntar la cantidad de registros encontrados segun el filtro
	 * aplicado
	 * 
	 * @param dto {@link BancoDTO}
	 * @throws {@link BancoException}
	 */
	public Integer getTotalRows(BancoDTO dto) throws BancoException;
}
