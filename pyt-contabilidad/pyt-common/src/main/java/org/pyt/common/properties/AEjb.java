package org.pyt.common.properties;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.pyt.common.common.Log;

public abstract class AEjb {
	private Properties properties;
	private Context context;

	private Log log = Log.Log(EjbRemote.class);

	public AEjb(String prop)throws Exception {
		context = new InitialContext();
		PropertiesUtils.getInstance().setNameProperties(prop).load()
		.getProperties();
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Object> T getEjb(Class<T> service) {
		try {
			if (service.isInterface()) {
				var key = service.getName();
				var jndi = properties.getProperty(key);
				var ejb = context.lookup(jndi);
				return (T) ejb;
			}
		} catch (Exception e) {
			log.logger("Error al obtener el ejb.", e);
		}
		return null;
	}

	public void setContext(Context context) {
		this.context = context;
	}
}
