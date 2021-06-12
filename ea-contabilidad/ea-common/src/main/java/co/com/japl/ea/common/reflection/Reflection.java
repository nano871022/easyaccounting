package co.com.japl.ea.common.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
import java.util.ServiceLoader;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.Inject;
import org.pyt.common.annotations.PostConstructor;
import org.pyt.common.annotations.Singleton;
import org.pyt.common.annotations.SubcribcionesToComunicacion;
import org.pyt.common.annotations.SubcribirToComunicacion;
import org.pyt.common.common.Comunicacion;
import org.pyt.common.common.ListUtils;
import org.pyt.common.common.Log;
import org.pyt.common.constants.AppConstants;
import org.pyt.common.constants.ReflectionConstants;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.common.interfaces.IComunicacion;
import co.com.japl.ea.common.properties.CacheInjects;
import co.com.japl.ea.common.properties.CacheServiceInject;
import co.com.japl.ea.common.properties.EjbHome;
import co.com.japl.ea.common.properties.EjbRemote;
import co.com.japl.ea.common.properties.ServiceSimple;
import co.com.japl.ea.exceptions.ReflectionException;

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
	@SuppressWarnings({ "unchecked", "rawtypes" })
	default <L extends Comunicacion, N, T, S extends Object, M extends IComunicacion> void inject()
			throws ReflectionException {
		try {
			Class<S> clase = (Class<S>) this.getClass();
			inject((S) this, clase);
			subscriberInject(this, clase);
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
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private <T> Field[] getAnnotedField(Class<T> clazz, Class annotatedClass) throws Exception {
		if (clazz == Object.class)
			return null;
		Field[] fields = null;
		try {
			fields = Arrays.asList(clazz.getDeclaredFields()).stream()
					.filter(field -> field.getAnnotation(annotatedClass) != null).toArray(Field[]::new);
		} catch (Exception e) {
			fields = Arrays.asList(clazz.getFields()).stream()
					.filter(field -> field.getAnnotation(annotatedClass) != null).toArray(Field[]::new);
		} finally {
			if (fields.length == 0) {
				return getAnnotedField(clazz.getSuperclass(), annotatedClass);
			}
		}
		return fields;
	}

	/**
	 * Obtiene los campos obtenidos de las clases
	 * 
	 * @param clazz {@link Class}
	 * @return {@link Field}
	 * @throws {@link Exception}
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private <T> Method[] getAnnotedMethod(Class<T> clazz, Class annotatedClass) throws Exception {
		if (clazz == Object.class)
			return null;
		Method[] methods = null;
		try {
			methods = Arrays.asList(clazz.getDeclaredMethods()).stream()
					.filter(method -> method.getAnnotation(annotatedClass) != null).toArray(Method[]::new);
		} catch (Exception e) {
			methods = Arrays.asList(clazz.getMethods()).stream()
					.filter(method -> method.getAnnotation(annotatedClass) != null).toArray(Method[]::new);
		} finally {
			if (methods.length == 0) {
				return getAnnotedMethod(clazz.getSuperclass(), annotatedClass);
			}
		}
		return methods;
	}

	@SuppressWarnings({ "unchecked" })
	private <T, S> void inject(S object, Class<S> clase) throws Exception {
		if (object != null && clase != Object.class && clase != Reflection.class) {
			Field[] fields = getAnnotedField(clase, Inject.class);
			if (ListUtils.isNotBlank(fields)) {
				Arrays.asList(fields).stream().filter(value->value!=null).forEach(value->listInject(value,object));
			}
			inject(object, (Class<S>) clase.getSuperclass());
		}
	}
	
	private <S> void listInject(Field field,S object) {
		try {
			Inject inject = field.getAnnotation(Inject.class);
			var obj = CacheServiceInject.instance().implement(field.getType());
			if (obj.isEmpty()) {
				obj = CacheInjects.instance().getInjectCache(field);
				obj = searchInjectFileParameterizeds(obj, field, inject);
			}
			if (obj.isPresent()) {
				CacheInjects.instance().addInjectToCache(obj.get(), field);
				if (!CacheInjects.instance().invokeConstructorAnnotatedCache(obj.get())) {
					postConstructor(obj.get(), obj.get().getClass());
				}
				put(object, field, obj.get());
			}
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException
				| IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (ReflectionException e) {
			throw new RuntimeException(e);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}

	@SuppressWarnings("unchecked")
	private <T> Optional<T> searchInjectFileParameterizeds(Optional<T> obj, Field field, Inject inject)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		if (obj.isEmpty()) {
			obj = locatorServices(field);
			if (obj.isEmpty() && StringUtils.isNotBlank(inject.resource())) {
				var resource = inject.resource();
				var clazz = Class.forName(resource);
				return Optional.of((T) clazz.getConstructor().newInstance());
			} else if (obj.isEmpty()) {
				return getSingletonAnnotated(inject.resource(), field);
			}
		}
		return obj;
	}

	@SuppressWarnings("unchecked")
	private <T> Optional<T> locatorServices(Field field) {
		var service = ServiceLoader.load(field.getType());
		if (service != null && service.findFirst() != null && service.findFirst().isPresent()) {
			return Optional.of((T) service.findFirst().get());
		} else {
			try {
				return Optional.of((T) EjbRemote.getInstance().getEjb(field.getType()));
			} catch (Exception e) {
				try {
					return Optional.of((T) EjbHome.getInstance().getEjb(field.getType()));
				} catch (Exception e1) {
					try {
						return Optional.of((T) ServiceSimple.getInstance().getService(field.getType()));
					} catch (Exception e2) {
					}
				}
			}
		}
		return Optional.empty();
	}

	@SuppressWarnings("unchecked")
	private <T> Optional<T> getSingletonAnnotated(String resource, Field field)
			throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, InstantiationException {
		if (StringUtils.isBlank(resource)) {
			Class<T> classe = (Class<T>) field.getType();
			Singleton singleton = classe.getAnnotation(Singleton.class);
			if (singleton != null) {
				var method = classe.getDeclaredMethod(AppConstants.ANNOT_SINGLETON);
				return Optional.of((T) method.invoke(classe));
			} else if(!classe.isInterface()){
				return Optional.of((T) field.getType().getConstructor().newInstance());
			}
		}
		return Optional.empty();
	}

	/**
	 * Se encarga de buscar dentro de la instancia un metodo que este anotado con
	 * poscontructor para cargar algo
	 * 
	 * @param instance {@link Object} extends
	 * @param clase    {@link Class}
	 */
	private <S, M extends Object> void postConstructor(M instance, Class<S> clase) throws Exception {
		try {
			if (clase == null || clase == Object.class || clase == Reflection.class)
				return;
			var metodos = getAnnotedMethod(clase, PostConstructor.class);
			if (metodos != null && metodos.length > 0) {
				Arrays.asList(metodos).stream().forEach(metodo -> {
					try {
						CacheInjects.instance().addConstructorAnnotatedToCache(instance, metodo);
						metodo.invoke(instance);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						throw new RuntimeException(e);
					}
				});
				postConstructor(instance, clase.getSuperclass());
			}
		} catch (RuntimeException e) {
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
			Class<S> clase) throws Exception {
		if (instance == null || clase == null)
			return;
		Field fieldsSubs[] = getAnnotedField(clase, SubcribcionesToComunicacion.class),
				fieldsSub[] = getAnnotedField(clase, SubcribirToComunicacion.class);
		if (fieldsSubs != null) {
			Arrays.asList(fieldsSubs).stream().forEach(field -> {
				SubcribcionesToComunicacion subs = field.getAnnotation(SubcribcionesToComunicacion.class);
				if (subs != null) {
					L obj;
					obj = get(this, field);
					if (obj != null && (this) instanceof IComunicacion) {
						Arrays.asList(subs.value()).forEach(sub -> obj.subscriber((N) instance, sub.comando()));
					} else {
						logger().error("El objeto " + clase.getName() + " no tiene la implementacion de "
								+ IComunicacion.class.getName());
					}
				}
			});
		}
		if (fieldsSub != null) {
			Arrays.asList(fieldsSub).stream().forEach(field -> {
				SubcribirToComunicacion sub = field.getAnnotation(SubcribirToComunicacion.class);
				if (sub != null) {
					L obj = get(this, field);
					if (obj != null && (this) instanceof IComunicacion) {
						obj.subscriber((N) instance, sub.comando());
					} else {
						logger().error("El objeto " + clase.getName() + " no tiene la implementacion de "
								+ IComunicacion.class.getName());
					}
				}
			});
		}
		subscriberInject(instance, clase.getSuperclass());
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
