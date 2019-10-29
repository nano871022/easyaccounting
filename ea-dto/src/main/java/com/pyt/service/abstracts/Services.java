package com.pyt.service.abstracts;

import org.pyt.common.annotations.Inject;
import org.pyt.common.annotations.PostConstructor;
import org.pyt.common.common.I18n;
import org.pyt.common.common.Log;
import org.pyt.common.exceptions.ReflectionException;
import org.pyt.common.reflection.Reflection;

import com.pyt.service.interfaces.IGenericServiceSvc;

import co.com.japl.ea.dto.system.LanguagesDTO;


/**
 * Se usa cuando los servicios son manejados localmente por lo cual se encarga
 * de generalizar funcionalidades de carga de los servicios locales
 * 
 * @author Alejandro Parra
 * @since 22-06-2018
 */
public abstract class Services implements Reflection {
	private Log logger = Log.Log(this.getClass());
	@Inject(resource = "com.pyt.service.implement.GenericServiceSvc")
	private IGenericServiceSvc<LanguagesDTO> languagesSvc;

	@PostConstructor
	public void load() {
		try {
			inject();
		} catch (ReflectionException e) {
			logger.logger(e);
		}
	}

	private void loadLanguagesDB(I18n languages) {
		try {
			var dto = new LanguagesDTO();
			var list = languagesSvc.getAll(dto);
			languages.setLanguagesDB(list);
		} catch (Exception e) {
			logger.logger(e);
		}
	}

	public final Log logger() {
		return logger;
	}

	protected final I18n i18n() {
		var i18n = I18n.instance();
		if (i18n.isEmptyDBLanguages()) {
			loadLanguagesDB(i18n);
		}
		return i18n;
	}
}
