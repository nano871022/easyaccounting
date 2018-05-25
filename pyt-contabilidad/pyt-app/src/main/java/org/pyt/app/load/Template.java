package org.pyt.app.load;

import org.pyt.app.beans.EmpresaBean;
import org.pyt.common.annotations.FXMLFile;
import org.pyt.common.annotations.Inject;
import org.pyt.common.annotations.SubcribirToComunicacion;
import org.pyt.common.common.Comunicacion;
import org.pyt.common.common.LoadAppFxml;
import org.pyt.common.common.Log;
import org.pyt.common.exceptions.LoadAppFxmlException;
import org.pyt.common.exceptions.ReflectionException;
import org.pyt.common.reflection.Reflection;

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
public class Template extends Reflection {
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
	@SubcribirToComunicacion(comando = "progress")
	@SubcribirToComunicacion(comando = "mensajeIzquierda")
	@SubcribirToComunicacion(comando = "mensajeDerecha")
	@SubcribirToComunicacion(comando = "mensajeCentro")
	private Comunicacion comunicacion;

	@FXML
	public void initialize() {
		try {
			inject();
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
						// LoadAppFxml.BeanFxml(principal, EmpresaBean.class);
						LoadAppFxml.BeanFxmlScroller(scroller, EmpresaBean.class);
					} catch (LoadAppFxmlException e) {
						Log.logger(e);
					}
				}
			});
			menuItems.add(item);
			menus.add(menu);
			Log.logger("Cargando ventana principal");
		} catch (ReflectionException e1) {
			Log.logger(e1);
		}
	}
}
