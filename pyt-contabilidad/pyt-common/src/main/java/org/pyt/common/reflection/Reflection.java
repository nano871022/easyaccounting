package org.pyt.common.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.pyt.common.annotations.FXMLFile;
import org.pyt.common.annotations.Inject;
import org.pyt.common.constants.ReflectionConstants;
import org.pyt.common.exceptions.ReflectionException;

/**
 * Se encargad e realizar el codigo de refleccion para tener informacion o
 * codigo necesariodiferente
 * 
 * @author Alejandro Parra
 * @since 2018-05-19
 *
 */
public abstract class Reflection {
	/**
	 * Se encarga de verificar la cclase y objeter la anotacion inject, con la ccual
	 * por medio del recurso puesto dentro de la anotacion se obtiene una instancia
	 * y se pone sobre el objeto
	 * 
	 * @return {@link Object}
	 */
	@SuppressWarnings("unchecked")
	protected <T, S extends Object> void inject() throws ReflectionException {
		try {
			Class<S> clase = (Class<S>) this.getClass();
			Field[] fields = clase.getDeclaredFields();
			for (Field field : fields) {
				Inject inject = field.getAnnotation(Inject.class);
				if (inject != null && inject.resource() != null) {
					T obj = (T) Class.forName(inject.resource()).getConstructor().newInstance();
					put(clase, field, obj);
				}
			}
		} catch (InstantiationException e) {
			throw new ReflectionException(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			throw new ReflectionException(e.getMessage(), e);
		} catch (ClassNotFoundException e) {
			throw new ReflectionException(e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			throw new ReflectionException(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			throw new ReflectionException(e.getMessage(), e);
		} catch (NoSuchMethodException e) {
			throw new ReflectionException(e.getMessage(), e);
		} catch (SecurityException e) {
			throw new ReflectionException(e.getMessage(), e);
		}
	}

	/**
	 * Se encarga de poner un valor dentro del campo indicando en el objeto
	 * 
	 * @param clase
	 *            {@link Object}
	 * @param obj
	 *            {@link Field}
	 * @param valor
	 *            {@link Object}
	 */

	@SuppressWarnings({ "unchecked" })
	protected <T, S, A extends Object> void put(T obj, Field field, S valor) throws ReflectionException {
		Class<T> clase = (Class<T>) obj.getClass();
		Method metodo;
		String get;
		try {
			get = String.format("%s%s%s", ReflectionConstants.SET, field.getName().substring(0, 1).toUpperCase(),
					field.getName().substring(1));
			metodo = clase.getMethod(get, valor.getClass());
			metodo.invoke(obj, valor);
		} catch (NoSuchMethodException e) {
			field.setAccessible(true);
			try {
				field.set(this, valor);
			} catch (IllegalArgumentException e1) {
				throw new ReflectionException(e.getMessage(), e);
			} catch (IllegalAccessException e1) {
				throw new ReflectionException(e.getMessage(), e);
			}
		} catch (SecurityException e) {
			throw new ReflectionException(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			throw new ReflectionException(e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			throw new ReflectionException(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			throw new ReflectionException(e.getMessage(), e);
		}

	}

	/**
	 * Retorna la ruta con el nombre del archivo fxml
	 * 
	 * @return {@link String}
	 */
	@SuppressWarnings("unchecked")
	public final <T extends Object> String pathFileFxml() {
		Class<T> clase = (Class<T>) this.getClass();
		FXMLFile fxmlFile = clase.getDeclaredAnnotation(FXMLFile.class);
		return String.format("%s/%s", fxmlFile.path(), fxmlFile.file());
	}
}
