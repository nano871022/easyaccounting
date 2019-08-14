package org.pyt.app.beans.generic.interfaces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pyt.app.components.ConfirmPopupBean;
import org.pyt.app.components.DataTableFXML;
import org.pyt.app.components.IGenericColumn;
import org.pyt.app.components.IGenericFilter;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.I18n;
import org.pyt.common.common.Log;

import com.pyt.service.interfaces.IGenericServiceSvc;
import com.pyt.service.pojo.GenericPOJO;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.ABean;
import co.com.japl.ea.dto.system.ConfigGenericFieldDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

@FXMLFile(file = "list_generic_interfaces.fxml", path = "/view/genericInterfaces", nombreVentana = "Lista Interfaces Genericas")
public class ListGenericInterfacesBean extends ABean<ConfigGenericFieldDTO>
		implements IGenericFilter<ConfigGenericFieldDTO>, IGenericColumn<ConfigGenericFieldDTO> {
	@Inject(resource = "com.pyt.service.implement.GenericServiceSvc")
	private IGenericServiceSvc<ConfigGenericFieldDTO> configGenericSvc;
	@FXML
	private HBox paginador;
	@FXML
	private TableView<ConfigGenericFieldDTO> tableGeneric;
	@FXML
	private HBox filterGeneric;
	@FXML
	private Label lblTitle;
	private ConfigGenericFieldDTO filtro;
	private GridPane gridPane;
	private DataTableFXML<ConfigGenericFieldDTO, ConfigGenericFieldDTO> tablaConfigGeneric;
	private Map<String, GenericPOJO<ConfigGenericFieldDTO>> filtersMap;
	private Map<String, GenericPOJO<ConfigGenericFieldDTO>> columnsMap;

	@FXML
	private void initialize() {
		gridPane = new GridPane();
		filtro = new ConfigGenericFieldDTO();
		filterGeneric.getChildren().addAll(gridPane);
		filtersMap = new HashMap<String, GenericPOJO<ConfigGenericFieldDTO>>();
		lblTitle.setText(i18n().valueBundle("generic.lbl.list.generic.interfaces"));
		configTable();
		load();
	}

	private final void configTable() {
		tablaConfigGeneric = new DataTableFXML<ConfigGenericFieldDTO, ConfigGenericFieldDTO>(paginador, tableGeneric) {

			@Override
			public Integer getTotalRows(ConfigGenericFieldDTO filter) {
				try {
					return configGenericSvc.getTotalRows(filter);
				} catch (Exception e) {
					error(e);
				}
				return 0;
			}

			@Override
			public List<ConfigGenericFieldDTO> getList(ConfigGenericFieldDTO filter, Integer page, Integer rows) {
				List<ConfigGenericFieldDTO> list = new ArrayList<ConfigGenericFieldDTO>();
				try {
					list = configGenericSvc.gets(filter, page, rows);
				} catch (Exception e) {
					error(e);
				}
				return list;
			}

			@Override
			public ConfigGenericFieldDTO getFilter() {
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
					"¿Desea eliminar los registros seleccionados?");
		} catch (Exception e) {
			error(e);
		}
	}

	public final void setDelete(Boolean del) {
		if (del) {
			tablaConfigGeneric.getSelectedRows().forEach(row -> {
				try {
					configGenericSvc.delete(row, userLogin);
					notificar("Se elimino la configuración con codigo " + row.getCodigo());
				} catch (Exception e) {
					error(e);
				}
			});
		}
	}

	public final void set() {
		this.getController(GenericInterfacesBean.class).load(tablaConfigGeneric.getSelectedRow());
	}

	public final void load() {
		try {
			configFilters();
			configColumns();
		} catch (Exception e) {
			error(e);
		}
	}

	@Override
	public Map<String, GenericPOJO<ConfigGenericFieldDTO>> getFilters() {
		return filtersMap;
	}

	@Override
	public void setFilters(Map<String, GenericPOJO<ConfigGenericFieldDTO>> filters) {
		filtersMap = filters;
	}

	@Override
	public Map<String, GenericPOJO<ConfigGenericFieldDTO>> getColumns() {
		return columnsMap;
	}

	@Override
	public void setColumns(Map<String, GenericPOJO<ConfigGenericFieldDTO>> columns) {
		columnsMap = columns;
	}

	@Override
	public Log getLogger() {
		return logger;
	}

	@Override
	public I18n getI18n() {
		return i18n();
	}

	@Override
	public Class<ConfigGenericFieldDTO> getClazz() {
		return ConfigGenericFieldDTO.class;
	}

	@Override
	public void setClazz(Class<ConfigGenericFieldDTO> clazz) {
	}

	@Override
	public ConfigGenericFieldDTO getFilter() {
		return filtro;
	}

	@Override
	public void setFilter(ConfigGenericFieldDTO filter) {
		filtro = filter;
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

}
