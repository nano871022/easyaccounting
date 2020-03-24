package org.pyt.app.beans.users;

import static org.pyt.common.constants.InsertResourceConstants.CONST_RESOURCE_IMPL_SVC_CONFIG_GENERIC_FIELD;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.glyphfont.FontAwesome.Glyph;
import org.pyt.app.components.PasswordPopupBean;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.DtoUtils;
import org.pyt.common.constants.PermissionConstants;
import org.pyt.common.exceptions.GenericServiceException;
import org.pyt.common.validates.ValidFields;

import com.pyt.service.dto.PersonaDTO;
import com.pyt.service.interfaces.IConfigGenericFieldSvc;
import com.pyt.service.interfaces.IGenericServiceSvc;
import com.pyt.service.interfaces.IUsersSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.AGenericInterfacesFieldBean;
import co.com.japl.ea.common.button.apifluid.ButtonsImpl;
import co.com.japl.ea.dto.system.GroupUsersDTO;
import co.com.japl.ea.dto.system.UsuarioDTO;
import co.com.japl.ea.utls.LoginUtil;
import co.com.japl.ea.utls.PermissionUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

@FXMLFile(file = "user.fxml", path = "view/users")
public class UserBean extends AGenericInterfacesFieldBean<UsuarioDTO> {
	@Inject(resource = "com.pyt.service.implement.UserSvc")
	private IUsersSvc usersSvc;
	@Inject
	private IGenericServiceSvc<GroupUsersDTO> groupUserSvc;
	@Inject
	private IGenericServiceSvc<PersonaDTO> personSvc;
	@FXML
	private GridPane gridPane;
	@FXML
	private Label lblTitle;
	@FXML
	private Button btnAdd;
	public static final String CONST_FIELD_NAME_USERS_NAME = "nombre";
	public static final String CONST_FIELD_NAME_USERS_PASSWORD = "password";
	public static final String CONST_FIELD_NAME_USERS_STATE = "state";
	public static final String CONST_FIELD_NAME_USERS_PERSON = "person";
	public static final String CONST_FIELD_NAME_USERS_GROUP_USER = "grupoUser";
	public static final String CONST_FIELD_NAME_USERS_INIT_DATE = "fechaInicio";
	@Inject(resource = CONST_RESOURCE_IMPL_SVC_CONFIG_GENERIC_FIELD)
	private IConfigGenericFieldSvc configGenericSvc;
	private MultiValuedMap<String, Object> choiceBox;
	@FXML
	private HBox buttons;

	@FXML
	public void initialize() {
		try {
			choiceBox = new ArrayListValuedHashMap<>();
			registro = new UsuarioDTO();
			setClazz(UsuarioDTO.class);
			fields = configGenericSvc.getFieldToFields(this.getClass(), UsuarioDTO.class);
			loadChoiceBox();
			loadFields(TypeGeneric.FIELD);
			visibleButtons();
			ButtonsImpl.Stream(HBox.class).setLayout(buttons).setName("fxml.btn.save").action(this::add)
					.icon(Glyph.SAVE).isVisible(save).setName("fxml.btn.edit").action(this::add).icon(Glyph.EDIT)
					.isVisible(edit).setName("fxml.btn.cancel").action(this::cancel).build();
		} catch (Exception e) {
			error(e);
		}
	}

	private void loadChoiceBox() {
		try {
			groupUserSvc.getAll(new GroupUsersDTO()).forEach(group -> choiceBox.put("grupoUser", group));
			personSvc.getAll(new PersonaDTO()).forEach(person -> choiceBox.put("person", person));
		} catch (GenericServiceException e) {
			logger.logger(e);
		}
	}

	public final void load() {
		registro = new UsuarioDTO();
		loadFields(TypeGeneric.FIELD);
		btnAdd.setText("fxml.btn.create");
	}

	public final void load(UsuarioDTO dto) {
		registro = dto;
		visibleButtons();
		loadFields(TypeGeneric.FIELD);
		btnAdd.setText("fxml.btn.update");
	}

	@Override
	public GridPane getGridPane(TypeGeneric typeGeneric) {
		return gridPane;
	}

	public final Boolean valid() {
		Boolean valid = true;

		valid &= getMapFields(TypeGeneric.FIELD).get(CONST_FIELD_NAME_USERS_INIT_DATE).stream().map(row -> ValidFields
				.valid(registro.getFechaIncio(), row, true, null, null, i18n().valueBundle("msn.error.field.empty")))
				.findFirst().orElse(false);

		valid &= getMapFields(TypeGeneric.FIELD).get(CONST_FIELD_NAME_USERS_NAME).stream().map(row -> ValidFields
				.valid(registro.getNombre(), row, true, 1, 100, i18n().valueBundle("msn.error.field.empty")))
				.findFirst().orElse(false);
		valid &= getMapFields(TypeGeneric.FIELD).get(CONST_FIELD_NAME_USERS_PERSON).stream().map(row -> ValidFields
				.valid(registro.getPerson(), row, true, null, null, i18n().valueBundle("msn.error.field.empty")))
				.findFirst().orElse(false);
		valid &= getMapFields(TypeGeneric.FIELD).get(CONST_FIELD_NAME_USERS_STATE).stream().map(row -> ValidFields
				.valid(registro.getState(), row, true, null, null, i18n().valueBundle("msn.error.field.empty")))
				.findFirst().orElse(false);
		valid &= getMapFields(TypeGeneric.FIELD).get(CONST_FIELD_NAME_USERS_GROUP_USER).stream().map(row -> ValidFields
				.valid(registro.getGrupoUser(), row, true, null, null, i18n().valueBundle("msn.error.field.empty")))
				.findFirst().orElse(false);
		return valid;
	}

	public final void add() {
		try {
			if (valid()) {
				if (StringUtils.isNotBlank(registro.getPassword())) {
					controllerPopup(PasswordPopupBean.class).load("#{UserBean.save}",
							i18n().valueBundle("msn.input.confirm.password"));
				} else {
					setSave(null);
				}
			}
		} catch (Exception e) {
			error(e);
		}
	}

	public final void setSave(String value) {
		try {
			if (value != null) {
				if (!value.contentEquals(registro.getPassword())) {
					registro.setPassword(null);
					((TextField) getMapFields(TypeGeneric.FIELD).get("password").stream().findAny().get())
							.setText(null);
					ValidFields.valid(registro.getPassword(),
							getMapFields(TypeGeneric.FIELD).get("password").stream().findAny().get(), true, null, null,
							i18n().valueBundle("err.msn.password.not.equals"));
					alerta(i18n().valueBundle("err.msn.password.not.equals"));
					throw new Exception(i18n().valueBundle("err.msn.password.not.equals").get());
				} else {
					var pass = LoginUtil.encodePassword(registro.getNombre(), registro.getPassword());
					registro.setPassword(pass);
				}
			}
			if (StringUtils.isBlank(registro.getCodigo())) {
				usersSvc.create(registro, getUsuario());
				notificar(i18n().valueBundle("mensaje.user.inserted"));
				((TextField) getMapFields(TypeGeneric.FIELD).get("password").stream().findAny().get()).setText(null);
			} else {
				if (StringUtils.isNotBlank(registro.getPassword())) {
					controllerPopup(PasswordPopupBean.class).load("#{UserBean.saveUpdate}",
							i18n().valueBundle("msn.input.latest.password"));
				} else {
					setSaveUpdate(null);
				}
			}
		} catch (Exception e) {
			error(e);
			((TextField) getMapFields(TypeGeneric.FIELD).get("password").stream().findAny().get()).setText(null);
		}

	}

	public final void setSaveUpdate(String value) {
		try {
			var password = registro.getPassword();
			registro.setPassword(null);
			usersSvc.update(registro, getUsuario());
			if (StringUtils.isNotBlank(value)) {
				if (StringUtils.isNotBlank(password) && StringUtils.isNotBlank(value)) {
					usersSvc.passwordChange(password, password, LoginUtil.encodePassword(registro.getNombre(), value),
							registro);
				}
				notificar(i18n().valueBundle("mensaje.user.updated"));
			}
		} catch (Exception e) {
			error(e);
		}
		((TextField) getMapFields(TypeGeneric.FIELD).get("password").stream().findAny().get()).setText(null);
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
		return choiceBox;
	}

	@Override
	protected void visibleButtons() {
		var save = !DtoUtils.haveCode(registro) && PermissionUtil.INSTANCE()
				.havePerm(PermissionConstants.CONST_PERM_CREATE, ListUsersBean.class, getUsuario().getGrupoUser());
		var edit = DtoUtils.haveCode(registro) && PermissionUtil.INSTANCE()
				.havePerm(PermissionConstants.CONST_PERM_UPDATE, ListUsersBean.class, getUsuario().getGrupoUser());
		this.save.setValue(save);
		this.edit.setValue(edit);
	}

}
