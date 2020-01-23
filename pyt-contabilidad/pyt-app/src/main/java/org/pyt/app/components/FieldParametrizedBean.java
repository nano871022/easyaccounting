package org.pyt.app.components;

import org.pyt.common.abstracts.ADto;
import org.pyt.common.constants.StylesPrincipalConstant;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.AGenericInterfacesReflectionBean;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

@FXMLFile(path = "/", file = ".", nombreVentana = "FieldParametrizedBean")
public class FieldParametrizedBean<T extends ADto> extends AGenericInterfacesReflectionBean<T> {
	private Boolean isPopup;

	public FieldParametrizedBean(Class<T> clazz) throws Exception {
		super(clazz);
	}

	@Override
	@FXML
	public void initialize() {
		isPopup = false;

	}

	/**
	 * Se le indica en el momento de la carga a quien le debe retornar al
	 * seleccionar un registro
	 * 
	 * @param caller {@link String}
	 */
	public final void load(String caller) throws Exception {
		this.caller = caller;
		loadColumns(StylesPrincipalConstant.CONST_TABLE_CUSTOM);
		loadFields(TypeGeneric.FILTER, StylesPrincipalConstant.CONST_GRID_STANDARD);
	}

	public final Boolean isPopup() {
		return isPopup;
	}

	@Override
	public Integer getMaxColumns(TypeGeneric typeGeneric) {
		return 2;
	}

	@Override
	public void selectedRow(MouseEvent eventHandler) {
		// TODO Auto-generated method stub

	}

	@Override
	public T applyFilterToDataTableSearch() {
		return registro;
	}

}
