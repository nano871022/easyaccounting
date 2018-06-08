package org.pyt.app.load;

import org.pyt.app.beans.EmpresaBean;
import org.pyt.common.annotations.FXMLFile;
import org.pyt.common.annotations.Inject;
import org.pyt.common.annotations.SubcribirToComunicacion;
import org.pyt.common.common.Comunicacion;
import org.pyt.common.common.LoadAppFxml;
import org.pyt.common.common.Log;
import org.pyt.common.constants.AppConstants;
import org.pyt.common.exceptions.LoadAppFxmlException;
import org.pyt.common.exceptions.ReflectionException;
import org.pyt.common.interfaces.IComunicacion;
import org.pyt.common.reflection.Reflection;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 * Se encarga del control del archivo de template
 * 
 * @author Alejandro Parra
 * @since 2018-05-24
 */
@FXMLFile(path = "view", file = "Template.fxml", nombreVentana = "Contabilidad PyT")
public class Template extends Reflection implements IComunicacion {
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
	@Inject
	@SubcribirToComunicacion(comando = AppConstants.COMMAND_PROGRESS)
	@SubcribirToComunicacion(comando = AppConstants.COMMAND_MSN_IZQ)
	@SubcribirToComunicacion(comando = AppConstants.COMMAND_MSN_DER)
	@SubcribirToComunicacion(comando = AppConstants.COMMAND_MSN_CTR)
	private Comunicacion comunicacion;

	@FXML
	public void initialize() {
		try {
			inject();
			rightMessage.setText("");
			leftMessage.setText("");
			centerMessage.setText("");
			progressBar.setProgress(0.0);
			menu.getMenus().clear();
			ObservableList<Menu> menus = menu.getMenus();
			Menu menu = new Menu("Modulos");
			menu.getItems().clear();
			ObservableList<MenuItem> menuItems = menu.getItems();
			MenuItem item = new MenuItem("Empresa");
			item.onActionProperty().set((event)->{
					try {
						LoadAppFxml.BeanFxmlScroller(scroller, EmpresaBean.class);
					} catch (LoadAppFxmlException e) {
						Log.logger(e);
					}
			});
			MenuItem itemCLose = new MenuItem("Cerrar");
			itemCLose.onActionProperty().set((event)->{Platform.exit();System.exit(0);});
			menuItems.add(item);
			menuItems.add(itemCLose);
			menus.add(menu);
			Log.logger("Cargando ventana principal");
		} catch (ReflectionException e1) {
			Log.logger(e1);
		}
	}

	@Override
	public <T> void get(String comando, T valor) {
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
		}
	}
}
