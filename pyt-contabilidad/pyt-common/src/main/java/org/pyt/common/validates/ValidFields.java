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
import javafx.scene.layout.GridPane;

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

	/**
	 * Se encarga de validar campos de texto y text area que se encuentran dentro
	 * del grid pane
	 * 
	 * @param nameField {@link String} nombre del campo en el dto
	 * @param notEmpty  {@link Boolean} indica si es obligatorio
	 * @param min       {@link Integer} indica los caracteres minimos,si es nulo sin
	 *                  limite
	 * @param max       {@link Integer} iidnica los caracteres maximos, si es nulo
	 *                  sin limite
	 * @param msnError  {@link String} mensaje de error que se mostrara cuando no
	 *                  cumpla las condiciones
	 * @param gridpane  {@link GridPane} donde se encuentra el campo buscado
	 * @return {@link Boolean}
	 */
	public static final Boolean valid(String nameField, boolean notEmpty, Integer min, Integer max, String msnError,
			GridPane gridpane) {
		var list = gridpane.getChildren().stream().filter(node -> node.getId().contentEquals(nameField));
		while (list.iterator().hasNext()) {
			var node = list.iterator().next();
			if (node instanceof TextField) {
				return ValidFields.valid((TextField) node, notEmpty, min, max, msnError);
			}
			if (node instanceof TextArea) {
				return ValidFields.valid((TextArea) node, notEmpty, min, max, msnError);
			}
		}
		return false;
	}

	/**
	 * Se encarga de validar campos de choice box que se encuentran dentro del grid
	 * pane
	 * 
	 * @param nameField {@link String} nombre del campo
	 * @param notEmpty  {@link Boolean} si el campo no debe estar vacio
	 * @param msnError  {@link String} mensaje de error
	 * @param gridpane  {@link GridPane} donde se encentra el campo buscado
	 * @return {@link Boolean}
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static final Boolean valid(String nameField, Boolean notEmpty, String msnError, GridPane gridpane) {
		var list = gridpane.getChildren().stream().filter(node -> node.getId().contentEquals(nameField));
		while (list.iterator().hasNext()) {
			var node = list.iterator().next();
			if (node instanceof ChoiceBox) {
				return ValidFields.valid((ChoiceBox) node, notEmpty, msnError);
			}
		}
		return false;
	}

	/**
	 * Se encarga de validar campos de choice box que se encuentran dentro del grid
	 * pane
	 * 
	 * @param nameField {@link String} nombre del campo
	 * @param map       {@link Map} mapa con los campos a validar.
	 * @param notEmpty  {@link Boolean} si el campo no debe estar vacio
	 * @param msnError  {@link String} mensaje de error
	 * @param gridpane  {@link GridPane} donde se encentra el campo buscado
	 * @return {@link Boolean}
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static final Boolean valid(String nameField, Map map, Boolean notEmpty, String msnError, GridPane gridpane) {
		var list = gridpane.getChildren().stream().filter(node -> node.getId().contentEquals(nameField));
		while (list.iterator().hasNext()) {
			var node = list.iterator().next();
			if (node instanceof ChoiceBox) {
				return ValidFields.valid((ChoiceBox) node, map, notEmpty, msnError);
			}
		}
		return false;
	}

	/**
	 * Se encarga de validar campos de choice box que se encuentran dentro del grid
	 * pane
	 * 
	 * @param nameField {@link String} nombre del campo
	 * @param list      {@link List} lista con los campos a validar.
	 * @param notEmpty  {@link Boolean} si el campo no debe estar vacio
	 * @param msnError  {@link String} mensaje de error
	 * @param gridpane  {@link GridPane} donde se encentra el campo buscado
	 * @return {@link Boolean}
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static final Boolean valid(String nameField, List listDto, String fieldNameFromList, Boolean notEmpty,
			String msnError, GridPane gridpane) {
		var list = gridpane.getChildren().stream().filter(node -> node.getId().contentEquals(nameField));
		while (list.iterator().hasNext()) {
			var node = list.iterator().next();
			if (node instanceof ChoiceBox) {
				return ValidFields.valid((ChoiceBox) node, listDto, fieldNameFromList, notEmpty, msnError);
			}
		}
		return false;
	}

	/**
	 * Se encargha de validar campos de textfield que contiene valores numericos y
	 * se realiza la busqueda de este campo sobre el {@link GridPane}
	 * 
	 * @param nameField {@link String} nombre del campo
	 * @param notEmpty  {@link Boolean} si el campo no debe estar vacio
	 * @param min       {@link Object} generic valor minimo
	 * @param max       {@link Object} generic valor maximo
	 * @param msnError  {@link String} mensaje deerro
	 * @param gridpane  {@link GridPane} donde se enucnetra el campo buscado
	 * @return {@link Boolean}
	 */
	public static final <T extends Object> Boolean validNumber(String nameField, Boolean notEmpty, T min, T max,
			String msnError, GridPane gridpane) {
		var list = gridpane.getChildren().stream().filter(node -> node.getId().contentEquals(nameField));
		while (list.iterator().hasNext()) {
			var node = list.iterator().next();
			if (node instanceof TextField) {
				if ((min != null && min instanceof Integer) || (max != null && max instanceof Integer)) {
					return ValidFields.numeric((TextField) node, notEmpty, (Integer) min, (Integer) max, msnError);
				}
				if ((min != null && min instanceof BigDecimal) || (max != null && max instanceof BigDecimal)) {
					return ValidFields.numeric((TextField) node, notEmpty, (BigDecimal) min, (BigDecimal) max,
							msnError);
				}
				if ((min != null && min instanceof Double) || (max != null && max instanceof Double)) {
					return ValidFields.numeric((TextField) node, notEmpty, (Double) min, (Double) max, msnError);
				}
				if ((min != null && min instanceof Long) || (max != null && max instanceof Long)) {
					return ValidFields.numeric((TextField) node, notEmpty, (Long) min, (Long) max, msnError);
				}
			}
		}
		return false;
	}

}
