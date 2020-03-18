package org.pyt.app.beans.help;

import java.util.List;

import org.apache.commons.collections4.MultiValuedMap;
import org.pyt.common.annotations.Inject;

import com.pyt.service.interfaces.IGenericServiceSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.AGenericToBean;
import co.com.japl.ea.dto.system.ConfigGenericFieldDTO;
import co.com.japl.ea.dto.system.HelpDTO;
import co.com.japl.ea.utls.DataTableFXMLUtil;
import co.com.japl.ea.utls.LoadAppFxml;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

@FXMLFile(file = "show.fxml", path = "view/help")
public class HelpViewBean extends AGenericToBean<HelpDTO> {
	@Inject(resource = "com.pyt.service.implement.GenericServiceSvc")
	private IGenericServiceSvc<HelpDTO> helpsSvc;
	@FXML
	private WebView wvBottom;
	@FXML
	private WebView wvRight;
	@FXML
	private WebView titlePanel;
	@FXML
	private WebView bodyPanel;
	private static final String CONST_TEXT_HTML = "text/html";

	@FXML
	public void initialize() {
		try {
			registro = new HelpDTO();
			registro.setClassPathBean(LoadAppFxml.getCurrentControl().getClass().getName().replace("Class ", ""));
			var list = helpsSvc.getAll(registro);
			if (list.size() == 0 || list == null) {
				var stage = (Stage) LoadAppFxml.getLastControl().getParent().getScene().getWindow();
				if (stage != null) {
					stage.close();
				}
			}
			titlePanel.getEngine().loadContent(registro.getTitle(), CONST_TEXT_HTML);
			wvRight.getEngine().loadContent(registro.getMsgRigth(), CONST_TEXT_HTML);
			wvBottom.getEngine().loadContent(registro.getMsgBottom(), CONST_TEXT_HTML);
			bodyPanel.getEngine().loadContent(registro.getBody(), CONST_TEXT_HTML);
		} catch (Exception e) {
			logger().logger(e);
		}
	}

	public final void load() {
		registro = new HelpDTO();
	}

	@Override
	public GridPane getGridPane(TypeGeneric typeGeneric) {
		return null;
	}

	@Override
	public MultiValuedMap<String, Object> getMapListToChoiceBox() {
		return null;
	}

	@Override
	public Integer getMaxColumns(TypeGeneric typeGeneric) {
		return null;
	}

	@Override
	public List<ConfigGenericFieldDTO> getListGenericsFields(TypeGeneric typeGeneric) {
		return null;
	}

	@Override
	public void selectedRow(MouseEvent eventHandler) {
	}

	@Override
	public TableView<HelpDTO> getTableView() {
		return null;
	}

	@Override
	public DataTableFXMLUtil<HelpDTO, HelpDTO> getTable() {
		return null;
	}

}
