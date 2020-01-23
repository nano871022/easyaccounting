package com.pyt.service.interfaces;

import java.util.List;

import org.pyt.common.exceptions.ParametroException;

import com.pyt.service.dto.ParametroDTO;
import com.pyt.service.dto.ParametroGrupoDTO;

import co.com.japl.ea.dto.system.UsuarioDTO;

/**
 * Se encarga de realizar crud sobre los registros de parametros
 * 
 * @author alejandro parra
 * @since 06/05/2018
 */
public interface IParametrosSvc {
	/**
	 * Se encarga de obtner todos los parametros
	 * 
	 * @param dto {@link ParametroDTO}
	 * @return {@link List} of {@link ParametroDTO}
	 * @throws {@link ParametroException}
	 */
	public List<ParametroDTO> getAllParametros(ParametroDTO dto) throws ParametroException;

	/**
	 * Se encarga de buscar todos los registros asociadeos al grupo indicado
	 * 
	 * @param dto   {@link ParametroDTO}
	 * @param grupo {@link String} Grupo almacenado en {@link ParametroGrupoDTO} y
	 *              es un grupo creado en una cosntante
	 * @return {@link List} of {@link ParametroDTO}
	 * @throws {@link ParametroException}
	 */
	public List<ParametroDTO> getAllParametros(ParametroDTO dto, String grupo) throws ParametroException;

	/**
	 * Se encarga de obtener todos los parametros paginados
	 * 
	 * @param dto  {@link ParametroDTO}
	 * @param init {@link Integer}
	 * @param end  {@link Integer}
	 * @return {@link ParametroDTO}
	 * @throws {@link ParametroException}
	 */
	public List<ParametroDTO> getParametros(ParametroDTO dto, Integer init, Integer end) throws ParametroException;

	/**
	 * Se encarga de obtenre un registro de parametros
	 * 
	 * @param dto {@link ParametroDTO}
	 * @return {@link ParametroDTO}
	 * @throws {@link ParametroException}
	 */
	public ParametroDTO getParametro(ParametroDTO dto) throws ParametroException;

	/**
	 * Se encarga de actualizar un registro de parametro
	 * 
	 * @param dto  {@link ParametroDTO}
	 * @param user {@link UsuarioDTO}
	 * @throws {@link ParametroException}
	 */
	public void update(ParametroDTO dto, UsuarioDTO user) throws ParametroException;

	/**
	 * Se encarga de insertar un registro de parametro
	 * 
	 * @param dto  {@link ParametroDTO}
	 * @param user {@link UsuarioDTO}
	 * @return {@link ParametroDTO}
	 * @throws {@link ParametroException}
	 */
	public ParametroDTO insert(ParametroDTO dto, UsuarioDTO user) throws ParametroException;

	/**
	 * Se usa para el ingreso de informacion por medio de un servicio que recive los
	 * registros cargados
	 * 
	 * @param dto  {@link ParametroDTO}
	 * @param user {@link UsuarioDTO}
	 * @return {@link ParametroDTO}
	 * @throws {@link ParametroException}
	 */
	public ParametroDTO insertService(ParametroDTO dto, UsuarioDTO user) throws ParametroException;

	/**
	 * Se encarga de eliminar un regiistro de parametro
	 * 
	 * @param dto  {@link ParametroDTO}
	 * @param user {@link UsuarioDTO}
	 * @throws {@link ParametroException}
	 */
	public void delete(ParametroDTO dto, UsuarioDTO user) throws ParametroException;

	/**
	 * Se encarga de obtener la cantidad de registros segun el dto aplicado como
	 * filtro en la busqueda
	 * 
	 * @param dto {@link ParametroDTO}
	 * @return {@link Integer}
	 * @throws {@link ParametroException}
	 */
	public Integer totalCount(ParametroDTO dto) throws ParametroException;

	/**
	 * Se encarga de obtner los parametros asociados a los grupos
	 * 
	 * @param dto {@link ParametroGrupoDTO}
	 * @return {@link List} of {@link ParametroGrupoDTO}
	 * @throws {@link ParametroException}
	 */
	public List<ParametroGrupoDTO> getParametroGrupo(ParametroGrupoDTO dto) throws ParametroException;

	/**
	 * Se encarga de ingresar el nuevo parametro de grupo
	 * 
	 * @param dto  {@link ParametroGrupoDTO}
	 * @param user {@link UsuarioDTO}
	 * @throws {@link ParametroException}
	 */
	public ParametroGrupoDTO insert(ParametroGrupoDTO dto, UsuarioDTO user) throws ParametroException;

	/**
	 * Se encarga de actualizar el registro de parametro grupo
	 * 
	 * @param dto  {@link ParametroGrupoDTO}
	 * @param user {@link UsuarioDTO}
	 * @throws {@link ParametroException}
	 */
	public void update(ParametroGrupoDTO dto, UsuarioDTO user) throws ParametroException;

	/**
	 * Se encarga de eliminar el registro
	 * 
	 * @param dto  {@link ParametroGrupoDTO}
	 * @param user {@link UsuarioDTO}
	 * @throws {@link ParametroException}
	 */
	public void delete(ParametroGrupoDTO dto, UsuarioDTO user) throws ParametroException;

	/**
	 * Se encarga de obtener el id del parametro del grupo indicado
	 * 
	 * @param grupo {@link String} Grupo a buscar
	 * @return {@link String} Id del grupo
	 * @throws {@link ParametroException}
	 */
	public String getIdByParametroGroup(String grupo) throws ParametroException;

}
