package org.pyt.app.beans.generic.interfaces;

import java.util.List;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.lang3.StringUtils;
import org.pyt.app.components.ConfirmPopupBean;
import org.pyt.common.annotations.Inject;
import org.pyt.common.constants.LanguageConstant;
import org.pyt.common.constants.StylesPrincipalConstant;
import org.pyt.common.exceptions.ConfigGenericFieldException;

import com.pyt.service.interfaces.IConfigGenericFieldSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.AGenericInterfacesBean;
import co.com.japl.ea.dto.system.ConfigGenericFieldDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
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
	private List<ConfigGenericFieldDTO> listFieldsToFilters;
	private List<ConfigGenericFieldDTO> listFieldsToColumns;
	@Inject(resource = "com.pyt.service.implement.ConfigGenericFieldSvc")
	private IConfigGenericFieldSvc configGenericFieldsSvc;

	@FXML
	private void initialize() {
		gridPane = new GridPane();
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		filtro = new ConfigGenericFieldDTO();
		filterGeneric.getChildren().addAll(gridPane);
		lblTitle.setText(i18n().valueBundle(LanguageConstant.GENERIC_LBL_LIST_GENERIC_INTERFACES));
		try {
			listFieldsToFilters = configGenericFieldsSvc.getFieldToFilters(this.getClass(),
					ConfigGenericFieldDTO.class);
			listFieldsToColumns = configGenericFieldsSvc.getFieldToColumns(this.getClass(),
					ConfigGenericFieldDTO.class);
		} catch (ConfigGenericFieldException e) {
			error(e);
		}
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
					getServiceSvc().delete(row, getUsuario());
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
			loadFields(TypeGeneric.FILTER, StylesPrincipalConstant.CONST_GRID_STANDARD);
			loadColumns(StylesPrincipalConstant.CONST_TABLE_CUSTOM);
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
	public GridPane getGridPane(TypeGeneric typeGeneric) {
		return gridPane;
	}

	@Override
	public TableView<ConfigGenericFieldDTO> getTableView() {
		return tableGeneric;
	}

	@Override
	public Integer getMaxColumns(TypeGeneric typeGeneric) {
		return 2;
	}

	@Override
	public ConfigGenericFieldDTO getFilterToTable(ConfigGenericFieldDTO filter) {
		if (StringUtils.isNotBlank(filter.getAlias()) && !filter.getAlias().contains("%")) {
			filter.setAlias("%" + filter.getAlias() + "%");
		}
		if (StringUtils.isNotBlank(filter.getClassPath()) && !filter.getClassPath().contains("%")) {
			filter.setClassPath("%" + filter.getClassPath() + "%");
		}
		if (StringUtils.isNotBlank(filter.getClassPathBean()) && !filter.getClassPathBean().contains("%")) {
			filter.setClassPathBean("%" + filter.getClassPathBean() + "%");
		}
		if (StringUtils.isNotBlank(filter.getDescription()) && !filter.getDescription().contains("%")) {
			filter.setDescription("%" + filter.getDescription() + "%");
		}
		if (StringUtils.isNotBlank(filter.getName()) && !filter.getName().contains("%")) {
			filter.setName("%" + filter.getName() + "%");
		}
		return filter;
	}

	@Override
	public void selectedRow(MouseEvent eventHandler) {
		if (getTable().isSelected()) {
			set();
		}
	}

	@Override
	public List<ConfigGenericFieldDTO> getListGenericsFields(TypeGeneric typeGeneric) {
		switch (typeGeneric) {
		case FILTER:
			return listFieldsToFilters;
		case COLUMN:
			return listFieldsToColumns;
		default:
			throw new RuntimeException("Opcion no valida");
		}
	}

	@Override
	public MultiValuedMap<String, Object> getMapListToChoiceBox() {
		return null;
	}

	@Override
	public Class<ConfigGenericFieldDTO> getClazz() {
		return ConfigGenericFieldDTO.class;
	}
}
