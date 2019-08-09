package org.pyt.app.beans.dinamico;

import org.pyt.common.exceptions.validates.ValidateValueException;

import javafx.scene.control.TextField;

/**
 * Se encarga de generaliza la forma de poner el valor sobre un campo
 * 
 * @author Alejandro Parra
 * @since 09/08/2019
 */
public interface IGenericFieldValueToField extends IGenericFieldCommon {
	/**
	 * Se encarga de poner en un campo el valor suministrado
	 * 
	 * @param nombreCampo {@link String}
	 * @param value       {@link Object} extends
	 */
	default <S extends Object> void putValueField(String nombreCampo, S value) {
		try {
			if (value == null)
				return;
			Object obj = getConfigFields().get(nombreCampo);
			if (obj instanceof TextField) {
				((TextField) obj).setText(validateValue.cast(value, String.class));
			}
		} catch (ValidateValueException e) {
			error(e);
		}
	}
}
