package com.pyt.service.interfaces;

import java.util.List;

import org.pyt.common.common.UsuarioDTO;
import org.pyt.common.exceptions.ParametroException;

import com.pyt.service.dto.ParametroDTO;

/**
 * Se encarga de realizar crud sobre los registros de parametros
 * @author alejandro parra
 * @since 06/05/2018
 */
public interface IParametrosSvc {
	/**
	 * Se encarga de obtner todos los parametros
	 * @param dto {@link ParametroDTO}
	 * @return {@link List} of {@link ParametroDTO}
	 * @throws {@link ParametroException}
	 */
	public List<ParametroDTO> getAllParametros(ParametroDTO dto) throws ParametroException;
	/**
	 * Se encarga de obtener todos los parametros paginados
	 * @param dto {@link ParametroDTO}
	 * @param init {@link Integer}
	 * @param end {@link Integer}
	 * @return {@link ParametroDTO}
	 * @throws {@link ParametroException}
	 */
	public List<ParametroDTO> getParametros(ParametroDTO dto,Integer init,Integer end) throws ParametroException;
	/**
	 * Se encarga de obtenre un registro de parametros
	 * @param dto {@link ParametroDTO}
	 * @return {@link ParametroDTO}
	 * @throws {@link ParametroException}
	 */
	public ParametroDTO getParametro(ParametroDTO dto) throws ParametroException;
	/**
	 * Se encarga de actualizar un registro de parametro
	 * @param dto {@link ParametroDTO}
	 * @param user {@link UsuarioDTO}
	 * @throws {@link ParametroException}
	 */
	public void update(ParametroDTO dto, UsuarioDTO user) throws ParametroException;
	/**
	 * Se encarga de insertar un registro de parametro
	 * @param dto {@link ParametroDTO}
	 * @param user {@link UsuarioDTO}
	 * @return {@link ParametroDTO}
	 * @throws {@link ParametroException}
	 */
	public ParametroDTO insert(ParametroDTO dto, UsuarioDTO user) throws ParametroException;
	/**
	 * Se encarga de eliminar un regiistro de parametro
	 * @param dto {@link ParametroDTO}
	 * @param user {@link UsuarioDTO}
	 * @throws {@link ParametroException}
	 */
	public void delete(ParametroDTO dto, UsuarioDTO user) throws ParametroException;
	/**
	 * Se encarga de obtener la cantidad de registros segun el dto aplicado como filtro en la busqueda
	 * @param dto {@link ParametroDTO}
	 * @return {@link Integer}
	 * @throws {@link ParametroException}
	 */
	public Integer totalCount(ParametroDTO dto)throws ParametroException;
}
