package org.pyt.common.common;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.constants.AppConstants;
import org.pyt.common.constants.LanguageConstant;

import co.com.japl.ea.common.abstracts.ADto;
import co.com.japl.ea.exceptions.ReflectionException;

/**
 * Archvios que se encarga de cargar la internacionalizacion de lenguage y
 * usarla donde se desea
 * 
 * @author Alejandro Parra
 * @since 15/04/2019
 */
public final class I18n {
	private final Log logger = Log.Log(this.getClass());
	private ResourceBundle bundle;
	private String bundleNoDefault;
	private Map<String, Object> languagesDB;
	private static I18n instances;

	private I18n() {

	}

	public final static synchronized I18n instance() {
		if (CacheUtil.INSTANCE().isRefresh("International(I18n)")) {
			instances = new I18n();
			CacheUtil.INSTANCE().load("International(I18n)");
		}
		return instances;
	}

	/**
	 * Se encarga de retornar la lista de lenguages que se encuentran en el
	 * resources bundle de la aplicacion
	 * 
	 * @return {@link ResourceBundle}
	 */
	private final ResourceBundle getLanguages() {
		if (bundle == null) {
			bundle = ResourceBundle.getBundle(AppConstants.RESOURCE_BUNDLE, getLocaleUsed());
		}
		return bundle;
	}

	/**
	 * Se encarga de obtener la conguguracion de locale
	 * 
	 * @return {@link Locale}
	 */
	private final Locale getLocaleUsed() {
		if (StringUtils.isNotBlank(bundleNoDefault)) {
			return new Locale(bundleNoDefault);
		}
		return Locale.getDefault();
	}

	/**
	 * Permite obtener valores de i18n de forma rápida
	 * 
	 * @param key {@link String}
	 * @return {@link String}
	 */
	public final String get(String key) {
		return valueBundle(key).get();
	}

	/**
	 * Retorna el valor que se encuentra en la llave suministrada de la
	 * internacionalizacion
	 * 
	 * @param key {@link String}
	 * @return {@link String}
	 */
	public final OptI18n valueBundle(String key) {
		return OptI18n.process(value -> {
		try {
			if (languagesDB != null && languagesDB.containsKey(key)) {
					return ((ADto) languagesDB.get(value)).get(LanguageConstant.CONST_TEXT_BUNDLE_LANGUAGES);
			} else {
					return getLanguages().getString(value);
			}
		} catch (Exception exception) {
			try {
				logger.logger(String.format(getLanguages().getString(LanguageConstant.LANGUAGE_KEY_NOT_FOUND), key));
			} catch (MissingResourceException e) {
				logger.logger(exception);
				logger.logger(e);
			}
				return value;
		}
		}, key);
	}
	
	public final OptI18n valueBundle(String key, Object... formats) {
		var pattern = valueBundle(key);
		return OptI18n.ifNullable(pattern.get(), key, formats);
	}

	public final void setBunde(String bundle) {
		this.bundleNoDefault = bundle;
	}

	/**
	 * recargar el bundle para volver a configurar la locación
	 */
	public final void reloadBundle() {
		bundle = null;
		getLanguages();
	}

	public final <T extends ADto> void setLanguagesDB(List<T> listLanguages) {
		if (listLanguages != null && listLanguages.size() > 0) {
			if (languagesDB == null) {
				languagesDB = new HashMap<String, Object>();
			}
			listLanguages.forEach(languageDto -> {
				try {
					String key = languageDto.get(LanguageConstant.CONST_CODE_BUNDLE_LANGUAGES);
					if (!languagesDB.containsKey(key)) {
						languagesDB.put(key, languageDto);
					}
				} catch (ReflectionException e) {
					logger.logger(LanguageConstant.CONST_ERR_MSG_ADDED_LANGUAGES, e);
				}
			});
		}
	}

	public final Boolean isEmptyDBLanguages() {
		return languagesDB == null || languagesDB.size() == 0;
	}
}