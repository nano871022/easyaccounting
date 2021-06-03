package co.com.japl.ea.dto.interfaces;
/**
 * Se encarga de realizar crud sobre los registros de centro de costos
 * @author alejandro parra
 * @since 06/05/2018
 */

import java.util.List;

import co.com.japl.ea.dto.dto.CentroCostoDTO;
import co.com.japl.ea.dto.system.UsuarioDTO;
import co.com.japl.ea.exceptions.CentroCostosException;

/**
 * Se encarga de realizar crud sobre los registros de centro de costos
 * 
 * @author alejandro parra
 * @since 06/05/2018
 */
public interface ICentroCostosSvc {
	/**
	 * Se encarga de obtneer todos los registros de centro de costos
	 * 
	 * @param dto
	 *            {@link CentroCostoDTO}
	 * @return {@link List} of {@link CentroCostoDTO}
	 * @throws {@link
	 *             CentroCostosException}
	 */
	public List<CentroCostoDTO> getAllCentroCostos(CentroCostoDTO dto) throws CentroCostosException;

	/**
	 * Se encarga de obtener los registros para paginacion
	 * 
	 * @param dto
	 *            {@link CentroCostoDTO}
	 * @param inti
	 *            {@link Integer}
	 * @param end
	 *            {@link Integer}
	 * @return {@link List} of {@link CentroCostoDTO}
	 * @throws {@link
	 *             CentroCostosException}
	 */
	public List<CentroCostoDTO> getCentroCostos(CentroCostoDTO dto, Integer init, Integer end)
			throws CentroCostosException;
	/**
	 * Se encarga de obtener un registro
	 * @param dto {@link CentroCostoDTO}
	 * @return {@link CentroCostoDTO}
	 * @throws {@link CentroCostosException}
	 */
	public CentroCostoDTO getCentroCosto(CentroCostoDTO dto)throws CentroCostosException;
	/**
	 * Se encarga de actualizar un registro
	 * @param dto {@link CentroCostoDTO}
	 * @param user {@link UsuarioDTO}
	 * @throws {@link CentroCostosException}
	 */
	public void update(CentroCostoDTO dto,UsuarioDTO user)throws CentroCostosException;
	/**
	 * Se encarga de eliminar un registro
	 * @param dto {@link CentroCostoDTO}
	 * @param user {@link UsuarioDTO}
	 * @throws {@link CentroCostosException}
	 */
	public void delete(CentroCostoDTO dto,UsuarioDTO user)throws CentroCostosException;
	/**
	 * Se encarga de insertar un registro
	 * @param dto {@link CentroCostoDTO}
	 * @param user {@link UsuarioDTO}
	 * @throws {@link CentroCostosException}
	 */
	public CentroCostoDTO insert(CentroCostoDTO dto,UsuarioDTO user)throws CentroCostosException;
	/**
	 * Se encarga de contar los registros encontrados aplicando el filtro
	 * @param dto {@link CentroCostoDTO}
	 * @return {@link Integer}
	 * @throws {@link CentroCostosException}
	 */
	public Integer getTotalRows(CentroCostoDTO dto)throws CentroCostosException;
}
