package com.japl.ea.query.privates;

import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.pyt.common.common.Log;

import com.japl.ea.query.constants.H2PropertiesConstant;
import com.japl.ea.query.privates.utils.PropertiesH2;

public class SessionFactoryBuild {

	private String resourceConfiguration;
	private SessionFactory sessionFactory;
	private Configuration configuration;
	private Boolean reloadConfiguration = false;
	private PropertiesH2 propertiesH2;
	private Log logger = Log.Log(this.getClass());

	public SessionFactoryBuild() {
		try {
			propertiesH2 = PropertiesH2.getInstance().load();
			resourceConfiguration = propertiesH2.getValue(H2PropertiesConstant.PROP_HIBERNATE_CONFIG);
		} catch (Exception e) {
			logger.logger(e);
		}
	}

	public SessionFactory buildSessionFactory() {
		if (sessionFactory == null) {
			loadConfiguration();
			if (configuration != null) {
				sessionFactory = configuration.buildSessionFactory(getServiceRegistry(configuration));
			}
		}
		return sessionFactory;
	}

	private ServiceRegistry getServiceRegistry(Configuration configuration) {
		if (configuration != null) {
			Properties properties = configuration.getProperties();
			properties.setProperty(H2PropertiesConstant.PROP_HIBERNATE_URL,
					propertiesH2.getValue(H2PropertiesConstant.PROP_HIBERNATE_URL));
			properties.setProperty(H2PropertiesConstant.PROP_HIBERNATE_USER_NAME,
					propertiesH2.getValue(H2PropertiesConstant.PROP_HIBERNATE_USER_NAME));
			properties.setProperty(H2PropertiesConstant.PROP_HIBERNATE_PASSWORD,
					propertiesH2.getValue(H2PropertiesConstant.PROP_HIBERNATE_PASSWORD));
			return new StandardServiceRegistryBuilder().applySettings(properties).build();
		}
		return null;
	}

	private void loadConfiguration() {
		if (resourceConfiguration != null && configuration == null || reloadConfiguration) {
			configuration = new Configuration();
			configuration.configure(resourceConfiguration);
		}
	}

}
