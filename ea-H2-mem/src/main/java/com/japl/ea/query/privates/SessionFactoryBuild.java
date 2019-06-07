package com.japl.ea.query.privates;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
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
				var configure = configuration.addPackage(H2PropertiesConstant.CONST_PACKAGE_JPA);
				loadJPA(configure);
				sessionFactory = configure.buildSessionFactory(getServiceRegistry(configuration));
			}
		}
		return sessionFactory;
	}

	private final void loadJPA(Configuration configure) {
		var in = getClass().getProtectionDomain().getCodeSource().getLocation().getPath()
				+ H2PropertiesConstant.CONST_PACKAGE_JPA.replace(H2PropertiesConstant.CONST_DOT,
						H2PropertiesConstant.CONST_SLASH)
				+ H2PropertiesConstant.CONST_SLASH;
		var file = new File((String) in);
		if (file.exists() && file.isDirectory()) {
			var jpas = new ArrayList<String>();
			Arrays.stream(file.listFiles()).forEach(
					f -> jpas.add(H2PropertiesConstant.CONST_PACKAGE_JPA + H2PropertiesConstant.CONST_DOT + f.getName()
							.replace(H2PropertiesConstant.CONST_DOT_CLASS, H2PropertiesConstant.CONST_EMPTY_STRING)));
			jpas.forEach(clazz -> {
				try {
					var classO = Class.forName(clazz);
					configure.addAnnotatedClass(classO);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			});
		}

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
