package com.pyt.service.abstracts;

import org.pyt.common.annotations.PostConstructor;
import org.pyt.common.common.Log;
import org.pyt.common.exceptions.ReflectionException;
import org.pyt.common.reflection.Reflection;

/**
 * Se usa cuando los servicios son manejados localmente por lo cual se encarga
 * de generalizar funcionalidades de carga de los servicios locales
 * 
 * @author Alejandro Parra
 * @since 22-06-2018
 */
public abstract class Services extends Reflection {
	private Log logger = Log.Log(this.getClass());

	@PostConstructor
	public void load() {
		try {
			inject();
		} catch (ReflectionException e) {
			logger.logger(e);
		}
	}

}
