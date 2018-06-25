package org.pyt.app.load;

import java.util.HashMap;
import java.util.Map;

import org.pyt.app.beans.empresa.EmpresaBean;
import org.pyt.app.beans.parametros.ParametrosBean;
import org.pyt.app.beans.trabajador.TrabajadorBean;
import org.pyt.common.common.LoadAppFxml;
import org.pyt.common.common.Log;
import org.pyt.common.exceptions.LoadAppFxmlException;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;

/**
 * Se encarga de tener todos las pantallas a mostrar
 * @author Alejandro Parra
 * @since 22-06-2018
 */
public class MenuItems {
	 private Map<String,Menu> mapaMenu;
	 private MenuBar menu;
	 private ScrollPane scroll;
	 private final static String MENU_PRINCIPAL = "Principal";
	 private final static String MENU_ADMINISTRADOR = "Administrador";
	 private final static String MENU_MODULOS = "Modulos";
	 private final static String BTN_CERRAR = "Cerrar";
	 private final static String BTN_PROVEEDOR = "Proveedores";
	 private final static String BTN_EMPLEADOS = "Empleados";
	 private final static String BTN_PARAMETRO = "Parametros";
	 public MenuItems(MenuBar menu,ScrollPane scroll) {
		 mapaMenu = new HashMap<String,Menu>();
		 this.menu = menu;
		 this.scroll = scroll;
		 menus();
	 }
	 
	 private final void menus() {
		 mapaMenu.put("Principal", new Menu(MENU_PRINCIPAL));
		 mapaMenu.put("Administrador", new Menu(MENU_ADMINISTRADOR));
		 mapaMenu.put("Modulos", new Menu(MENU_MODULOS));
	 }
	 
	 private final Menu getMenu (String key) {
		 Menu menu = mapaMenu.get(key);
		 if(menu == null) {
			 menu = new Menu(key);
			 mapaMenu.put(key, menu);
		 }
		return menu; 
	 }
	 
	 private final void add(Menu menu) {
		 ObservableList<Menu> list = this.menu.getMenus();
		 list.add(menu);
	 }
	 
	 private final ObservableList<MenuItem> getItems(Menu menu){
		 menu.getItems().clear();
		 return menu.getItems();
	 }
	 
	 private final MenuItem addItem(String nombre,EventHandler<ActionEvent> event) {
		 MenuItem item = new MenuItem(nombre);
		 item.onActionProperty().set(event);
		 return item;
	 }
	 private final void loadPrincipal() {
		 Menu principal = getMenu(MENU_PRINCIPAL);
		 ObservableList<MenuItem> items = getItems(principal);
		 items.add(addItem(BTN_CERRAR,event->{Platform.exit();System.exit(0);}));
		 add(principal);
	 }
	 private final void loadModulos() {
		 Menu modulo = getMenu(MENU_MODULOS);
		 ObservableList<MenuItem> items = getItems(modulo);
		items.add(addItem(MENU_MODULOS,event->System.out.println("sin config.")));
		add(modulo);
	 }
	 private final void loadAdministrador() {
		 Menu admin = getMenu(MENU_ADMINISTRADOR);
		 ObservableList<MenuItem> items = getItems(admin);
		 items.add(addItem(BTN_PROVEEDOR,event->{
			 try {
				 LoadAppFxml.BeanFxmlScroller(scroll, EmpresaBean.class);
			 } catch (LoadAppFxmlException e) {
				 Log.logger(e);
			 }
		 }));
		 items.add(addItem(BTN_EMPLEADOS,event->{
			 try {
				 LoadAppFxml.BeanFxmlScroller(scroll, TrabajadorBean.class);
			 } catch (LoadAppFxmlException e) {
				 Log.logger(e);
			 }
		 }));
		 items.add(addItem(BTN_PARAMETRO,event->{
			 try {
				 LoadAppFxml.BeanFxmlScroller(scroll, ParametrosBean.class);
			 } catch (LoadAppFxmlException e) {
				 Log.logger(e);
			 }
		 }));
		 add(admin);
	 }
	 /**
	  * Carga todos los menus 
	  */
	 public final void load() {
		 loadPrincipal();
		 loadModulos();
		 loadAdministrador();
	 }
}
