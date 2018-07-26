package org.pyt.common.reflection;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.pyt.common.common.ADto;
import org.pyt.common.common.Log;
import org.pyt.common.common.ValidateValues;
import org.pyt.common.exceptions.ReflectionException;

/**
 * Se encarga de realizar acciones con relection sobre objetos
 * 
 * @author Alejandro Parra
 * @since 26/07/2018
 */
public final class ReflectionUtils {
	private ValidateValues valid;
	private static ReflectionUtils instancia;
	private ReflectionUtils() {
		valid = new ValidateValues();
	}
	/**
	 * Instancia de la clase 
	 * @return {@link ReflectionUtils}
	 */
	public final static ReflectionUtils instanciar() {
		if(instancia == null) {
			instancia = new ReflectionUtils();
		}
		return instancia;
	}
	/**
	 * Se encarga de copiar un onjeto en otro, esta copia se debe realizar el que el
	 * objeto de destino debe ser ina insancia del origen
	 * 
	 * @param origen {@link Object} extends {@link ADto}
	 * @param destino {@link Class} extends {@link ADto}
	 * @return {@link Object} extends {@link ADto}
	 */
	public final <T extends ADto, S extends ADto> S copy(T origen, Class<S> destino) {
		try {
			List<String> campos = origen.getNameFields();
			S out = destino.getConstructor().newInstance();
			if (valid.isCast(origen, destino)) {
				for (String campo: campos) {
					out.set(campo, origen.get(campo));
				}
			}
			return out;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException | ReflectionException e) {
			Log.logger(e);
		}
		return null;
	}
}
