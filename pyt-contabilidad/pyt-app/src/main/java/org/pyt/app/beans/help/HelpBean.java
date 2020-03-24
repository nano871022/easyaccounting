package org.pyt.app.beans.help;

import static org.pyt.common.constants.InsertResourceConstants.CONST_RESOURCE_IMPL_SVC_CONFIG_GENERIC_FIELD;

import org.apache.commons.lang3.StringUtils;
import org.controlsfx.glyphfont.FontAwesome.Glyph;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.DtoUtils;
import org.pyt.common.constants.PermissionConstants;
import org.pyt.common.validates.ValidFields;

import com.pyt.service.interfaces.IConfigGenericFieldSvc;
import com.pyt.service.interfaces.IGenericServiceSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.AGenericInterfacesFieldBean;
import co.com.japl.ea.common.button.apifluid.ButtonsImpl;
import co.com.japl.ea.dto.system.HelpDTO;
import co.com.japl.ea.utls.PermissionUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

@FXMLFile(file = "help.fxml", path = "view/help")
public class HelpBean extends AGenericInterfacesFieldBean<HelpDTO> {
	@Inject(resource = "com.pyt.service.implement.GenericServiceSvc")
	private IGenericServiceSvc<HelpDTO> helpsSvc;
	@FXML
	private GridPane gridPane;
	@FXML
	private Label lblTitle;
	public static final String CONST_FIELD_NAME_MENUS_STATE = "state";
	@Inject(resource = CONST_RESOURCE_IMPL_SVC_CONFIG_GENERIC_FIELD)
	private IConfigGenericFieldSvc configGenericSvc;
	@FXML
	private HBox buttons;

	@FXML
	public void initialize() {
		try {
			registro = new HelpDTO();
			setClazz(HelpDTO.class);
			fields = configGenericSvc.getFieldToFields(this.getClass(), HelpDTO.class);
			visibleButtons();
			loadFields(TypeGeneric.FIELD);
			ButtonsImpl.Stream(HBox.class).setLayout(buttons).setName("fxml.btn.save").action(this::add)
					.icon(Glyph.SAVE).isVisible(save).setName("fxml.btn.edit").action(this::add).icon(Glyph.SAVE)
					.isVisible(edit).setName("fxml.btn.cancel").action(this::cancel).build();
		} catch (Exception e) {
			error(e);
		}
	}

	public final void load() {
		registro = new HelpDTO();
	}

	public final void load(HelpDTO dto) {
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

		valid &= ValidFields.valid(registro.getClassPathBean(),
				getMapFields(TypeGeneric.FIELD).get("classPathBean").stream().findFirst().get(), true, 1, 300,
				i18n().valueBundle("msn.error.field.empty"));
		valid &= ValidFields.valid(registro.getState(),
				getMapFields(TypeGeneric.FIELD).get(CONST_FIELD_NAME_MENUS_STATE).stream().findFirst().get(), true, 1,
				2, i18n().valueBundle("msn.error.field.empty"));
		valid &= ValidFields.valid(validClass(registro.getClassPathBean()),
				getMapFields(TypeGeneric.FIELD).get("classPathBean").stream().findFirst().get(), true, null, null,
				i18n().valueBundle("msn.error.field.empty"));
		return valid;
	}

	private final boolean validClass(String clazz) {
		try {
			if (StringUtils.isNotBlank(clazz)) {
				Class.forName(clazz);
			}
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
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
	protected void visibleButtons() {
		var save = !DtoUtils.haveCode(registro) && PermissionUtil.INSTANCE()
				.havePerm(PermissionConstants.CONST_PERM_CREATE, ListHelpBean.class, getUsuario().getGrupoUser());
		var edit = DtoUtils.haveCode(registro) && PermissionUtil.INSTANCE()
				.havePerm(PermissionConstants.CONST_PERM_UPDATE, ListHelpBean.class, getUsuario().getGrupoUser());
		this.save.setValue(save);
		this.edit.setValue(edit);
	}
}
