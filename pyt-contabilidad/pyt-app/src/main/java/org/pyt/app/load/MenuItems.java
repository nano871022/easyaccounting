package org.pyt.app.load;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.pyt.common.abstracts.ADto;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.I18n;
import org.pyt.common.common.Log;
import org.pyt.common.constants.PropertiesConstants;
import org.pyt.common.exceptions.LoadAppFxmlException;
import org.pyt.common.properties.PropertiesUtils;

import com.pyt.service.interfaces.IGenericServiceSvc;

import co.com.japl.ea.beans.ABean;
import co.com.japl.ea.beans.LoadAppFxml;
import co.com.japl.ea.dto.system.MenuDTO;
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
 * 
 * @author Alejandro Parra
 * @since 22-06-2018
 */
public class MenuItems {
	@Inject(resource = "com.pyt.service.implement.GenericServiceSvc")
	private IGenericServiceSvc<MenuDTO> menusSvc;
	private Properties propertiesMenu;
	private I18n i18n;

	private Map<String, Menu> mapaMenu;
	private MenuBar menu;
	private ScrollPane scroll;
	private final static String MENU_PRINCIPAL = "Inicio";
	private final static String BTN_CERRAR = "Cerrar";
	private final static String LBL_MENU = "lbl.menu.";
	private Log logger = Log.Log(this.getClass());

	public MenuItems(MenuBar menu, ScrollPane scroll) {
		try {
			mapaMenu = new HashMap<String, Menu>();
			propertiesMenu = PropertiesUtils.getInstance().setNameProperties(PropertiesConstants.PROP_MENU).load()
					.getProperties();
			i18n = new I18n();
			this.menu = menu;
			this.scroll = scroll;
		} catch (Exception e) {
			logger.logger(e);
		}
	}

	private final List<MenuDTO> getMenusFromDB() {
		List<MenuDTO> list = new ArrayList<MenuDTO>();
		try {
			var dto = new MenuDTO();
			list = menusSvc.getAll(dto);
		} catch (Exception e) {
			logger.logger(e);
		}
		return list;
	}

	private final void loadMenus() {
		var list = getMenusFromDB();
		if (list != null && list.size() > 0) {
			list.forEach(menuDto -> {
				var menus = menuDto.getUrl().split("/");
				var menu = getMenu(menus[0]);
				processMenu(menu, menuDto.getClassPath(), 0, menus);
			});
		}
		propertiesMenu.keySet().forEach(key -> {
			var menus = ((String)key).split("/");
			var menu = getMenu(menus[0]);
			processMenu(menu, (String) propertiesMenu.get(key), 0, menus);
		});
	}

	private final <T extends ADto,B extends ABean<T>> void processMenu(Menu menu, String classString, Integer position, String... menusSplit) {
		var nameLabel = nameLabel(menusSplit[position + 1]);
		if (menusSplit.length > position) {
			var founds = menu.getItems().stream()
					.filter(menuFilter -> menuFilter.getText().contentEquals(nameLabel))
					.toArray(Menu[]::new);
			Menu found = null;
			if (founds == null || founds.length == 0) {

				if (menusSplit.length - 1 == position + 1) {
					var nameLabel2 = "";
					if(nameLabel.contains("?"))  {
						nameLabel2 = nameLabel.substring(0, nameLabel.indexOf("?"));
					}else{
						nameLabel2 = nameLabel;
					}
					var menuItem = new MenuItem(nameLabel2);
					menu.getItems().add(menuItem);
					menuItem.onActionProperty().set( event -> onActionProperty(classString,nameLabel));
					return;
				} else {
					found = new Menu(nameLabel);
					menu.getItems().add(found);
				}
			}
			if (founds.length == 1) {
				found = founds[0];
			}
			if (found != null) {
				processMenu(found, classString, position + 1, menusSplit);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private final <B extends ABean<T>,T extends ADto> void onActionProperty(String classString,String nameLabel)  {
		try {
			Class<?> [] parametersType = null;
			Object[] parameters = null;
			if(nameLabel.contains("?")) {
				var nameParameters = nameLabel.substring(nameLabel.indexOf("?")+1);
				parameters = nameParameters.split("&");
				parametersType = new Class<?>[parameters.length];
				for(var i = 0; i< parameters.length;i++) {
					parametersType[i] = (parameters[i]).getClass();
				}
			}
			Class<B> beanToLoad = (Class<B>) Class.forName(classString);
			var beanFxml = LoadAppFxml.BeanFxmlScroller(scroll, beanToLoad);
			if(parametersType != null && parametersType.length > 0) {
				var methodLoadParameters = beanFxml.getClass().getDeclaredMethod("loadParameters", parametersType);
				if(methodLoadParameters != null) {
					methodLoadParameters.invoke(beanFxml, parameters);
				}
			}
		} catch (LoadAppFxmlException | ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			logger.logger(e);
		}
	}

	private final String nameLabel(String key) {
		var search = LBL_MENU + key;
		var result = i18n.valueBundle(search);
		if (!search.contains(result)) {
			key = result;
		}
		return key;
	}

	private final Menu getMenu(String key) {
		key = nameLabel(key);
		Menu menu = mapaMenu.get(key);
		if (menu == null) {
			menu = new Menu(key);
			mapaMenu.put(key, menu);
		}
		return menu;
	}

	private final void add(Menu menu) {
		ObservableList<Menu> list = this.menu.getMenus();
		list.add(menu);
	}

	private final ObservableList<MenuItem> getItems(Menu menu) {
		menu.getItems().clear();
		return menu.getItems();
	}

	private final MenuItem addItem(String nombre, EventHandler<ActionEvent> event) {
		nombre = nameLabel(nombre);
		MenuItem item = new MenuItem(nombre);
		item.onActionProperty().set(event);
		return item;
	}

	private final void loadPrincipal() {
		Menu principal = getMenu(MENU_PRINCIPAL);
		ObservableList<MenuItem> items = getItems(principal);
		items.add(addItem(BTN_CERRAR, event -> {
			Platform.exit();
			System.exit(0);
		}));
	}

	/**
	 * Carga todos los menus
	 */
	public final void load() {
		loadPrincipal();
		loadMenus();
		mapaMenu.entrySet().forEach(entry -> add(entry.getValue()));
	}
}
