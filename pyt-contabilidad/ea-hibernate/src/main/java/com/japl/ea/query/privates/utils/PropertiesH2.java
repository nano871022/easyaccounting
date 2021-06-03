package com.japl.ea.query.privates.utils;

import java.util.Properties;

import com.japl.ea.query.constants.H2PropertiesConstant;

import co.com.japl.ea.common.properties.PropertiesUtils;
import co.com.japl.ea.constants.utils.EnviromentProperties;


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
		var path = EnviromentProperties.getPath();
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
