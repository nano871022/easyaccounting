package org.pyt.app.beans.generic.interfaces;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.pyt.app.components.ConfirmPopupBean;
import org.pyt.app.components.DataTableFXML;
import org.pyt.common.constants.AppConstants;
import org.pyt.common.constants.LanguageConstant;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.AGenericInterfacesBean;
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
	private DataTableFXML<ConfigGenericFieldDTO, ConfigGenericFieldDTO> tablaConfigGeneric;

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
		configTable();
		load();
	}

	private final void configTable() {
		tablaConfigGeneric = new DataTableFXML<ConfigGenericFieldDTO, ConfigGenericFieldDTO>(paginador, tableGeneric) {

			@Override
			public Integer getTotalRows(ConfigGenericFieldDTO filter) {
				try {
					return configGenericFieldSvc().getTotalRows(filter);
				} catch (Exception e) {
					error(e);
				}
				return 0;
			}

			@Override
			public List<ConfigGenericFieldDTO> getList(ConfigGenericFieldDTO filter, Integer page, Integer rows) {
				List<ConfigGenericFieldDTO> list = new ArrayList<ConfigGenericFieldDTO>();
				try {
					list = configGenericFieldSvc().gets(filter, page, rows);
				} catch (Exception e) {
					error(e);
				}
				return list;
			}

			@Override
			public ConfigGenericFieldDTO getFilter() {
				if (StringUtils.isNotBlank(filtro.getClassPath())) {
					filtro.setClassPath(
							AppConstants.CONST_PERCENT + filtro.getClassPath() + AppConstants.CONST_PERCENT);
				}
				if (StringUtils.isNotBlank(filtro.getClassPathBean())) {
					filtro.setClassPath(
							AppConstants.CONST_PERCENT + filtro.getClassPathBean() + AppConstants.CONST_PERCENT);
				}
				if (StringUtils.isNotBlank(filtro.getDescription())) {
					filtro.setClassPath(
							AppConstants.CONST_PERCENT + filtro.getDescription() + AppConstants.CONST_PERCENT);
				}
				if (StringUtils.isNotBlank(filtro.getName())) {
					filtro.setClassPath(AppConstants.CONST_PERCENT + filtro.getName() + AppConstants.CONST_PERCENT);
				}
				if (StringUtils.isNotBlank(filtro.getAlias())) {
					filtro.setClassPath(AppConstants.CONST_PERCENT + filtro.getAlias() + AppConstants.CONST_PERCENT);
				}
				return filtro;
			}
		};
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
			tablaConfigGeneric.getSelectedRows().forEach(row -> {
				try {
					configGenericFieldSvc().delete(row, userLogin);
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
		this.getController(GenericInterfacesBean.class).load(tablaConfigGeneric.getSelectedRow());
	}

	public final void clickTable() {
		if (tablaConfigGeneric.getSelectedRow() != null) {
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
			configColumns();
		} catch (Exception e) {
			error(e);
		}
	}

	public void copy() {
		try {
			var dto = tablaConfigGeneric.getSelectedRow();
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
	public DataTableFXML<ConfigGenericFieldDTO, ConfigGenericFieldDTO> getTable() {
		return tablaConfigGeneric;
	}

	@Override
	public TableView<ConfigGenericFieldDTO> getTableView() {
		return tableGeneric;
	}

	@Override
	public Integer countFieldsInRow() {
		return 2;
	}
}
