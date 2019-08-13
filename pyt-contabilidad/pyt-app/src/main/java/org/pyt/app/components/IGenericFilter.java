package org.pyt.app.components;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.controlsfx.glyphfont.FontAwesome;
import org.pyt.common.abstracts.ADto;
import org.pyt.common.annotation.generics.AssingValue;
import org.pyt.common.annotation.generics.DefaultFieldToGeneric;
import org.pyt.common.common.UtilControlFieldFX;
import org.pyt.common.constants.LanguageConstant;
import org.pyt.common.constants.StylesPrincipalConstant;
import org.pyt.common.exceptions.ReflectionException;

import com.pyt.service.pojo.GenericPOJO;

import co.com.japl.ea.beans.AGenericToBean;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public interface IGenericFilter<T extends ADto> extends IGenericCommon<T> {

	public Map<String, Object> defaultValuesGenericParametrized = new HashMap<String, Object>();

	Map<String, GenericPOJO<T>> getFilters();

	void setFilters(Map<String, GenericPOJO<T>> filters);

	T getFilter();

	void setFilter(T filter);

	GridPane getGridPaneFilter();

	default <O extends Object> void assingsValueToField(String nameField, O value) {
		try {
			if (value == null) {
				getLogger().warn(getI18n().valueBundle(String.format(LanguageConstant.LANGUAGE_FIELD_ASSIGN_EMPTY,
						nameField, getInstaceOfGenericADto().getClass().getSimpleName())));
			} else {
				getFilter().set(nameField, value);
			}
		} catch (ReflectionException | InvocationTargetException | IllegalAccessException | InstantiationException
				| NoSuchMethodException e) {
			getLogger().logger(e);
		}
	}

	default T assingValuesParameterized(T dto) {
		defaultValuesGenericParametrized.forEach((key, value) -> {
			try {
				dto.set(key, value);
			} catch (ReflectionException e) {
				getLogger().logger(e);
			}
		});
		return assingValueAnnotations(dto);
	}

	/**
	 * Se encarga asignar las anotaciones para poner los valores en el dto
	 * 
	 * @param dto extends {@link Object}
	 * @return extends {@link Object}
	 */
	@SuppressWarnings("unchecked")
	private T assingValueAnnotations(T dto) {
		Class<T> clazz = (Class<T>) dto.getClass();
		Arrays.asList(clazz.getDeclaredFields()).stream()
				.filter(field -> field.getAnnotation(AssingValue.class) != null).forEach(field -> {
					Arrays.asList(field.getAnnotationsByType(AssingValue.class)).forEach(annotation -> {
						try {
							dto.set(annotation.nameField(), annotation.value());
						} catch (ReflectionException e) {
							getLogger().logger(e);
						}
					});
				});
		return dto;
	}

	default void cleanFilter() {
		try {
			var util = new UtilControlFieldFX();
			setFilter(assingValuesParameterized(getInstaceOfGenericADto()));
			getGridPaneFilter().getChildren().forEach(child -> util.cleanValueByFieldFX(child));
		} catch (InvocationTargetException | IllegalAccessException | InstantiationException
				| NoSuchMethodException e) {
			getLogger().logger(e);
		}

	}

	/**
	 * Se encarga de configurar el mapa de filtros y agregar los campos de filtros a
	 * la pantalla
	 * 
	 * @throws {@link IllegalAccessException}
	 */
	default void configFilters() throws IllegalAccessException {
		var filters = getMapFieldsByObject(getFilter(), GenericPOJO.Type.FILTER);
		setFilters(filters);
		final var indices = new Index();
		var util = new UtilControlFieldFX();
		getFilters().forEach((key, value) -> {
			var input = util.getFieldByField(value.getField());
			if (input != null) {
				var label = new Label(getI18n().valueBundle(
						LanguageConstant.GENERIC_FILTER_LBL + getClazz().getSimpleName() + "." + value.getNameShow()));
				getGridPaneFilter().add(label, indices.columnIndex, indices.rowIndex);
				if (value.getWidth() > 0) {
					input.prefWidth(value.getWidth());
				}
				util.inputListenerToAssingValue(input, (obj) -> assingsValueToField(value.getField().getName(), obj));
				getGridPaneFilter().add(input, indices.columnIndex + 1, indices.rowIndex);
				indices.columnIndex = indices.columnIndex == 4 ? 0 : indices.columnIndex + 2;
				indices.rowIndex = indices.columnIndex == 0 ? indices.rowIndex + 1 : indices.rowIndex;
			}
		});
		getGridPaneFilter().getStyleClass().add(StylesPrincipalConstant.CONST_GRID_STANDARD);
		getGridPaneFilter().add(
				util.buttonGenericWithEventClicked(() -> getTable().search(),
						getI18n().valueBundle(LanguageConstant.GENERIC_FILTER_BTN_SEARCH), FontAwesome.Glyph.SEARCH),
				0, indices.rowIndex + 1);
		getGridPaneFilter().add(
				util.buttonGenericWithEventClicked(() -> cleanFilter(),
						getI18n().valueBundle(LanguageConstant.GENERIC_FILTER_BTN_CLEAN), FontAwesome.Glyph.REMOVE),
				1, indices.rowIndex + 1);

	}

	@SuppressWarnings("unchecked")
	default <S extends AGenericToBean<T>> S addDefaultValuesToGenericParametrized(String key, Object value) {
		defaultValuesGenericParametrized.put(key, value);
		return (S) this;
	}

	default void setDefaultValuesToGenericParametrized(Map<String, Object> defaultParameterized) {
		defaultParameterized.forEach((key, value) -> defaultValuesGenericParametrized.put(key, value));
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

	class Index {
		Integer columnIndex = 0;
		Integer rowIndex = 0;
	}

}
