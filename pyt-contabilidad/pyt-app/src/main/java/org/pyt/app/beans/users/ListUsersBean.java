package org.pyt.app.beans.users;

import static org.pyt.common.constants.InsertResourceConstants.CONST_RESOURCE_IMPL_SVC_CONFIG_GENERIC_FIELD;

import java.util.List;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.controlsfx.glyphfont.FontAwesome.Glyph;
import org.pyt.app.components.ConfirmPopupBean;
import org.pyt.common.annotations.Inject;
import org.pyt.common.constants.LanguageConstant;
import org.pyt.common.constants.PermissionConstants;
import org.pyt.common.exceptions.GenericServiceException;

import com.pyt.service.dto.PersonaDTO;
import com.pyt.service.interfaces.IConfigGenericFieldSvc;
import com.pyt.service.interfaces.IGenericServiceSvc;
import com.pyt.service.interfaces.IUsersSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.AGenericInterfacesBean;
import co.com.japl.ea.common.button.apifluid.ButtonsImpl;
import co.com.japl.ea.dto.system.ConfigGenericFieldDTO;
import co.com.japl.ea.dto.system.GroupUsersDTO;
import co.com.japl.ea.dto.system.UsuarioDTO;
import co.com.japl.ea.utls.PermissionUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

@FXMLFile(file = "list_users.fxml", path = "view/users")
public class ListUsersBean extends AGenericInterfacesBean<UsuarioDTO> {

	@Inject(resource = "com.pyt.service.implement.UserSvc")
	private IUsersSvc usersSvc;
	@Inject
	private IGenericServiceSvc<GroupUsersDTO> grupoUserSvc;
	@Inject
	private IGenericServiceSvc<PersonaDTO> personSvc;
	@FXML
	private TableView<UsuarioDTO> tableGeneric;
	@FXML
	private HBox filterGeneric;
	@FXML
	private Label lblTitle;
	@FXML
	private HBox paginator;
	private GridPane gridPane;
	@Inject(resource = CONST_RESOURCE_IMPL_SVC_CONFIG_GENERIC_FIELD)
	private IConfigGenericFieldSvc configGenericSvc;
	private List<ConfigGenericFieldDTO> listFilters;
	private List<ConfigGenericFieldDTO> listColumns;
	private MultiValuedMap<String, Object> choiceBox;
	@FXML
	private HBox buttons;

	@FXML
	public void initialize() {
		try {
			filtro = new UsuarioDTO();
			lblTitle.setText(i18n().valueBundle("fxml.lbl.title.list.users").get());
			lblTitle.getStyleClass().add("titulo-page");
			lblTitle.setGraphic(
					new org.controlsfx.glyphfont.Glyph("FontAwesome", org.controlsfx.glyphfont.FontAwesome.Glyph.TAGS));
			gridPane = new GridPane();
			gridPane.setHgap(10);
			gridPane.setVgap(10);
			filterGeneric.getChildren().addAll(gridPane);
			listFilters = configGenericSvc.getFieldToFilters(this.getClass(), UsuarioDTO.class);
			listColumns = configGenericSvc.getFieldToColumns(this.getClass(), UsuarioDTO.class);
			loadChoiceBox();
			loadDataModel(paginator, tableGeneric);
			loadFields(TypeGeneric.FILTER);
			loadColumns();
			visibleButtons();
			ButtonsImpl.Stream(HBox.class).setLayout(buttons).setName("fxml.btn.save").action(this::add)
					.icon(Glyph.SAVE).isVisible(save).setName("fxml.btn.edit").action(this::set).icon(Glyph.EDIT)
					.isVisible(edit).setName("fxml.btn.delete").action(this::del).icon(Glyph.REMOVE).isVisible(delete)
					.setName("fxml.btn.view").action(this::set).icon(Glyph.FILE_TEXT).isVisible(view).build();
		} catch (Exception e) {
			error(e);
		}
	}

	private void loadChoiceBox() {
		try {
			choiceBox = new ArrayListValuedHashMap<>();
			grupoUserSvc.getAll(new GroupUsersDTO()).forEach(row -> choiceBox.put("grupoUser", row));
			personSvc.getAll(new PersonaDTO()).forEach(row -> choiceBox.put("person", row));
		} catch (GenericServiceException e) {
			logger.logger(e);
		}
	}

	public final void add() {
		try {
			getController(UserBean.class).load();
		} catch (Exception e) {
			error(e);
		}
	}

	public final void del() {
		try {
			controllerPopup(ConfirmPopupBean.class).load("#{ListUsersBean.delete}",
					i18n().valueBundle(LanguageConstant.LANGUAGE_WARNING_DELETE_ROW));
		} catch (Exception e) {
			error(e);
		}
	}

	public final void setDelete(Boolean valid) {
		try {
			if (valid) {
				var list = dataTable.getSelectedRows();
				if (list != null && list.size() > 0) {
					list.forEach(dto -> {
						try {
							usersSvc.delete(dto, getUsuario());
							alerta(String.format(LanguageConstant.LANGUAGE_SUCCESS_DELETE_USERS_ROW_CODE,
									dto.getCodigo()));
						} catch (Exception e) {
							error(e);
						}
					});
					dataTable.search();
				}
			}
		} catch (Exception e) {
			error(e);
		}
	}

	public final void clickTable() {
		visibleButtons();
	}

	public final void set() {
		try {
			var dto = dataTable.getSelectedRow();
			getController(UserBean.class).load(dto);
		} catch (Exception e) {
			error(e);
		}
	}

	@Override
	public TableView<UsuarioDTO> getTableView() {
		return tableGeneric;
	}

	@Override
	public GridPane getGridPane(TypeGeneric typeGeneric) {
		return gridPane;
	}

	@Override
	public Integer getMaxColumns(TypeGeneric typeGeneric) {
		return 2;
	}

	@Override
	public UsuarioDTO getFilterToTable(UsuarioDTO filter) {
		return filter;
	}

	@Override
	public void selectedRow(MouseEvent eventHandler) {
		visibleButtons();
	}

	@Override
	public List<ConfigGenericFieldDTO> getListGenericsFields(TypeGeneric typeGeneric) {
		switch (typeGeneric) {
		case FILTER:
			return listFilters;
		case COLUMN:
			return listColumns;
		}
		return null;
	}

	@Override
	public MultiValuedMap<String, Object> getMapListToChoiceBox() {
		return choiceBox;
	}

	@Override
	public Class<UsuarioDTO> getClazz() {
		return UsuarioDTO.class;
	}

	@Override
	protected void visibleButtons() {
		var save = PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_CREATE, ListUsersBean.class,
				getUsuario().getGrupoUser());
		var edit = dataTable.isSelected() && PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_UPDATE,
				ListUsersBean.class, getUsuario().getGrupoUser());
		var delete = dataTable.isSelected() && PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_DELETE,
				ListUsersBean.class, getUsuario().getGrupoUser());
		var view = !save && !edit && PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_READ,
				ListUsersBean.class, getUsuario().getGrupoUser());
		this.save.setValue(save);
		this.edit.setValue(edit);
		this.view.setValue(view);
		this.delete.setValue(delete);
	}
}
