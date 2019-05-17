package com.pyt.service.abstracts;

import org.pyt.common.annotations.PostConstructor;
import org.pyt.common.common.I18n;
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
public abstract class Services implements Reflection {
	private Log logger = Log.Log(this.getClass());
	private I18n i18n;
	@PostConstructor
	public void load() {
		try {
			inject();
			i18n = new I18n();
		} catch (ReflectionException e) {
			logger.logger(e);
		}
	}
 
	public final Log logger() {
		return logger;
	}
	protected final I18n i18n() {
		return i18n;
	}
}
