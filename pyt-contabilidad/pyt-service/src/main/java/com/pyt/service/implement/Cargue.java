package com.pyt.service.implement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.ADto;
import org.pyt.common.exceptions.CargueException;
import org.pyt.common.exceptions.MarcadorServicioException;
import org.pyt.common.exceptions.ProccesConfigServiceException;

import com.pyt.service.dto.ConfiguracionDTO;
import com.pyt.service.dto.ServicioCampoBusquedaDTO;
import com.pyt.service.interfaces.ICargue;
import com.pyt.service.interfaces.IConfigMarcadorServicio;
import com.pyt.service.pojo.ProccessPOJO;
import com.pyt.service.proccess.ProccesConfigService;

import co.com.japl.ea.loader.implement.FileLoadText;
import co.com.japl.ea.loader.interfaces.IFileLoader;
import co.com.japl.ea.loader.pojo.FilePOJO;

public class Cargue implements ICargue {
	@Inject(resource = "com.pyt.service.implement.ConfigMarcadorServicioSvc")
	private IConfigMarcadorServicio configMarkService;
	private final static String DOUBLE_2_DOT = "::";
	@Override
	public FilePOJO cargue(String nameConfig, FilePOJO file) throws CargueException {
		try {
			Map<String, ProccessPOJO> mapa = new HashMap<String, ProccessPOJO>();
			ProccesConfigService proccess = new ProccesConfigService();
			if (StringUtils.isNotBlank(nameConfig))
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
			marcadores.forEach(marcador -> {
				try {
					if (mapa.get(marcador.getServicio()) != null) {
						mapa.put(marcador.getServicio(), new ProccessPOJO());
						mapa.get(marcador.getServicio()).setServicio(marcador.getServicio());
						mapa.get(marcador.getServicio()).setService(proccess.getService(marcador.getServicio()));
					}
					if(mapa.get(marcador.getServicio()).get((marcador.getMarcador().split(DOUBLE_2_DOT))[0]) != null) {
						mapa.get(marcador.getServicio()).add(proccess.getDTOService(marcador.getServicio(), marcador.getCampo()));
					}
				} catch (ProccesConfigServiceException e) {
					e.printStackTrace();
				}
			});

			IFileLoader fileLoader = new FileLoadText();
			fileLoader.setFile(file);
			fileLoader.load(line -> {
				String[] columnas = line.split(file.getSeparate());
				if (marcadores.size() == columnas.length)
					throw new Exception("Las columnas suministradas no son la misma cantidad a las configuradas.");
				int column = 1;
				for (String columna : columnas) {
					for (ServicioCampoBusquedaDTO marcador : marcadores) {
						if (marcador.getColumna() == column) {
							Object inst = mapa.get(marcador.getServicio()).getService();
							if (inst instanceof ADto) {
								((ADto) inst).set((marcador.getCampo().split(DOUBLE_2_DOT))[1], columna);
							}
						}
					}
					column++;
				}
			});

		} catch (MarcadorServicioException e) {
			throw new CargueException("Se presento problema con la obtencion de la configuracion.", e);
		} catch (Exception e) {
			throw new CargueException("Se presento un problema.", e);
		}
		return null;
	}
}
