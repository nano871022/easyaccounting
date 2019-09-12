package org.pyt.app.load;

import org.pyt.app.components.PopupBean;
import org.pyt.common.annotations.Inject;
import org.pyt.common.annotations.SubcribirToComunicacion;
import org.pyt.common.common.Comunicacion;
import org.pyt.common.common.I18n;
import org.pyt.common.common.Log;
import org.pyt.common.constants.AppConstants;
import org.pyt.common.exceptions.LoadAppFxmlException;
import org.pyt.common.exceptions.ReflectionException;
import org.pyt.common.interfaces.IComunicacion;
import org.pyt.common.reflection.Reflection;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.utls.LoadAppFxml;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Se encarga del control del archivo de template
 * 
 * @author Alejandro Parra
 * @since 2018-05-24
 */
@FXMLFile(path = "view", file = "Template.fxml", nombreVentana = "Contabilidad PyT")
public class Template implements IComunicacion,Reflection {
	@FXML
	private MenuBar menu;
	@FXML
	private Label leftMessage;
	@FXML
	private Label centerMessage;
	@FXML
	private Label rightMessage;
	@FXML
	private ProgressBar progressBar;
	@FXML
	private VBox notificacion;
	@FXML
	private javafx.scene.layout.Pane principal;
	@FXML
	private BorderPane panel;
	@FXML
	private javafx.scene.control.ScrollPane scroller;
	@SuppressWarnings("rawtypes")
	@Inject
	@SubcribirToComunicacion(comando = AppConstants.COMMAND_PROGRESS)
	@SubcribirToComunicacion(comando = AppConstants.COMMAND_MSN_IZQ)
	@SubcribirToComunicacion(comando = AppConstants.COMMAND_MSN_DER)
	@SubcribirToComunicacion(comando = AppConstants.COMMAND_MSN_CTR)
	@SubcribirToComunicacion(comando = AppConstants.COMMAND_POPUP_WARN)
	@SubcribirToComunicacion(comando = AppConstants.COMMAND_POPUP_INFO)
	@SubcribirToComunicacion(comando = AppConstants.COMMAND_POPUP_ERROR)
	@SubcribirToComunicacion(comando = AppConstants.COMMAND_LANGUAGES)
	private Comunicacion comunicacion;
	private Log logger = Log.Log(this.getClass());
	@FXML
	public void initialize() {
		try {
			inject();
			rightMessage.setText("");
			leftMessage.setText("");
			centerMessage.setText("");
			progressBar.setProgress(0.0);
			new MenuItems(menu, scroller).load();
			logger.logger("Cargando ventana principal");
		} catch (ReflectionException e1) {
			logger.logger(e1);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> void get(String comando, T valor) {
		try {
			switch (comando) {
			case AppConstants.COMMAND_MSN_CTR:
				if (valor instanceof String)
					centerMessage.setText((String) valor);
				break;
			case AppConstants.COMMAND_MSN_DER:
				if (valor instanceof String)
					rightMessage.setText((String) valor);
				break;
			case AppConstants.COMMAND_MSN_IZQ:
				if (valor instanceof String)
					leftMessage.setText((String) valor);
				break;
			case AppConstants.COMMAND_PROGRESS:
				if (valor instanceof Double) {
					progressBar.setProgress((Double) valor);
				}
				break;
			case AppConstants.COMMAND_POPUP_ERROR:
				if (valor instanceof String) {
					LoadAppFxml.loadBeanFxml2(new Stage(), PopupBean.class).load((String) valor, PopupBean.TIPOS.ERROR);
				}
				break;
			case AppConstants.COMMAND_POPUP_INFO:
				if (valor instanceof String) {
					LoadAppFxml.loadBeanFxml(new Stage(), PopupBean.class).load((String) valor, PopupBean.TIPOS.INFO);
				}
				break;
			case AppConstants.COMMAND_POPUP_WARN:
				if (valor instanceof String) {
					LoadAppFxml.loadBeanFxml(new Stage(), PopupBean.class).load((String) valor, PopupBean.TIPOS.WARNING);
				}
				break;
			}
		} catch (LoadAppFxmlException e) {
			centerMessage.setText((String) valor);
		}
	}

	@Override
	public Log logger() {
		return logger;
	}
}
