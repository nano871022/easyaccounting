package org.pyt.app.components;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.pyt.common.abstracts.ADto;
import org.pyt.common.annotation.generics.DefaultFieldToGeneric;
import org.pyt.common.common.I18n;
import org.pyt.common.common.Log;
import org.pyt.common.constants.LanguageConstant;
import org.pyt.common.constants.ParametroConstants;
import org.pyt.common.exceptions.GenericServiceException;
import org.pyt.common.validates.ValidateValues;

import com.pyt.service.interfaces.IGenericServiceSvc;
import com.pyt.service.pojo.GenericPOJO;

import co.com.japl.ea.beans.ABean;
import co.com.japl.ea.dto.system.ConfigGenericFieldDTO;

public interface IGenericCommon<T extends ADto> {
	ValidateValues validateValues = new ValidateValues();

	Log getLogger();

	I18n getI18n();

	Class<T> getClazz();

	void setClazz(Class<T> clazz);

	DataTableFXML<T, T> getTable();

	IGenericServiceSvc<ConfigGenericFieldDTO> configGenericFieldSvc();

	@SuppressWarnings("unchecked")
	default <O extends Object> Map<String, GenericPOJO<T>> getConfigFields(T instance, boolean isColumn,
			boolean isFilter) {
		try {
			var dto = new ConfigGenericFieldDTO();
			dto.setClassPath(getClazz().getName());
			if (isColumn) {
				dto.setIsColumn(isColumn);
			}
			if (isFilter) {
				dto.setIsFilter(isFilter);
			}
			dto.setState(ParametroConstants.COD_ESTADO_PARAMETRO_ACTIVO);
			dto.setClassPathBean(((ABean<T>) this).getClass().getName());
			var list = configGenericFieldSvc().getAll(dto);
			if (list != null && list.size() > 0) {
				Map<String, GenericPOJO<T>> maps = new TreeMap<String, GenericPOJO<T>>();

				list.forEach(row -> {
					try {
						var clazz = Class.forName(row.getClassPath());
						var field = clazz.getDeclaredField(row.getName());
						var typeGeneric = row.getIsColumn() ? GenericPOJO.Type.COLUMN : GenericPOJO.Type.FILTER;
						var pojo = new GenericPOJO<T>(row.getAlias(), field, null, typeGeneric);
						pojo.setRequired(row.getIsRequired());
						pojo.setDescription(row.getDescription());
						pojo.setOrder(row.getOrden());
						maps.put(row.getName(), pojo);
					} catch (Exception e) {
						getLogger().logger(e);
					}
				});
				return maps;
			} else {
				var typeGeneric = isColumn ? GenericPOJO.Type.COLUMN : GenericPOJO.Type.FILTER;
				return getMapFieldsByObject(instance, typeGeneric);
			}
		} catch (Exception e) {
			getLogger().logger(e);
		}
		return null;
	}

	/**
	 * Se usa para generar la consulta para obtener los registros asociados de la
	 * configuracion creada en la tabla de configuracion generica de campos.
	 * 
	 * @param bean   {@link ABean} clase de bean
	 * @param column {@link Boolean}
	 * @param filter {@link Boolean}
	 * @return {@link List} < {@link ConfigGenericFieldDTO} >
	 */
	default <B extends ABean<T>> List<ConfigGenericFieldDTO> getConfigGenericFields(Class<B> bean, Boolean column,
			Boolean filter) {
		try {
			var dto = new ConfigGenericFieldDTO();
			dto.setClassPath(getClazz().getName());
			dto.setClassPathBean(bean.getName());
			dto.setState(ParametroConstants.COD_ESTADO_PARAMETRO_ACTIVO);
			dto.setIsColumn(column);
			dto.setIsFilter(filter);
			return configGenericFieldSvc().getAll(dto);
		} catch (GenericServiceException e) {
			getLogger().logger(e);
		}
		return null;
	}

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
	private <O extends Object> void addFieldIntoMap(Map<String, GenericPOJO<T>> maps, Field field, O instance,
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
	private <O extends Object> Map<String, GenericPOJO<T>> getMapFieldsByObject(O instance,
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

	default String getNameShowInLabel(GenericPOJO<T> value) {
		if (value.getField().getName().contains(value.getNameShow())) {
			return getI18n().valueBundle(LanguageConstant.GENERIC_FILTER_LBL + getClazz().getSimpleName() + "."
					+ value.getField().getName());
		} else {
			return value.getNameShow();
		}
	}
}
