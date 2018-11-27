package com.pyt.service.implement;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

	@SuppressWarnings({ "unchecked", "unused", "rawtypes" })
	@Override
	public <T extends ADto> FilePOJO cargue(String nameConfig, FilePOJO file, UsuarioDTO user) throws CargueException {
		FilePOJO output_file = null;
		try {
			Map<String, ProccessPOJO> mapa = new HashMap<String, ProccessPOJO>();
			ProccesConfigService proccess = new ProccesConfigService();
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
			for(ServicioCampoBusquedaDTO marcador : marcadores ){
				try {
					if (mapa.get(marcador.getServicio()) == null) {
						mapa.put(marcador.getServicio(), new ProccessPOJO());
						mapa.get(marcador.getServicio()).setServicio(marcador.getServicio());
						mapa.get(marcador.getServicio()).setService(proccess.getService(marcador.getServicio()));
						mapa.get(marcador.getServicio()).setMethod(proccess.getMetodo(marcador.getServicio()));
					}
					if (mapa.get(marcador.getServicio()).get((marcador.getMarcador().split(DOUBLE_2_DOT))[0]) != null) {
						mapa.get(marcador.getServicio())
								.add(proccess.getDTOService(marcador.getServicio(), marcador.getCampo()));
					}
				} catch (ProccesConfigServiceException e) {
					e.printStackTrace();
				}
			}

			IFileLoader fileLoader = new FileLoadText();
			fileLoader.setFile(file);
			fileLoader.load((line,numLine) -> {
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
								aap.size((ADto) inst, columna);
								((ADto) inst).set((marcador.getCampo().split(DOUBLE_2_DOT))[1], columna);
							}

						}
					}
					column++;
				}

			});
			AnalizedAnnotationProcces aap = new AnalizedAnnotationProcces();
			Set<String> sets = mapa.keySet();
			for (String set : sets) {
				for (String parameter : mapa.get(set).parameters()) {
					if (!aap.isNotBlank((ADto) mapa.get(set).get(parameter))) {
						mapa.get(set).addError("Uno de los campos se encuentra vacio.");
					}
					try {
						T valid = aap.valid((T) mapa.get(set).get(parameter));
					} catch (Exception e) {
						mapa.get(set).add(e.getMessage());
					}
				}
			}

			for (String set : sets) {
				for (String parameter : mapa.get(set).parameters()) {
					Method method = mapa.get(set).getMethod();
					Object[] params = new Object[method.getParameterTypes().length];
					int i = 0;
					for (Class clazz : method.getParameterTypes()) {
						if (clazz == UsuarioDTO.class) {
							params[i] = user;
						} else {
							Object param = mapa.get(set).get(clazz.getSimpleName());
							if (param != null) {
								params[i] = param;
							}
						}
						i++;
					}
					try {

						Object result = method.invoke(mapa.get(set).getService(), params);
						if (!Void.TYPE.isAssignableFrom(method.getReturnType())) {
							mapa.get(set).setService(result);
						}
					} catch (Exception e) {
						mapa.get(set).addError(e.getMessage());
					}
				}
			}
			for(String set : sets) {
				String[] errores = mapa.get(set).getErrores().toArray(new String[mapa.get(set).getErrores().size()]);
				fileLoader.addResults(mapa.get(set).getNumLine(), errores);
			}
			output_file = fileLoader.genFileOut();
		} catch (MarcadorServicioException e) {
			throw new CargueException("Se presento problema con la obtencion de la configuracion.", e);
		} catch (Exception e) {
			throw new CargueException("Se presento un problema.", e);
		}
		return output_file;
	}
}
