package org.pyt.app.components;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.controlsfx.glyphfont.FontAwesome;
import org.pyt.common.abstracts.ADto;
import org.pyt.common.annotation.generics.AssingValue;
import org.pyt.common.common.UtilControlFieldFX;
import org.pyt.common.constants.LanguageConstant;
import org.pyt.common.constants.StylesPrincipalConstant;
import org.pyt.common.exceptions.ReflectionException;

import com.pyt.service.pojo.GenericPOJO;

import co.com.japl.ea.beans.AGenericToBean;
import javafx.scene.layout.GridPane;

public interface IGenericFilter<T extends ADto> extends IGenericFieldsCommon<T> {

	public Map<String, Object> defaultValuesGenericParametrized = new HashMap<String, Object>();

	Map<String, GenericPOJO<T>> getFilters();

	void setFilters(Map<String, GenericPOJO<T>> filters);

	T getFilter();

	void setFilter(T filter);

	GridPane getGridPaneFilter();

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
	 * @throws NoSuchMethodException
	 * @throws InstantiationException
	 * @throws InvocationTargetException
	 * 
	 * @throws                           {@link IllegalAccessException}
	 */
	default void configFilters() throws Exception {
		var filters = getConfigFields(getInstaceOfGenericADto(), false, true);
		if (filters == null)
			throw new Exception(getI18n().valueBundle(LanguageConstant.GENERIC_FIELD_NOT_FOUND_FIELD_TO_USE));
		setFilters(filters);
		final var indices = new Index();
		var util = new UtilControlFieldFX();

		getFilters().entrySet().stream()
				.sorted((compare1, compare2) -> sortByOrderField(compare1.getValue().getOrder(),
						compare2.getValue().getOrder()))
				.forEachOrdered(
						value -> configFields(getFilter(), value.getValue(), util, getGridPaneFilter(), indices));
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
}
