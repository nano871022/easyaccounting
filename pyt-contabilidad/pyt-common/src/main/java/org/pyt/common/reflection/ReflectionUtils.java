package org.pyt.common.reflection;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.pyt.common.abstracts.ADto;
import org.pyt.common.common.Log;
import org.pyt.common.exceptions.ReflectionException;
import org.pyt.common.validates.ValidateValues;

/**
 * Se encarga de realizar acciones con relection sobre objetos
 * 
 * @author Alejandro Parra
 * @since 26/07/2018
 */
public final class ReflectionUtils {
	private ValidateValues valid;
	private static ReflectionUtils instancia;
	private final static String FIELD_CLONE = "clone";
	private Log logger = Log.Log(this.getClass());

	private ReflectionUtils() {
		valid = new ValidateValues();
	}

	/**
	 * Instancia de la clase
	 * 
	 * @return {@link ReflectionUtils}
	 */
	public final static ReflectionUtils instanciar() {
		if (instancia == null) {
			instancia = new ReflectionUtils();
		}
		return instancia;
	}

	/**
	 * Se encarga de copiar un onjeto en otro, esta copia se debe realizar el que el
	 * objeto de destino debe ser ina insancia del origen
	 * 
	 * @param origen
	 *            {@link Object} extends {@link ADto}
	 * @param destino
	 *            {@link Class} extends {@link ADto}
	 * @return {@link Object} extends {@link ADto}
	 */
	public final <T extends ADto, S extends ADto> S copy(T origen, Class<S> destino) {
		try {
			List<String> campos = origen.getNameFields();
			S out = destino.getConstructor().newInstance();
			if (valid.isCast(origen, destino)) {
				for (String campo : campos) {
					out.set(campo, origen.get(campo));
				}
			}
			return out;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException | ReflectionException e) {
			logger.logger(e);
		}
		return null;
	}

	/**
	 * Usado si esta clase implementa la interfce cloneable
	 * 
	 * @param obj
	 * @return
	 * @throws ReflectionException
	 */
	@SuppressWarnings("unchecked")
	public final static <T extends Cloneable> T clone(T obj) throws ReflectionException {
		Method clone;
		try {
			clone = Object.class.getMethod(FIELD_CLONE);
			return (T) clone.invoke(obj);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			throw new ReflectionException("Se encontro error en refleccion de cloneable.", e);
		}
	}

	/**
	 * Obliga a generar una copia del objeto suministrado por medio de bytes
	 * serializable
	 * 
	 * @param obj
	 *            {@link Object}
	 * @return {@link Object}
	 */
	@SuppressWarnings("unchecked")
	public final static <T> T copy(T obj) throws ReflectionException {
		try {
			ByteArrayOutputStream ostream = new ByteArrayOutputStream();
			ObjectOutputStream oostream = new ObjectOutputStream(ostream);
			oostream.writeObject(obj);
			oostream.flush();
			byte[] bytes = ostream.toByteArray();
			InputStream istream = new ByteArrayInputStream(bytes);
			ObjectInputStream oistream = new ObjectInputStream(istream);
			return (T) oistream.readObject();
		} catch (RuntimeException e) {
			throw new ReflectionException("Error en runtime para copia de archivo por bytes", e);
		} catch (Exception e) {
			throw new ReflectionException("Error por copia de archivo por bytes.", e);
		}
	}

	/**
	 * Se encarga de obtener el valor de un campo tipo get normal, segun el
	 * fieldname suministrado
	 * 
	 * @param object
	 *            {@link Object}
	 * @param fieldName
	 *            {@link String}
	 * @return {@link Object}
	 * @throws {@link
	 *             ReflectionException}
	 */
	@SuppressWarnings("unchecked")
	public final static <T, S> S getValueField(T object, String fieldName) throws ReflectionException {
		try {
			Class<T> clase = (Class<T>) object.getClass();
			Field field = clase.getDeclaredField(fieldName);
			String nombre = field.getName();
			nombre = nombre.substring(0, 1).toUpperCase() + nombre.substring(1);
			Method metodo = clase.getDeclaredMethod("get" + nombre);
			return (S) metodo.invoke(object);
		} catch (NoSuchFieldException e) {
			throw new ReflectionException("No se encuentro el campo " + e.getMessage(), e);
		} catch (SecurityException e) {
			throw new ReflectionException("Problema de seguridad en busca de campo " + e.getMessage(), e);
		} catch (NoSuchMethodException e) {
			throw new ReflectionException("No se encontro el metodo " + e.getMessage(), e);
		} catch (IllegalAccessException e) {
			throw new ReflectionException(e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			throw new ReflectionException(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			throw new ReflectionException(e.getMessage(), e);
		}
	}

	/**
	 * Se encarga de invoicar un metodo suministrando los valores indicados
	 * 
	 * @param instance
	 *            {@link Object} extends
	 * @param methodName
	 *            {@link String} nombre del metodo a invoicar
	 * @param values
	 *            {@link Object} extends
	 * @return {@link Object} extends
	 * @throws ReflectionException
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	public final <I extends Object, R extends Object, P extends Object> R invoke(I instance, String methodName,
			P... values) throws ReflectionException {
		try {
			Class<I> clazz = (Class<I>) instance.getClass();
			Method method = getMethod(instance.getClass(), methodName, getParameters(values));
			R results;
			results = (R) method.invoke(instance, values);
			return results;
		} catch (IllegalAccessException e) {
			throw new ReflectionException("Acceso ilegal sobre metodo.", e);
		} catch (IllegalArgumentException e) {
			throw new ReflectionException("Acceso ilela sobre archumentos.", e);
		} catch (InvocationTargetException e) {
			throw new ReflectionException("Problema en la invocacion del metodo.", e);
		}
	}

	/**
	 * Se realiza busqueda sobre la clase del metodo que concuerde con el nombre y
	 * los parametros suministrados
	 * 
	 * @param clazz
	 *            {@link Class}
	 * @param methodName
	 *            {@link String}
	 * @param values
	 *            {@link Object} extends
	 * @return {@link Method}
	 * @throws {@link
	 *             ReflectionException}
	 */
	@SuppressWarnings("unchecked")
	public final <C extends Object, P extends Object> Method getMethod(Class<C> clazz, String methodName, P... values)
			throws ReflectionException {
		Method method = null;
		if (clazz == Object.class)
			throw new ReflectionException(
					"El metodo " + methodName + " no fue enccontrado en " + clazz.getCanonicalName() + ".");
		try {
			method = clazz.getDeclaredMethod(methodName, getParameters(values));
		} catch (NoSuchMethodException e) {
			method = getMethod(clazz.getSuperclass(), methodName, values);
		}
		return method;
	}

	/**
	 * Se encarga de obener las clases de cada uno de los valores suministrados y
	 * los retorna en un array de clases
	 * 
	 * @param values
	 *            {@link Object} extends {@link Arrays}
	 * @return {@link Class} {@link Arrays}
	 * @throws ReflectionException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private final <P extends Object> Class[] getParameters(P... values) throws ReflectionException {
		List<Class> listClazz = new ArrayList<Class>();
		if (values != null && values.length > 0) {
			for (P value : values) {
				listClazz.add(value.getClass());
			}
			return listClazz.toArray(new Class[listClazz.size()]);
		}
		return null;
	}
	
	/**
	 * Se encarga de buscar un campo dentro del objeto y retornarlo
	 * @param clazz {@link Class} clase de inicio de busqueda
	 * @param nameField {@link String} nombre del campo
	 * @return {@link Field}
	 * @throws {@link ReflectionException}
	 */
	public final <C extends Object> Field getField(Class<C> clazz,String nameField)throws ReflectionException{
		if(clazz == Object.class)return null;
		Field field = null;
		try {
			field = clazz.getDeclaredField(nameField);
		} catch (NoSuchFieldException | SecurityException e) {
			field = getField(clazz.getSuperclass(),nameField);
		}
		return field;
	}
}
