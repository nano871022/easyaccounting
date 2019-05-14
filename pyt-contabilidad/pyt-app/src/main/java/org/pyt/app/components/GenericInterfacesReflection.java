package org.pyt.app.components;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.pyt.common.abstracts.ADto;
import org.pyt.common.abstracts.AGenericToBean;
import org.pyt.common.annotation.generics.DefaultFieldToGeneric;
import org.pyt.common.annotations.Inject;

import com.pyt.service.interfaces.IQuerysPopup;
import com.pyt.service.pojo.GenericPOJO;

/**
 * Clase encargada de tener codio generico para poder implementar las paginas de
 * creacion dinamica segun el tipo de la clase indicado
 * 
 * @author Alejandro Parra
 * @since 09/04/2019
 */
public abstract class GenericInterfacesReflection<T extends ADto> extends AGenericToBean<T> {

	@Inject(resource = "com.pyt.service.implement.QuerysPopupSvc")
	protected IQuerysPopup querys;

	/**
	 * Se debe suministrar la clase con la cual se indica el generico
	 * 
	 * @param clazz {@link Class}
	 * @throws {@link Exception}
	 */
	public GenericInterfacesReflection(Class<T> clazz) throws Exception {
		super();
		setClassParameterized(clazz);
		instaceOfGenericDTOAll();
	}

	/**
	 * Se encarga de recorrer todos los campos de la aplicacion y aquellos que sean
	 * del generico de la clase les crea una instancia en la cosntruccion de la
	 * clase
	 * 
	 * @throws {@link Exception}
	 */
	private final void instaceOfGenericDTOAll() throws Exception {
		Field[] fields = this.getClass().getDeclaredFields();
		Arrays.asList(fields).stream().filter(field -> field.getType() == ADto.class)
				.forEach(field -> configFieldValue(field));
	}

	/**
	 * Se encargad e agregar el valor en el campo y que se obliga a poder ingresarle
	 * informacion directamente
	 * 
	 * @param field {@link Field}
	 */
	private final void configFieldValue(Field field) {
		try {
			if (!field.canAccess(this)) {
				field.trySetAccessible();
			}
			field.set(this, getInstaceOfGenericADto());
		} catch (Exception exception) {
			logger().logger(exception);
		}
	}

	/**
	 * Se encarga de crear una instancia del generic pojo con la instancia usada, el
	 * campo y el tipo de la instancia
	 * 
	 * @param instance {@link Object} instancia que contiene el campo suministrado
	 * @param field    {@link Field} campo con el cual se debe tener relacion
	 * @param type     {@link GenericPOJO.Type} tipo de configuracion el objeto
	 * @return {@link GenericPOJO}
	 * @throws {@link IllegalAccessException}
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private final <O extends Object> GenericPOJO<T> fillGenericPOJO(O instance, Field field, GenericPOJO.Type type)
			throws IllegalAccessException {
		if (field.canAccess(instance)) {
			field.trySetAccessible();
		}
		DefaultFieldToGeneric dftg = field.getDeclaredAnnotation(DefaultFieldToGeneric.class);
		if(dftg != null) {
			return new GenericPOJO(field.getName(), field, field.get(instance), type,dftg.width());
		}
		return new GenericPOJO(field.getName(), field, field.get(instance), type);
	}

	/**
	 * Se encarga de generar un mapa con todos los campos que se encuentran dentro
	 * de un objeto
	 * 
	 * @param instance    {@link Object}
	 * @param typeGeneric {@link GenericPOJO#Type}
	 * @return {@link Map} < {@link String},{@link GenericPOJO}<T>>
	 * @throws {@link IllegalAccessException}
	 */
	protected final <O extends Object> Map<String, GenericPOJO<T>> getMapFieldsByObject(O instance,
			GenericPOJO.Type typeGeneric) throws IllegalAccessException {
		Map<String, GenericPOJO<T>> maps = new HashMap<String, GenericPOJO<T>>();
		Field[] fields = instance.getClass().getDeclaredFields();
		Arrays.asList(fields).stream()
				.filter(field -> !Modifier.isStatic(field.getModifiers())
						&& field.getAnnotationsByType(DefaultFieldToGeneric.class).length > 0
						&& !Modifier.isAbstract(field.getModifiers()) && !Modifier.isFinal(field.getModifiers()))
				.forEach(field -> agregarFieldIntoMap(maps, field, instance, typeGeneric));
		return maps;
	}

	/**
	 * Se encarga de agregar el field dentro de un wrapper para para poder ser
	 * puesto en un mapa
	 * 
	 * @param maps        {@link Map}
	 * @param field       {@link Field}
	 * @param instance    {@link Object}
	 * @param typeGeneric {@link GenericPOJO#Type}
	 */
	private final <O extends Object> void agregarFieldIntoMap(Map<String, GenericPOJO<T>> maps, Field field, O instance,
			GenericPOJO.Type typeGeneric) {
		try {
			Boolean valid = false;
			if (GenericPOJO.Type.FILTER == typeGeneric) {
				DefaultFieldToGeneric[] annotated = field.getAnnotationsByType(DefaultFieldToGeneric.class);
				valid = Arrays.asList(annotated).stream()
						.filter(annotate -> annotate.use() == DefaultFieldToGeneric.Uses.FILTER
								&& annotate.simpleNameClazzBean().contentEquals(this.getClass().getSimpleName())
						).toArray().length > 0;
			} else if (GenericPOJO.Type.COLUMN == typeGeneric) {
				DefaultFieldToGeneric[] annotated = field.getAnnotationsByType(DefaultFieldToGeneric.class);
				valid = Arrays.asList(annotated).stream()
						.filter(annotate -> annotate.use() == DefaultFieldToGeneric.Uses.COLUMN 
						&& annotate.simpleNameClazzBean().contentEquals(this.getClass().getSimpleName())
						).toArray().length > 0;
			}
			if (valid) {
				if (!field.canAccess(instance)) {
					field.trySetAccessible();
				}
				maps.put(field.getName(), fillGenericPOJO(instance, field, typeGeneric));
			}
		} catch (IllegalAccessException e) {
			logger.logger(e);
		}
	}

}
