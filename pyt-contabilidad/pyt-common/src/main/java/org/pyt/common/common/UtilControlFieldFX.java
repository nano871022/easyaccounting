package org.pyt.common.common;

import java.lang.reflect.Field;
import java.util.regex.Pattern;

import org.pyt.common.interfaces.IAssingValueToField;
import org.pyt.common.interfaces.ICaller;

import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;

import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

/**
 * 
 * Se encarg de contener todo lo necesario para el control de los campos creados
 * de tipo FX
 * 
 * @author Alejandro Parra
 * @since 12/04/2019
 */
public final class UtilControlFieldFX {
	private final static String CONTS_EXP_REG_TO_TEXTFIELD = "(String|Long|Integer|BigDecimal|LongDecimal|Float|Char|char|long|int|floar|Decimal)";
	private final static String CONTS_EXP_REG_TO_CHECKBOX = "(Boolean|bool|boolean)";

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
	 * interfaz el valor ingresado/seleccionado sera enviado el valor a un lambda indicado
	 * 
	 * @param input {@link Control} campo FX que se le agrega el evento de cambio de su informacion
	 * @param assignValue {@link IAssingValueToField#assingValueToField(Object)} lambda para asignar el valor
	 * @return {@link Control} el mismo campo FX ingresado
	 */
	public <T extends Control> T inputListenerToAssingValue(T input, IAssingValueToField assignValue) {
		StringProperty property = null;
		if (input instanceof TextField) {
			property = ((TextField) input).textProperty();
		}
		if (input instanceof CheckBox) {
			property = ((CheckBox) input).textProperty();
		}
		if (input instanceof ChoiceBox<?>) {
			property = ((ChoiceBox<?>) input).idProperty();
		}
		property.addListener((observable, oldValue, newValue) -> assignValue.assingValueToField(newValue));
		return input;
	}
	/**
	 * Se crea para facilitar la creacion de boton fX con evento de click asignado, el evento se ejecuta segun el lambda ingresado
	 * @param caller {@link ICaller} lambda para llamar accion
	 * @param showName {@link String} valor que se mostrara en el boton
	 * @return {@link Button}
	 */
	public Button buttonGenericWithEventClicked(ICaller caller, String showName) {
		var button = new Button(showName);
		button.onMouseClickedProperty().set((event) -> caller.caller());
		return button;
	}
	/**
	 * Se crea para facilitar la creacion de boton fX con evento de click asignado, el evento se ejecuta segun el lambda ingresado
	 * @param caller {@link ICaller} lambda para llamar accion
	 * @param showName {@link String} valor que se mostrara en el boton
	 * @param fontIconIkonli {@link String} icono de ikonli que se agrega al texto 
	 * @return {@link Button}
	 */
	public Button buttonGenericWithEventClicked(ICaller caller, String showName,FontAwesome.Glyph fontIcon) {
		var button = new Button(showName);
		button.onMouseClickedProperty().set((event) -> caller.caller());
		
		Glyph fi = new Glyph("FontAwesome",fontIcon);
		button.setGraphic(fi);
		return button;
	}

	/**
	 * Se encarga de limpiar el {@link Node campo} suministrado, dejar el valor por default
	 * @param child {@link Node}
	 */
	public <T extends Control>void cleanValueByFieldFX(Node child) {
		if (child instanceof TextField) {
			((TextField) child).setText(null);
		}
		if(child instanceof CheckBox) {
			((CheckBox)child).setText(null);
		}
		if(child instanceof ChoiceBox) {
			((ChoiceBox<?>)child).getSelectionModel().clearSelection();
		}
	}
}
