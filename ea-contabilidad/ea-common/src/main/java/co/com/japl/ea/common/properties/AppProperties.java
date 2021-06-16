package co.com.japl.ea.common.properties;

import java.util.Properties;

import org.pyt.common.common.CacheUtil;
import org.pyt.common.common.I18n;
import org.pyt.common.constants.PropertiesConstants;

import co.com.japl.ea.constants.utils.EnviromentProperties;

public final class AppProperties {
	private static AppProperties instance;
	private Properties properties;

	private AppProperties() {
	}

	public final static AppProperties instance() {
		if (instance == null) {
			instance = new AppProperties();
		}
		return instance;
	}

	public final AppProperties load() {
		try {
			if (properties == null || CacheUtil.INSTANCE().isRefresh("AppProperties")) {
				CacheUtil.INSTANCE().load("AppProperties");
				properties = PropertiesUtils.getInstance().setNameProperties(PropertiesConstants.PROP_DATA)
						.setPathProperties(getPath()).load().getProperties();
				if (properties.isEmpty()) {
					throw new Exception("ERROR");
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(i18n().get("error.appproperties.no.load"));
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
