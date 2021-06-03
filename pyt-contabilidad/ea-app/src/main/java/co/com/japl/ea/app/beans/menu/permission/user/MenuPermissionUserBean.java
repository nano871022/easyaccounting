package co.com.japl.ea.app.beans.menu.permission.user;

import static org.pyt.common.constants.InsertResourceConstants.CONST_RESOURCE_IMPL_SVC_CONFIG_GENERIC_FIELD;

import org.apache.commons.lang3.StringUtils;
import org.controlsfx.glyphfont.FontAwesome.Glyph;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.DtoUtils;
import org.pyt.common.constants.PermissionConstants;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.AGenericInterfacesFieldBean;
import co.com.japl.ea.common.button.apifluid.ButtonsImpl;
import co.com.japl.ea.common.validates.ValidFields;
import co.com.japl.ea.dto.interfaces.IConfigGenericFieldSvc;
import co.com.japl.ea.dto.interfaces.IGenericServiceSvc;
import co.com.japl.ea.dto.system.GroupUsersDTO;
import co.com.japl.ea.dto.system.MenuDTO;
import co.com.japl.ea.dto.system.MenuPermUsersDTO;
import co.com.japl.ea.dto.system.PermissionDTO;
import co.com.japl.ea.dto.system.UsuarioDTO;
import co.com.japl.ea.exceptions.GenericServiceException;
import co.com.japl.ea.service.implement.UserSvc;
import co.com.japl.ea.utls.PermissionUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

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
	private Button btnCopy;
	public static final String CONST_FIELD_NAME_MPU_MENU = "menu";
	public static final String CONST_FIELD_NAME_MPU_PERMSSION = "perm";
	public static final String CONST_FIELD_NAME_MPU_STATE = "state";
	@Inject(resource = CONST_RESOURCE_IMPL_SVC_CONFIG_GENERIC_FIELD)
	private IConfigGenericFieldSvc configGenericSvc;
	@FXML
	private HBox buttons;

	@FXML
	public void initialize() {
		try {
			registro = new MenuPermUsersDTO();
			setClazz(MenuPermUsersDTO.class);
			fields = configGenericSvc.getFieldToFields(this.getClass(), MenuPermUsersDTO.class);
			findChoiceBox();
			loadFields(TypeGeneric.FIELD);
			visibleButtons();
			ButtonsImpl.Stream(HBox.class).setLayout(buttons).setName("fxml.btn.save").action(this::add)
					.icon(Glyph.SAVE).isVisible(save).setName("fxml.btn.edit").action(this::add).icon(Glyph.EDIT)
					.isVisible(edit).setName("fxml.btn.copy").action(this::copy).icon(Glyph.COPY).isVisible(edit)
					.setName("fxml.btn.cancel").action(this::cancel).build();
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
		btnCopy.setVisible(false);
		loadFields(TypeGeneric.FIELD);
	}

	public final void load(MenuPermUsersDTO dto) {
		registro = dto;
		visibleButtons();
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
					btnCopy.setVisible(true);
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
		btnCopy.setVisible(false);
		notificar(i18n().valueBundle("mensaje.perm.menu.user.copy"));
	}

	@Override
	public Integer getMaxColumns(TypeGeneric typeGeneric) {
		return 2;
	}

	@Override
	protected void visibleButtons() {
		var save = !DtoUtils.haveCode(registro) && PermissionUtil.INSTANCE().havePerm(
				PermissionConstants.CONST_PERM_CREATE, ListMenuPermissionUserBean.class, getUsuario().getGrupoUser());
		var edit = DtoUtils.haveCode(registro) && PermissionUtil.INSTANCE().havePerm(
				PermissionConstants.CONST_PERM_UPDATE, ListMenuPermissionUserBean.class, getUsuario().getGrupoUser());
		this.save.setValue(save);
		this.edit.setValue(edit);
	}

}
