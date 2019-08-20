package org.pyt.app.components;

import java.lang.reflect.InvocationTargetException;

import org.pyt.common.abstracts.ADto;
import org.pyt.common.common.UtilControlFieldFX;
import org.pyt.common.constants.LanguageConstant;
import org.pyt.common.exceptions.ReflectionException;

import com.pyt.service.pojo.GenericPOJO;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public interface IGenericFieldsCommon<T extends ADto> extends IGenericCommon<T> {

	public Integer countFieldsInRow();

	default <O extends Object> void assingsValueToField(T dto, String nameField, O value) {
		try {
			if (value == null) {
				getLogger().warn(getI18n().valueBundle(String.format(LanguageConstant.LANGUAGE_FIELD_ASSIGN_EMPTY,
						nameField, getInstaceOfGenericADto().getClass().getSimpleName())));
			} else {
				dto.set(nameField, value);
			}
		} catch (ReflectionException | InvocationTargetException | IllegalAccessException | InstantiationException
				| NoSuchMethodException e) {
			getLogger().logger(e);
		}
	}

	public class Index {
		Integer columnIndex = 0;
		Integer rowIndex = 0;
	}

	private void calcPosFieldInGrid(Index indices) {
		indices.columnIndex = indices.columnIndex == countFieldsInRow() ? 0 : indices.columnIndex + 2;
		indices.rowIndex = indices.columnIndex == 0 ? indices.rowIndex + 1 : indices.rowIndex;
	}

	default int sortByOrderField(Integer order1, Integer order2) {
		if (order1 == null)
			return -1;
		return order1.compareTo(order2);
	}

	default void configFields(T dto, GenericPOJO<T> pojo, UtilControlFieldFX util, GridPane gridPane, Index indices) {
		var input = util.getFieldByField(pojo.getField());
		if (input != null) {

			var label = new Label(getNameShowInLabel(pojo));
			gridPane.add(label, indices.columnIndex, indices.rowIndex);
			if (pojo.getWidth() > 0) {
				input.prefWidth(pojo.getWidth());
			}
			util.inputListenerToAssingValue(input, (obj) -> assingsValueToField(dto, pojo.getField().getName(), obj));
			gridPane.add(input, indices.columnIndex + 1, indices.rowIndex);
			calcPosFieldInGrid(indices);
		}
	}
}
