package com.pyt.query.interfaces;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

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
	 * @param obj  extends {@link ADto}
	 * @param init {@link Integer}
	 * @param end  {@link Integer}
	 * @return {@link List} of extends {@link ADto}
	 * @throws QueryException
	 */
	public <T extends ADto> List<T> gets(T obj, Integer init, Integer end) throws QueryException;

	/**
	 * Se encarga de obtener la lista de objetos resultates
	 * 
	 * @param obj extends {@link ADto}
	 * @return {@link List} of {@link ObjectADto}
	 * @throws {@link QueryException}
	 */
	public <T extends ADto> List<T> gets(T obj) throws QueryException;

	/**
	 * Se encarha de obtener un solo registro
	 * 
	 * @param obj extends {@link ADto}
	 * @return extends {@link ADto}
	 * @throws {@link QueryException}
	 */
	public <T extends ADto> T get(T obj) throws QueryException;

	/**
	 * Se encarga de realizar un inser o actualizacion de un registro
	 * 
	 * @param obj  extends {@link ADto}
	 * @param user {@link UsuarioDTO}
	 * @return extends {@link ADto}
	 * @throws {@link QueryException}
	 */
	public <T extends ADto> T set(T obj, UsuarioDTO user) throws QueryException;

	/**
	 * Servicio encargado de realizar la actualizacion de un registro
	 * 
	 * @param obj  {@link ADto}
	 * @param user {@link UsuarioDTO}
	 * @return {@link Boolean}
	 * @throws {@link QueryException}
	 */
	public <T extends ADto> Boolean update(T obj, UsuarioDTO user) throws QueryException;

	/**
	 * Se encarga de realkziar el ingreso de un registro
	 * 
	 * @param obj  {@link ADto}
	 * @param user {@link UsuarioDTO}
	 * @return {@link Boolean}
	 * @throws {@link QueryException}
	 */
	public <T extends ADto> Boolean insert(T obj, UsuarioDTO user) throws QueryException;

	/**
	 * Se encarga de realizar la eliminacion de una consulta
	 * 
	 * @param obj  extends {@link ADto}
	 * @param user {@link UsuarioDTO}
	 * @throws {@link QueryException}
	 */
	public <T extends ADto> void del(T obj, UsuarioDTO user) throws QueryException;

	/**
	 * Se encargad e obtener todos los registros del dto
	 * 
	 * @param obj extrends {@link ADto}
	 * @return {@link Integer}
	 * @throws {@link QueryException}
	 */
	public <T extends ADto> Integer countRow(T obj) throws QueryException;
	/**
	 * Servicio con el cual se puede realizar un query personalizado y solo se puede construir desde los servicos,
	 * los valores pasados son optional de tipo string que son retornados por las construcciones de los filtros
	 *
	 * @param <T> {@link ADto}
	 * @param obj {@link ADto}
	 * @param addToWhere {@link Array} by {@link String}
	 * @return {@link List} by {@link ADto}
	 * @throws {@link QueryException}
	 */
	public <T extends ADto> List<T> gets(T obj,Optional<String>... addToWhere)throws QueryException;
	/**
	 * Esto permite crear el filtro de tipo between indicando los valores entre los cuales debe contrner el filtro
	 * @param fieldName {@link String}
	 * @param value1 {@link String}
	 * @param value2 {@link String}
	 * @return {@link Optional} by {@link String}
	 * @throws {@link QueryException}
	 */
	public  <T extends ADto> Optional<String> filterBetween(T obj,String fieldName,String value1,String value2)throws QueryException;
	/**
	 * Esto permite crear un filtro de tipo menor a un valor dado
	 * @param fieldName {@link String}
	 * @param value {@link String}
	 * @return {@link Optional} by {@link String}
	 * @throws {@link QueryException}
	 */
	public  <T extends ADto> Optional<String> filterLess(T obj,String fieldName,String value)throws QueryException;
	/**
	 * Esto permite crear el filtro de tipo mayor a un valor dado
	 * @param fieldName {@link String}
	 * @param value {@link String}
	 * @return {@link Optional} by {@link String}
	 * @throws {@link QueryException}
	 */
	public  <T extends ADto> Optional<String> filterGreater(T obj,String fieldName,String value)throws QueryException;
	/**
	 * Esto permite crear el filtro de tipo menor que a un valor dado
	 * @param fieldName {@link String}
	 * @param value {@link String}
	 * @return {@link Optional} by {@link String}
	 * @throws {@link QueryException}
	 */
	public  <T extends ADto> Optional<String> filterLessThat(T obj,String fieldName,String value)throws QueryException;
	/**
	 * Esto permite crear el filtro de tipo mayor que a un valor dado
	 * @param fieldName {@link String}
	 * @param value {@link String}
	 * @return {@link Optional} by {@link String}
	 * @throws {@link QueryException}
	 */
	public  <T extends ADto> Optional<String> filterGreaterThat(T obj,String fieldName,String value)throws QueryException;
	/**
	 * Esto permite crear el filtro de tipo select con el objeto dato
	 * @param fieldName {@link String}
	 * @param obj {@link ADto}
	 * @return {@link Optional} by {@link String}
	 * @throws {@link QueryException}
	 */
	public <T extends ADto,D extends ADto> Optional<String> filterSelect(T dto,String fieldName, D obj,String nameFilter) throws QueryException;
}
