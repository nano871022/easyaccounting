package org.pyt.app.beans.languages;

import java.util.List;

import org.pyt.app.components.ConfirmPopupBean;
import org.pyt.app.components.DataTableFXML;
import org.pyt.common.annotations.Inject;
import org.pyt.common.constants.LanguageConstant;

import com.pyt.service.interfaces.IGenericServiceSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.AGenericInterfacesBean;
import co.com.japl.ea.dto.system.LanguagesDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

@FXMLFile(file = "listLanguages.fxml", path = "view/languages")
public class ListLanguagesBean extends AGenericInterfacesBean<LanguagesDTO> {

	private DataTableFXML<LanguagesDTO, LanguagesDTO> dataModel;

	@Inject(resource = "com.pyt.service.implement.GenericServiceSvc")
	private IGenericServiceSvc<LanguagesDTO> languagesSvc;
	@FXML
	private Button btnMod;
	@FXML
	private Button btnDel;
	@FXML
	private TableView<LanguagesDTO> tableGeneric;
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
			lblTitle.setText(i18n().valueBundle("fxml.lbl.title.list.languages"));
			gridPane = new GridPane();
			gridPane.setHgap(10);
			gridPane.setVgap(10);
			filterGeneric.getChildren().addAll(gridPane);
			loadDataModel();
			setClazz(LanguagesDTO.class);
			configFilters();
			configColumns();
		} catch (Exception e) {
			error(e);
		}
	}

	private void loadDataModel() {
		dataModel = new DataTableFXML<LanguagesDTO, LanguagesDTO>(paginator, tableGeneric) {

			@Override
			public Integer getTotalRows(LanguagesDTO filter) {
				try {
					return languagesSvc.getTotalRows(filter);
				} catch (Exception e) {
					error(e);
				}
				return 0;
			}

			@Override
			public List<LanguagesDTO> getList(LanguagesDTO filter, Integer page, Integer rows) {
				try {
					return languagesSvc.gets(filter, page, rows);
				} catch (Exception e) {
					error(e);
				}
				return null;
			}

			@Override
			public LanguagesDTO getFilter() {
				return filtro;
			}
		};
	}

	public final void add() {
		try {
			getController(LanguageBean.class).load();
		} catch (Exception e) {
			error(e);
		}
	}

	public final void del() {
		try {
			controllerPopup(ConfirmPopupBean.class).load("#{ListLanguagesBean.delete}",
					i18n().valueBundle(LanguageConstant.LANGUAGE_WARNING_DELETE_ROW));
		} catch (Exception e) {
			error(e);
		}
	}

	public final void setDelete(Boolean valid) {
		try {
			if (valid) {
				var list = dataModel.getSelectedRows();
				if (list != null && list.size() > 0) {
					list.forEach(dto -> {
						try {
							languagesSvc.delete(dto, userLogin);
							alerta(String.format(LanguageConstant.LANGUAGE_SUCCESS_DELETE_LANGUAGES_ROW_CODE,
									dto.getCode()));
						} catch (Exception e) {
							error(e);
						}
					});
				}
			}
		} catch (Exception e) {
			error(e);
		}
	}

	public final void clickTable() {
		if (dataModel.getSelectedRows().size() > 0) {
			btnDel.setVisible(true);
			btnMod.setVisible(true);
		}
	}

	public final void set() {
		try {
			var dto = dataModel.getSelectedRow();
			getController(LanguageBean.class).load(dto);
		} catch (Exception e) {
			error(e);
		}
	}

	@Override
	public TableView<LanguagesDTO> getTableView() {
		return tableGeneric;
	}

	@Override
	public DataTableFXML<LanguagesDTO, LanguagesDTO> getTable() {
		return dataModel;
	}

	@Override
	public GridPane getGridPaneFilter() {
		return gridPane;
	}

	@Override
	public Integer countFieldsInRow() {
		return 2;
	}
}
