package org.pyt.common.properties;

import static org.pyt.common.constants.LanguageConstant.CONST_EXC_SERVICE;

import java.util.Properties;

import org.pyt.common.common.I18n;
import org.pyt.common.common.Log;
import org.pyt.common.constants.PropertiesConstants;

import co.com.japl.ea.constants.utils.EnviromentProperties;


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
	private I18n i18n = I18n.instance();

	protected Log log = Log.Log(ServiceSimple.class);

	private ServiceSimple() throws Exception {
		var propertiesUtil = PropertiesUtils.getInstance()
				.setNameProperties(PropertiesConstants.PROP_SIMPLE_SERVICE)
				.setPathProperties(EnviromentProperties.getPath());
		properties = propertiesUtil.load().getProperties();
	}

	public static ServiceSimple getInstance() throws Exception {
		if (ss == null) {
			ss = new ServiceSimple();
		}
		return ss;
	}

	@SuppressWarnings("unchecked")
	public <T extends Object> T getService(Class<T> service) {
		String className= "";
		try {
			if (service.isInterface()) {
				var key = service.getName();
				className = properties.getProperty(key);
				if(className != null) {
				var clazz = Class.forName(className);
				var instance = clazz.getConstructor().newInstance();
				return (T) instance;
				}
			}
		} catch (Exception e) {
			log.logger(i18n.valueBundle(CONST_EXC_SERVICE, service.getSimpleName(), className).get(), e);
		}
		return null;
	}
}
