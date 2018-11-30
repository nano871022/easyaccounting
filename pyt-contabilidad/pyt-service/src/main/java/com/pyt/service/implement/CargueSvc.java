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
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.ADto;
import org.pyt.common.common.UsuarioDTO;
import org.pyt.common.exceptions.CargueException;
import org.pyt.common.exceptions.MarcadorServicioException;
import org.pyt.common.exceptions.ProccesConfigServiceException;

import com.pyt.service.abstracts.Services;
import com.pyt.service.dto.ConfiguracionDTO;
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
			
			servicioMarcadores(marcadores,mapa);

			IFileLoader fileLoader = new FileLoadText();
			fileLoader.setFile(file);
			fileLoader.load((line, numLine) -> {
				AnalizedAnnotationProcces aap = new AnalizedAnnotationProcces();
				String[] columnas = line.split(file.getSeparate());
				if (marcadores.size() != columnas.length)
					throw new Exception("Las columnas suministradas no son la misma cantidad a las configuradas.");
				int column = 1;
				Object inst = null;
				for (String columna : columnas) {
					for (ServicioCampoBusquedaDTO marcador : marcadores) {
						if (marcador.getColumna() == column) {
							inst = mapa.get(marcador.getServicio()).get(marcador.getCampo().split("::")[0]);
							mapa.get(marcador.getServicio()).setNumLine(numLine);
							if (inst instanceof ADto) {
								Field field = inst.getClass()
										.getDeclaredField(marcador.getCampo().split(DOUBLE_2_DOT)[1]);
								if (field.getType() == Date.class || field.getType() == LocalDate.class
										|| field.getType() == LocalDateTime.class) {
									inst = aap.DateTime((ADto) inst, field.getName(), columna);
								}
								((ADto) inst).set((marcador.getCampo().split(DOUBLE_2_DOT))[1], columna);
								aap.size((ADto) inst, field.getName());
								break;
							}

						}
					}
					column++;
				}
				for (String servicio : mapa.keySet()) {
					ProccessPOJO pojo = mapa.get(servicio).copy(); 
					loading(pojo, user);
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
	 * Se encarga de cargar los marcadores  dentro de un mapa
	 * @param marcadores {@link List} extends {@link ServicioCampoBusquedaDTO}
	 * @param mapa {@link Map} < {@link String} , {@link ProccessPOJO} >
	 */
	public final void servicioMarcadores(List<ServicioCampoBusquedaDTO> marcadores,Map<String, ProccessPOJO> mapa) {
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
	 * @param list
	 * @param fileLoader
	 * @throws Exception
	 */
	public final void fileLoaderOut(List<ProccessPOJO> list,IFileLoader fileLoader ) throws Exception{
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
	 * Se encarga de cargar cada registro en la base de datos y realizar las alidaciones necesarias 
	 * @param procces {@link ProccessPOJO}
	 * @param user {@link UsuarioDTO}
	 */
	@SuppressWarnings({ "rawtypes", "unused", "unchecked" })
	public final <T extends ADto>void loading(ProccessPOJO procces,UsuarioDTO user) {
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
					procces.addError("Uno de los campos se encuentra vacio.");
				}
				T valid = aap.valid((T) procces.get(parameter));
				error = false;
			} catch (Exception e) {
				procces.addError(e);
				error = true;
			}
		}
		try {
			if (error)return;
			Object result = method.invoke(procces.getService(), params);
			if (!Void.TYPE.isAssignableFrom(method.getReturnType())) {
				procces.add(result);
			}
		} catch (Exception e) {
			procces.addError(e);
		}
	}
}
