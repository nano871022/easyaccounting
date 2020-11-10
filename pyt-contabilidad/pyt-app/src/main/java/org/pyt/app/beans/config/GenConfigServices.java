package org.pyt.app.beans.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.ListUtils;
import org.pyt.common.exceptions.MarcadorServicioException;

import com.pyt.service.dto.ConfiguracionDTO;
import com.pyt.service.dto.ServicioCampoBusquedaDTO;
import com.pyt.service.interfaces.IConfigMarcadorServicio;

/**
 * Se encarga de generar la configuracion de servicios de impresion
 * 
 * @author Alejandro Parra
 * @since 23-09-2018
 */
public class GenConfigServices {
	@Inject(resource = "com.pyt.service.implement.ConfigMarcadorServicioSvc")
	private IConfigMarcadorServicio configMarcadorServicio;
	private String nombreConfiguracion;
	private String nameFiler;

	public GenConfigServices(IConfigMarcadorServicio config) {
		if (configMarcadorServicio == null) {
			configMarcadorServicio = config;
		}
	}

	/**
	 * Obtiene la lista de campos de busqueda apartir del nombre de configuración
	 * 
	 * @return {@link List} < {@link ServicioCampoBusquedaDTO} >
	 * @throws {@link Exception}
	 */
	public final List<ServicioCampoBusquedaDTO> getServicioCampo() throws Exception {
		try {
			var configuracion = new ConfiguracionDTO();
			configuracion.setConfiguracion(nombreConfiguracion);
			return configMarcadorServicio.getServiciosCampoBusqueda(configuracion);
		} catch (MarcadorServicioException e) {
			throw new Exception(e);
		}
	}

	/**
	 * Se enarga de obtener la confguracion de los campos asociados de sevicios de
	 * busqueda
	 * 
	 * @return {@link Map} < {@link String} , {@link List} < {@link String} > >
	 * @throws {@link Exception}
	 */
	public final String[] getConfigAsociar() throws Exception {
		var lista = getServicioCampo();
		if (ListUtils.isNotBlank(lista)) {
			return lista.stream().map(servicio -> servicio.getMarcador()).distinct().toArray(String[]::new);
		}
		return null;
	}

	/**
	 * Se encarga de generrar los mapas seguns los valores de busqueda
	 * 
	 * @param busqueda
	 * @throws Exception
	 */
	public final <T extends Object> List<Object> gen(String nombreConfig, Map<String, Object> busqueda)
			throws Exception {
		var campoValorServ = new HashMap<String, Map<String, Object>>();
		var list = new ArrayList<Object>();
		var configuracion = new ConfiguracionDTO();
		configuracion.setConfiguracion(nombreConfig);
		var lstServFieldSearch = configMarcadorServicio.getServiciosCampoBusqueda(configuracion);
		for (ServicioCampoBusquedaDTO servFieldSearch : lstServFieldSearch) {
			var campoValor = campoValorServ.get(servFieldSearch.getServicio());
			if (campoValor == null) {
				campoValor = new HashMap<String, Object>();
			}
			campoValor.put(servFieldSearch.getCampo(), busqueda.get(servFieldSearch.getMarcador()));
			campoValorServ.put(servFieldSearch.getServicio(), campoValor);
		}
		var servicios = campoValorServ.keySet();
		for (String servicio : servicios) {
			T bookmark = configMarcadorServicio.generar(nombreConfig, servicio, campoValorServ.get(servicio));
			if (bookmark != null) {
				list.add(bookmark);
			}
		}
		return list;
	}

	/**
	 * Se encarga de configurar el nombre de la configuración
	 * 
	 * @param nombreConfiguracion {@link String}
	 */
	public final void setNombreConfiguracion(String nombreConfiguracion) {
		this.nombreConfiguracion = nombreConfiguracion;
	}

	public final String getNameFile() throws Exception {
		if (StringUtils.isBlank(nameFiler)) {
			ConfiguracionDTO dto = new ConfiguracionDTO();
			dto.setConfiguracion(nombreConfiguracion);
			List<ConfiguracionDTO> lista = configMarcadorServicio.getConfiguraciones(dto);
			if (ListUtils.isNotBlank(lista)) {
				nameFiler = lista.get(0).getArchivo();
			}
		}
		return nameFiler;
	}

}
