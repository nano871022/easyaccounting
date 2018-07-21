package com.pyt.service.interfaces;

import java.util.List;

import org.pyt.common.common.UsuarioDTO;
import org.pyt.common.exceptions.ServiciosException;

import com.pyt.service.dto.ServicioDTO;

/**
 * Se encarga de realizar crud sobre los registros de {@link ServicioDTO}
 * @author alejandro parra
 * @since 06/05/2018
 */
public interface IServiciosSvc {
	/**
	 * Se encarga de obtner todos los {@link ServicioDTO}
	 * @param dto {@link ServicioDTO}
	 * @return {@link List} of {@link ServicioDTO}
	 * @throws {@link ServicioException}
	 */
	public List<ServicioDTO> getAllServicios(ServicioDTO dto) throws ServiciosException;
	/**
	 * Se encarga de obtener todos los {@link ServicioDTO} paginados
	 * @param dto {@link ServicioDTO}
	 * @param init {@link Integer}
	 * @param end {@link Integer}
	 * @return {@link ServicioDTO}
	 * @throws {@link SeriviciosException}
	 */
	public List<ServicioDTO> getServicios(ServicioDTO dto,Integer init,Integer end) throws ServiciosException;
	/**
	 * Se encarga de obtenre un registro de servicios
	 * @param dto {@link ServicioDTO}
	 * @return {@link ServicioDTO}
	 * @throws {@link ServicioException}
	 */
	public ServicioDTO getServicio(ServicioDTO dto) throws ServiciosException;
	/**
	 * Se encarga de actualizar un registro de {@link ServicioDTO}
	 * @param dto {@link SerivicioDTO}
	 * @param user {@link UsuarioDTO}
	 * @throws {@link ServicioException}
	 */
	public void update(ServicioDTO dto, UsuarioDTO user) throws ServiciosException;
	/**
	 * Se encarga de insertar un registro de {@link ServicioDTO}
	 * @param dto {@link ServicioDTO}
	 * @param user {@link UsuarioDTO}
	 * @return {@link ServicioDTO}
	 * @throws {@link ServicioException}
	 */
	public ServicioDTO insert(ServicioDTO dto, UsuarioDTO user) throws ServiciosException;
	/**
	 * Se encarga de eliminar un regiistro de {@link ServicioDTO}
	 * @param dto {@link ServicioDTO}
	 * @param user {@link UsuarioDTO}
	 * @throws {@link ServicioException}
	 */
	public void delete(ServicioDTO dto, UsuarioDTO user) throws ServiciosException;
	/**
	 * Se encarga de obtener la cantidad de registros encontrados aplicando el filtro
	 * @param filter {@link ServicioDTO}
	 * @throws {@link ServiciosException}
	 */
	public Integer getTotalRows(ServicioDTO filter)throws ServiciosException;
}
