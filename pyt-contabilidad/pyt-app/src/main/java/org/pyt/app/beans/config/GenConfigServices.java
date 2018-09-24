package org.pyt.app.beans.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pyt.common.annotations.Inject;
import org.pyt.common.exceptions.MarcadorServicioException;

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

	public GenConfigServices(IConfigMarcadorServicio config) {
		if (configMarcadorServicio == null) {
			configMarcadorServicio = config;
		}
	}

	/**
	 * Obtiene la lista de campos de busqueda apartir del nombre de configuración
	 * 
	 * @return {@link List} < {@link ServicioCampoBusquedaDTO} >
	 * @throws {@link
	 *             Exception}
	 */
	private final List<ServicioCampoBusquedaDTO> getServicioCampo() throws Exception {
		try {
			return configMarcadorServicio.getServiciosCampoBusqueda(nombreConfiguracion);
		} catch (MarcadorServicioException e) {
			throw new Exception(e);
		}
	}

	/**
	 * Se enarga de obtener la confguracion de los campos asociados de sevicios de
	 * busqueda
	 * 
	 * @return {@link Map} < {@link String} , {@link List} < {@link String} > >
	 * @throws {@link
	 *             Exception}
	 */
	public final Map<String, List<String>> getConfigAsociar() throws Exception {
		Map<String, List<String>> mapaServicioCampo = new HashMap<String, List<String>>();
		List<ServicioCampoBusquedaDTO> lista = getServicioCampo();
		for (ServicioCampoBusquedaDTO dto : lista) {
			List<String> campos = mapaServicioCampo.get(dto.getServicio());
			if (campos == null) {
				campos = new ArrayList<String>();
			}
			campos.add(dto.getCampo());
			mapaServicioCampo.put(dto.getServicio(), campos);
		}
		return mapaServicioCampo;
	}

	/**
	 * Se encarga de generrar los mapas seguns los valores de busqueda
	 * 
	 * @param busqueda
	 * @throws Exception
	 */
	public final <T extends Object> T gen(String nombreConfig, String servicio, Map<String, Object> busqueda)
			throws Exception {
		return configMarcadorServicio.generar(nombreConfig, servicio, busqueda);
	}

	/**
	 * Se encarga de configurar el nombre de la configuración
	 * 
	 * @param nombreConfiguracion
	 *            {@link String}
	 */
	public final void setNombreConfiguracion(String nombreConfiguracion) {
		this.nombreConfiguracion = nombreConfiguracion;
	}

}
