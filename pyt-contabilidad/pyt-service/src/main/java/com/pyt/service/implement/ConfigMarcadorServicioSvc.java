package com.pyt.service.implement;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.ADto;
import org.pyt.common.common.UsuarioDTO;
import org.pyt.common.constants.AppConstants;
import org.pyt.common.exceptions.MarcadorServicioException;
import org.pyt.common.exceptions.QueryException;
import org.pyt.common.poi.docs.Bookmark;
import org.pyt.common.poi.docs.TableBookmark;

import com.pyt.query.interfaces.IQuerySvc;
import com.pyt.service.abstracts.Services;
import com.pyt.service.dto.ConfiguracionDTO;
import com.pyt.service.dto.MarcadorDTO;
import com.pyt.service.dto.MarcadorServicioDTO;
import com.pyt.service.dto.ServicioCampoBusquedaDTO;
import com.pyt.service.interfaces.IConfigMarcadorServicio;

import co.com.arquitectura.librerias.implement.Services.ServicePOJO;
import co.com.arquitectura.librerias.implement.listProccess.AbstractListFromProccess;

public class ConfigMarcadorServicioSvc extends Services implements IConfigMarcadorServicio {
	@Inject(resource = "com.pyt.query.implement.QuerySvc")
	private IQuerySvc query;

	@Override
	public List<ServicioCampoBusquedaDTO> getServiciosCampoBusqueda(String configuracion)
			throws MarcadorServicioException {
		ServicioCampoBusquedaDTO dto = new ServicioCampoBusquedaDTO();
		dto.setConfiguracion(configuracion);
		try {
			return query.gets(dto);
		} catch (QueryException e) {
			throw new MarcadorServicioException(e);
		}
	}

	@Override
	public List<MarcadorServicioDTO> getMarcadorServicio(String configuracion) throws MarcadorServicioException {
		MarcadorServicioDTO dto = new MarcadorServicioDTO();
		dto.setConfiguracion(configuracion);
		try {
			return query.gets(dto);
		} catch (QueryException e) {
			throw new MarcadorServicioException(e);
		}
	}

	@Override
	public List<MarcadorDTO> getMarcador(String configuracion) throws MarcadorServicioException {
		MarcadorDTO dto = new MarcadorDTO();
		dto.setConfiguracion(configuracion);
		try {
			return query.gets(dto);
		} catch (QueryException e) {
			throw new MarcadorServicioException(e);
		}
	}

	@Override
	public MarcadorDTO insertMarcador(MarcadorDTO marcadorDto, UsuarioDTO usuario) throws MarcadorServicioException {
		if (marcadorDto == null)
			throw new MarcadorServicioException("No se suministro el marcador.");
		if (usuario == null)
			throw new MarcadorServicioException("No se suministro el usuario");
		if (StringUtils.isNotBlank(marcadorDto.getCodigo()))
			throw new MarcadorServicioException("El codigo del marcador  no se encuentra vacio");
		try {
			return query.set(marcadorDto, usuario);
		} catch (QueryException e) {
			throw new MarcadorServicioException(e);
		}
	}

	@Override
	public MarcadorServicioDTO insertMarcadorServicio(MarcadorServicioDTO dto, UsuarioDTO usuario)
			throws MarcadorServicioException {
		if (dto == null)
			throw new MarcadorServicioException("No se suministro el marcador asociado a servicio.");
		if (usuario == null)
			throw new MarcadorServicioException("No se suministro el usuario");
		if (StringUtils.isNotBlank(dto.getCodigo()))
			throw new MarcadorServicioException("El codigo del marcador asociado al servicio no se encuentra vacio");
		try {
			return query.set(dto, usuario);
		} catch (QueryException e) {
			throw new MarcadorServicioException(e);
		}
	}

	@Override
	public ServicioCampoBusquedaDTO insertServicioCampoBusqueda(ServicioCampoBusquedaDTO dto, UsuarioDTO usuario)
			throws MarcadorServicioException {
		if (dto == null)
			throw new MarcadorServicioException("No se suministro el servicio campo.");
		if (usuario == null)
			throw new MarcadorServicioException("No se suministro el usuario");
		if (StringUtils.isNotBlank(dto.getCodigo()))
			throw new MarcadorServicioException("El codigo del servicio campo no se encuentra vacio");
		try {
			return query.set(dto, usuario);
		} catch (QueryException e) {
			throw new MarcadorServicioException(e);
		}
	}

	@Override
	public void updateMarcador(MarcadorDTO dto, UsuarioDTO usuario) throws MarcadorServicioException {
		if (dto == null)
			throw new MarcadorServicioException("No se suministro el marcador.");
		if (usuario == null)
			throw new MarcadorServicioException("No se suministro el usuario");
		if (StringUtils.isBlank(dto.getCodigo()))
			throw new MarcadorServicioException("El codigo del marcador  se encuentra vacio");
		try {
			query.set(dto, usuario);
		} catch (QueryException e) {
			throw new MarcadorServicioException(e);
		}
	}

	@Override
	public void updateServicioCampo(ServicioCampoBusquedaDTO dto, UsuarioDTO usuario) throws MarcadorServicioException {
		if (dto == null)
			throw new MarcadorServicioException("No se suministro el servicio campo.");
		if (usuario == null)
			throw new MarcadorServicioException("No se suministro el usuario");
		if (StringUtils.isBlank(dto.getCodigo()))
			throw new MarcadorServicioException("El codigo del servicio campo se encuentra vacio");
		try {
			query.set(dto, usuario);
		} catch (QueryException e) {
			throw new MarcadorServicioException(e);
		}
	}

	@Override
	public void updateServicioMarcador(MarcadorServicioDTO dto, UsuarioDTO usuario) throws MarcadorServicioException {
		if (dto == null)
			throw new MarcadorServicioException("No se suministro el marcador asociado a servicio.");
		if (usuario == null)
			throw new MarcadorServicioException("No se suministro el usuario");
		if (StringUtils.isBlank(dto.getCodigo()))
			throw new MarcadorServicioException("El codigo del marcador asociado al servicio se encuentra vacio");
		try {
			query.set(dto, usuario);
		} catch (QueryException e) {
			throw new MarcadorServicioException(e);
		}
	}

	@Override
	public List<ServicioCampoBusquedaDTO> getServiciosCampoBusqueda(String configuracion, Integer inicio,
			Integer cantidad) throws MarcadorServicioException {
		ServicioCampoBusquedaDTO dto = new ServicioCampoBusquedaDTO();
		dto.setConfiguracion(configuracion);
		try {
			return query.gets(dto, inicio, cantidad);
		} catch (QueryException e) {
			throw new MarcadorServicioException(e);
		}
	}

	@Override
	public List<MarcadorServicioDTO> getMarcadorServicio(String configuracion, Integer inicio, Integer cantidad)
			throws MarcadorServicioException {
		MarcadorServicioDTO dto = new MarcadorServicioDTO();
		dto.setConfiguracion(configuracion);
		try {
			return query.gets(dto, inicio, cantidad);
		} catch (QueryException e) {
			throw new MarcadorServicioException(e);
		}
	}

	@Override
	public List<MarcadorDTO> getMarcador(String configuracion, Integer inicio, Integer cantidad)
			throws MarcadorServicioException {
		MarcadorDTO dto = new MarcadorDTO();
		dto.setConfiguracion(configuracion);
		try {
			return query.gets(dto, inicio, cantidad);
		} catch (QueryException e) {
			throw new MarcadorServicioException(e);
		}
	}

	@Override
	public Integer cantidadServiciosCampoBusqueda(String configuracion) throws MarcadorServicioException {
		ServicioCampoBusquedaDTO dto = new ServicioCampoBusquedaDTO();
		dto.setConfiguracion(configuracion);
		try {
			return query.countRow(dto);
		} catch (QueryException e) {
			throw new MarcadorServicioException(e);
		}
	}

	@Override
	public Integer cantidadMarcadorServicio(String configuracion) throws MarcadorServicioException {
		MarcadorServicioDTO dto = new MarcadorServicioDTO();
		dto.setConfiguracion(configuracion);
		try {
			return query.countRow(dto);
		} catch (QueryException e) {
			throw new MarcadorServicioException(e);
		}
	}

	@Override
	public Integer cantidadMarcador(String configuracion) throws MarcadorServicioException {
		MarcadorDTO dto = new MarcadorDTO();
		dto.setConfiguracion(configuracion);
		try {
			return query.countRow(dto);
		} catch (QueryException e) {
			throw new MarcadorServicioException(e);
		}
	}

	@Override
	public List<ConfiguracionDTO> getConfiguraciones(ConfiguracionDTO dto) throws MarcadorServicioException {
		if (dto == null)
			throw new MarcadorServicioException("No se suministro la configuración.");
		try {
			return query.gets(dto);
		} catch (QueryException e) {
			throw new MarcadorServicioException(e);
		}
	}

	@Override
	public ConfiguracionDTO insertConfiguracion(ConfiguracionDTO dto, UsuarioDTO user)
			throws MarcadorServicioException {
		if (dto == null)
			throw new MarcadorServicioException("No se suministro la configuración.");
		if (user == null)
			throw new MarcadorServicioException("NO se suministro el usuario.");
		if (StringUtils.isNotBlank(dto.getCodigo()))
			throw new MarcadorServicioException("El código no se ensuentra vacio.");
		try {
			return query.set(dto, user);
		} catch (QueryException e) {
			throw new MarcadorServicioException(e);
		}
	}

	@Override
	public void updateConfiguracion(ConfiguracionDTO dto, UsuarioDTO user) throws MarcadorServicioException {
		if (dto == null)
			throw new MarcadorServicioException("No se suministro la configuración.");
		if (user == null)
			throw new MarcadorServicioException("NO se suministro el usuario.");
		if (StringUtils.isBlank(dto.getCodigo()))
			throw new MarcadorServicioException("El código se ensuentra vacio.");
		try {
			query.set(dto, user);
		} catch (QueryException e) {
			throw new MarcadorServicioException(e);
		}
	}

	@Override
	public void deleteConfiguracion(ConfiguracionDTO dto, UsuarioDTO user) throws MarcadorServicioException {
		if (dto == null)
			throw new MarcadorServicioException("No se suministro la configuración.");
		if (user == null)
			throw new MarcadorServicioException("NO se suministro el usuario.");
		if (StringUtils.isBlank(dto.getCodigo()))
			throw new MarcadorServicioException("El código se ensuentra vacio.");
		if (getMarcador(dto.getConfiguracion()).size() > 0) {
			throw new MarcadorServicioException("Se encontraron marcadores asignados.");
		}
		if (getMarcadorServicio(dto.getConfiguracion()).size() > 0) {
			throw new MarcadorServicioException("Se encontraron marcadores asociado a servicio");
		}
		if (getServiciosCampoBusqueda(dto.getConfiguracion()).size() > 0) {
			throw new MarcadorServicioException("Se econtraron campos de busqueda en el servicios.");
		}
		try {
			query.del(dto, user);
		} catch (QueryException e) {
			throw new MarcadorServicioException(e);
		}
	}

	@Override
	public Integer count(ConfiguracionDTO dto) throws MarcadorServicioException {
		if (dto == null)
			throw new MarcadorServicioException("No se suministro la configuración.");
		try {
			return query.countRow(dto);
		} catch (QueryException e) {
			throw new MarcadorServicioException(e);
		}
	}

	@Override
	public List<ConfiguracionDTO> getConfiguracion(ConfiguracionDTO configuracion, Integer inicio, Integer cantidad)
			throws MarcadorServicioException {
		if (configuracion == null)
			throw new MarcadorServicioException("No se suministro la configuración.");
		try {
			return query.gets(configuracion, inicio, cantidad);
		} catch (QueryException e) {
			throw new MarcadorServicioException(e);
		}
	}

	@Override
	public void deleteMarcador(MarcadorDTO marcador, UsuarioDTO user) throws MarcadorServicioException {
		if (marcador == null)
			throw new MarcadorServicioException("No se suministro el marcador a eliminar.");
		if (user == null)
			throw new MarcadorServicioException("NO se suministro el usuario.");
		if (StringUtils.isBlank(marcador.getCodigo()))
			throw new MarcadorServicioException("El codigo del marcador se encuentra vacio.");
		try {
			query.del(marcador, user);
		} catch (QueryException e) {
			throw new MarcadorServicioException(e);
		}
	}

	@Override
	public void deleteServicioCampo(ServicioCampoBusquedaDTO dto, UsuarioDTO user) throws MarcadorServicioException {
		if (dto == null)
			throw new MarcadorServicioException("No se suministro el campo del servicio de busqueda a eliminar.");
		if (user == null)
			throw new MarcadorServicioException("NO se suministro el usuario.");
		if (StringUtils.isBlank(dto.getCodigo()))
			throw new MarcadorServicioException("El codigo del campo del servicio de busqueda se encuentra vacio.");
		try {
			query.del(dto, user);
		} catch (QueryException e) {
			throw new MarcadorServicioException(e);
		}
	}

	@Override
	public void deleteServicioMarcador(MarcadorServicioDTO dto, UsuarioDTO user) throws MarcadorServicioException {
		if (dto == null)
			throw new MarcadorServicioException(
					"No se suministro el campo del servicio asociado al marcador a eliminar.");
		if (user == null)
			throw new MarcadorServicioException("NO se suministro el usuario.");
		if (StringUtils.isBlank(dto.getCodigo()))
			throw new MarcadorServicioException(
					"El codigo del campo del servicio asociado al marcador se encuentra vacio.");
		try {
			query.del(dto, user);
		} catch (QueryException e) {
			throw new MarcadorServicioException(e);
		}
	}

	@Override
	public List<ServicioCampoBusquedaDTO> getServicioCampo(String nombreCampo) throws MarcadorServicioException {
		ConfiguracionDTO configuracion = new ConfiguracionDTO();
		configuracion.setConfiguracion(nombreCampo);

		List<ConfiguracionDTO> list;
		try {
			list = query.gets(configuracion);
			if (list.size() == 1) {
				for (ConfiguracionDTO dto : list) {
					ServicioCampoBusquedaDTO marcador = new ServicioCampoBusquedaDTO();
					marcador.setConfiguracion(dto.getConfiguracion());
					List<ServicioCampoBusquedaDTO> lista = query.gets(marcador);
					return lista;
				}
			}
		} catch (QueryException e) {
			throw new MarcadorServicioException(e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends ADto, D extends ADto, S extends Object, L extends Object, N extends Object, K extends Services> N generar(
			String nombreConfiguracion, String servicio, Map<String, Object> busqueda)
			throws MarcadorServicioException {
		Set<String> set = busqueda.keySet();
		T dto = null;
		for (String campo : set) {
			if (dto == null) {
				dto = getDTOService(servicio, campo);
				if (dto != null) {
					try {
						String[] split = campo.split("::");
						dto.set(split[1], busqueda.get(campo));
					} catch (Exception e) {
						throw new MarcadorServicioException(e);
					}
				}else {
					return null;
				}
			}
		}
		Map<String, String> mapa = new HashMap<String, String>();
		List<MarcadorServicioDTO> marcadores = getMarcadorServicio(nombreConfiguracion);
		for (MarcadorServicioDTO marcador : marcadores) {
			if (servicio.contains(marcador.getServicio())) {
				mapa.put(marcador.getNombreCampo(), marcador.getMarcador());
			}
		}
		S service = getService(servicio);
		K servic = (K) service;
		servic.load();
		L resultado = getResultService(service, getMetodo(servicio), dto);

		if (resultado instanceof List) {
			List<D> lista = (List<D>) resultado;
			if (lista != null && lista.size() > 0) {
				TableBookmark tbm = new TableBookmark();
				for (D d : lista) {
					Bookmark bookmark = getAsociaciones(d, mapa);
					if (bookmark != null) {
						tbm.add(bookmark);
					}
				}
				if (tbm.getSize() > 0) {
					return (N) tbm;
				}
				return null;
			}
		} else if (resultado instanceof ADto) {
			D d = (D) resultado;
			Bookmark bookmark = getAsociaciones(d, mapa);
			return (N) bookmark;
		}
		return null;
	}

	/**
	 * Obtiene todas las asociaciones entre campos y el resultado
	 * 
	 * @param dto
	 * @param mapa
	 *            {@link Map}
	 * @return {@link Bookmark}
	 * @throws MarcadorServicioException
	 */
	@SuppressWarnings("unchecked")
	private <T extends ADto> Bookmark getAsociaciones(T dto, Map<String, String> mapa)
			throws MarcadorServicioException {
		try {
			Bookmark book = null;
			Set<String> set = mapa.keySet();
			Class<T> clase = (Class<T>) dto.getClass();
			for (String campo : set) {
				String[] split = campo.split("::");
				if (clase.getSimpleName().contains(split[0])) {
					if (book == null) {
						book = new Bookmark();
					}
					book.add(split[1], dto.get(split[1]));
				}
			}
			return book;
		} catch (Exception e) {
			throw new MarcadorServicioException(e);
		}
	}

	@SuppressWarnings("unchecked")
	private <T extends Object, L extends Object, S extends ADto> T getResultService(L service, String metodo, S dto)
			throws MarcadorServicioException {
		try {
			Method method = getMethod((Class<Services>) service.getClass(), dto, metodo);
			if (method != null) {
				return (T) method.invoke(service, dto);
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new MarcadorServicioException(e);
		}
		return null;
	}

	/**
	 * Se encarga de obtener el metodo con el cual se necesita, para invoicar el
	 * servicio correctamente.
	 * 
	 * @param service
	 *            {@link Services} extends
	 * @param dto
	 *            {@link ADto} extends
	 * @param name
	 *            {@link String}
	 * @return {@link Method}
	 * @throws {@link
	 *             MarcadorServicioException}
	 */
	@SuppressWarnings("unchecked")
	private <T extends Services, L extends ADto, A extends ADto> Method getMethod(Class<T> service, L dto, String name)
			throws MarcadorServicioException {
		Method[] metodos = service.getDeclaredMethods();
		for (Method metodo : metodos) {
			if (metodo.getName().contains(name)) {
				Class<A>[] parametros = (Class<A>[]) metodo.getParameterTypes();
				if (parametros.length == 1) {
					for (Class<A> parametro : parametros) {
						if (parametro == dto.getClass()) {
							return metodo;
						}
					}
				}
			}
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes", "static-access" })
	private String getMetodo(String servicio) throws MarcadorServicioException {
		AbstractListFromProccess<ServicePOJO> listServices;
		try {
			listServices = (AbstractListFromProccess) this.getClass().forName(AppConstants.PATH_LIST_SERVICE)
					.getConstructor().newInstance();
			for (ServicePOJO service : listServices.getList()) {
				if ((service.getClasss().getSimpleName() + ":" + service.getAlias()).contains(servicio)) {
					return service.getName();
				}
			}
			return null;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException | ClassNotFoundException e) {
			throw new MarcadorServicioException(e);
		}
	}

	@SuppressWarnings({ "static-access", "unchecked", "rawtypes" })
	private <T extends Object> T getService(String servicio) throws MarcadorServicioException {
		try {
			AbstractListFromProccess<ServicePOJO> listServices = (AbstractListFromProccess) this.getClass()
					.forName(AppConstants.PATH_LIST_SERVICE).getConstructor().newInstance();
			for (ServicePOJO service : listServices.getList()) {
				if ((service.getClasss().getSimpleName() + ":" + service.getAlias()).contains(servicio)) {
					return (T) service.getClasss().getConstructor().newInstance();
				}
			}
			return null;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new MarcadorServicioException(e);
		} catch (ClassNotFoundException e) {
			throw new MarcadorServicioException(e);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes", "static-access" })
	private <T extends Object, S extends ADto> S getDTOService(String servicio, String campo)
			throws MarcadorServicioException {
		try {
			AbstractListFromProccess<ServicePOJO> listServices = (AbstractListFromProccess) this.getClass()
					.forName(AppConstants.PATH_LIST_SERVICE).getConstructor().newInstance();
			String[] split = campo.split("::");
			Boolean valid = true;
			for (ServicePOJO service : listServices.getList()) {
				if ((service.getClasss().getSimpleName() + ":" + service.getAlias()).contains(servicio)) {
					Method[] metodos = service.getClasss().getDeclaredMethods();
					for (Method metodo : metodos) {
						if (metodo.getName().contains(service.getName())) {
							Parameter[] parametros = metodo.getParameters();
							for (String parametro : service.getParameter()) {
								for (Parameter parameter : parametros) {
									System.out.println(parameter.getParameterizedType().getTypeName() + " - "
											+ (parametro) + " - " + split[0]);
									valid &= parameter.getParameterizedType().getTypeName().contains(parametro)
											&& parametro.contains(split[0]);
								}
							}
							if (valid) {
								for (Parameter parametro : parametros) {
									System.out.println(
											parametro.getParameterizedType().getTypeName() + " - " + (split[0]));
									if (parametro.getParameterizedType().getTypeName().contains(split[0])) {
										Object obj = Class.forName(parametro.getParameterizedType().getTypeName())
												.getConstructor().newInstance();
										if (obj instanceof ADto) {
											S ret = (S) obj;
											return ret;
										}
										return (S) obj;
									}
								}
							} else {
								valid = true;
							}

						}
					}
				}
			}
		} catch (InstantiationException e) {
			throw new MarcadorServicioException(e);
		} catch (IllegalAccessException e) {
			throw new MarcadorServicioException(e);
		} catch (IllegalArgumentException e) {
			throw new MarcadorServicioException(e);
		} catch (InvocationTargetException e) {
			throw new MarcadorServicioException(e);
		} catch (NoSuchMethodException e) {
			throw new MarcadorServicioException(e);
		} catch (SecurityException e) {
			throw new MarcadorServicioException(e);
		} catch (ClassNotFoundException e) {
			throw new MarcadorServicioException(e);
		}
		return null;
	}

}