package org.pyt.common.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ServiceLoader;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.Inject;
import org.pyt.common.annotations.PostConstructor;
import org.pyt.common.annotations.Singleton;
import org.pyt.common.annotations.SubcribcionesToComunicacion;
import org.pyt.common.annotations.SubcribirToComunicacion;
import org.pyt.common.common.Comunicacion;
import org.pyt.common.common.Log;
import org.pyt.common.constants.AppConstants;
import org.pyt.common.constants.ReflectionConstants;
import org.pyt.common.exceptions.ReflectionException;
import org.pyt.common.interfaces.IComunicacion;
import org.pyt.common.properties.CacheInjects;
import org.pyt.common.properties.EjbHome;
import org.pyt.common.properties.EjbRemote;
import org.pyt.common.properties.ServiceSimple;

import co.com.arquitectura.annotation.proccessor.FXMLFile;

/**
 * Se encargad e realizar el codigo de refleccion para tener informacion o
 * codigo necesariodiferente
 * 
 * @author Alejandro Parra
 * @since 2018-05-19
 *
 */
public interface Reflection {
	
	public Log logger();
	
	/**
	 * Se encarga de verificar la cclase y objeter la anotacion inject, con la ccual
	 * por medio del recurso puesto dentro de la anotacion se obtiene una instancia
	 * y se pone sobre el objeto
	 * 
	 * @return {@link Object}
	 */
	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	default <L extends Comunicacion, N, T, S extends Object, M extends IComunicacion> void inject()
			throws ReflectionException {
		try {
			Class<S> clase = (Class<S>) this.getClass();
			Field[] fields = null;
			fields = getFields(clase);
			if (fields != null && fields.length > 0) {
				Inject injects = clase.getDeclaredAnnotation(Inject.class);
				inject((S) this, fields, clase);
				subscriberInject(this, fields, clase);
			} else {
				logger().warn("No se encontraron campos en la clase.");
			}
		} catch (IllegalArgumentException | SecurityException e) {
			throw new ReflectionException(e.getMessage(), e);
		} catch (NullPointerException e) {
			logger().logger(e);
		} catch (Exception e) {
			throw new ReflectionException(e.getMessage(), e);
		}
	}

	/**
	 * Obtiene los campos obtenidos de las clases
	 * 
	 * @param clazz {@link Class}
	 * @return {@link Field}
	 * @throws {@link Exception}
	 */
	private <T extends Object> Field[] getFields(Class<T> clazz) throws Exception {
		Field[] fields = null;
		try {
			fields = clazz.getDeclaredFields();
		} catch (Exception e) {
			fields = clazz.getFields();
		}
		return fields;
	}

	@SuppressWarnings({ "unchecked" })
	private <T, S extends Object> void inject(S object, Field[] fields, Class<S> clase) throws ReflectionException {
		if (fields != null && fields.length > 0 && clase != Object.class && clase != Reflection.class) {
			try {
				for (Field field : fields) {
					Inject inject = field.getAnnotation(Inject.class);
					if (inject == null)
						continue;
					T obj = null;
					obj = CacheInjects.instance().getInjectCache(field);
					if (obj == null) {
						obj = locatorServices(field);
						if (obj == null && StringUtils.isNotBlank(inject.resource())) {
							var resource = inject.resource();
							var clazz = Class.forName(resource);
							obj = (T) clazz.getConstructor().newInstance();
						} else if (obj == null) {
							obj = getSingletonAnnotated(inject.resource(), field);
						}
					}
					if (obj != null) {
						CacheInjects.instance().addInjectToCache(obj, field);
						if (!CacheInjects.instance().getConstructorAnnotatedCache(obj)) {
							postConstructor(obj, obj.getClass());
						}
						put(object, field, obj);
					}
				} // end for
				inject(object, clase.getSuperclass().getDeclaredFields(), (Class<S>) clase.getSuperclass());
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				throw new ReflectionException(e.getMessage(), e);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private <T> T locatorServices(Field field) {
		var service = ServiceLoader.load(field.getType());
		if (service != null && service.findFirst() != null && service.findFirst().isPresent()) {
			return (T) service.findFirst().get();
		} else {
			try {
				return (T) EjbRemote.getInstance().getEjb(field.getType());
			} catch (Exception e) {
				try {
					return (T) EjbHome.getInstance().getEjb(field.getType());
				} catch (Exception e1) {
					try {
						return (T) ServiceSimple.getInstance().getService(field.getType());
					} catch (Exception e2) {
					}
				}
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private <T> T getSingletonAnnotated(String resource, Field field) throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
		if (StringUtils.isBlank(resource)) {
			Class<T> classe = (Class<T>) field.getType();
			Singleton singleton = classe.getAnnotation(Singleton.class);
			if (singleton != null) {
				var method = classe.getDeclaredMethod(AppConstants.ANNOT_SINGLETON);
				return (T) method.invoke(classe);
			} else {
				return (T) field.getType().getConstructor().newInstance();
			}
		}
		return null;
	}

	/**
	 * Se encarga de buscar dentro de la instancia un metodo que este anotado con
	 * poscontructor para cargar algo
	 * 
	 * @param instance {@link Object} extends
	 * @param clase    {@link Class}
	 */
	private <S, M extends Object> void postConstructor(M instance, Class<S> clase) throws ReflectionException {
		try {
			if (clase == null || clase == Object.class || clase == Reflection.class)
				return;
			var metodos = clase.getDeclaredMethods();
			if (metodos != null && metodos.length > 0) {
				for (Method metodo : metodos) {
					if (metodo.getAnnotation(PostConstructor.class) != null) {
						CacheInjects.instance().addConstructorAnnotatedToCache(instance, metodo);
						metodo.invoke(instance);
					}
				}
				postConstructor(instance, clase.getSuperclass());
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new ReflectionException("Problema en la ejecucion del metodo anotado con pos constructor", e);
		}
	}

	/**
	 * Se encarga de procesar la clase en busqueda de anotacion de subscripcion a
	 * sistema de mensajeria
	 * 
	 * @param fields Fields[]
	 * @param clase  Class
	 * @throws ReflectionException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	default <S, M extends Object, L extends Comunicacion, N extends IComunicacion> void subscriberInject(M instance,
			Field[] fields, Class<S> clase) throws ReflectionException {
		if (fields == null || fields.length == 0)
			return;
		// procesado despues de injecciones
		for (Field field : fields) {
			SubcribcionesToComunicacion subs = field.getAnnotation(SubcribcionesToComunicacion.class);
			SubcribirToComunicacion subsc = field.getAnnotation(SubcribirToComunicacion.class);
			if (subs != null) {
				L obj;
				obj = get(this, field);
				if (obj != null && (this) instanceof IComunicacion) {
					for (SubcribirToComunicacion sub : subs.value()) {
						obj.subscriber((N) instance, sub.comando());
					}
				} else {
					logger().error("El objeto " + clase.getName() + " no tiene la implementacion de "
							+ IComunicacion.class.getName());
				}
			} else if (subsc != null) {
				L obj = get(this, field);
				if (obj != null && (this) instanceof IComunicacion) {
					obj.subscriber((N) instance, subsc.comando());
				} else {
					logger().error("El objeto " + clase.getName() + " no tiene la implementacion de "
							+ IComunicacion.class.getName());
				}
			}
		}
		subscriberInject(instance, clase.getSuperclass().getDeclaredFields(), clase.getSuperclass());
	}

	/**
	 * Se encarga de obtener el objeto que se encuentra en el campo indicado
	 * 
	 * @param obj   {@link Object} instancia actual
	 * @param field {@link Field}
	 * @return {@link Object} almacenado en field
	 * @throws {@link ReflectionException}
	 */
	@SuppressWarnings("unchecked")
	default <T extends Reflection, S extends Object> S get(T obj, Field field) throws ReflectionException {
		try {
			Class<T> clase = (Class<T>) obj.getClass();
			var methodName = ReflectionUtils.instanciar().getNameMethod(ReflectionConstants.GET, field.getName());
			var method = clase.getMethod(methodName);
			return (S) method.invoke(obj);
		} catch (NoSuchMethodException e) {
			field.setAccessible(true);
			try {
				return (S) field.get(obj);
			} catch (IllegalArgumentException | IllegalAccessException e1) {
				throw new ReflectionException(e.getMessage(), e);
			}
		} catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new ReflectionException(e.getMessage(), e);
		}
	}

	/**
	 * Se encarga de poner un valor dentro del campo indicando en el objeto
	 * 
	 * @param clase {@link Object}
	 * @param obj   {@link Field}
	 * @param valor {@link Object}
	 */

	@SuppressWarnings({ "unchecked" })
	default <T, S, A extends Object> void put(T obj, Field field, S valor) throws ReflectionException {
		try {
			var clase = (Class<T>) obj.getClass();
			var methodName = ReflectionUtils.instanciar().getNameMethod(ReflectionConstants.SET, field.getName());
			var metodo = clase.getMethod(methodName, valor.getClass());
			metodo.invoke(obj, valor);
		} catch (NoSuchMethodException e) {
			field.setAccessible(true);
			try {
				field.set(obj, valor);
			} catch (IllegalArgumentException | IllegalAccessException e1) {
				throw new ReflectionException(e.getMessage(), e);
			}
		} catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new ReflectionException(e.getMessage(), e);
		}

	}

	/**
	 * Retorna la ruta con el nombre del archivo fxml
	 * 
	 * @return {@link String}
	 */
	@SuppressWarnings("unchecked")
	default <T extends Object> String pathFileFxml() {
		Class<T> clase = (Class<T>) this.getClass();
		FXMLFile fxmlFile = clase.getDeclaredAnnotation(FXMLFile.class);
		return String.format(ReflectionConstants.CONST_PATH_FILE_FXML_JOIN_ANNOTATED, fxmlFile.path(), fxmlFile.file());
	}

}
