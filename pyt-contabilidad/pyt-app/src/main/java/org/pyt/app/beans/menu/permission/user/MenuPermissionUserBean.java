package org.pyt.app.beans.menu.permission.user;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.UtilControlFieldFX;
import org.pyt.common.validates.ValidFields;

import com.pyt.service.interfaces.IGenericServiceSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.AGenericInterfacesFieldBean;
import co.com.japl.ea.dto.system.MenuPermUsersDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

@FXMLFile(file = "menu_permission_user.fxml", path = "view/menu_permission_user")
public class MenuPermissionUserBean extends AGenericInterfacesFieldBean<MenuPermUsersDTO> {
	@Inject(resource = "com.pyt.service.implement.GenericServiceSvc")
	private IGenericServiceSvc<MenuPermUsersDTO> groupUsersSvc;
	@FXML
	private GridPane gridPane;
	@FXML
	private Label lblTitle;
	public static final String CONST_FIELD_NAME_MPU_MENU = "menu";
	public static final String CONST_FIELD_NAME_MPU_PERMSSION = "perm";
	public static final String CONST_FIELD_NAME_MPU_STATE = "state";

	@FXML
	public void initialize() {
		try {
			registro = new MenuPermUsersDTO();
			setClazz(MenuPermUsersDTO.class);
			configFields();
		} catch (Exception e) {
			error(e);
		}
	}

	public final void load() {
		registro = new MenuPermUsersDTO();
	}

	public final void load(MenuPermUsersDTO dto) {
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
		valid &= ValidFields.valid((TextField) getMapFieldUseds().get(CONST_FIELD_NAME_MPU_MENU), true, 1, 100,
				i18n().valueBundle("msn.error.field.empty"));
		valid &= ValidFields.valid((TextField) getMapFieldUseds().get(CONST_FIELD_NAME_MPU_PERMSSION), true, 1, 2,
				i18n().valueBundle("msn.error.field.empty"));
		valid &= ValidFields.valid((TextField) getMapFieldUseds().get(CONST_FIELD_NAME_MPU_STATE), true, 1, 100,
				i18n().valueBundle("msn.error.field.empty"));
		return valid;
	}

	public final void add() {
		try {
			if (valid()) {
				if (StringUtils.isBlank(registro.getCodigo())) {
					groupUsersSvc.insert(registro, userLogin);
					notificar(i18n().valueBundle("mensaje.user.inserted"));
				} else {
					groupUsersSvc.update(registro, userLogin);
					notificar(i18n().valueBundle("mensaje.user.updated"));
				}
			}
		} catch (Exception e) {
			error(e);
		}
	}

	public final void cancel() {
		getController(ListMenuPermissionUserBean.class);
	}

	@Override
	public Integer countFieldsInRow() {
		return 2;
	}

}
