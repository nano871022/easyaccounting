package org.pyt.common.validates;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.abstracts.ADto;
import org.pyt.common.common.SelectList;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Control;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;

/**
 * Se encarga de validar los campos,
 * 
 * @author Alejandro Parra
 * @since 17-12-2018
 */
public final class ValidFields {
	/**
	 * Se encarga de validar un campo de tipo de texto {@link TextField}
	 * 
	 * @param field    {@link TextField}
	 * @param notEmpty {@link Boolean}
	 * @param min      {@link Integer}
	 * @param max      {@link Integer}
	 * @return {@link Boolean}
	 */
	@SuppressWarnings("null")
	public static final Boolean valid(TextField field, Boolean notEmpty, Integer min, Integer max, String msnError) {
		Boolean valid = true;
		if (field == null)
			return false;
		String value = field.getText();
		valid &= notEmpty ? StringUtils.isNotBlank(value) : true;
		if (min != null && min >= 0)
			valid &= value.length() >= min;
		if (max != null && max >= 0)
			valid &= value.length() <= max;
		if (!valid)
			error(field, msnError);
		if (valid)
			success(field);
		return valid;
	}

	/**
	 * Se encarga de validar un campo de tipo de area {@link TextArea}
	 * 
	 * @param field    {@link TextArea}
	 * @param notEmpty {@link Boolean}
	 * @param min      {@link Integer}
	 * @param max      {@link Integer}
	 * @return {@link Boolean}
	 */
	@SuppressWarnings("null")
	public static final Boolean valid(TextArea field, Boolean notEmpty, Integer min, Integer max, String msnError) {
		Boolean valid = true;
		if (field == null)
			return false;
		String value = field.getText();
		valid &= notEmpty ? StringUtils.isNotBlank(value) : true;
		if (min != null && min >= 0)
			valid &= value.length() >= min;
		if (max != null && max >= 0)
			valid &= value.length() <= max;
		if (!valid)
			error(field, msnError);
		if (valid)
			success(field);
		return valid;
	}

	/**
	 * Se encarga de validar el campo seleccionable
	 * 
	 * @param selects  {@link ChoiceBox}
	 * @param notEmpty {@link Boolean}
	 * @return {@link Boolean}
	 */
	public static final <T extends Object> Boolean valid(ChoiceBox<T> selects, Boolean notEmpty, String msnError) {
		Boolean valid = true;
		if (selects == null)
			return false;
		T select = SelectList.get(selects);
		if (notEmpty && select == null)
			return false;
		if (!valid)
			error(selects, msnError);
		if (valid)
			success(selects);
		return valid;
	}

	/**
	 * Se encarga de validar el campo seleccionable
	 * 
	 * @param selects  {@link ChoiceBox}
	 * @param map      {@link Map}
	 * @param notEmpty {@link Boolean}
	 * @return {@link Boolean}
	 */
	public static final <T extends Object, S extends Object> Boolean valid(ChoiceBox<S> selects, Map<S, T> map,
			Boolean notEmpty, String msnError) {
		Boolean valid = true;
		if (selects == null)
			return false;
		T select = SelectList.get(selects, map);
		if (notEmpty && select == null)
			return false;
		if (!valid)
			error(selects, msnError);
		if (valid)
			success(selects);
		return valid;
	}

	/**
	 * Se encarga de validar el campo seleccionable
	 * 
	 * @param selects  {@link ChoiceBox}
	 * @param lista    {@link List}
	 * @param campo    {@link String}
	 * @param notEmpty {@link Boolean}
	 * @return {@link Boolean}
	 */
	public static final <T extends ADto, S extends Object> Boolean valid(ChoiceBox<S> selects, List<T> list,
			String fieldName, Boolean notEmpty, String msnError) {
		Boolean valid = true;
		if (selects == null)
			return false;
		T select = SelectList.get(selects, list, fieldName);
		if (notEmpty && select == null)
			return false;
		if (!valid)
			error(selects, msnError);
		if (valid)
			success(selects);
		return valid;
	}

	/**
	 * Se encarga de validar si el valor del campo es numerico y que el valor se
	 * encuentra en el rango configurado
	 * 
	 * @param field    {@link TextField}
	 * @param notEmpty {@link Boolean}
	 * @param min      {@link Integer}
	 * @param max      {@link Integer}
	 * @return {@link Boolean}
	 */
	public static final Boolean numeric(TextField field, Boolean notEmpty, Integer min, Integer max, String msnError) {
		Boolean valid = true;
		try {
			Integer value = null;
			ValidateValues vv = new ValidateValues();
			if (field == null)
				return false;
			valid &= vv.isCast(field.getText().trim(), Integer.class);
			if (valid)
				value = vv.cast(field.getText().trim(), Integer.class);
			if (value != null) {
				valid &= min != null ? min <= value : true;
				valid &= max != null ? max >= value : true;
			}
		} catch (Exception e) {
			valid = false;
		}
		if (!valid)
			error(field, msnError);
		if (valid)
			success(field);
		return valid;
	}

	/**
	 * Se encarga de validar si el valor del campo es numerico y que el valor se
	 * encuentra en el rango configurado
	 * 
	 * @param field    {@link TextField}
	 * @param notEmpty {@link Boolean}
	 * @param min      {@link BigDecimal}
	 * @param max      {@link BigDecimal}
	 * @return {@link Boolean}
	 */

	public static final Boolean numeric(TextField field, Boolean notEmpty, BigDecimal min, BigDecimal max,
			String msnError) {
		Boolean valid = true;
		try {
			BigDecimal value = null;
			ValidateValues vv = new ValidateValues();
			if (field == null)
				return false;
			valid &= vv.isCast(field.getText().trim(), BigDecimal.class);
			if (valid)
				value = vv.cast(field.getText().trim(), BigDecimal.class);
			if (value != null) {
				valid &= min != null ? min.compareTo(value) <= 0 : true;
				valid &= max != null ? max.compareTo(value) >= 0 : true;
			}
		} catch (Exception e) {
			valid = false;
		}
		if (!valid)
			error(field, msnError);
		if (valid)
			success(field);
		return valid;
	}

	/**
	 * Se encarga de validar si el valor del campo es numerico y que el valor se
	 * encuentra en el rango configurado
	 * 
	 * @param field    {@link TextField}
	 * @param notEmpty {@link Boolean}
	 * @param min      {@link Long}
	 * @param max      {@link Long}
	 * @return {@link Boolean}
	 */
	public static final Boolean numeric(TextField field, Boolean notEmpty, Long min, Long max, String msnError) {
		Boolean valid = true;
		try {
			Long value = null;
			ValidateValues vv = new ValidateValues();
			if (field == null)
				return false;
			valid &= vv.isCast(field.getText().trim(), Long.class);
			if (valid)
				value = vv.cast(field.getText().trim(), Long.class);
			if (value != null) {
				valid &= min != null ? min.compareTo(value) <= 0 : true;
				valid &= max != null ? max.compareTo(value) >= 0 : true;
			}
		} catch (Exception e) {
			valid = false;
		}
		if (!valid)
			error(field, msnError);
		if (valid)
			success(field);
		return valid;
	}

	/**
	 * Se encarga de validar si el valor del campo es numerico y que el valor se
	 * encuentra en el rango configurado
	 * 
	 * @param field    {@link TextField}
	 * @param notEmpty {@link Boolean}
	 * @param min      {@link Double}
	 * @param max      {@link Double}
	 * @return {@link Boolean}
	 */
	public static final Boolean numeric(TextField field, Boolean notEmpty, Double min, Double max, String msnError) {
		Boolean valid = true;
		try {
			Double value = null;
			ValidateValues vv = new ValidateValues();
			if (field == null)
				return false;
			valid &= vv.isCast(field.getText().trim(), Double.class);
			if (valid)
				value = vv.cast(field.getText().trim(), Double.class);
			if (value != null) {
				valid &= min != null ? min.compareTo(value) <= 0 : true;
				valid &= max != null ? max.compareTo(value) >= 0 : true;
			}
		} catch (Exception e) {
			valid = false;
		}
		if (!valid)
			error(field, msnError);
		if (valid)
			success(field);
		return valid;
	}

	/**
	 * Se encarga de poner color el campo de color rojo alerta de problema y un
	 * tooltip sobre el campo indicando el problema.
	 * 
	 * @param control  {@link Control}
	 * @param errorMsn {@link String}
	 */
	private static final <T extends Control> void error(T control, String errorMsn) {
		control.setStyle("-fx-border-color:red;");
		if (StringUtils.isNotBlank(errorMsn)) {
			Tooltip tooltip = new Tooltip();
			tooltip.setText(errorMsn);
			control.setTooltip(tooltip);
		}
	}

	/**
	 * Se encarga de poner el campo de lcolor verde
	 * 
	 * @param control {@link Control}
	 */
	private static final <T extends Control> void success(T control) {
		control.setStyle("-fx-border-color:green;");
		control.setTooltip(null);
	}
}
