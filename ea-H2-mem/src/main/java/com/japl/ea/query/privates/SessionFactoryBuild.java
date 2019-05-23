package com.japl.ea.query.privates;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class SessionFactoryBuild {

	private String resourceConfiguration;
	private SessionFactory sessionFactory;
	private Configuration configuration;
	private Boolean reloadConfiguration = false;
	
	public SessionFactoryBuild() {
		resourceConfiguration = "hibernate.cfg.xml";
	}
	
	public SessionFactory buildSessionFactory() {
		if (sessionFactory == null) {
			loadConfiguration();
			sessionFactory = configuration.buildSessionFactory(getServiceRegistry(configuration));
		}
		return sessionFactory;
	}

	private ServiceRegistry getServiceRegistry(Configuration configuration) {
		return new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
	}

	private void loadConfiguration() {
		if (configuration == null || reloadConfiguration) {
			configuration = new Configuration();
			configuration.configure(resourceConfiguration);
		}
	}

}
