package org.pyt.common.common;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.pyt.common.abstracts.ADto;
import org.pyt.common.interfaces.IAssingValueToField;
import org.pyt.common.interfaces.ICaller;
import org.pyt.common.validates.ValidateValues;

import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

/**
 * 
 * Se encarg de contener todo lo necesario para el control de los campos creados
 * de tipo FX
 * 
 * @author Alejandro Parra
 * @since 12/04/2019
 */
public final class UtilControlFieldFX {
	private final Log logger = Log.Log(this.getClass());
	private final static String CONTS_EXP_REG_TO_TEXTFIELD = "(String|Long|Integer|BigDecimal|LongDecimal|Float|Char|char|long|int|floar|Decimal)";
	private final static String CONTS_EXP_REG_TO_CHECKBOX = "(Boolean|bool|boolean)";
	private ValidateValues validateValue = new ValidateValues();
	
	/**
	 * Se encarga de obenetr un campo tipo label
	 * @param name {@link String} nombre de la etiqueta
	 * @param value Se recibiran los campos :
	 * <li>
	 * <ul>
	 * 	tooltip
	 * </ul>
	 * <ul>
	 * Mensaje tooltip
	 * </ul>
	 * </li>
	 * @return {@link Label}
	 */
	public final Label createLabel(String name,String...value) {
		var label = new Label();
		label.setText(name);
		return label;
	}
	
	/**
	 * Se ecarga de retornar un campo de tipo FX segun el tipo(Class) de un
	 * {@link Field}
	 * 
	 * @param field {@link Field}
	 * @return {@link Control} extends
	 */
	@SuppressWarnings("unchecked")
	public <T extends Control> T getFieldByField(Field field) {
		if (Pattern.compile(CONTS_EXP_REG_TO_TEXTFIELD).matcher(field.getType().getSimpleName()).find()) {
			return (T) new TextField();
		}
		if (Pattern.compile(CONTS_EXP_REG_TO_CHECKBOX).matcher(field.getType().getSimpleName()).find()) {
			return (T) new CheckBox();
		}
		return null;
	}

	/**
	 * Se encarga de configurar el metodo de cuando se ejectua un cambio en la
	 * interfaz el valor ingresado/seleccionado sera enviado el valor a un lambda
	 * indicado
	 * 
	 * @param input       {@link Control} campo FX que se le agrega el evento de
	 *                    cambio de su informacion
	 * @param assignValue {@link IAssingValueToField#assingValueToField(Object)}
	 *                    lambda para asignar el valor
	 * @return {@link Control} el mismo campo FX ingresado
	 */
	public <T extends Node> T inputListenerToAssingValue(T input, IAssingValueToField assignValue) {
		StringProperty property = null;
		if (input instanceof TextField) {
			property = ((TextField) input).textProperty();
		}
		if (input instanceof CheckBox) {
			((CheckBox) input).selectedProperty()
					.addListener((observable, oldValue, newValue) -> assignValue.assingValueToField(newValue));
		}
		if (input instanceof ChoiceBox<?>) {
			property = ((ChoiceBox<?>) input).idProperty();
		}
		if (property != null) {
			property.addListener((observable, oldValue, newValue) -> assignValue.assingValueToField(newValue));
		}
		return input;
	}

	/**
	 * Se crea para facilitar la creacion de boton fX con evento de click asignado,
	 * el evento se ejecuta segun el lambda ingresado
	 * 
	 * @param caller   {@link ICaller} lambda para llamar accion
	 * @param showName {@link String} valor que se mostrara en el boton
	 * @return {@link Button}
	 */
	public Button buttonGenericWithEventClicked(ICaller caller, String showName) {
		var button = new Button(showName);
		button.onMouseClickedProperty().set((event) -> caller.caller());
		return button;
	}

	/**
	 * Se crea para facilitar la creacion de boton fX con evento de click asignado,
	 * el evento se ejecuta segun el lambda ingresado
	 * 
	 * @param caller         {@link ICaller} lambda para llamar accion
	 * @param showName       {@link String} valor que se mostrara en el boton
	 * @param fontIconIkonli {@link String} icono de ikonli que se agrega al texto
	 * @return {@link Button}
	 */
	public Button buttonGenericWithEventClicked(ICaller caller, String showName, FontAwesome.Glyph fontIcon) {
		var button = new Button(showName);
		button.onMouseClickedProperty().set((event) -> caller.caller());

		Glyph fi = new Glyph("FontAwesome", fontIcon);
		button.setGraphic(fi);
		return button;
	}

	/**
	 * Se encarga de limpiar el {@link Node campo} suministrado, dejar el valor por
	 * default
	 * 
	 * @param child {@link Node}
	 */
	public <T extends Control> void cleanValueByFieldFX(Node child) {
		if (child instanceof TextField) {
			((TextField) child).setText(null);
		}
		if (child instanceof CheckBox) {
			((CheckBox) child).selectedProperty().setValue(false);
		}
		if (child instanceof ChoiceBox) {
			((ChoiceBox<?>) child).getSelectionModel().clearSelection();
		}
	}

	/**
	 * Se encarga de obtener todos los campos de tipo texto y chechbox y agregarles
	 * el valor encontrado en el dto suministrado
	 * 
	 * @param dto      {@link Object}
	 * @param gridPane {@link GridPane}
	 */
	@SuppressWarnings({})
	public <T extends Control, D extends ADto, M extends ADto> void loadValuesInFxml(D dto, GridPane gridPane) {
		gridPane.getChildren().forEach(node -> {
			try {
				var value = dto.get(node.getId());
				if (value != null) {
					if (node instanceof TextField) {
						((TextField) node).setText(String.valueOf(value));
					}
					if (node instanceof CheckBox) {
						((CheckBox) node).setSelected(Boolean.valueOf((boolean) value));
					}
				}
			} catch (Exception e) {
				logger.logger(e);
			}
		});
	}

	/**
	 * Se encarga de buscar los choice box dentro del {@link GridPane} y seleccionar
	 * un valor segun la info del campo indicado en el {@link ADto dto}
	 * 
	 * @param fieldName {@link String} nombre del campo a buscar para choice
	 * @param dto       {@link ADto} contiene el valor del campo a buscar
	 * @param map       {@link Map} opcional si no se pone list
	 * @param list      {@link List} opcional va con nameshow
	 * @param nameShow  {@link String} opcional va con list
	 * @param gridPane  {@link GridPane} DOnde se encuentran todos los campos
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T extends Control, D extends ADto, M extends ADto> void loadValuesInFxmlToChoice(String fieldName, D dto,
			Map map, List<M> list, String nameShow, GridPane gridPane) {
		gridPane.getChildren().stream().filter(node -> node.getId().contentEquals(fieldName)).forEach(node -> {
			try {
				var value = dto.get(node.getId());
				if (value != null) {
					if (node instanceof ChoiceBox && map == null && list == null) {
						SelectList.selectItem((ChoiceBox) node, value);
					}
					if (node instanceof ChoiceBox && map != null && list == null) {
						SelectList.selectItem((ChoiceBox) node, map, value);
					}
					if (node instanceof ChoiceBox && map == null && list != null && nameShow != null) {
						SelectList.selectItem((ChoiceBox) node, list, nameShow, (M) value);
					}
				}
			} catch (Exception e) {
				logger.logger(e);
			}
		});
	}

	/**
	 * Se encarga de configurar el grid panel para el formulario
	 * 
	 * @return {@link GridPane}
	 */
	public final GridPane configGridPane(GridPane gridPane) {
		var formulario = gridPane;
		formulario.setHgap(5);
		formulario.setVgap(5);
		formulario.setMaxWidth(1.7976931348623157E308);
		formulario.setPadding(new Insets(10));
		formulario.setAlignment(Pos.CENTER);
		BorderPane.setAlignment(formulario, Pos.TOP_LEFT);
		return formulario;
	}

	/**
	 * Obtiene el campo en el cual sera usado para poner en el formulario generado
	 * apartir del tipo de dato que retorna el nombre del campo a usar
	 * 
	 * @param type {@link Class}
	 * @return {@link Node} campos javafx
	 */
	@SuppressWarnings("hiding")
	public final <T extends Object> Node getFieldNodeFormFromTypeAndValue(Class<T> type, T value) {
		if (type == Date.class) {
			DatePicker dp = new DatePicker();
			if (validateValue.isCast(value, Date.class)) {
				LocalDate ld = ((Date) value).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				dp.setValue(ld);
			}
			return dp;
		}
		if (type == LocalDate.class) {
			DatePicker dp = new DatePicker();
			if (validateValue.isCast(value, LocalDate.class))
				dp.setValue((LocalDate) value);
			return dp;
		}
		if (type == Boolean.class) {
			CheckBox cb = new CheckBox();
			if (validateValue.isCast(value, Boolean.class))
				cb.setSelected((Boolean) value);
			return cb;
		}
		if (type == String.class) {
			TextField tf = new TextField();
			if (validateValue.isCast(value, String.class))
				tf.setText((String) value);
			return tf;
		}
		if (type == Double.class) {
			TextField tf = new TextField();
			if (validateValue.isCast(value, Double.class))
				tf.setText(String.valueOf((Double) value));
			return tf;
		}
		if (type == Integer.class) {
			TextField tf = new TextField();
			if (validateValue.isCast(value, Integer.class))
				tf.setText(String.valueOf((Integer) value));
			return tf;
		}
		if (type == BigDecimal.class) {
			TextField tf = new TextField();
			if (validateValue.isCast(value, BigDecimal.class))
				tf.setText(String.valueOf((BigDecimal) value));
			return tf;
		}
		if (type == BigInteger.class) {
			TextField tf = new TextField();
			if (validateValue.isCast(value, BigInteger.class))
				tf.setText(String.valueOf((BigInteger) value));
			return tf;
		}
		if (type == Long.class) {
			TextField tf = new TextField();
			if (validateValue.isCast(value, Long.class))
				tf.setText(String.valueOf((Long) value));
			return tf;
		}
		if (type == Float.class) {
			TextField tf = new TextField();
			if (validateValue.isCast(value, Float.class))
				tf.setText(String.valueOf((Float) value));
			return tf;
		}
		return null;
	}

	/**
	 * Se encarga de configurar el grid panel para el formulario
	 * 
	 * @return {@link GridPane}
	 */
	public final TableView configTableView(TableView table) {
		table.setMaxWidth(1.7976931348623157E308);
		table.setPadding(new Insets(10));
		BorderPane.setAlignment(table, Pos.TOP_LEFT);
		return table;
	}
}
