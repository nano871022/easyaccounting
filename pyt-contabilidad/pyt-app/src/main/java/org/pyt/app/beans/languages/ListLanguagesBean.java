package org.pyt.app.beans.languages;

import java.util.List;

import org.apache.commons.collections4.MultiValuedMap;
import org.pyt.app.components.ConfirmPopupBean;
import org.pyt.common.annotations.Inject;
import org.pyt.common.constants.LanguageConstant;
import org.pyt.common.constants.StylesPrincipalConstant;

import com.pyt.service.interfaces.IGenericServiceSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.AGenericInterfacesBean;
import co.com.japl.ea.dto.system.ConfigGenericFieldDTO;
import co.com.japl.ea.dto.system.LanguagesDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

@FXMLFile(file = "listLanguages.fxml", path = "view/languages")
public class ListLanguagesBean extends AGenericInterfacesBean<LanguagesDTO> {

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
			filtro = new LanguagesDTO();
			lblTitle.setText(i18n().valueBundle("fxml.lbl.title.list.languages"));
			gridPane = new GridPane();
			gridPane.setHgap(10);
			gridPane.setVgap(10);
			filterGeneric.getChildren().addAll(gridPane);
			loadDataModel(paginator, tableGeneric);
			loadFields(TypeGeneric.FIELD, StylesPrincipalConstant.CONST_GRID_STANDARD);
			loadColumns(StylesPrincipalConstant.CONST_TABLE_CUSTOM);
		} catch (Exception e) {
			error(e);
		}
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
				var list = dataTable.getSelectedRows();
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
	public GridPane getGridPane(TypeGeneric typeGeneric) {
		return gridPane;
	}

	@Override
	public Integer getMaxColumns(TypeGeneric typeGeneric) {
		return 2;
	}

	@Override
	public LanguagesDTO getFilterToTable(LanguagesDTO filter) {
		return filter;
	}

	@Override
	public void selectedRow(MouseEvent eventHandler) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<ConfigGenericFieldDTO> getListGenericsFields(TypeGeneric typeGeneric) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MultiValuedMap<String, Object> getMapListToChoiceBox() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<LanguagesDTO> getClazz() {
		return LanguagesDTO.class;
	}
}
