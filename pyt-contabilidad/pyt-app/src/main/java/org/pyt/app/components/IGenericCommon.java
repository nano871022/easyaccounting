package org.pyt.app.components;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.pyt.common.abstracts.ADto;
import org.pyt.common.annotation.generics.DefaultFieldToGeneric;
import org.pyt.common.common.I18n;
import org.pyt.common.common.Log;
import org.pyt.common.validates.ValidateValues;

import com.pyt.service.pojo.GenericPOJO;

public interface IGenericCommon<T extends ADto> {
	ValidateValues validateValues = new ValidateValues();
	Log getLogger();

	I18n getI18n();

	Class<T> getClazz();

	void setClazz(Class<T> clazz);

	DataTableFXML<T, T> getTable();

	/**
	 * Se encarhad e obtener una nueva instancia de la clase usada como generico de
	 * la aplicacion
	 * 
	 * @return T generico usado, extiende de {@link ADto}
	 * @throws {@link InvocationTargetException}
	 * @throws {@link IllegalAccessException}
	 * @throws {@link InstantiationException}
	 * @throws {@link NoSuchMethodException}
	 */
	default T getInstaceOfGenericADto()
			throws InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException {
		return getClazz().getConstructor().newInstance();
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
	default <O extends Object> void addFieldIntoMap(Map<String, GenericPOJO<T>> maps, Field field, O instance,
			GenericPOJO.Type typeGeneric) {
		try {
			Boolean valid = false;
			if (GenericPOJO.Type.FILTER == typeGeneric) {
				DefaultFieldToGeneric[] annotated = field.getAnnotationsByType(DefaultFieldToGeneric.class);
				valid = Arrays.asList(annotated).stream()
						.filter(annotate -> annotate.use() == DefaultFieldToGeneric.Uses.FILTER
								&& annotate.simpleNameClazzBean().contentEquals(this.getClass().getSimpleName()))
						.toArray().length > 0;
			} else if (GenericPOJO.Type.COLUMN == typeGeneric) {
				DefaultFieldToGeneric[] annotated = field.getAnnotationsByType(DefaultFieldToGeneric.class);
				valid = Arrays.asList(annotated).stream()
						.filter(annotate -> annotate.use() == DefaultFieldToGeneric.Uses.COLUMN
								&& annotate.simpleNameClazzBean().contentEquals(this.getClass().getSimpleName()))
						.toArray().length > 0;
			}
			if (valid) {
				if (!field.canAccess(instance)) {
					field.trySetAccessible();
				}
				maps.put(field.getName(), fillGenericPOJO(instance, field, typeGeneric));
			}
		} catch (IllegalAccessException e) {
			getLogger().logger(e);
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
	private <O extends Object> GenericPOJO<T> fillGenericPOJO(O instance, Field field, GenericPOJO.Type type)
			throws IllegalAccessException {
		if (field.canAccess(instance)) {
			field.trySetAccessible();
		}
		DefaultFieldToGeneric dftg = field.getDeclaredAnnotation(DefaultFieldToGeneric.class);
		if (dftg != null) {
			return new GenericPOJO(field.getName(), field, field.get(instance), type, dftg.width());
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
	default <O extends Object> Map<String, GenericPOJO<T>> getMapFieldsByObject(O instance,
			GenericPOJO.Type typeGeneric) throws IllegalAccessException {
		Map<String, GenericPOJO<T>> maps = new HashMap<String, GenericPOJO<T>>();
		Field[] fields = instance.getClass().getDeclaredFields();
		Arrays.asList(fields).stream()
				.filter(field -> !Modifier.isStatic(field.getModifiers())
						&& field.getAnnotationsByType(DefaultFieldToGeneric.class).length > 0
						&& !Modifier.isAbstract(field.getModifiers()) && !Modifier.isFinal(field.getModifiers()))
				.forEach(field -> addFieldIntoMap(maps, field, instance, typeGeneric));
		return maps;
	}
}
