package org.pyt.app.beans.menu.permission.user;

import static org.pyt.common.constants.InsertResourceConstants.CONST_RESOURCE_IMPL_SVC_CONFIG_GENERIC_FIELD;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.Inject;
import org.pyt.common.exceptions.GenericServiceException;
import org.pyt.common.validates.ValidFields;

import com.pyt.service.implement.UserSvc;
import com.pyt.service.interfaces.IConfigGenericFieldSvc;
import com.pyt.service.interfaces.IGenericServiceSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.AGenericInterfacesFieldBean;
import co.com.japl.ea.dto.system.GroupUsersDTO;
import co.com.japl.ea.dto.system.MenuDTO;
import co.com.japl.ea.dto.system.MenuPermUsersDTO;
import co.com.japl.ea.dto.system.PermissionDTO;
import co.com.japl.ea.dto.system.UsuarioDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

@FXMLFile(file = "menu_permission_user.fxml", path = "view/menu_permission_user")
public class MenuPermissionUserBean extends AGenericInterfacesFieldBean<MenuPermUsersDTO> {
	@Inject(resource = "com.pyt.service.implement.GenericServiceSvc")
	private IGenericServiceSvc<MenuPermUsersDTO> menuGroupUsersSvc;
	@Inject
	private UserSvc usersSvc;
	@Inject
	private IGenericServiceSvc<MenuDTO> menusSvc;
	@Inject
	private IGenericServiceSvc<GroupUsersDTO> groupUsersSvc;
	@Inject
	private IGenericServiceSvc<PermissionDTO> permissionsSvc;
	@FXML
	private GridPane gridPane;
	@FXML
	private Label lblTitle;
	@FXML
	private Button btnAdd;
	@FXML
	private Button btnCopy;
	public static final String CONST_FIELD_NAME_MPU_MENU = "menu";
	public static final String CONST_FIELD_NAME_MPU_PERMSSION = "perm";
	public static final String CONST_FIELD_NAME_MPU_STATE = "state";
	@Inject(resource = CONST_RESOURCE_IMPL_SVC_CONFIG_GENERIC_FIELD)
	private IConfigGenericFieldSvc configGenericSvc;

	@FXML
	public void initialize() {
		try {
			registro = new MenuPermUsersDTO();
			setClazz(MenuPermUsersDTO.class);
			fields = configGenericSvc.getFieldToFields(this.getClass(), MenuPermUsersDTO.class);
			findChoiceBox();
			loadFields(TypeGeneric.FIELD);
		} catch (Exception e) {
			error(e);
		}
	}

	private void findChoiceBox() {
		try {
			getMapListToChoiceBox();
			usersSvc.getAll(new UsuarioDTO()).forEach(row -> toChoiceBox.put("user", row));
			menusSvc.getAll(new MenuDTO()).forEach(row -> toChoiceBox.put(CONST_FIELD_NAME_MPU_MENU, row));
			groupUsersSvc.getAll(new GroupUsersDTO()).forEach(row -> toChoiceBox.put("groupUsers", row));
			permissionsSvc.getAll(new PermissionDTO())
					.forEach(row -> toChoiceBox.put(CONST_FIELD_NAME_MPU_PERMSSION, row));
		} catch (GenericServiceException e) {
			error(e);
		} catch (Exception e) {
			error(e);
		}
	}

	public final void load() {
		registro = new MenuPermUsersDTO();
		btnAdd.setText(i18n().valueBundle("fxml.form.button.add").get());
		loadFields(TypeGeneric.FIELD);
	}

	public final void load(MenuPermUsersDTO dto) {
		registro = dto;
		btnAdd.setText(i18n().valueBundle("fxml.form.button.update").get());
		loadFields(TypeGeneric.FIELD);
	}

	@Override
	public GridPane getGridPane(TypeGeneric typeGeneric) {
		return gridPane;
	}

	public final Boolean valid() {
		Boolean valid = true;
		valid &= ValidFields.valid(registro.getState(),
				getMapFields(TypeGeneric.FIELD).get(CONST_FIELD_NAME_MPU_STATE).stream().findAny().get(), true, 1, 100,
				i18n().valueBundle("msn.error.field.empty"));
		return valid;
	}

	public final void add() {
		try {
			if (valid()) {
				if (StringUtils.isBlank(registro.getCodigo())) {
					menuGroupUsersSvc.insert(registro, getUsuario());
					notificar(i18n().valueBundle("mensaje.user.inserted"));
				} else {
					menuGroupUsersSvc.update(registro, getUsuario());
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

	public final void copy() {
		registro.setCodigo(null);
		registro.setActualizador(null);
		registro.setFechaActualizacion(null);
		registro.setFechaCreacion(null);
		notificar(i18n().valueBundle("mensaje.perm.menu.user.copy"));
	}

	@Override
	public Integer getMaxColumns(TypeGeneric typeGeneric) {
		return 2;
	}

}
