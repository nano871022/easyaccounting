package org.pyt.app.beans.help;

import org.pyt.common.annotations.Inject;
import org.pyt.common.constants.CSSConstant;

import com.pyt.service.interfaces.IGenericServiceSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.APopupFromBean;
import co.com.japl.ea.dto.system.HelpDTO;
import co.com.japl.ea.utls.LoadAppFxml;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

@FXMLFile(file = "show.fxml", path = "view/help")
public class HelpViewBean extends APopupFromBean<HelpDTO> {

	@Inject(resource = "com.pyt.service.implement.GenericServiceSvc")
	private IGenericServiceSvc<HelpDTO> helpsSvc;
	@FXML
	private WebView wvBottom;
	@FXML
	private WebView wvRight;
	@FXML
	private WebView wvTittle;
	@FXML
	private WebView wvCenter;
	@FXML
	private BorderPane panel;
	private BorderPane principalPanel;
	private static final String CONST_TEXT_HTML = "text/html";
	private HelpDTO registro;
	private String tittleWindowI18n;

	public HelpViewBean() {
		super(HelpDTO.class);
		principalPanel = new BorderPane();
		inject();
	}

	@FXML
	public void initialize() {
		try {
			registro = new HelpDTO();
			// principalPanel.setCenter(panel);
			registro.setClassPathBean(LoadAppFxml.getCurrentControl().getClass().getName().replace("Class ", ""));
			var list = helpsSvc.getAll(registro);
			if (list.size() == 0 || list == null) {
				var stage = (Stage) LoadAppFxml.getLastControl().getParent().getScene().getWindow();
				if (stage != null) {
					stage.close();
				}
			}
			registro = list.get(0);
			wvTittle.getEngine().loadContent(registro.getTitle(), CONST_TEXT_HTML);
			wvRight.getEngine().loadContent(registro.getMsgRigth(), CONST_TEXT_HTML);
			wvBottom.getEngine().loadContent(registro.getMsgBottom(), CONST_TEXT_HTML);
			wvCenter.getEngine().loadContent(registro.getBody(), CONST_TEXT_HTML);
		} catch (Exception e) {
			logger().logger(e);
		}
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		if (width != null) {
			primaryStage.setWidth(width);
		}
		if (height != null) {
			primaryStage.setHeight(height);
		}
		primaryStage.setTitle(i18n().valueBundle(tittleWindowI18n).get());
		Scene scene = new Scene(principalPanel);
		scene.getStylesheets().add(CSSConstant.CONST_PRINCIPAL);
		primaryStage.setScene(scene);
		primaryStage.hide();
	}

	public void load() {
		try {
			registro = new HelpDTO();
			LoadAppFxml.loadBeanPopup(primaryStage, HelpViewBean.class);
			showWindow();
		} catch (Exception e) {
			logger.logger(e);
		}
	}

	@Override
	public HelpDTO getRegistro() {
		return registro;
	}

	@Override
	public void setRegistro(HelpDTO registro) {
		this.registro = registro;
	}

	@Override
	public String getNombreVentana() {
		return tittleWindowI18n;
	}

	@Override
	public void setNombreVentana(String nombreVentana) {
		this.tittleWindowI18n = nombreVentana;
	}

}
