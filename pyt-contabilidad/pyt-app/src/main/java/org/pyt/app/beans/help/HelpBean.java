package org.pyt.app.beans.help;

import static org.pyt.common.constants.InsertResourceConstants.CONST_RESOURCE_IMPL_SVC_CONFIG_GENERIC_FIELD;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.UtilControlFieldFX;
import org.pyt.common.validates.ValidFields;

import com.pyt.service.interfaces.IConfigGenericFieldSvc;
import com.pyt.service.interfaces.IGenericServiceSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.AGenericInterfacesFieldBean;
import co.com.japl.ea.dto.system.HelpDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

@FXMLFile(file = "help.fxml", path = "view/help")
public class HelpBean extends AGenericInterfacesFieldBean<HelpDTO> {
	@Inject(resource = "com.pyt.service.implement.GenericServiceSvc")
	private IGenericServiceSvc<HelpDTO> helpsSvc;
	@FXML
	private GridPane gridPane;
	@FXML
	private Label lblTitle;
	public static final String CONST_FIELD_NAME_MENUS_URL = "url";
	public static final String CONST_FIELD_NAME_MENUS_CLASS_PATH = "classPath";
	public static final String CONST_FIELD_NAME_MENUS_STATE = "state";
	@Inject(resource = CONST_RESOURCE_IMPL_SVC_CONFIG_GENERIC_FIELD)
	private IConfigGenericFieldSvc configGenericSvc;

	@FXML
	public void initialize() {
		try {
			registro = new HelpDTO();
			setClazz(HelpDTO.class);
			fields = configGenericSvc.getFieldToFields(this.getClass(), HelpDTO.class);
			loadFields(TypeGeneric.FIELD);
		} catch (Exception e) {
			error(e);
		}
	}

	public final void load() {
		registro = new HelpDTO();
	}

	public final void load(HelpDTO dto) {
		registro = dto;
		var util = new UtilControlFieldFX();
		util.loadValuesInFxml(dto, gridPane);
	}

	@Override
	public GridPane getGridPane(TypeGeneric typeGeneric) {
		return gridPane;
	}

	public final Boolean valid() {
		Boolean valid = true;
		valid &= ValidFields.valid((TextField) getMapFields(TypeGeneric.FIELD).get(CONST_FIELD_NAME_MENUS_URL), true, 1,
				100, i18n().valueBundle("msn.error.field.empty"));
		valid &= ValidFields.valid((TextField) getMapFields(TypeGeneric.FIELD).get(CONST_FIELD_NAME_MENUS_CLASS_PATH),
				true, 1, 100, i18n().valueBundle("msn.error.field.empty"));
		valid &= ValidFields.valid((TextField) getMapFields(TypeGeneric.FIELD).get(CONST_FIELD_NAME_MENUS_STATE), true,
				1, 2, i18n().valueBundle("msn.error.field.empty"));
		return valid;
	}

	public final void add() {
		try {
			if (valid()) {
				if (StringUtils.isBlank(registro.getCodigo())) {
					helpsSvc.insert(registro, getUsuario());
					notificar(i18n().valueBundle("mensaje.help.inserted"));
				} else {
					helpsSvc.update(registro, getUsuario());
					notificar(i18n().valueBundle("mensaje.help.updated"));
				}
			}
		} catch (Exception e) {
			error(e);
		}
	}

	public final void cancel() {
		getController(ListHelpBean.class);
	}

	@Override
	public Integer getMaxColumns(TypeGeneric typeGeneric) {
		return 2;
	}

	@Override
	public MultiValuedMap<String, Object> getMapListToChoiceBox() {
		return null;
	}

}
