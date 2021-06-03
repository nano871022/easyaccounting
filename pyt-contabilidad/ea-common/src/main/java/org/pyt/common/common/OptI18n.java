package org.pyt.common.common;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.function.Function;

public final class OptI18n {
	private String search;
	private String message;
	private boolean found;
	private Object[] formats;

	private OptI18n() {
		found = false;
	}

	/**
	 * Se encarga de procesar la funcion pasando el valor key y obtener el
	 * resultado, con esto se retorna la instancia optional de I18n
	 * 
	 * @param function {@Link Function}
	 * @param key      {@Link String}
	 */
	public final static OptI18n process(Function<String, String> function, String key) {
		OptI18n opt = new OptI18n();
		var result = function.apply(key);
		opt.search = key;
		if (result == null || result.contentEquals(key)) {
			opt.message = key;
			opt.found = false;
		} else {
			opt.message = result;
			opt.found = true;
		}
		return opt;
	}

	/**
	 * Se encarga de procesar la funcion pasando el valor key y obtener el
	 * resultado, con esto se retorna la instancia optional de I18n y se le puede
	 * aplicar valores para formatear el texto
	 * 
	 * @param function {@Link Function}
	 * @param key      {@Link String}
	 * @param formats  {@link Object} {@link Arrays}
	 */
	public final static OptI18n process(Function<String, String> function, String key, Object... formats) {
		OptI18n opt = new OptI18n();
		var result = function.apply(key);
		opt.formats = formats;
		opt.search = key;
		if (result == null) {
			opt.message = key;
			opt.found = false;
		} else {
			opt.message = new MessageFormat(result).format(formats);
			opt.found = true;
		}
		return opt;
	}

	/**
	 * Cuando es suministrado un nullo en el original pone en el encontrado como en
	 * busqueda el {@link search}
	 * 
	 * @param found  {@link String}
	 * @param search {@link String}
	 * @return {@link OptI18n}
	 */
	public final static OptI18n ifNullable(String found, String search) {
		OptI18n opt = new OptI18n();
		if (found == null) {
			opt.search = search;
			opt.message = search;
			opt.found = false;
		} else {
			opt.found = true;
			opt.search = search;
			opt.message = found;
		}
		return opt;
	}

	/**
	 * Cuando es suministrado un nullo en el original pone en el encontrado como en
	 * busqueda el {@link search} y se le puede aplicar valores para formatear el
	 * texto
	 * 
	 * @param found   {@link String}
	 * @param search  {@link String}
	 * @param formats {@link Object}{@link Arrays}
	 * @return {@link OptI18n}
	 */
	public final static OptI18n ifNullable(String found, String search, Object... formats) {
		OptI18n opt = new OptI18n();
		opt.formats = formats;
		if (found == null) {
			opt.search = search;
			opt.message = search;
			opt.found = false;
		} else {
			opt.found = true;
			opt.search = search;
			opt.message = new MessageFormat(found).format(formats);
		}
		return opt;
	}

	public final static OptI18n noChange(String key) {
		OptI18n opt = new OptI18n();
		opt.found = true;
		opt.search = key;
		opt.message = key;
		return opt;
	}

	public final static OptI18n noChange(String key, Object... formats) {
		OptI18n opt = new OptI18n();
		opt.found = true;
		opt.search = key;
		opt.message = new MessageFormat(key).format(formats);
		opt.formats = formats;
		return opt;
	}

	public Object[] getFormats() {
		return formats;
	}

	public String get() {
		return message;
	}

	public String getFound() {
		return search;
	}

	public boolean isFound() {
		return found;
	}

}
