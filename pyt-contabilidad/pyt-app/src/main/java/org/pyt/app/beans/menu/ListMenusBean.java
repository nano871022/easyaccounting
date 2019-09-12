package org.pyt.app.beans.menu;

import org.pyt.app.components.ConfirmPopupBean;
import org.pyt.common.annotations.Inject;
import org.pyt.common.constants.LanguageConstant;

import com.pyt.service.interfaces.IGenericServiceSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.AGenericInterfacesBean;
import co.com.japl.ea.dto.system.MenuDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

@FXMLFile(file = "listmenus.fxml", path = "view/menu")
public class ListMenusBean extends AGenericInterfacesBean<MenuDTO> {

	@Inject(resource = "com.pyt.service.implement.GenericServiceSvc")
	private IGenericServiceSvc<MenuDTO> menusSvc;
	@FXML
	private Button btnMod;
	@FXML
	private Button btnDel;
	@FXML
	private TableView<MenuDTO> tableGeneric;
	@FXML
	private HBox filterGeneric;
	@FXML
	private Label lblTitle;
	@FXML
	private HBox paginator;
	private GridPane gridPane;

	@FXML
	public void initialize() {
		try {
			filtro = new MenuDTO();
			lblTitle.setText(i18n().valueBundle("fxml.lbl.title.list.menus"));
			gridPane = new GridPane();
			gridPane.setHgap(10);
			gridPane.setVgap(10);
			filterGeneric.getChildren().addAll(gridPane);
			loadDataModel(paginator, tableGeneric);
			setClazz(MenuDTO.class);
			configFilters();
			configColumns();
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
			controllerPopup(ConfirmPopupBean.class).load("#{ListMenuBean.delete}",
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
							menusSvc.delete(dto, userLogin);
							alerta(String.format(LanguageConstant.LANGUAGE_SUCCESS_DELETE_MENUS_ROW_CODE,
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
		if (dataTable.getSelectedRows().size() > 0) {
			btnDel.setVisible(true);
			btnMod.setVisible(true);
		}
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
	public GridPane getGridPaneFilter() {
		return gridPane;
	}

	@Override
	public Integer countFieldsInRow() {
		return 2;
	}

	@Override
	public MenuDTO getFilterToTable(MenuDTO filter) {
		return filter;
	}
}
