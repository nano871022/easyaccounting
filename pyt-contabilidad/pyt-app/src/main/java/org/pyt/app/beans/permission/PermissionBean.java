package org.pyt.app.beans.permission;

import static org.pyt.common.constants.InsertResourceConstants.CONST_RESOURCE_IMPL_SVC_CONFIG_GENERIC_FIELD;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.Inject;
import org.pyt.common.validates.ValidFields;

import com.pyt.service.interfaces.IConfigGenericFieldSvc;
import com.pyt.service.interfaces.IGenericServiceSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.AGenericInterfacesFieldBean;
import co.com.japl.ea.dto.system.PermissionDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

@FXMLFile(file = "permission.fxml", path = "view/permission")
public class PermissionBean extends AGenericInterfacesFieldBean<PermissionDTO> {
	@Inject(resource = "com.pyt.service.implement.GenericServiceSvc")
	private IGenericServiceSvc<PermissionDTO> groupUsersSvc;
	@FXML
	private GridPane gridPane;
	@FXML
	private Label lblTitle;
	public static final String CONST_FIELD_NAME_GROUP_USERS_NAME = "name";
	public static final String CONST_FIELD_NAME_GROUP_USERS_STATE = "state";
	public static final String CONST_FIELD_NAME_GROUP_USERS_ACTION = "action";
	@Inject(resource = CONST_RESOURCE_IMPL_SVC_CONFIG_GENERIC_FIELD)
	private IConfigGenericFieldSvc configGenericSvc;

	@FXML
	public void initialize() {
		try {
			registro = new PermissionDTO();
			setClazz(PermissionDTO.class);
			fields = configGenericSvc.getFieldToFields(this.getClass(), PermissionDTO.class);
			loadFields(TypeGeneric.FIELD);
		} catch (Exception e) {
			error(e);
		}
	}

	public final void load() {
		registro = new PermissionDTO();
	}

	public final void load(PermissionDTO dto) {
		registro = dto;
		loadFields(TypeGeneric.FIELD);
	}

	@Override
	public GridPane getGridPane(TypeGeneric typeGeneric) {
		return gridPane;
	}

	public final Boolean valid() {
		Boolean valid = true;
		var name = getMapFields(TypeGeneric.FIELD).get(CONST_FIELD_NAME_GROUP_USERS_NAME).stream().findFirst().get();
		var state = getMapFields(TypeGeneric.FIELD).get(CONST_FIELD_NAME_GROUP_USERS_STATE).stream().findFirst().get();
		var action = getMapFields(TypeGeneric.FIELD).get(CONST_FIELD_NAME_GROUP_USERS_ACTION).stream().findFirst()
				.get();
		valid &= ValidFields.valid((TextField) name, true, 1, 100, i18n().valueBundle("msn.error.field.empty"));
		valid &= ValidFields.valid((TextField) state, true, 1, 2, i18n().valueBundle("msn.error.field.empty"));
		valid &= ValidFields.valid((TextField) action, true, 1, 100, i18n().valueBundle("msn.error.field.empty"));
		return valid;
	}

	public final void add() {
		try {
			if (valid()) {
				if (StringUtils.isBlank(registro.getCodigo())) {
					groupUsersSvc.insert(registro, getUsuario());
					notificar(i18n().valueBundle("mensaje.group.user.inserted"));
				} else {
					groupUsersSvc.update(registro, getUsuario());
					notificar(i18n().valueBundle("mensaje.group.user.updated"));
				}
			}
		} catch (Exception e) {
			error(e);
		}
	}

	public final void cancel() {
		getController(ListPermissionBean.class);
	}

	@Override
	public Integer getMaxColumns(TypeGeneric typeGeneric) {
		return 2;
	}

}
