package org.pyt.common.properties;

import java.util.Properties;

import org.pyt.common.common.Log;
import org.pyt.common.constants.PropertiesConstants;

/**
 * Usado para obtener instancias simples para interfaces cuando no se usa ejb,
 * para liberar la especificacion sobre la anotacion y poderlo modificar mas
 * simplemente
 * 
 * @author Alejandro Parra
 * @since 14/05/2019
 */
public final class ServiceSimple {
	private Properties properties;
	private static ServiceSimple ss;

	protected Log log = Log.Log(ServiceSimple.class);

	private ServiceSimple() throws Exception {
		PropertiesUtils.getInstance().setNameProperties(PropertiesConstants.PROP_SIMPLE_SERVICE).load().getProperties();
	}

	public static ServiceSimple getInstance() throws Exception {
		if (ss == null) {
			ss = new ServiceSimple();
		}
		return ss;
	}

	@SuppressWarnings("unchecked")
	public <T extends Object> T getService(Class<T> service) {
		try {
			if (service.isInterface()) {
				var key = service.getName();
				var className = properties.getProperty(key);
				var clazz = Class.forName(className);
				var instance = clazz.getConstructor().newInstance();
				return (T) instance;
			}
		} catch (Exception e) {
			log.logger("Error al obtener el servicio.", e);
		}
		return null;
	}
}
