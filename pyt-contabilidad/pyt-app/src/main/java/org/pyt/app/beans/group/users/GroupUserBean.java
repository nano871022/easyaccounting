package org.pyt.app.beans.group.users;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.UtilControlFieldFX;
import org.pyt.common.validates.ValidFields;

import com.pyt.service.interfaces.IGenericServiceSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.AGenericInterfacesFieldBean;
import co.com.japl.ea.dto.system.GroupUsersDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

@FXMLFile(file = "group_user.fxml", path = "view/group_users")
public class GroupUserBean extends AGenericInterfacesFieldBean<GroupUsersDTO> {
	@Inject(resource = "com.pyt.service.implement.GenericServiceSvc")
	private IGenericServiceSvc<GroupUsersDTO> groupUsersSvc;
	@FXML
	private GridPane gridPane;
	@FXML
	private Label lblTitle;
	public static final String CONST_FIELD_NAME_GROUP_USERS_NAME = "name";
	public static final String CONST_FIELD_NAME_GROUP_USERS_STATE = "state";

	@FXML
	public void initialize() {
		try {
			registro = new GroupUsersDTO();
			setClazz(GroupUsersDTO.class);
			configFields();
		} catch (Exception e) {
			error(e);
		}
	}

	public final void load() {
		registro = new GroupUsersDTO();
	}

	public final void load(GroupUsersDTO dto) {
		registro = dto;
		var util = new UtilControlFieldFX();
		util.loadValuesInFxml(dto, gridPane);
	}

	@Override
	public GridPane getGridPaneField() {
		return gridPane;
	}

	public final Boolean valid() {
		Boolean valid = true;
		valid &= ValidFields.valid((TextField) getMapFieldUseds().get(CONST_FIELD_NAME_GROUP_USERS_NAME), true, 1, 100,
				i18n().valueBundle("msn.error.field.empty"));
		valid &= ValidFields.valid((TextField) getMapFieldUseds().get(CONST_FIELD_NAME_GROUP_USERS_STATE), true, 1, 100,
				i18n().valueBundle("msn.error.field.empty"));
		return valid;
	}

	public final void add() {
		try {
			if (valid()) {
				if (StringUtils.isBlank(registro.getCodigo())) {
					groupUsersSvc.insert(registro, userLogin);
					notificar(i18n().valueBundle("mensaje.group.user.inserted"));
				} else {
					groupUsersSvc.update(registro, userLogin);
					notificar(i18n().valueBundle("mensaje.group.user.updated"));
				}
			}
		} catch (Exception e) {
			error(e);
		}
	}

	public final void cancel() {
		getController(ListGroupUsersBean.class);
	}

	@Override
	public Integer countFieldsInRow() {
		return 2;
	}

}