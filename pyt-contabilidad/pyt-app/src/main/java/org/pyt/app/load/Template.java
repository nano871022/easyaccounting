package org.pyt.app.load;

import org.pyt.app.beans.EmpresaBean;
import org.pyt.common.annotations.FXMLFile;
import org.pyt.common.common.LoadAppFxml;
import org.pyt.common.common.Log;
import org.pyt.common.exceptions.LoadAppFxmlException;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 * Se encarga del control del archivo de template
 * 
 * @author Alejandro Parra
 * @since 2018-05-24
 */
@FXMLFile(path = "view", file = "Template.fxml", nombreVentana = "Contabilidad PyT")
public class Template {
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
	private AnchorPane principal;
	@FXML
	private BorderPane panel;

	@FXML
	public void initialize() {
		menu.getMenus().clear();
		ObservableList<Menu> menus = menu.getMenus();
		Menu menu = new Menu("Modulos");
		menu.getItems().clear();
		ObservableList<MenuItem> menuItems = menu.getItems();
		MenuItem item = new MenuItem("Empresa");
		item.onActionProperty().set(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					LoadAppFxml.BeanFxml(principal, EmpresaBean.class);
				} catch (LoadAppFxmlException e) {
					Log.logger(e);
				}
			}
		});
		menuItems.add(item);
		menus.add(menu);
		Log.logger("Cargando ventana principal");
	}
}
