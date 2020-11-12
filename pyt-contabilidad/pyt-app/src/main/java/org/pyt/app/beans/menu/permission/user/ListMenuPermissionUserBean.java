package org.pyt.app.beans.menu.permission.user;

import static org.pyt.common.constants.InsertResourceConstants.CONST_RESOURCE_IMPL_SVC_CONFIG_GENERIC_FIELD;

import java.util.List;

import org.controlsfx.glyphfont.FontAwesome.Glyph;
import org.pyt.app.components.ConfirmPopupBean;
import org.pyt.common.annotations.Inject;
import org.pyt.common.constants.LanguageConstant;
import org.pyt.common.constants.PermissionConstants;
import org.pyt.common.exceptions.GenericServiceException;

import com.pyt.service.implement.UserSvc;
import com.pyt.service.interfaces.IConfigGenericFieldSvc;
import com.pyt.service.interfaces.IGenericServiceSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.AGenericInterfacesBean;
import co.com.japl.ea.common.button.apifluid.ButtonsImpl;
import co.com.japl.ea.dto.system.ConfigGenericFieldDTO;
import co.com.japl.ea.dto.system.GroupUsersDTO;
import co.com.japl.ea.dto.system.MenuDTO;
import co.com.japl.ea.dto.system.MenuPermUsersDTO;
import co.com.japl.ea.dto.system.PermissionDTO;
import co.com.japl.ea.dto.system.UsuarioDTO;
import co.com.japl.ea.utls.PermissionUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

@FXMLFile(file = "list_menu_permission_user.fxml", path = "view/menu_permission_user")
public class ListMenuPermissionUserBean extends AGenericInterfacesBean<MenuPermUsersDTO> {

	@Inject(resource = "com.pyt.service.implement.GenericServiceSvc")
	private IGenericServiceSvc<MenuPermUsersDTO> menuPermUsersSvc;
	@Inject
	private UserSvc usersSvc;
	@Inject
	private IGenericServiceSvc<MenuDTO> menusSvc;
	@Inject
	private IGenericServiceSvc<GroupUsersDTO> groupUsersSvc;
	@Inject
	private IGenericServiceSvc<PermissionDTO> permissionsSvc;
	@FXML
	private TableView<MenuPermUsersDTO> tableGeneric;
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
	@FXML
	private HBox buttons;

	@FXML
	public void initialize() {
		try {
			filtro = new MenuPermUsersDTO();
			lblTitle.setText(i18n().valueBundle("fxml.lbl.title.list.menu.permission.user").get());
			lblTitle.getStyleClass().add("titulo-page");
			lblTitle.setGraphic(
					new org.controlsfx.glyphfont.Glyph("FontAwesome", org.controlsfx.glyphfont.FontAwesome.Glyph.TAGS));
			gridPane = new GridPane();
			gridPane.setHgap(10);
			gridPane.setVgap(10);
			filterGeneric.getChildren().addAll(gridPane);
			listColumns = configGenericSvc.getFieldToColumns(this.getClass(), MenuPermUsersDTO.class);
			listFilters = configGenericSvc.getFieldToFilters(this.getClass(), MenuPermUsersDTO.class);
			findChoiceBox();
			loadDataModel(paginator, tableGeneric);
			loadFields(TypeGeneric.FILTER);
			loadColumns();
			visibleButtons();
			ButtonsImpl.Stream(HBox.class).setLayout(buttons).setName("fxml.btn.add").action(this::add).icon(Glyph.SAVE)
					.isVisible(save).setName("fxml.btn.edit").action(this::set).icon(Glyph.EDIT).isVisible(edit)
					.setName("fxml.btn.delete").action(this::del).icon(Glyph.REMOVE).isVisible(delete)
					.setName("fxml.btn.view").action(this::set).icon(Glyph.FILE_TEXT).isVisible(view).build();
		} catch (Exception e) {
			error(e);
		}
	}

	private void findChoiceBox() {
		try {
			getMapListToChoiceBox();
			usersSvc.getAll(new UsuarioDTO()).forEach(row -> toChoiceBox.put("user", row));
			menusSvc.getAll(new MenuDTO()).forEach(row -> toChoiceBox.put("menu", row));
			groupUsersSvc.getAll(new GroupUsersDTO()).forEach(row -> toChoiceBox.put("groupUsers", row));
			permissionsSvc.getAll(new PermissionDTO()).forEach(row -> toChoiceBox.put("perm", row));
		} catch (GenericServiceException e) {
			error(e);
		} catch (Exception e) {
			error(e);
		}
	}

	public final void add() {
		try {
			getController(MenuPermissionUserBean.class).load();
		} catch (Exception e) {
			error(e);
		}
	}

	public final void del() {
		try {
			controllerPopup(ConfirmPopupBean.class).load("#{ListMenuPermissionUserBean.delete}",
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
							menuPermUsersSvc.delete(dto, getUsuario());
							alerta(String.format(
									LanguageConstant.LANGUAGE_SUCCESS_DELETE_MENU_PERMISSION_USERS_ROW_CODE,
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
			getController(MenuPermissionUserBean.class).load(dto);
		} catch (Exception e) {
			error(e);
		}
	}

	@Override
	public TableView<MenuPermUsersDTO> getTableView() {
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
	public MenuPermUsersDTO getFilterToTable(MenuPermUsersDTO filter) {
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
	public Class<MenuPermUsersDTO> getClazz() {
		return MenuPermUsersDTO.class;
	}

	@Override
	protected void visibleButtons() {
		var save = PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_CREATE,
				ListMenuPermissionUserBean.class, getUsuario().getGrupoUser());
		var edit = dataTable.isSelected() && PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_UPDATE,
				ListMenuPermissionUserBean.class, getUsuario().getGrupoUser());
		var delete = dataTable.isSelected() && PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_DELETE,
				ListMenuPermissionUserBean.class, getUsuario().getGrupoUser());
		var view = !save && !edit && PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_READ,
				ListMenuPermissionUserBean.class, getUsuario().getGrupoUser());
		this.save.setValue(save);
		this.edit.setValue(edit);
		this.delete.setValue(delete);
		this.view.setValue(view);
	}
}
