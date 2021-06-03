package co.com.japl.ea.dto.interfaces;

import java.util.List;

import co.com.japl.ea.common.abstracts.ADto;
import co.com.japl.ea.dto.system.UsuarioDTO;
import co.com.japl.ea.exceptions.QuerysPopupException;

/**
 * Se usa para realizar consultas sobre la base de datos por medio del popup
 * bean genericos
 * 
 * @author Alejandro Parra
 * @since 28/01/2018
 *
 */
public interface IQuerysPopup {
	/**
	 * Se ecnarga de obtener todos los registros segun el filtro indicado y los
	 * permisos del usuario
	 * 
	 * @param filter {@link ADto} extends
	 * @param user   {@link UsuarioDTO}
	 * @return {@link List}
	 * @throws {@link QuerysPopupException}
	 */
	public <T extends ADto> List<T> list(T filter, UsuarioDTO user) throws QuerysPopupException;

	/**
	 * Se encarga de obtener los regisos segun el filtro indicado, la cantidad de
	 * registros y los permisos del usuario
	 * 
	 * @param filter   {@link ADto} extends
	 * @param inicial  {@link Integer}
	 * @param cantidad {@link Integer}
	 * @param user     {@link UsuarioDTO}
	 * @return {@link List}
	 * @throws {@link QuerysPopupException}
	 */
	public <T extends ADto> List<T> list(T filter, Integer inicial, Integer cantidad, UsuarioDTO user)
			throws QuerysPopupException;

	/**
	 * Se encarga de obtener la cantidad de registros que se encontraron apartir del
	 * filtro suministrados y los permisos sobre el usuario.
	 * 
	 * @return
	 * @throws QuerysPopupException
	 */
	public <T extends ADto> Integer records(T filter, UsuarioDTO user) throws QuerysPopupException;
}
