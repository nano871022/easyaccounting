package com.pyt.service.implement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.abstracts.ADto;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.ListUtils;
import org.pyt.common.constants.ConfigServiceConstant;
import org.pyt.common.exceptions.MarcadorServicioException;
import org.pyt.common.exceptions.ProccesConfigServiceException;
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
import com.pyt.service.proccess.ProccesConfigService;

import co.com.japl.ea.dto.system.UsuarioDTO;

public class ConfigMarcadorServicioSvc extends Services implements IConfigMarcadorServicio {
	@Inject(resource = "com.pyt.query.implement.QuerySvc")
	private IQuerySvc query;

	@Override
	public List<ServicioCampoBusquedaDTO> getServiciosCampoBusqueda(ConfiguracionDTO configuracion)
			throws MarcadorServicioException {
		ServicioCampoBusquedaDTO dto = new ServicioCampoBusquedaDTO();
		var list = getConfiguraciones(configuracion);
		if (list == null || list.size() == 0) {
			return new ArrayList<ServicioCampoBusquedaDTO>();
		}
		dto.setConfiguracion(list.get(0));

		try {
			return query.gets(dto);
		} catch (QueryException e) {
			throw new MarcadorServicioException(e);
		}
	}

	@Override
	public List<MarcadorServicioDTO> getMarcadorServicio(ConfiguracionDTO configuracion)
			throws MarcadorServicioException {
		MarcadorServicioDTO dto = new MarcadorServicioDTO();
		var list = getConfiguraciones(configuracion);
		if (list == null || list.size() == 0) {
			return new ArrayList<MarcadorServicioDTO>();
		}
		dto.setConfiguracion(list.get(0));

		try {
			return query.gets(dto);
		} catch (QueryException e) {
			throw new MarcadorServicioException(e);
		}
	}

	@Override
	public List<MarcadorDTO> getMarcador(ConfiguracionDTO configuracion) throws MarcadorServicioException {
		MarcadorDTO dto = new MarcadorDTO();
		var list = getConfiguraciones(configuracion);
		if (list == null || list.size() == 0) {
			return new ArrayList<MarcadorDTO>();
		}
		dto.setConfiguracion(list.get(0));

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
	public List<ServicioCampoBusquedaDTO> getServiciosCampoBusqueda(ConfiguracionDTO configuracion, Integer inicio,
			Integer cantidad) throws MarcadorServicioException {
		ServicioCampoBusquedaDTO dto = new ServicioCampoBusquedaDTO();
		var list = getConfiguraciones(configuracion);
		if (list == null || list.size() == 0) {
			return new ArrayList<ServicioCampoBusquedaDTO>();
		}
		dto.setConfiguracion(list.get(0));
		try {
			if (StringUtils.isNotBlank(dto.getConfiguracion().getConfiguracion())
					&& StringUtils.isBlank(dto.getConfiguracion().getCodigo())) {
				dto.setConfiguracion(query.get(dto.getConfiguracion()));
			}
			return query.gets(dto, inicio, cantidad);
		} catch (QueryException e) {
			throw new MarcadorServicioException(e);
		}
	}

	@Override
	public List<MarcadorServicioDTO> getMarcadorServicio(ConfiguracionDTO configuracion, Integer inicio,
			Integer cantidad) throws MarcadorServicioException {
		MarcadorServicioDTO dto = new MarcadorServicioDTO();
		var list = getConfiguraciones(configuracion);
		if (list == null || list.size() == 0) {
			return new ArrayList<MarcadorServicioDTO>();
		}
		dto.setConfiguracion(list.get(0));

		try {
			return query.gets(dto, inicio, cantidad);
		} catch (QueryException e) {
			throw new MarcadorServicioException(e);
		}
	}

	@Override
	public List<MarcadorDTO> getMarcador(ConfiguracionDTO configuracion, Integer inicio, Integer cantidad)
			throws MarcadorServicioException {
		MarcadorDTO dto = new MarcadorDTO();
		var list = getConfiguraciones(configuracion);
		if (list == null || list.size() == 0) {
			return new ArrayList<MarcadorDTO>();
		}
		dto.setConfiguracion(list.get(0));

		try {
			return query.gets(dto, inicio, cantidad);
		} catch (QueryException e) {
			throw new MarcadorServicioException(e);
		}
	}

	@Override
	public Integer cantidadServiciosCampoBusqueda(ConfiguracionDTO configuracion) throws MarcadorServicioException {
		ServicioCampoBusquedaDTO dto = new ServicioCampoBusquedaDTO();
		var list = getConfiguraciones(configuracion);
		if (list == null || list.size() == 0) {
			return 0;
		}
		dto.setConfiguracion(list.get(0));

		try {
			return query.countRow(dto);
		} catch (QueryException e) {
			throw new MarcadorServicioException(e);
		}
	}

	@Override
	public Integer cantidadMarcadorServicio(ConfiguracionDTO configuracion) throws MarcadorServicioException {
		MarcadorServicioDTO dto = new MarcadorServicioDTO();
		var list = getConfiguraciones(configuracion);
		if (list == null || list.size() == 0) {
			return 0;
		}
		dto.setConfiguracion(list.get(0));

		try {
			return query.countRow(dto);
		} catch (QueryException e) {
			throw new MarcadorServicioException(e);
		}
	}

	@Override
	public Integer cantidadMarcador(ConfiguracionDTO configuracion) throws MarcadorServicioException {
		MarcadorDTO dto = new MarcadorDTO();
		var list = getConfiguraciones(configuracion);
		if (list == null || list.size() == 0) {
			return 0;
		}
		dto.setConfiguracion(list.get(0));
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
		if (getMarcador(dto).size() > 0) {
			throw new MarcadorServicioException("Se encontraron marcadores asignados.");
		}
		if (getMarcadorServicio(dto).size() > 0) {
			throw new MarcadorServicioException("Se encontraron marcadores asociado a servicio");
		}
		if (getServiciosCampoBusqueda(dto).size() > 0) {
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
					marcador.setConfiguracion(dto);
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
		try {
			ProccesConfigService proccess = new ProccesConfigService();
			Set<String> set = busqueda.keySet();
			T dto = null;
			for (String campo : set) {
				if (dto == null) {
					dto = proccess.getDTOService(servicio, campo);
					if (dto != null) {
						try {
							String[] split = campo.split(ConfigServiceConstant.SEP_2_DOTS);
							dto.set(split[1], busqueda.get(campo));
						} catch (Exception e) {
							throw new MarcadorServicioException(e);
						}
					} else {
						return null;
					}
				}
			}
			var Configuracion = new ConfiguracionDTO();
			Configuracion.setConfiguracion(nombreConfiguracion);
			Configuracion = query.get(Configuracion);
			Map<String, String> mapa = new HashMap<String, String>();
			List<MarcadorServicioDTO> marcadores = getMarcadorServicio(Configuracion);
			for (MarcadorServicioDTO marcador : marcadores) {
				if (servicio.contains(marcador.getServicio())) {
					mapa.put(marcador.getNombreCampo(), marcador.getMarcador());
				}
			}
			S service = proccess.getService(servicio);
			K servic = (K) service;
			servic.load();
			L resultado = proccess.getResultService(service, proccess.getMetodo(servicio).getName(), dto);

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
		} catch (ProccesConfigServiceException | QueryException e) {
		}
		return null;
	}

	/**
	 * Obtiene todas las asociaciones entre campos y el resultado
	 * 
	 * @param dto
	 * @param mapa {@link Map}
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
				String[] split = campo.split(ConfigServiceConstant.SEP_2_DOTS);
				if (clase.getSimpleName().contains(split[0])) {
					if (book == null) {
						book = new Bookmark();
					}
					book.add(mapa.get(campo), dto.get(split[1]));
				}
			}
			return book;
		} catch (Exception e) {
			throw new MarcadorServicioException(e);
		}
	}

	@Override
	public <T extends ADto> List<ConfiguracionDTO> getConfiguraciones(Class<T> searchInput, boolean report,
			UsuarioDTO user) throws MarcadorServicioException {
		var list = new ArrayList<ConfiguracionDTO>();
		try {
			ProccesConfigService proccess = new ProccesConfigService();
			var services = proccess.getServices(searchInput);
			for (var service : services) {
				var pair = service.getClasss().getSimpleName() + ConfigServiceConstant.SEP_2_DOT + service.getAlias();
				var servicio = new ServicioCampoBusquedaDTO();
				servicio.setServicio(pair);
				var founds = query.gets(servicio);
				if (ListUtils.isNotBlank(founds)) {
					founds.stream()
							.filter(found -> !list.stream().anyMatch(conf -> conf.getCodigo().contentEquals(found.getCodigo())))
							.filter(found -> found.getConfiguracion().getReport() == report)
							.map( found -> found.getConfiguracion())
							.forEach(list::add);
				}
			}
		} catch (Exception e) {
			throw new MarcadorServicioException(messageI18n("err.service.getConfigurations.error"),e);
		}
		return list;
	}
}
