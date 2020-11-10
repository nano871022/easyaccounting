package com.pyt.service.interfaces;

import java.util.List;
import java.util.Map;

import org.pyt.common.abstracts.ADto;
import org.pyt.common.exceptions.MarcadorServicioException;

import com.pyt.service.abstracts.Services;
import com.pyt.service.dto.ConfiguracionDTO;
import com.pyt.service.dto.MarcadorDTO;
import com.pyt.service.dto.MarcadorServicioDTO;
import com.pyt.service.dto.ServicioCampoBusquedaDTO;

import co.com.japl.ea.dto.system.UsuarioDTO;

/**
 * Se encarga de realizar el crud sobre asociacion de marcadores y servicios
 * para la impresion de archivos o carga de archivos
 * 
 * @author Alejandro Parra
 * @since 16/09/2018
 */
public interface IConfigMarcadorServicio {

	/**
	 * Se encarga de traer el listado de los servicios campo busqueda segun el
	 * nombre de la configuracion
	 * 
	 * @param configuracion {@link String}
	 * @param inicio        {@link Integer}
	 * @param cantidad      {@link Integer}
	 * @return {@link List} of {@link ServicioCampoBusquedaDTO}
	 * @throws {@link MarcadorServicioException}
	 */
	public List<ServicioCampoBusquedaDTO> getServiciosCampoBusqueda(ConfiguracionDTO configuracion, Integer inicio,
			Integer cantidad) throws MarcadorServicioException;

	/**
	 * Se encarga de traer el listados de la asociacion de marcadores servicios
	 * 
	 * @param configuracion {@link String}
	 * @param inicio        {@link Integer}
	 * @param cantidad      {@link Integer}
	 * @return {@link List} of {@link MarcadorServicioDTO}
	 * @throws {@link MarcadorServicioException}
	 */
	public List<MarcadorServicioDTO> getMarcadorServicio(ConfiguracionDTO configuracion, Integer inicio,
			Integer cantidad) throws MarcadorServicioException;

	/**
	 * Se encarga de obtener todos los marcadores configurados
	 * 
	 * @param configuracion {@link String}
	 * @param inicio        {@link Integer}
	 * @param cantidad      {@link Integer}
	 * @return {@link List} of {@link MarcadorDTO}
	 * @throws {@link MarcadorServicioException}
	 */
	public List<MarcadorDTO> getMarcador(ConfiguracionDTO configuracion, Integer inicio, Integer cantidad)
			throws MarcadorServicioException;

	/**
	 * Se encarga de traer el listado de los servicios campo busqueda segun el
	 * nombre de la configuracion
	 * 
	 * @param configuracion {@link String}
	 * @return {@link List} of {@link ServicioCampoBusquedaDTO}
	 * @throws {@link MarcadorServicioException}
	 */
	public Integer cantidadServiciosCampoBusqueda(ConfiguracionDTO configuracion) throws MarcadorServicioException;

	/**
	 * Se encarga de traer el listados de la asociacion de marcadores servicios
	 * 
	 * @param configuracion {@link String}
	 * @return {@link List} of {@link MarcadorServicioDTO}
	 * @throws {@link MarcadorServicioException}
	 */
	public Integer cantidadMarcadorServicio(ConfiguracionDTO configuracion) throws MarcadorServicioException;

	/**
	 * Se encarga de obtener todos los marcadores configurados
	 * 
	 * @param configuracion {@link String}
	 * @return {@link List} of {@link MarcadorDTO}
	 * @throws {@link MarcadorServicioException}
	 */
	public Integer cantidadMarcador(ConfiguracionDTO configuracion) throws MarcadorServicioException;

	/**
	 * Se encarga de traer el listado de los servicios campo busqueda segun el
	 * nombre de la configuracion
	 * 
	 * @param configuracion {@link String}
	 * @return {@link List} of {@link ServicioCampoBusquedaDTO}
	 * @throws {@link MarcadorServicioException}
	 */
	public List<ServicioCampoBusquedaDTO> getServiciosCampoBusqueda(ConfiguracionDTO configuracion)
			throws MarcadorServicioException;

	/**
	 * Se encarga de traer el listados de la asociacion de marcadores servicios
	 * 
	 * @param configuracion {@link String}
	 * @return {@link List} of {@link MarcadorServicioDTO}
	 * @throws {@link MarcadorServicioException}
	 */
	public List<MarcadorServicioDTO> getMarcadorServicio(ConfiguracionDTO configuracion)
			throws MarcadorServicioException;

	/**
	 * Se encarga de obtener todos los marcadores configurados
	 * 
	 * @param configuracion {@link String}
	 * @return {@link List} of {@link MarcadorDTO}
	 * @throws {@link MarcadorServicioException}
	 */
	public List<MarcadorDTO> getMarcador(ConfiguracionDTO configuracion) throws MarcadorServicioException;

	/**
	 * Se encarga de ingresar un nuevo marcador
	 * 
	 * @param marcadorDto {@link MarcadorDTO}
	 * @param usuarioDto  {@link usuarioDTO}
	 * @return {@link MarcadorDTO}
	 * @throws {@link MarcadorServicioException}
	 */
	public MarcadorDTO insertMarcador(MarcadorDTO marcadorDto, UsuarioDTO usuario) throws MarcadorServicioException;

	/**
	 * Se encarga de ingresar asociaciones de marcadores y servicios
	 * 
	 * @param dto     {@link MarcadorServicioDTO}
	 * @param usuario UsuarioDTO
	 * @return {@link MarcadorServicioDTO}
	 * @throws {@link MarcadorServicioException}
	 */
	public MarcadorServicioDTO insertMarcadorServicio(MarcadorServicioDTO dto, UsuarioDTO usuario)
			throws MarcadorServicioException;

	/**
	 * Se encarga de ingresar el servicio y campo de busqueda
	 * 
	 * @param dto     {@link ServicioCampoBusquedaDTO}
	 * @param usuario {@link UsuarioDTO}
	 * @return {@link ServicioCampoBusquedaDTO}
	 * @throws {@link MarcadorServicioException}
	 */
	public ServicioCampoBusquedaDTO insertServicioCampoBusqueda(ServicioCampoBusquedaDTO dto, UsuarioDTO usuario)
			throws MarcadorServicioException;

	/**
	 * Se encarga de actualizar el marcador
	 * 
	 * @param dto     {@link MarcadorDTO}
	 * @param usuario {@link UsuarioDTO}
	 * @throws {@link MarcadorServicioException}
	 */
	public void updateMarcador(MarcadorDTO dto, UsuarioDTO usuario) throws MarcadorServicioException;

	/**
	 * Se encarga de actualizar un campo de busquqeda sobre el servicio
	 * 
	 * @param dto     {@link ServicioCampoBusquedaDTO}
	 * @param usuario {@link UsuarioDTO}
	 * @throws {@link MarcadorServicioException}
	 */
	public void updateServicioCampo(ServicioCampoBusquedaDTO dto, UsuarioDTO usuario) throws MarcadorServicioException;

	/**
	 * Se encarga de acctualizar el servicio acosiao a un marcado
	 * 
	 * @param dto     {@link MarcadorServicioDTO}
	 * @param usuario {@link UsuarioDTO}
	 * @throws {@link MarcadorServicioException}
	 */
	public void updateServicioMarcador(MarcadorServicioDTO dto, UsuarioDTO usuario) throws MarcadorServicioException;

	/**
	 * Se encarga de obtener las configuraciones apartir de los campos del dto
	 * 
	 * @param dto {@link ConfiguracionDTO}
	 * @return {@link List} of {@link ConfiguracionDTO}
	 * @throws {@link MarcadorServicioException}
	 */
	public List<ConfiguracionDTO> getConfiguraciones(ConfiguracionDTO dto) throws MarcadorServicioException;

	/**
	 * Se encarga de ingresar una nueva configuracion
	 * 
	 * @param dto  {@link ConfiguracionDTO}
	 * @param user {@link UsuarioDTO}
	 * @return {@link ConfiguracionDTO}
	 * @throws {@link MarcadorServicioException}
	 */
	public ConfiguracionDTO insertConfiguracion(ConfiguracionDTO dto, UsuarioDTO user) throws MarcadorServicioException;

	/**
	 * Se encarga de actualizar una configuracion
	 * 
	 * @param dto  {@link ConfiguracionDTO}
	 * @param user {@link UsuarioDTO}
	 * @throws {@link MarcadorServicioException}
	 */
	public void updateConfiguracion(ConfiguracionDTO dto, UsuarioDTO user) throws MarcadorServicioException;

	/**
	 * Se encarga de eliminar la configuracion
	 * 
	 * @param dto  {@link ConfiguracionDTO}
	 * @param user {@link UsuarioDTO}
	 * @throws {@link MarcadorServicioException}
	 */
	public void deleteConfiguracion(ConfiguracionDTO dto, UsuarioDTO user) throws MarcadorServicioException;

	/**
	 * Se encarga de contar los registros
	 * 
	 * @param dto {@link ConfiguracionDTO}
	 * @return {@link Integer}
	 * @throws {@link MarcadorServicioException}
	 */
	public Integer count(ConfiguracionDTO dto) throws MarcadorServicioException;

	/**
	 * Se encarga de obtener los registros paginados
	 * 
	 * @param configuracion {@link ConfiguracionDTO}
	 * @param inicio        {@link Integer}
	 * @param cantidad      {@link Integer}
	 * @return {@link List} of {@link ConfiguracionDTO}
	 * @throws {@link MarcadorServicioException}
	 */
	public List<ConfiguracionDTO> getConfiguracion(ConfiguracionDTO configuracion, Integer inicio, Integer cantidad)
			throws MarcadorServicioException;

	/**
	 * Se encarga de eliminar el registro de marcador
	 * 
	 * @param marcador {@link MarcadorDTO}
	 * @param user     {@link UsuarioDTO}
	 * @throws {@link MarcadorServicioException}
	 */
	public void deleteMarcador(MarcadorDTO marcador, UsuarioDTO user) throws MarcadorServicioException;

	/**
	 * Se encarga de eliminar el registro de servicio de campo de busqueda
	 * 
	 * @param dto  {@link ServicioCampoBusquedaDTO}
	 * @param user {@link UsuarioDTO}
	 * @throws {@link MarcadorServicioException}
	 */
	public void deleteServicioCampo(ServicioCampoBusquedaDTO dto, UsuarioDTO user) throws MarcadorServicioException;

	/**
	 * Se encarga de eliminar el campo del servicio asociado a un marcador
	 * 
	 * @param dto  {@link MarcadorServicioDTO}
	 * @param user {@link UsuarioDTO}
	 * @throws {@link MarcadorServicioException}
	 */
	public void deleteServicioMarcador(MarcadorServicioDTO dto, UsuarioDTO user) throws MarcadorServicioException;

	/**
	 * Se encarga de obtener los servicios asociados, segun en nombre del campo
	 * 
	 * @param nombreCampo {@link String}
	 * @return {@link List} of {@link String}
	 * @throws {@link MarcadorServicioException}
	 */
	public List<ServicioCampoBusquedaDTO> getServicioCampo(String nombreCampo) throws MarcadorServicioException;

	/**
	 * Se encarga de obtener los datos de configuracion con los marcadores de
	 * respuesta de la informacion que se encontro en la base de datos
	 * 
	 * @param nombreConfiguracion {@link String}
	 * @param servicio            {@link String}
	 * @param busqueda            {@link Map} < {@link String} , {@link Object} >
	 * @return {@link List} of {@link Map} < {@link String} , {@link Object} >
	 * @throws MarcadorServicioException
	 */
	public <T extends ADto, D extends ADto, S extends Object, L extends Object, N extends Object, K extends Services> N generar(
			String nombreConfiguracion, String servicio, Map<String, Object> busqueda) throws MarcadorServicioException;
	/**
	 * Se realiza busqueda de las configuraciones que tengan en los datos de entrada la clase suministrada
	 * @param <T>
	 * @param searchInput {@link Class} extends {@link ADto}
	 * @param report {@link boolean} 
	 * @param user {@link UsuarioDTO}
	 * @return {@link List} extends {@link ConfiguracionDTO}
	 * @throws {@link MarcadorServicioException}
	 */
	public <T extends ADto> List<ConfiguracionDTO> getConfiguraciones(Class<T> searchInput, boolean report, UsuarioDTO user)throws MarcadorServicioException;
}
