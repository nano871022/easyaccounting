package com.pyt.service.interfaces;

import java.util.List;

import org.pyt.common.abstracts.ADto;
import org.pyt.common.exceptions.GenericServiceException;

import co.com.japl.ea.dto.system.UsuarioDTO;

/**
 * Se encarga de realizar crud sobre los registros de {@link T}
 * @author alejandro parra
 * @since 06/05/2018
 */
public interface IGenericServiceSvc <T extends ADto>{
	/**
	 * Se encarga de obtner todos los {@link T}
	 * @param dto {@link T}
	 * @return {@link List} of {@link T}
	 * @throws {@link ServicioException}
	 */
	public List<T> getAll(T dto) throws GenericServiceException;
	/**
	 * Se encarga de obtener todos los {@link T} paginados
	 * @param dto {@link T}
	 * @param init {@link Integer}
	 * @param end {@link Integer}
	 * @return {@link T}
	 * @throws {@link SeriviciosException}
	 */
	public List<T> gets(T dto,Integer init,Integer end) throws GenericServiceException;
	/**
	 * Se encarga de obtenre un registro de servicios
	 * @param dto {@link T}
	 * @return {@link T}
	 * @throws {@link ServicioException}
	 */
	public T get(T dto) throws GenericServiceException;
	/**
	 * Se encarga de actualizar un registro de {@link T}
	 * @param dto {@link SerivicioDTO}
	 * @param user {@link UsuarioDTO}
	 * @throws {@link ServicioException}
	 */
	public void update(T dto, UsuarioDTO user) throws GenericServiceException;
	/**
	 * Se encarga de insertar un registro de {@link T}
	 * @param dto {@link T}
	 * @param user {@link UsuarioDTO}
	 * @return {@link T}
	 * @throws {@link ServicioException}
	 */
	public T insert(T dto, UsuarioDTO user) throws GenericServiceException;
	/**
	 * Se encarga de eliminar un regiistro de {@link T}
	 * @param dto {@link T}
	 * @param user {@link UsuarioDTO}
	 * @throws {@link ServicioException}
	 */
	public void delete(T dto, UsuarioDTO user) throws GenericServiceException;
	/**
	 * Se encarga de obtener la cantidad de registros encontrados aplicando el filtro
	 * @param filter {@link T}
	 * @throws {@link GenericServiceException}
	 */
	public Integer getTotalRows(T filter)throws GenericServiceException;
}
