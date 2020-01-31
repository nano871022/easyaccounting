package org.pyt.app.beans.users;

import static org.pyt.common.constants.InsertResourceConstants.CONST_RESOURCE_IMPL_SVC_CONFIG_GENERIC_FIELD;

import java.util.List;

import org.apache.commons.collections4.MultiValuedMap;
import org.pyt.app.components.ConfirmPopupBean;
import org.pyt.common.annotations.Inject;
import org.pyt.common.constants.LanguageConstant;

import com.pyt.service.interfaces.IConfigGenericFieldSvc;
import com.pyt.service.interfaces.IUsersSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.AGenericInterfacesBean;
import co.com.japl.ea.dto.system.ConfigGenericFieldDTO;
import co.com.japl.ea.dto.system.UsuarioDTO;
import co.com.japl.ea.utls.DataTableFXMLUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

@FXMLFile(file = "list_users.fxml", path = "view/users")
public class ListUsersBean extends AGenericInterfacesBean<UsuarioDTO> {

	@Inject(resource = "com.pyt.service.implement.UserSvc")
	private IUsersSvc usersSvc;
	@FXML
	private Button btnMod;
	@FXML
	private Button btnDel;
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

	@FXML
	public void initialize() {
		try {
			filtro = new UsuarioDTO();
			lblTitle.setText(i18n().valueBundle("fxml.lbl.title.list.users").get());
			gridPane = new GridPane();
			gridPane.setHgap(10);
			gridPane.setVgap(10);
			filterGeneric.getChildren().addAll(gridPane);
			listFilters = configGenericSvc.getFieldToFilters(this.getClass(), UsuarioDTO.class);
			listColumns = configGenericSvc.getFieldToColumns(this.getClass(), UsuarioDTO.class);
			loadDataModel(paginator, tableGeneric);
			loadFields(TypeGeneric.FILTER);
			loadColumns();
		} catch (Exception e) {
			error(e);
		}
	}

	@Override
	protected void loadDataModel(HBox paginator, TableView<UsuarioDTO> tableView) {
		dataTable = new DataTableFXMLUtil<UsuarioDTO, UsuarioDTO>(paginator, tableView) {
			@Override
			public Integer getTotalRows(UsuarioDTO filter) {
				try {
					return usersSvc.countRow(filter);
				} catch (Exception e) {
					error(e);
				}
				return 0;
			}

			@Override
			public List<UsuarioDTO> getList(UsuarioDTO filter, Integer page, Integer rows) {
				try {
					return usersSvc.getAll(filter, page, rows);
				} catch (Exception e) {
					error(e);
				}
				return null;
			}

			@Override
			public UsuarioDTO getFilter() {
				return filtro;
			}
		};
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
		if (dataTable.getSelectedRows().size() > 0) {
			btnDel.setVisible(true);
			btnMod.setVisible(true);
		}
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
		return null;
	}

	@Override
	public void selectedRow(MouseEvent eventHandler) {
		set();
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
		return null;
	}

	@Override
	public Class<UsuarioDTO> getClazz() {
		return UsuarioDTO.class;
	}
}
