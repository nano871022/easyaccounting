package org.pyt.app.load;

import org.controlsfx.glyphfont.FontAwesome.Glyph;
import org.pyt.app.beans.dinamico.FormularioBean;
import org.pyt.app.beans.generic.interfaces.ListGenericInterfacesBean;
import org.pyt.app.beans.help.HelpViewBean;
import org.pyt.app.beans.languages.LanguageBean;
import org.pyt.app.beans.languages.ListLanguagesBean;
import org.pyt.app.components.PopupBean;
import org.pyt.app.components.PopupFromBean;
import org.pyt.common.annotations.Inject;
import org.pyt.common.annotations.SubcribirToComunicacion;
import org.pyt.common.common.Comunicacion;
import org.pyt.common.common.Log;
import org.pyt.common.constants.AppConstants;
import org.pyt.common.constants.PermissionConstants;
import org.pyt.common.exceptions.LoadAppFxmlException;
import org.pyt.common.exceptions.ReflectionException;
import org.pyt.common.interfaces.IComunicacion;
import org.pyt.common.reflection.Reflection;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.common.button.apifluid.ButtonsImpl;
import co.com.japl.ea.dto.system.LanguagesDTO;
import co.com.japl.ea.utls.LoadAppFxml;
import co.com.japl.ea.utls.LoginUtil;
import co.com.japl.ea.utls.PermissionUtil;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Se encarga del control del archivo de template
 * 
 * @author Alejandro Parra
 * @since 2018-05-24
 */
@FXMLFile(path = "view", file = "Template.fxml", nombreVentana = "Contabilidad PyT")
public class Template implements IComunicacion, Reflection {
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
	@FXML
	private HBox topBodyTemplate;
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
	private BooleanProperty languageBP;
	private BooleanProperty helpBP;
	private BooleanProperty documentsBP;
	private BooleanProperty genericBP;

	@FXML
	public void initialize() {
		try {
			inject();
			languageBP = new SimpleBooleanProperty(true);
			helpBP = new SimpleBooleanProperty(true);
			documentsBP = new SimpleBooleanProperty(true);
			genericBP = new SimpleBooleanProperty(true);
			rightMessage.setText("");
			leftMessage.setText("");
			centerMessage.setText("");
			progressBar.setProgress(0.0);
			new MenuItems(menu, scroller).load();
			logger.DEBUG("Cargando ventana principal");
			LoadAppFxml.addCommandsToPopup(new KeyCodeCombination(KeyCode.I, KeyCombination.ALT_DOWN),
					LanguagesDTO.class);
			visibleButtons();
			ButtonsImpl.Stream(HBox.class).setLayout(topBodyTemplate).setReference("fxml.btn.language")
					.action(this::language).isVisible(languageBP).icon(Glyph.LANGUAGE).setReference("fxml.btn.help")
					.action(this::help).isVisible(helpBP).icon(Glyph.QUESTION)
					.setReference("fxml.btn.dinamic.documents").icon(Glyph.FILE_TEXT)
					.action(this::configDinamicDocuments).isVisible(documentsBP)
					.setReference("fxml.btn.generic.interface").icon(Glyph.FILE_TEXT_ALT)
					.action(this::configGenericInterface).isVisible(genericBP).build();
		} catch (ReflectionException e1) {
			logger.logger(e1);
		}
	}

	private void language() {
		try {
			var popup = new PopupFromBean(LanguageBean.class);
			LoadAppFxml.loadBeanFX(popup);
			LanguageBean bean = (LanguageBean) popup.getBean();
			bean.openPopup();
		} catch (Exception e) {
			logger().logger(e);
		}
	}

	private void help() {
		try {
			var popup = new HelpViewBean();
			LoadAppFxml.loadBeanPopupBean(popup);
		} catch (Exception e) {
			logger().logger(e);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void configGenericInterface() {
		try {
			var popup = new PopupFromBean(ListGenericInterfacesBean.class);
			LoadAppFxml.loadBeanFX(popup);
		} catch (Exception e) {
			logger().logger(e);
		}
	}

	private void configDinamicDocuments() {
		try {
			var popup = new PopupFromBean(FormularioBean.class);
			LoadAppFxml.loadBeanFX(popup);
		} catch (Exception e) {
			logger().logger(e);
		}
	}

	private void visibleButtons() {
		var language = PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_CREATE,
				ListLanguagesBean.class, LoginUtil.getUsuarioLogin().getGrupoUser());
		var generic = PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_CREATE,
				ListGenericInterfacesBean.class, LoginUtil.getUsuarioLogin().getGrupoUser());
		var formulario = PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_CREATE, FormularioBean.class,
				LoginUtil.getUsuarioLogin().getGrupoUser());
		this.languageBP.setValue(language);
		this.genericBP.setValue(generic);
		this.documentsBP.setValue(formulario);
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
				LoadAppFxml.loadBeanFxml2(new Stage(), PopupBean.class).load(valor, PopupBean.TIPOS.ERROR);
				break;
			case AppConstants.COMMAND_POPUP_INFO:
				LoadAppFxml.loadBeanFxml(new Stage(), PopupBean.class).load(valor, PopupBean.TIPOS.INFO);
				break;
			case AppConstants.COMMAND_POPUP_WARN:
				LoadAppFxml.loadBeanFxml(new Stage(), PopupBean.class).load(valor, PopupBean.TIPOS.WARNING);
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
