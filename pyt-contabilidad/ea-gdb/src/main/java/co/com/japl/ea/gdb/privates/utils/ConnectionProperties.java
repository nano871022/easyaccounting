package co.com.japl.ea.gdb.privates.utils;

import java.util.Properties;

import org.pyt.common.properties.PropertiesUtils;

import co.com.japl.ea.constants.utils.EnviromentProperties;
import co.com.japl.ea.gdb.privates.constants.ConnectionPropertiesConstant;

public final class ConnectionProperties {
	private Properties properties;
	private static ConnectionProperties propertiesJdbc;

	private ConnectionProperties() {
	}

	public final static ConnectionProperties getInstance() {
		if (propertiesJdbc == null) {
			propertiesJdbc = new ConnectionProperties();
		}
		return propertiesJdbc;
	}
	
	public ConnectionProperties load() throws Exception {
		var pu = PropertiesUtils.getInstance();
		var path = EnviromentProperties.getPath(); 
		pu.setNameProperties(ConnectionPropertiesConstant.PROP_NAME);
		pu.setPathProperties(path);
		properties = pu.load().getProperties();
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Object> T getValue(String propertyName) {
		return (T) properties.get(propertyName);
	}

}
