package co.com.japl.ea.common.properties;

import java.util.Properties;

import org.pyt.common.common.CacheUtil;
import org.pyt.common.common.I18n;
import org.pyt.common.constants.PropertiesConstants;

import co.com.japl.ea.constants.utils.EnviromentProperties;

public final class LogProperties {
	private static LogProperties instance;
	private Properties properties;

	private LogProperties() {
	}

	public final static LogProperties instance() {
		if (instance == null) {
			instance = new LogProperties();
		}
		return instance;
	}

	public final LogProperties load() {
		try {
			if (properties == null || CacheUtil.INSTANCE().isRefresh("LogProperties")) {
				CacheUtil.INSTANCE().load("LogProperties");
				properties = PropertiesUtils.getInstance().setNameProperties(PropertiesConstants.PROP_LOG)
						.setPathProperties(getPath()).load().getProperties();
				if (properties.isEmpty()) {
					throw new Exception("ERROR");
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(i18n().get("error.logproperties.no.load"));
		}
		return instance;
	}

	public String get(String key) {
		return properties.getProperty(key);
	}

	private final String getPath() {
		return EnviromentProperties.getPath();
	}

	private I18n i18n() {
		return I18n.instance();
	}

}
