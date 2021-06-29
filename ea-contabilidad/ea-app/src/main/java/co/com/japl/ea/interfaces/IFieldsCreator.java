package co.com.japl.ea.interfaces;

import org.pyt.common.common.OptI18n;

import co.com.japl.ea.common.abstracts.ADto;
import co.com.japl.ea.dto.dto.ParametroDTO;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;

/**
 * Se encarga de realziar la creacion del campo segun el tipo de objeto creado
 * 
 * @author Alejandro Parra
 * @since 21/10/2019
 */
public interface IFieldsCreator {
	/***
	 * Se ecanrga de cargar el campo al cual se le realiza el procesamiento para ser
	 * cargado
	 * 
	 * @param field <@link ADto}
	 */
	<D extends ADto> void setFieldGeneric(D field);

	/**
	 * Se encarga de obtener el nombre de la etiqueta
	 * 
	 * @return {@link String}
	 */
	OptI18n getLabelText();

	/**
	 * Se encarga de obtener el nombre del campo
	 * 
	 * @return {@link String}
	 */
	String getNameField();

	/**
	 * Obtiene el tool tip para poner un mensaje en la etiqueta
	 * 
	 * @return {@link String}
	 */
	String getToolTip();

	/**
	 * Se encarga de crear el campo segun el generico suministrado.
	 * 
	 * @return {@link Node}
	 */
	Node create();

	/**
	 * Se encarga de obtener el nombre del campo dentro del {@link ADto dto} de la
	 * lista de campos de {@link ComboBox combo box} el cual sera mostrado en
	 * pantalla.
	 * 
	 * @return {@link String}
	 */
	String getNameFieldToShowInComboBox();

	ParametroDTO getParametroDto();

	String getValueDefault();

	Boolean isVisible();

	Boolean hasValueDefault();

	Boolean isRequired();

	Integer getOrder();

	String getFormat();
}
