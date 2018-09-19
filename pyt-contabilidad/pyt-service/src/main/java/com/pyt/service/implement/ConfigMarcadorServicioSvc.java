package com.pyt.service.implement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.UsuarioDTO;
import org.pyt.common.exceptions.MarcadorServicioException;
import org.pyt.common.exceptions.QueryException;

import com.pyt.query.interfaces.IQuerySvc;
import com.pyt.service.abstracts.Services;
import com.pyt.service.dto.ConfiguracionDTO;
import com.pyt.service.dto.MarcadorDTO;
import com.pyt.service.dto.MarcadorServicioDTO;
import com.pyt.service.dto.ServicioCampoBusquedaDTO;
import com.pyt.service.interfaces.IConfigMarcadorServicio;

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

	@Override
	public List<Map<String, Object>> generar(String nombreConfiguracion, String servicio, Map<String, Object> busqueda)
			throws MarcadorServicioException {
		ServicioCampoBusquedaDTO dto = new ServicioCampoBusquedaDTO();
		dto.setServicio(servicio);
		dto.setConfiguracion(nombreConfiguracion);
		Set<String> set = busqueda.keySet();
		for(String nombre : set) {
			dto.set(nombre, busqueda.get(nombre));
		}
		return null;
	}

}
