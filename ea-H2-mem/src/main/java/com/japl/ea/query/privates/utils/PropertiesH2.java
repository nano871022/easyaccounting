package com.japl.ea.query.privates.utils;

import java.util.Properties;

import org.pyt.common.constants.EnviromentConstants;
import org.pyt.common.properties.PropertiesUtils;

import com.japl.ea.query.constants.H2PropertiesConstant;

public final class PropertiesH2 {
	private Properties properties;
	private static PropertiesH2 propertiesH2;

	private PropertiesH2() {
	}

	public final static PropertiesH2 getInstance() {
		if (propertiesH2 == null) {
			propertiesH2 = new PropertiesH2();
		}
		return propertiesH2;
	}
	
	public PropertiesH2 load() throws Exception {
		var pu = PropertiesUtils.getInstance();
		var path = System.getenv(EnviromentConstants.PROP_ENV_PATH_H2);
		if(path == null) {
			path = H2PropertiesConstant.PROP_PATH;
		}
		pu.setNameProperties(H2PropertiesConstant.PROP_NAME);
		pu.setPathProperties(path);
		properties = pu.load().getProperties();
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Object> T getValue(String propertyName) {
		return (T) properties.get(propertyName);
	}

}
