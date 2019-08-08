package com.pyt.query.interfaces;

import java.util.List;

import org.pyt.common.abstracts.ADto;
import org.pyt.common.exceptions.QueryException;

import co.com.japl.ea.dto.system.UsuarioDTO;

/**
 * + Se encarga de realizar crud sobre un registro que se indique el objeto
 * 
 * @author alejandro parra
 * @since 06/05/2018
 */
public interface IQuerySvc {
	/**
	 * Se necarga de obentener todos los registros paginados
	 * 
	 * @param obj
	 *            extends {@link ADto}
	 * @param init
	 *            {@link Integer}
	 * @param end
	 *            {@link Integer}
	 * @return {@link List} of extends {@link ADto}
	 * @throws QueryException
	 */
	public <T extends ADto> List<T> gets(T obj, Integer init, Integer end) throws QueryException;

	/**
	 * Se encarga de obtener la lista de objetos resultates
	 * 
	 * @param obj
	 *            extends {@link ADto}
	 * @return {@link List} of {@link ObjectADto}
	 * @throws {@link
	 *             QueryException}
	 */
	public <T extends ADto> List<T> gets(T obj) throws QueryException;

	/**
	 * Se encarha de obtener un solo registro
	 * 
	 * @param obj
	 *            extends {@link ADto}
	 * @return extends {@link ADto}
	 * @throws {@link
	 *             QueryException}
	 */
	public <T extends ADto> T get(T obj) throws QueryException;

	/**
	 * Se encarga de realizar un inser o actualizacion de un registro
	 * 
	 * @param obj
	 *            extends {@link ADto}
	 * @param user {@link UsuarioDTO}
	 * @return extends {@link ADto}
	 * @throws {@link
	 *             QueryException}
	 */
	public <T extends ADto> T set(T obj,UsuarioDTO user) throws QueryException;

	/**
	 * Se encarga de realizar la eliminacion de una consulta
	 * 
	 * @param obj
	 *            extends {@link ADto}
	 * @param user {@link UsuarioDTO}
	 * @throws {@link
	 *             QueryException}
	 */
	public <T extends ADto> void del(T obj,UsuarioDTO user) throws QueryException;
	/**
	 * Se encargad e obtener todos los registros del dto
	 * @param obj extrends {@link ADto}
	 * @return {@link Integer}
	 * @throws {@link QueryException}
	 */
	public <T extends ADto> Integer countRow(T obj)throws QueryException;
}
