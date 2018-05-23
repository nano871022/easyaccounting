package org.pyt.common.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.pyt.common.common.ADto;
import org.pyt.common.constants.ReflectionConstants;
import org.pyt.common.exceptions.ReflectionException;

/**
 * Se encarga de realizar reflecciones para el dto
 * 
 * @author Alejandro Parra
 * @since 2018-05-18
 */
public abstract class ReflectionDto {
	/**
	 * Se encarga de poder realiar set sobre los valores de los campos del dto por
	 * medio de refleccion
	 * 
	 * @param nombreCampo
	 *            {@link String}
	 * @param value
	 *            {@link Object}
	 * @throws {@link
	 *             ReflectionException}
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	public final <S extends ADto, T extends Object> void set(String nombreCampo, T value) throws ReflectionException {
		Class<S> clase = (Class<S>) super.getClass();
		String nameMethod;
		Field field = null;
		try {
			field = clase.getDeclaredField(nombreCampo);
			nameMethod = ReflectionConstants.SET + field.getName().substring(0, 1).toUpperCase()
					+ field.getName().substring(1);

			Method method = clase.getMethod(nameMethod, value.getClass());
			method.invoke(this, value);
		} catch (NoSuchFieldException e) {
			throw new ReflectionException("El campo " + nombreCampo + " no fue encotrado", e);
		} catch (SecurityException e) {
			throw new ReflectionException("Problema de seguridad.", e);
		} catch (NoSuchMethodException e) {
			if (field != null) {
				field.setAccessible(true);
				try {
					field.set(this, value);
				} catch (IllegalArgumentException e1) {
					throw new ReflectionException("Problema argumento ilegal.", e);
				} catch (IllegalAccessException e1) {
					throw new ReflectionException("Problema de acceso ilegal.", e);
				}
			} else {
				throw new ReflectionException("El metodo no fue encontrado.", e);
			}
		} catch (IllegalAccessException e) {
			throw new ReflectionException("Problema de acceso ilegal.", e);
		} catch (IllegalArgumentException e) {
			throw new ReflectionException("Problema argumento ilegal.", e);
		} catch (InvocationTargetException e) {
			throw new ReflectionException("Problema objeto invocacion.", e);
		}
	}

	/**
	 * Se encarga de obtener de obtner un valor apartir de un nombre del campo del
	 * medio de refleccion
	 * 
	 * @param nombreCampo
	 *            {@link String}
	 * @param value
	 *            {@link Object}
	 * @return {@link Object}
	 * @throws {@link
	 *             ReflectionException}
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	public final <T, S extends Object> T get(String nombreCampo, S value) throws ReflectionException {
		Class<S> clase = (Class<S>) super.getClass();
		String nameMethod;
		Field field = null;
		try {
			field = clase.getDeclaredField(nombreCampo);
			nameMethod = ReflectionConstants.GET + field.getName().substring(0, 1).toUpperCase()
					+ field.getName().substring(1);

			Method method = clase.getMethod(nameMethod, value != null ? value.getClass() : null);
			return (T) method.invoke(this, value);
		} catch (NoSuchFieldException e) {
			throw new ReflectionException("El campo " + nombreCampo + " no fue encotrado", e);
		} catch (SecurityException e) {
			throw new ReflectionException("Problema de seguridad.", e);
		} catch (NoSuchMethodException e) {
			if (field != null) {
				field.setAccessible(true);
				try {
					return (T) field.get(this);
				} catch (IllegalArgumentException e1) {
					throw new ReflectionException("Problema argumento ilegal.", e);
				} catch (IllegalAccessException e1) {
					throw new ReflectionException("Problema de acceso ilegal.", e);
				}
			} else {
				throw new ReflectionException("El metodo no fue encontrado.", e);
			}
		} catch (IllegalAccessException e) {
			throw new ReflectionException("Problema de acceso ilegal.", e);
		} catch (IllegalArgumentException e) {
			throw new ReflectionException("Problema argumento ilegal.", e);
		} catch (InvocationTargetException e) {
			throw new ReflectionException("Problema objeto invocacion.", e);
		}
	}

	/**
	 * Se encarga de obtener de obtner un valor apartir de un nombre del campo del
	 * medio de refleccion
	 * 
	 * @param nombreCampo
	 *            {@link String}
	 * @return {@link Object}
	 * @throws {@link
	 *             ReflectionException}
	 */
	public final <T extends Object> T get(String nombreCampo) throws ReflectionException {
		return get(nombreCampo, null);
	}
	
}
