package org.pyt.app.beans.generic.interfaces;

import org.pyt.app.components.ConfirmPopupBean;
import org.pyt.common.constants.LanguageConstant;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.AGenericInterfacesBean;
import co.com.japl.ea.dto.system.ConfigGenericFieldDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

@FXMLFile(file = "list_generic_interfaces.fxml", path = "/view/genericInterfaces", nombreVentana = "Lista Interfaces Genericas")
public class ListGenericInterfacesBean extends AGenericInterfacesBean<ConfigGenericFieldDTO> {
	@FXML
	private HBox paginador;
	@FXML
	private TableView<ConfigGenericFieldDTO> tableGeneric;
	@FXML
	private HBox filterGeneric;
	@FXML
	private Label lblTitle;
	private GridPane gridPane;

	@FXML
	private Button btnDel;
	@FXML
	private Button btnMod;

	@FXML
	private void initialize() {
		gridPane = new GridPane();
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		setClazz(ConfigGenericFieldDTO.class);
		filtro = new ConfigGenericFieldDTO();
		filterGeneric.getChildren().addAll(gridPane);
		lblTitle.setText(i18n().valueBundle(LanguageConstant.GENERIC_LBL_LIST_GENERIC_INTERFACES));
		loadDataModel(paginador, tableGeneric);
		load();
	}

	public final void add() {
		this.getController(GenericInterfacesBean.class).load();
	}

	public final void del() {
		try {
			controllerPopup(ConfirmPopupBean.class).load("#{ListGenericInterfacesBean.delete}",
					i18n().valueBundle(LanguageConstant.LANGUAGE_WARNING_DELETE_ROW));
		} catch (Exception e) {
			error(e);
		}
	}

	public final void setDelete(Boolean del) {
		if (del) {
			dataTable.getSelectedRows().forEach(row -> {
				try {
					getServiceSvc().delete(row, userLogin);
					notificar(
							String.format(i18n().valueBundle(LanguageConstant.LANGUAGE_SUCCESS_DELETE_CONFIG_ROW_CODE),
									row.getCodigo()));
				} catch (Exception e) {
					error(e);
				}
			});
		}
	}

	public final void set() {
		this.getController(GenericInterfacesBean.class).load(dataTable.getSelectedRow());
	}

	public final void clickTable() {
		if (dataTable.getSelectedRow() != null) {
			btnDel.setVisible(true);
			btnMod.setVisible(true);
		} else {
			btnDel.setVisible(false);
			btnMod.setVisible(false);
		}
	}

	public final void load() {
		try {
			configFilters();
			loadColumnsIntoTableView();
		} catch (Exception e) {
			error(e);
		}
	}

	public void copy() {
		try {
			var dto = dataTable.getSelectedRow();
			dto.setCodigo(null);
			this.getController(GenericInterfacesBean.class).load(dto);
		} catch (Exception e) {
			error(e);
		}
	}

	@Override
	public GridPane getGridPaneFilter() {
		return gridPane;
	}

	@Override
	public TableView<ConfigGenericFieldDTO> getTableView() {
		return tableGeneric;
	}

	@Override
	public Integer countFieldsInRow() {
		return 2;
	}

	@Override
	public ConfigGenericFieldDTO getFilterToTable(ConfigGenericFieldDTO filter) {
		return filter;
	}
}
