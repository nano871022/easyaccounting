package org.pyt.app.beans.config;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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
	public final String[] getConfigAsociar() throws Exception {
		Set<String> campos = new HashSet<String>();
		List<ServicioCampoBusquedaDTO> lista = getServicioCampo();
		for (ServicioCampoBusquedaDTO dto : lista) {
			campos.add(dto.getMarcador());
		}
		return campos.toArray(new String[campos.size()]);
	}

	/**
	 * Se encarga de generrar los mapas seguns los valores de busqueda
	 * 
	 * @param busqueda
	 * @throws Exception
	 */
	public final <T extends Object> T gen(String nombreConfig, Map<String, Object> busqueda) throws Exception {
		Set<String> sets = busqueda.keySet();
		List<ServicioCampoBusquedaDTO> lstServFieldSearch = configMarcadorServicio
				.getServiciosCampoBusqueda(nombreConfig);
		for (ServicioCampoBusquedaDTO servFieldSearch : lstServFieldSearch) {
			Optional<String> opt = sets.stream().filter(p -> p.equalsIgnoreCase(servFieldSearch.getMarcador()))
					.findFirst();
			if (opt.isPresent()) {
				servFieldSearch.getServicio();
				servFieldSearch.getCampo();
			}
		}
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
