package org.pyt.app.beans.menu;

import static co.com.japl.ea.interfaces.IGenericCommons.TypeGeneric.FILTER;
import static org.pyt.common.constants.FxmlBeanConstant.CONST_LIST_MENU_FXML;
import static org.pyt.common.constants.FxmlBeanConstant.CONST_PATH_MENU;
import static org.pyt.common.constants.InsertResourceConstants.CONST_RESOURCE_IMPL_SVC_CONFIG_GENERIC_FIELD;
import static org.pyt.common.constants.InsertResourceConstants.CONST_RESOURCE_IMPL_SVC_GENERIC_SERVICE;
import static org.pyt.common.constants.StylesPrincipalConstant.CONST_GRID_STANDARD;
import static org.pyt.common.constants.StylesPrincipalConstant.CONST_TABLE_CUSTOM;
import static org.pyt.common.constants.languages.Menu.CONST_FXML_LABEL_TITLE_LIST_MENUS;

import java.util.List;

import org.controlsfx.glyphfont.FontAwesome.Glyph;
import org.pyt.app.components.ConfirmPopupBean;
import org.pyt.common.annotations.Inject;
import org.pyt.common.constants.LanguageConstant;
import org.pyt.common.constants.PermissionConstants;

import com.pyt.service.interfaces.IConfigGenericFieldSvc;
import com.pyt.service.interfaces.IGenericServiceSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.AGenericInterfacesBean;
import co.com.japl.ea.common.button.apifluid.ButtonsImpl;
import co.com.japl.ea.dto.system.ConfigGenericFieldDTO;
import co.com.japl.ea.dto.system.MenuDTO;
import co.com.japl.ea.utls.PermissionUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;;

@FXMLFile(file = CONST_LIST_MENU_FXML, path = CONST_PATH_MENU)
public class ListMenusBean extends AGenericInterfacesBean<MenuDTO> {

	@Inject(resource = CONST_RESOURCE_IMPL_SVC_GENERIC_SERVICE)
	private IGenericServiceSvc<MenuDTO> menusSvc;
	@FXML
	private TableView<MenuDTO> tableGeneric;
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
			filtro = new MenuDTO();
			lblTitle.setText(i18n().valueBundle(CONST_FXML_LABEL_TITLE_LIST_MENUS).get());
			gridPane = new GridPane();
			gridPane.setHgap(10);
			gridPane.setVgap(10);
			filterGeneric.getChildren().addAll(gridPane);
			listColumns = configGenericSvc.getFieldToColumns(this.getClass(), MenuDTO.class);
			listFilters = configGenericSvc.getFieldToFilters(this.getClass(), MenuDTO.class);
			loadDataModel(paginator, tableGeneric);
			loadFields(FILTER, CONST_GRID_STANDARD);
			loadColumns(CONST_TABLE_CUSTOM);
			visibleButtons();
			ButtonsImpl.Stream(HBox.class).setLayout(buttons).setName("fxml.btn.add").action(this::add).icon(Glyph.SAVE)
					.isVisible(save).setName("fxml.btn.edit").action(this::set).icon(Glyph.EDIT).isVisible(edit)
					.setName("fxml.btn.delete").action(this::del).icon(Glyph.REMOVE).isVisible(delete)
					.setName("fxml.btn.view").action(this::set).icon(Glyph.FILE_TEXT).isVisible(view).build();
		} catch (Exception e) {
			error(e);
		}
	}

	public final void add() {
		try {
			getController(MenuBean.class).load();
		} catch (Exception e) {
			error(e);
		}
	}

	public final void del() {
		try {
			controllerPopup(ConfirmPopupBean.class).load("#{ListMenusBean.delete}",
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
							menusSvc.delete(dto, getUsuario());
							alerta(i18n().valueBundle(LanguageConstant.LANGUAGE_SUCCESS_DELETE_MENUS_ROW_CODE,
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
			getController(MenuBean.class).load(dto);
		} catch (Exception e) {
			error(e);
		}
	}

	@Override
	public TableView<MenuDTO> getTableView() {
		return tableGeneric;
	}

	@Override
	public GridPane getGridPane(TypeGeneric typeGeneric) {
		return gridPane;
	}

	@Override
	public MenuDTO getFilterToTable(MenuDTO filter) {
		return filter;
	}

	@Override
	public void selectedRow(MouseEvent eventHandler) {
		clickTable();
	}

	@Override
	public Integer getMaxColumns(TypeGeneric typeGeneric) {
		return 4;
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
	public Class<MenuDTO> getClazz() {
		return MenuDTO.class;
	}

	@Override
	protected void visibleButtons() {
		var save = PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_CREATE, ListMenusBean.class,
				getUsuario().getGrupoUser());
		var edit = dataTable.isSelected() && PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_UPDATE,
				ListMenusBean.class, getUsuario().getGrupoUser());
		var delete = dataTable.isSelected() && PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_DELETE,
				ListMenusBean.class, getUsuario().getGrupoUser());
		var view = !save && !edit && PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_READ,
				ListMenusBean.class, getUsuario().getGrupoUser());
		this.save.setValue(save);
		this.edit.setValue(edit);
		this.delete.setValue(delete);
		this.view.setValue(view);
	}
}
