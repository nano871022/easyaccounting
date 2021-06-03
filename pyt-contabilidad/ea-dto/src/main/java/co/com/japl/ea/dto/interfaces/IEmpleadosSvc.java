package co.com.japl.ea.dto.interfaces;

import java.util.List;

import co.com.japl.ea.dto.dto.PersonaDTO;
import co.com.japl.ea.dto.dto.TrabajadorDTO;
import co.com.japl.ea.dto.system.UsuarioDTO;
import co.com.japl.ea.exceptions.EmpleadoException;

/**
 * Se encarga de relizar crud sobre la rabla de trabajadores y personas
 * 
 * @author alejandro parra
 * @since 06/05/2018
 */
public interface IEmpleadosSvc {
	/**
	 * Se encarga de obtener todos los registros de trabajadores
	 * 
	 * @param dto
	 *            {@link TrabajadorDTO}
	 * @return {@link List} of {@link TrabajadorDTO}
	 * @throws {@link
	 *             EmpleadoException}
	 */
	public List<TrabajadorDTO> getAllTrabajadores(TrabajadorDTO dto) throws EmpleadoException;

	/**
	 * Se encarga de obtener toos los registros de trabajadores paginados
	 * 
	 * @param dto
	 *            {@link TrabajadorDTO}
	 * @param init
	 *            {@link Integer}
	 * @param end
	 *            {@link Integer}
	 * @return {@link List} if {@link TrabajadorDTO}
	 * @throws {@link
	 *             EmpleadoException}
	 */
	public List<TrabajadorDTO> getTrabajadores(TrabajadorDTO dto, Integer init, Integer end) throws EmpleadoException;

	/**
	 * Se encarga de obtener un registro
	 * 
	 * @param dto
	 *            {@link TrabajadorDTO}
	 * @return {@link TrabajadorDTO}
	 * @throws {@link
	 *             EmpleadoException}
	 */
	public TrabajadorDTO getTrabajador(TrabajadorDTO dto) throws EmpleadoException;

	/**
	 * Se encarga de actualizar un registro
	 * 
	 * @param dto
	 *            {@link TrabajadorDTO}
	 * @param user
	 *            {@link UsuarioDTO}
	 * @throws {@link
	 *             EmpleadoException}
	 */
	public void update(TrabajadorDTO dto, UsuarioDTO user) throws EmpleadoException;

	/**
	 * Se encarga de eliminar un registro
	 * 
	 * @param dto
	 *            {@link TrabajadorDTO}
	 * @param user
	 *            {@link UsuarioDTO}
	 * @throws {@link
	 *             EmpleadoException}
	 */
	public void delete(TrabajadorDTO dto, UsuarioDTO user) throws EmpleadoException;

	/**
	 * Se encarga de ingresar un registro de la tabla de trabajadores
	 * 
	 * @param dto
	 *            {@link TrabajadorDTO}
	 * @param user
	 *            {@link UsuarioDTO}
	 * @return {@link TrabajadorDTO}
	 * @throws {@link
	 *             EmpleadoException}
	 */
	public TrabajadorDTO insert(TrabajadorDTO dto, UsuarioDTO user) throws EmpleadoException;

	/**
	 * Se encarga de obtneer todos los registros de personas
	 * 
	 * @param dto
	 *            {@link PersonaDTO}
	 * @return {@link List} of {@link PersonaDTO}
	 * @throws {@link
	 *             EmpleadoException}
	 */
	public List<PersonaDTO> getAllPersonas(PersonaDTO dto) throws EmpleadoException;
	/**
	 * Se encarga de obtner todos los registros paginados
	 * @param dto {@link PersonaDTO}
	 * @param init {@link Integer}
	 * @param end {@link Integer}
	 * @return {@link List} of {@link PersonaDTO}
	 * @throws {@link EmpleadoException}
	 */
	public List<PersonaDTO> getPersona(PersonaDTO dto, Integer init, Integer end) throws EmpleadoException;
	/**
	 * Se encarga de obtener un solo registro de persona
	 * @param dto {@link PersonaDTO}
	 * @return {@link PersonaDTO}
	 * @throws {@link EmpleadoException}
	 */
	public PersonaDTO getPersona(PersonaDTO dto)throws EmpleadoException;
	/**
	 * Se encarga de realizar una actualizacion 
	 * @param dto {@link PersonaDTO}
	 * @param user {@link UsuarioDTO}
	 * @throws {@link EmpleadoException}
	 */
	public void update(PersonaDTO dto, UsuarioDTO user)throws EmpleadoException;
	/**
	 * Se encarga de realizar un insert
	 * @param dto {@link PersonaDTO}
	 * @param user {@link UsuarioDTO}
	 * @return {@link PersonaDTO}
	 * @throws {@link EmpleadoException}
	 */
	public PersonaDTO insert(PersonaDTO dto,UsuarioDTO user)throws EmpleadoException;
	/**
	 * Se encarga de eliminar un registro de personas
	 * @param dto {@link PersonaDTO}
	 * @param user {@link UsuarioDTO}
	 * @throws {@link EmpleadoException}
	 */
	public void delete(PersonaDTO dto,UsuarioDTO user)throws EmpleadoException;
	/**
	 * Se encarga de obtener la cantidada de registros encontrados con el filtro aplicado
	 * @param dto {@link TrabajadorDTO}
	 * @return {@link Integer}
	 * @throws {@link EmpleadoException}
	 */
	public Integer getTotalRows(TrabajadorDTO dto)throws EmpleadoException;
	/**
	 * Se encarga de obtener la cantidada de registros encontrados con el filtro aplicado
	 * @param dto {@link PersonaDTO}
	 * @return {@link Integer}
	 * @throws {@link EmpleadoException}
	 */
	public Integer getTotalRows(PersonaDTO dto)throws EmpleadoException;

	
}
