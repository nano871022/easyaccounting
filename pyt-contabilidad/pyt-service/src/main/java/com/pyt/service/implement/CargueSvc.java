package com.pyt.service.implement;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.abstracts.ADto;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.UsuarioDTO;
import org.pyt.common.exceptions.CargueException;
import org.pyt.common.exceptions.MarcadorServicioException;
import org.pyt.common.exceptions.ProccesConfigServiceException;
import org.pyt.common.reflection.ReflectionUtils;

import com.pyt.service.abstracts.Services;
import com.pyt.service.dto.ConfiguracionDTO;
import com.pyt.service.dto.MarcadorDTO;
import com.pyt.service.dto.ServicioCampoBusquedaDTO;
import com.pyt.service.interfaces.ICargue;
import com.pyt.service.interfaces.IConfigMarcadorServicio;
import com.pyt.service.pojo.ProccessPOJO;
import com.pyt.service.proccess.AnalizedAnnotationProcces;
import com.pyt.service.proccess.ProccesConfigService;

import co.com.japl.ea.loader.implement.FileLoadText;
import co.com.japl.ea.loader.interfaces.IFileLoader;
import co.com.japl.ea.loader.pojo.FilePOJO;

public class CargueSvc extends Services implements ICargue {
	@Inject(resource = "com.pyt.service.implement.ConfigMarcadorServicioSvc")
	private IConfigMarcadorServicio configMarkService;
	private final static String DOUBLE_2_DOT = "::";

	/**
	 * Se encarga de obtener la cantidad de marcadores usados dentro de la
	 * configuración
	 * 
	 * @param marcadores
	 *            {@link List} < {@link ServicioCampoBusquedaDTO} >
	 * @return {@link Integer}
	 */
	private final Integer cantidadMarcadoresUsados(List<ServicioCampoBusquedaDTO> marcadores) {
		Map<String, String> marcadoress = new HashMap<String, String>();
		for (ServicioCampoBusquedaDTO dto : marcadores) {
			marcadoress.put(dto.getMarcador(), dto.getServicio());
		}
		return marcadoress.size();
	}

	/**
	 * Realiza la busqueda dentro de los marcadores y comprara con el orden idicado
	 * y retorna el arcador que corresponde al numero de la orden
	 * 
	 * @param list
	 *            {@link List} < {@link MarcadorDTO} >
	 * @param orden
	 *            {@link Integer}
	 * @return {@link MarcadorDTO}
	 */
	public final MarcadorDTO getMarcador(List<MarcadorDTO> list, Integer orden) {
		for (MarcadorDTO marcador : list) {
			if (marcador.getOrden().compareTo(orden) == 0) {
				return marcador;
			}
		}
		return null;
	}

	/**
	 * Se encarga de obtener todos los servicios asociados al marcador indicado
	 * 
	 * @param marcador
	 *            {@link MarcadorDTO}
	 * @param marcadoresServicios
	 *            {@link List} < {@link ServicioCampoBusquedaDTO} >
	 * @return {@link List} < {@link ServicioCampoBusquedaDTO} >
	 */
	public final List<ServicioCampoBusquedaDTO> getMarcadorServicio(MarcadorDTO marcador,
			List<ServicioCampoBusquedaDTO> marcadoresServicios) {
		List<ServicioCampoBusquedaDTO> list = new ArrayList<ServicioCampoBusquedaDTO>();
		for (ServicioCampoBusquedaDTO marcadorServicio : marcadoresServicios) {
			if (marcadorServicio.getMarcador().compareTo(marcador.getMarcador()) == 0) {
				list.add(marcadorServicio);
			}
		}
		return list;
	}

	@Override
	public <T extends ADto> FilePOJO cargue(String nameConfig, FilePOJO file, UsuarioDTO user) throws CargueException {
		FilePOJO output_file = null;
		try {
			List<ProccessPOJO> listRows = new ArrayList<ProccessPOJO>();
			Map<String, ProccessPOJO> mapa = new HashMap<String, ProccessPOJO>();
			if (StringUtils.isBlank(nameConfig))
				throw new CargueException("El nombre de la configuracion no se suministro.");
			if (file == null || file.getByte() == null)
				throw new CargueException("El archivo suministrado no fue cargado correctamente.");
			List<MarcadorDTO> listMarcadores = configMarkService.getMarcador(nameConfig);
			if (listMarcadores == null) {
				throw new CargueException("No se encontro los marcadores configurados.");
			}
			ConfiguracionDTO dto = new ConfiguracionDTO();
			dto.setConfiguracion(nameConfig);
			List<ConfiguracionDTO> list = configMarkService.getConfiguraciones(dto);
			if (list == null)
				throw new CargueException("No se encontro registros.");
			if (list.size() > 1)
				throw new CargueException("Se encontraron varios registros con el nombre de la configuracion.");
			dto = list.get(0);
			List<ServicioCampoBusquedaDTO> marcadores = configMarkService.getServicioCampo(nameConfig);
			if (marcadores == null || marcadores.size() == 0)
				throw new CargueException("No se encontraron marcadores.");

			servicioMarcadores(marcadores, mapa);
			Integer cantidad = cantidadMarcadoresUsados(marcadores);
			IFileLoader fileLoader = new FileLoadText();
			fileLoader.setFile(file);
			fileLoader.load((line, numLine) -> {
				AnalizedAnnotationProcces aap = new AnalizedAnnotationProcces();
				String[] columnas = line.split(file.getSeparate());
				if (cantidad != columnas.length)
					throw new Exception("Las columnas suministradas no son la misma cantidad a las configuradas. ("
							+ marcadores.size() + "," + columnas.length + ")");
				int column = 0;
				Object inst = null;
				for (String columna : columnas) {
					column++;
					MarcadorDTO mark = getMarcador(listMarcadores, column);
					List<ServicioCampoBusquedaDTO> listMarcadorUsado = getMarcadorServicio(mark, marcadores);
					for (ServicioCampoBusquedaDTO marcador : listMarcadorUsado) {
						inst = mapa.get(marcador.getServicio()).get(marcador.getCampo().split(DOUBLE_2_DOT)[0]);
						mapa.get(marcador.getServicio()).setNumLine(numLine);
						if (inst instanceof ADto) {
							Field field = ReflectionUtils.instanciar().getField(inst.getClass(), marcador.getCampo().split(DOUBLE_2_DOT)[1]);
							if (field.getType() == Date.class || field.getType() == LocalDate.class
									|| field.getType() == LocalDateTime.class) {
								inst = aap.DateTime((ADto) inst, field.getName(), columna);
							}
							aap.valueInObject((ADto)inst, (marcador.getCampo().split(DOUBLE_2_DOT))[1], columna);
							aap.size((ADto) inst, field.getName());
						}

					}
				}
				for (String servicio : mapa.keySet()) {
					ProccessPOJO pojo = mapa.get(servicio).copy();
					pojo = loading(pojo, user);
					listRows.add(pojo);
				}

			});
			fileLoaderOut(listRows, fileLoader);
			output_file = fileLoader.genFileOut();
		} catch (MarcadorServicioException e) {
			throw new CargueException("Se presento problema con la obtencion de la configuracion.", e);
		} catch (Exception e) {
			throw new CargueException("Se presento un problema.", e);
		}
		return output_file;
	}

	/**
	 * Se encarga de cargar los marcadores dentro de un mapa
	 * 
	 * @param marcadores
	 *            {@link List} extends {@link ServicioCampoBusquedaDTO}
	 * @param mapa
	 *            {@link Map} < {@link String} , {@link ProccessPOJO} >
	 */
	public final void servicioMarcadores(List<ServicioCampoBusquedaDTO> marcadores, Map<String, ProccessPOJO> mapa) {
		ProccesConfigService proccess = new ProccesConfigService();
		for (ServicioCampoBusquedaDTO marcador : marcadores) {
			try {
				if (mapa.get(marcador.getServicio()) == null) {
					mapa.put(marcador.getServicio(), new ProccessPOJO());
					mapa.get(marcador.getServicio()).setServicio(marcador.getServicio());
					mapa.get(marcador.getServicio()).setService(proccess.getService(marcador.getServicio()));
					mapa.get(marcador.getServicio()).setMethod(proccess.getMetodo(marcador.getServicio()));
				}
				if (mapa.get(marcador.getServicio()).get((marcador.getMarcador().split(DOUBLE_2_DOT))[0]) == null) {
					mapa.get(marcador.getServicio())
							.add(proccess.getDTOService(marcador.getServicio(), marcador.getCampo()));
				}
			} catch (ProccesConfigServiceException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Se encarga de cargar el resultado de los archivos de salida
	 * 
	 * @param list
	 * @param fileLoader
	 * @throws Exception
	 */
	public final void fileLoaderOut(List<ProccessPOJO> list, IFileLoader fileLoader) throws Exception {
		for (ProccessPOJO procces : list) {
			if (procces.getErrores() != null && procces.getErrores().size() > 0) {
				String[] errores = procces.getErrores().toArray(new String[procces.getErrores().size()]);
				fileLoader.addResults(procces.getNumLine(), errores);
			} else {
				fileLoader.addResults(procces.getNumLine(), "OK");
			}
		}
	}

	/**
	 * Se encarga de cargar cada registro en la base de datos y realizar las
	 * alidaciones necesarias
	 * 
	 * @param procces
	 *            {@link ProccessPOJO}
	 * @param user
	 *            {@link UsuarioDTO}
	 */
	@SuppressWarnings({ "rawtypes", "unused", "unchecked" })
	public final <T extends ADto> ProccessPOJO loading(ProccessPOJO procces, UsuarioDTO user) {
		AnalizedAnnotationProcces aap = new AnalizedAnnotationProcces();
		Boolean error = false;
		Method method = procces.getMethod();
		Object[] params = new Object[method.getParameterTypes().length];
		for (String parameter : procces.parameters()) {
			int i = 0;
			for (Class clazz : method.getParameterTypes()) {
				if (clazz == UsuarioDTO.class) {
					params[i] = user;
				} else {
					Object param = procces.get(clazz.getSimpleName());
					if (param instanceof ADto)
						if (StringUtils.isNotBlank(((ADto) param).getCodigo()))
							break;
					if (param != null) {
						params[i] = param;
					}
				}
				i++;
			}
			try {
				if (!aap.isNotBlank((ADto) procces.get(parameter))) {
					procces.addError("Los siguientes campos se encuentran vacios: "+String.join(",",aap.getMarkBlank().toArray(new String[aap.getMarkBlank().size()])));
				}
				T valid = aap.valid((T) procces.get(parameter));
				error = false;
			} catch (Exception e) {
				procces.addError(e);
				error = true;
			}
		}
		try {
			if (error)
				return procces;
			Object result = method.invoke(procces.getService(), params);
			if (!Void.TYPE.isAssignableFrom(method.getReturnType())) {
				procces.add(result);
			}
		} catch (Exception e) {
			procces.addError(e);
		}
		return procces;
	}
}
