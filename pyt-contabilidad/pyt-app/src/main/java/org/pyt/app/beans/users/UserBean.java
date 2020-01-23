package org.pyt.app.beans.users;

import static org.pyt.common.constants.InsertResourceConstants.CONST_RESOURCE_IMPL_SVC_CONFIG_GENERIC_FIELD;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.UtilControlFieldFX;
import org.pyt.common.validates.ValidFields;

import com.pyt.service.interfaces.IConfigGenericFieldSvc;
import com.pyt.service.interfaces.IUsersSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.AGenericInterfacesFieldBean;
import co.com.japl.ea.dto.system.UsuarioDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

@FXMLFile(file = "user.fxml", path = "view/users")
public class UserBean extends AGenericInterfacesFieldBean<UsuarioDTO> {
	@Inject(resource = "com.pyt.service.implement.UserSvc")
	private IUsersSvc usersSvc;
	@FXML
	private GridPane gridPane;
	@FXML
	private Label lblTitle;
	public static final String CONST_FIELD_NAME_USERS_NAME = "nombre";
	public static final String CONST_FIELD_NAME_USERS_PASSWORD = "password";
	public static final String CONST_FIELD_NAME_USERS_STATE = "state";
	public static final String CONST_FIELD_NAME_USERS_PERSON = "person";
	public static final String CONST_FIELD_NAME_USERS_GROUP_USER = "grupoUser";
	public static final String CONST_FIELD_NAME_USERS_INIT_DATE = "fechaInicio";
	@Inject(resource = CONST_RESOURCE_IMPL_SVC_CONFIG_GENERIC_FIELD)
	private IConfigGenericFieldSvc configGenericSvc;

	@FXML
	public void initialize() {
		try {
			registro = new UsuarioDTO();
			setClazz(UsuarioDTO.class);
			fields = configGenericSvc.getFieldToFields(this.getClass(), UsuarioDTO.class);
			loadFields(TypeGeneric.FIELD);
		} catch (Exception e) {
			error(e);
		}
	}

	public final void load() {
		registro = new UsuarioDTO();
	}

	public final void load(UsuarioDTO dto) {
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
		valid &= ValidFields.valid((TextField) getMapFields(TypeGeneric.FIELD).get(CONST_FIELD_NAME_USERS_GROUP_USER),
				true, 1, 100, i18n().valueBundle("msn.error.field.empty"));
		valid &= ValidFields.valid((TextField) getMapFields(TypeGeneric.FIELD).get(CONST_FIELD_NAME_USERS_INIT_DATE),
				true, 1, 2, i18n().valueBundle("msn.error.field.empty"));
		valid &= ValidFields.valid((TextField) getMapFields(TypeGeneric.FIELD).get(CONST_FIELD_NAME_USERS_NAME), true,
				1, 100, i18n().valueBundle("msn.error.field.empty"));
		valid &= ValidFields.valid((TextField) getMapFields(TypeGeneric.FIELD).get(CONST_FIELD_NAME_USERS_PASSWORD),
				true, 1, 100, i18n().valueBundle("msn.error.field.empty"));
		valid &= ValidFields.valid((TextField) getMapFields(TypeGeneric.FIELD).get(CONST_FIELD_NAME_USERS_PERSON), true,
				1, 100, i18n().valueBundle("msn.error.field.empty"));
		valid &= ValidFields.valid((TextField) getMapFields(TypeGeneric.FIELD).get(CONST_FIELD_NAME_USERS_STATE), true,
				1, 100, i18n().valueBundle("msn.error.field.empty"));
		return valid;
	}

	public final void add() {
		try {
			if (valid()) {
				if (StringUtils.isBlank(registro.getCodigo())) {
					usersSvc.create(registro, getUsuario());
					notificar(i18n().valueBundle("mensaje.user.inserted"));
				} else {
					usersSvc.update(registro, getUsuario());
					notificar(i18n().valueBundle("mensaje.user.updated"));
				}
			}
		} catch (Exception e) {
			error(e);
		}
	}

	public final void cancel() {
		getController(ListUsersBean.class);
	}

	@Override
	public Integer getMaxColumns(TypeGeneric typeGeneric) {
		return 2;
	}

	@Override
	public MultiValuedMap<String, Object> getMapListToChoiceBox() {
		// TODO Auto-generated method stub
		return null;
	}

}
