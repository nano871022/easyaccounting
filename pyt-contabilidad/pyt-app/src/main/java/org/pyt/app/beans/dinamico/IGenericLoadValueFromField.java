package org.pyt.app.beans.dinamico;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.abstracts.ADto;
import org.pyt.common.common.SelectList;
import org.pyt.common.exceptions.ReflectionException;
import org.pyt.common.exceptions.validates.ValidateValueException;

import com.pyt.service.dto.DocumentosDTO;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

/**
 * Interfaz que se encarga de generalizar el proceso de cargar los valores de
 * los campos creados dinámicamente, y ponerlos en la instancia deseada
 * 
 * @author Alejandro Parra
 * @since 09/08/2019
 */
public interface IGenericLoadValueFromField extends IGenericFieldCommon {

	/**
	 * Se encarga de cargar los datos de los campos
	 */
	@SuppressWarnings({ "rawtypes" })
	default void loadData() {
		getConfigFields().forEach((key, value) -> {
			if (value instanceof ChoiceBox) {
				loadValueToField((ChoiceBox) value, key);
			} else if (value instanceof TextField) {
				loadValueToField((TextField) value, key);
			} else if (value instanceof DatePicker) {
				loadValueToField((DatePicker) value, key);
			} else if (value instanceof CheckBox) {
				loadValueToField((CheckBox) value, key);
			}
		});
	}

	/**
	 * Cuando el valor que se desea ingresar es en un campo de tipo
	 * {@link ChoiceBox}
	 * 
	 * @param obj       {@link ChoiceBox}
	 * @param nameField {@link String}
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private <T, M extends ADto> void loadValueToField(ChoiceBox obj, String nameField) {
		var list = (List) getConfigFieldTypeList().get(nameField);
		var nameShow = getShow(nameField);
		if (StringUtils.isNotBlank(nameShow) && list != null && list.size() > 0) {
			M value = (M) SelectList.get((ChoiceBox) obj, list, nameShow);
			var nameAssign = getAssing(nameField);
			try {
				T value2 = null;
				if (value != null && StringUtils.isNotBlank(nameAssign)) {
					value2 = value.get(nameAssign);
				}
				getInstanceDTOUse().set(nameField, value2 != null ? value2 : value);
			} catch (ReflectionException e) {
				error(e);
			}
		}
	}

	/**
	 * Cuando el valor que se desea ingresar es en un campo de tipo
	 * {@link TextField}
	 * 
	 * @param obj       {@link TextField}
	 * @param nameField {@link String}
	 */
	private void loadValueToField(TextField obj, String nameField) {
		var value = ((TextField) obj).getText();
		try {
			var clase = getInstanceDTOUse().getType(nameField);
			var valueEnd = validateValue.cast(value, clase);
			if (valueEnd != null) {
				getInstanceDTOUse().set(nameField, valueEnd);
			}
		} catch (ReflectionException | ValidateValueException e) {
			error(e);
		}
	}

	/**
	 * Cuando el valor que se desea ingresar es en un campo de tipo
	 * {@link DataPicker}
	 * 
	 * @param obj       {@link DataPicker}
	 * @param nameField {@link String}
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void loadValueToField(DatePicker obj, String nameField) {
		var ld = ((DatePicker) obj).getValue();
		Class clase;
		try {
			clase = getInstanceDTOUse().getType(nameField);
			var valueEnd = validateValue.cast(ld, clase);
			if (valueEnd != null) {
				getInstanceDTOUse().set(nameField, valueEnd);
			}
		} catch (ReflectionException | ValidateValueException e) {
			error(e);
		}
	}

	/**
	 * Cuando el valor que se desea ingresar es en un campo de tipo {@link CheckBox}
	 * 
	 * @param obj       {@link CheckBox}
	 * @param nameField {@link String}
	 */
	private void loadValueToField(CheckBox obj, String nameField) {
		try {
			getInstanceDTOUse().set(nameField, ((CheckBox) obj).isSelected());
		} catch (ReflectionException e) {
			error(e);
		}
	}

	/**
	 * Se encarga de obtener el campo de mostrar apartir del nombre del campo a usar
	 * 
	 * @param fieldName {@link String}
	 * @return {@link String}
	 */
	private String getShow(String fieldName) {
		var campos = getFields();
		for (DocumentosDTO doc : campos) {
			if (doc.getFieldName().contains(fieldName)) {
				return doc.getPutNameShow();
			}
		}
		return null;
	}

	/**
	 * Se encagra de obtener el nombre de asignacion del nombre del campo
	 * 
	 * @param fieldName {@link String}
	 * @return {@link String}
	 */
	private String getAssing(String fieldName) {
		var campos = getFields();
		for (DocumentosDTO doc : campos) {
			if (doc.getFieldName().contains(fieldName)) {
				return doc.getPutNameAssign();
			}
		}
		return null;
	}

}
