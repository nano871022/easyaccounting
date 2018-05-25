package org.pyt.common.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.pyt.common.annotations.FXMLFile;
import org.pyt.common.annotations.Inject;
import org.pyt.common.annotations.Singleton;
import org.pyt.common.annotations.SubcribcionesToComunicacion;
import org.pyt.common.annotations.SubcribirToComunicacion;
import org.pyt.common.common.Comunicacion;
import org.pyt.common.common.Log;
import org.pyt.common.constants.AppConstants;
import org.pyt.common.constants.ReflectionConstants;
import org.pyt.common.exceptions.ReflectionException;
import org.pyt.common.interfaces.IComunicacion;

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
	protected <L extends Comunicacion, N, T, S extends Object, M extends IComunicacion> void inject()
			throws ReflectionException {
		try {
			Class<S> clase = (Class<S>) this.getClass();
			Field[] fields = clase.getDeclaredFields();
			for (Field field : fields) {
				Inject inject = field.getAnnotation(Inject.class);
				if (inject != null && inject.resource() != null && inject.resource().length() > 0) {
					T obj = (T) Class.forName(inject.resource()).getConstructor().newInstance();
					put(this, field, obj);
				} else if (inject != null && (inject.resource() == null || inject.resource().length() == 0)) {
					Class<T> classe = (Class<T>) field.getType();
					Singleton singleton = classe.getAnnotation(Singleton.class);
					if (singleton != null) {
						Method method = classe.getDeclaredMethod(AppConstants.ANNOT_SINGLETON);
						T obj = (T) method.invoke(classe);
						put(this, field, obj);
					} else {
						T obj = (T) field.getType().getConstructor().newInstance();
						put(this, field, obj);
					}
				}
			}
			// procesado despues de injecciones
			for (Field field : fields) {
				SubcribcionesToComunicacion subs = field.getAnnotation(SubcribcionesToComunicacion.class);
				if (subs != null) {
					L obj = get(this, field);
					if (obj != null && (this) instanceof IComunicacion) {
						for (SubcribirToComunicacion sub : subs.value()) {
							obj.subscriber((M) this, sub.comando());
						}
					} else {
						Log.error("El objeto " + clase.getName() + " no tiene la implementacion de "
								+ IComunicacion.class.getName());
					}

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
	 * Se encarga de obtener el objeto que se encuentra en el campo indicado
	 * 
	 * @param obj
	 *            {@link Object} instancia actual
	 * @param field
	 *            {@link Field}
	 * @return {@link Object} almacenado en field
	 * @throws {@link
	 *             ReflectionException}
	 */
	@SuppressWarnings("unchecked")
	protected final <T extends Reflection, S extends Object> S get(T obj, Field field) throws ReflectionException {
		try {
			Class<T> clase = (Class<T>) obj.getClass();
			String get = ReflectionConstants.GET + field.getName().substring(0, 1) + field.getName().substring(1);
			Method method;
			method = clase.getMethod(get);
			return (S) method.invoke(obj);
		} catch (NoSuchMethodException e) {
			field.setAccessible(true);
			try {
				return (S) field.get(obj);
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
				field.set(obj, valor);
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
