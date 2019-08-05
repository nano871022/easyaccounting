package org.pyt.common.abstracts;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.pyt.common.annotation.generics.AssingValue;
import org.pyt.common.common.I18n;
import org.pyt.common.common.Log;
import org.pyt.common.common.UtilControlFieldFX;
import org.pyt.common.constants.LanguageConstant;
import org.pyt.common.exceptions.ReflectionException;

import javafx.scene.layout.GridPane;

public interface IGenericFilter<T extends ADto> {
	public Map<String, Object> defaultValuesGenericParametrized = new HashMap<String, Object>();

	Log getLogger();

	I18n getI18n();

	Class<T> getClazz();

	T getFilter();

	GridPane getGridPaneFilter();

	void setFilter(T filter);

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

	@SuppressWarnings("unchecked")
	default <S extends AGenericToBean<T>> S addDefaultValuesToGenericParametrized(String key, Object value) {
		defaultValuesGenericParametrized.put(key, value);
		return (S) this;
	}

	default void setDefaultValuesToGenericParametrized(Map<String, Object> defaultParameterized) {
		defaultParameterized.forEach((key, value) -> defaultValuesGenericParametrized.put(key, value));
	}

}
