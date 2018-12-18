package com.pyt.service.interfaces.inventarios;

import java.util.List;

import org.pyt.common.common.UsuarioDTO;
import org.pyt.common.exceptions.ParametroException;

import com.pyt.service.dto.inventario.ParametroGrupoInventarioDTO;
import com.pyt.service.dto.inventario.ParametroInventarioDTO;

/**
 * Se encarga de realizar crud sobre los registros de parametros
 * 
 * @author alejandro parra
 * @since 06/05/2018
 */
public interface IParametroInventariosSvc {
	/**
	 * Se encarga de obtner todos los parametros
	 * 
	 * @param dto
	 *            {@link ParametroInventarioDTO}
	 * @return {@link List} of {@link ParametroInventarioDTO}
	 * @throws {@link
	 *             ParametroException}
	 */
	public List<ParametroInventarioDTO> getAllParametros(ParametroInventarioDTO dto) throws ParametroException;

	/**
	 * Se encarga de buscar todos los registros asociadeos al grupo indicado
	 * @param dto {@link ParametroInventarioDTO}
	 * @param grupo {@link String} Grupo almacenado en {@link ParametroGrupoInventarioDTO} y es un grupo creado en una cosntante 
	 * @return {@link List} of {@link ParametroInventarioDTO}
	 * @throws {@link ParametroException}
	 */
	public List<ParametroInventarioDTO> getAllParametros(ParametroInventarioDTO dto, String grupo) throws ParametroException;

	/**
	 * Se encarga de obtener todos los parametros paginados
	 * 
	 * @param dto
	 *            {@link ParametroInventarioDTO}
	 * @param init
	 *            {@link Integer}
	 * @param end
	 *            {@link Integer}
	 * @return {@link ParametroInventarioDTO}
	 * @throws {@link
	 *             ParametroException}
	 */
	public List<ParametroInventarioDTO> getParametros(ParametroInventarioDTO dto, Integer init, Integer end) throws ParametroException;

	/**
	 * Se encarga de obtenre un registro de parametros
	 * 
	 * @param dto
	 *            {@link ParametroInventarioDTO}
	 * @return {@link ParametroInventarioDTO}
	 * @throws {@link
	 *             ParametroException}
	 */
	public ParametroInventarioDTO getParametro(ParametroInventarioDTO dto) throws ParametroException;

	/**
	 * Se encarga de actualizar un registro de parametro
	 * 
	 * @param dto
	 *            {@link ParametroInventarioDTO}
	 * @param user
	 *            {@link UsuarioDTO}
	 * @throws {@link
	 *             ParametroException}
	 */
	public void update(ParametroInventarioDTO dto, UsuarioDTO user) throws ParametroException;

	/**
	 * Se encarga de insertar un registro de parametro
	 * 
	 * @param dto
	 *            {@link ParametroInventarioDTO}
	 * @param user
	 *            {@link UsuarioDTO}
	 * @return {@link ParametroInventarioDTO}
	 * @throws {@link
	 *             ParametroException}
	 */
	public ParametroInventarioDTO insert(ParametroInventarioDTO dto, UsuarioDTO user) throws ParametroException;

	/**
	 * Se encarga de eliminar un regiistro de parametro
	 * 
	 * @param dto
	 *            {@link ParametroInventarioDTO}
	 * @param user
	 *            {@link UsuarioDTO}
	 * @throws {@link
	 *             ParametroException}
	 */
	public void delete(ParametroInventarioDTO dto, UsuarioDTO user) throws ParametroException;

	/**
	 * Se encarga de obtener la cantidad de registros segun el dto aplicado como
	 * filtro en la busqueda
	 * 
	 * @param dto
	 *            {@link ParametroInventarioDTO}
	 * @return {@link Integer}
	 * @throws {@link
	 *             ParametroException}
	 */
	public Integer totalCount(ParametroInventarioDTO dto) throws ParametroException;

	/**
	 * Se encarga de obtner los parametros asociados a los grupos
	 * 
	 * @param dto
	 *            {@link ParametroGrupoInventarioDTO}
	 * @return {@link List} of {@link ParametroGrupoInventarioDTO}
	 * @throws {@link
	 *             ParametroException}
	 */
	public List<ParametroGrupoInventarioDTO> getParametroGrupo(ParametroGrupoInventarioDTO dto) throws ParametroException;

	/**
	 * Se encarga de ingresar el nuevo parametro de grupo
	 * 
	 * @param dto
	 *            {@link ParametroGrupoInventarioDTO}
	 * @param user
	 *            {@link UsuarioDTO}
	 * @throws {@link
	 *             ParametroException}
	 */
	public ParametroGrupoInventarioDTO insert(ParametroGrupoInventarioDTO dto, UsuarioDTO user) throws ParametroException;

	/**
	 * Se encarga de actualizar el registro de parametro grupo
	 * 
	 * @param dto
	 *            {@link ParametroGrupoInventarioDTO}
	 * @param user
	 *            {@link UsuarioDTO}
	 * @throws {@link
	 *             ParametroException}
	 */
	public void update(ParametroGrupoInventarioDTO dto, UsuarioDTO user) throws ParametroException;

	/**
	 * Se encarga de eliminar el registro
	 * 
	 * @param dto
	 *            {@link ParametroGrupoInventarioDTO}
	 * @param user
	 *            {@link UsuarioDTO}
	 * @throws {@link
	 *             ParametroException}
	 */
	public void delete(ParametroGrupoInventarioDTO dto, UsuarioDTO user) throws ParametroException;

}
