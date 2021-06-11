package co.com.japl.ea.app.load;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.I18n;
import org.pyt.common.common.Log;
import org.pyt.common.constants.AppConstants;
import org.pyt.common.constants.PropertiesConstants;

import co.com.japl.ea.app.beans.users.LoginBean;
import co.com.japl.ea.beans.abstracts.ABean;
import co.com.japl.ea.common.abstracts.ADto;
import co.com.japl.ea.common.properties.PropertiesUtils;
import co.com.japl.ea.common.reflection.Reflection;
import co.com.japl.ea.dto.interfaces.IGenericServiceSvc;
import co.com.japl.ea.dto.interfaces.IUsersSvc;
import co.com.japl.ea.dto.system.LanguagesDTO;
import co.com.japl.ea.dto.system.MenuDTO;
import co.com.japl.ea.dto.system.MenuPermUsersDTO;
import co.com.japl.ea.exceptions.GenericServiceException;
import co.com.japl.ea.exceptions.LoadAppFxmlException;
import co.com.japl.ea.interfaces.IUrlLoadBean;
import co.com.japl.ea.utls.LoadAppFxml;
import co.com.japl.ea.utls.LoginUtil;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCharacterCombination;
import javafx.scene.input.KeyCombination;

/**
 * Se encarga de tener todos las pantallas a mostrar
 * 
 * @author Alejandro Parra
 * @since 22-06-2018
 */
public class MenuItems implements Reflection {
	@Inject(resource = "com.pyt.service.implement.GenericServiceSvc")
	private IGenericServiceSvc<MenuPermUsersDTO> menuPermUsersSvc;
	@Inject(resource = "com.pyt.service.implement.GenericServiceSvc")
	private IGenericServiceSvc<LanguagesDTO> languagesSvc;
	@Inject(resource = "com.pyt.service.implement.UserSvc")
	private IUsersSvc loginSvc;
	private Properties propertiesMenu;

	private Map<String, Menu> mapaMenu;
	private MenuBar menu;
	private ScrollPane scroll;
	private final static String MENU_PRINCIPAL = "Inicio";
	private final static String BTN_CERRAR = "Cerrar";
	private final static String BTN_LOGOUT = "Logout";
	private final static String LBL_MENU = "lbl.menu.";
	private final static String CONST_URL_INIT_PARAMS = "?";
	private final static String CONST_URL_SPLIT_PARAMS = "&";
	private Log logger = Log.Log(this.getClass());

	public MenuItems(MenuBar menu, ScrollPane scroll) {
		try {
			mapaMenu = new HashMap<String, Menu>();
			propertiesMenu = PropertiesUtils.getInstance().setNameProperties(PropertiesConstants.PROP_MENU).load()
					.getProperties();
			this.menu = menu;
			this.scroll = scroll;
			inject();
			var listLanguages = languagesSvc.getAll(new LanguagesDTO());
			I18n.instance().setLanguagesDB(listLanguages);
		} catch (Exception e) {
			logger.logger(e);
		}
	}

	private final List<MenuDTO> getMenusFromDB() {
		List<MenuDTO> list = new ArrayList<MenuDTO>();
		try {
			var dto = new MenuPermUsersDTO();
			dto.setGroupUsers(LoginUtil.getUsuarioLogin().getGrupoUser());
			dto.setState(1);
			menuPermUsersSvc.getAll(dto).stream().filter(row -> row.getMenu() != null && row.getPerm() != null)
					.forEach(row -> list.add(row.getMenu()));
		} catch (GenericServiceException e) {
			logger.logger(e);
		}
		return list;
	}

	private final void loadMenus() {
		var list = getMenusFromDB();
		if (list != null && list.size() > 0) {
			list.stream().forEach(menuDto -> {
				var menus = menuDto.getUrl().split(AppConstants.SLASH);
				var menu = getMenu(menus[0]);
				processMenu(menu, menuDto.getClassPath(), menuDto.getShortcut(), 0, menus);//
			});
		} else {
			propertiesMenu.keySet().forEach(key -> {
				var menus = ((String) key).split(AppConstants.SLASH);
				var menu = getMenu(menus[0]);
				processMenu(menu, (String) propertiesMenu.get(key), null, 0, menus);
			});
		}
	}

	private final <T extends ADto, B extends ABean<T>> void processMenu(Menu menu, String classString, String shortcut,
			Integer position, String... menusSplit) {
		var nameLabel = nameLabel(menusSplit[position + 1]);
		if (menusSplit.length > position) {
			var founds = menu.getItems().stream().filter(menuFilter -> menuFilter.getText().contentEquals(nameLabel))
					.toArray(Menu[]::new);
			Menu found = null;
			if (founds == null || founds.length == 0) {

				if (menusSplit.length - 1 == position + 1) {
					var nameLabel2 = "";
					if (nameLabel.contains("?")) {
						nameLabel2 = nameLabel.substring(0, nameLabel.indexOf("?"));
					} else {
						nameLabel2 = nameLabel;
					}
					var menuItem = new MenuItem(nameLabel2);

					menu.getItems().add(menuItem);
					if (StringUtils.isNotBlank(shortcut)) {
						menuItem.setAccelerator(new KeyCharacterCombination(shortcut, KeyCombination.ALT_DOWN));
					}
					menuItem.onActionProperty().set(event -> onActionProperty(classString, nameLabel));
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
				processMenu(found, classString, shortcut, position + 1, menusSplit);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private final <B extends ABean<T>, T extends ADto> void onActionProperty(String classString, String nameLabel) {
		try {
			Class<B> beanToLoad = (Class<B>) Class.forName(classString);
			var beanFxml = LoadAppFxml.BeanFxmlScroller(scroll, beanToLoad);
			invokeLoadParameters(nameLabel, beanFxml);
		} catch (LoadAppFxmlException | ClassNotFoundException | SecurityException | IllegalArgumentException e) {
			logger.logger(e);
		}
	}

	/**
	 * Se encarga de obtener los parameteros apartir del nombre del menu a cargar y
	 * imvoicar los parametros si contiene parametros, ademas que debe ser
	 * implemetacion de la interfaz {@link IUrlLoadBean}
	 * 
	 * @param nameLabel {@link String}
	 * @param beanFxml  {@link ABean}
	 */
	private final <B extends ABean<T>, T extends ADto> void invokeLoadParameters(String nameLabel, B beanFxml) {
		String[] parameters = null;
		if (nameLabel.contains(CONST_URL_INIT_PARAMS)) {
			var nameParameters = nameLabel.substring(nameLabel.indexOf(CONST_URL_INIT_PARAMS) + 1);
			parameters = nameParameters.split(CONST_URL_SPLIT_PARAMS);
		}
		if (beanFxml instanceof IUrlLoadBean && parameters != null && parameters.length > 0) {
			((IUrlLoadBean) beanFxml).loadParameters(parameters);
		}
	}

	private final String nameLabel(String key) {
		var search = LBL_MENU + key;
		var result = I18n.instance().valueBundle(search);
		if (!search.contains(result.get())) {
			key = result.get();
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
		var logOut = addItem(BTN_LOGOUT, event -> {
			try {
				loginSvc.logout(LoginUtil.getUsuarioLogin(), InetAddress.getLocalHost().getHostAddress(),
						LoginUtil.isRemember());
				LoginUtil.deleteRemember();
				LoadAppFxml.loadingApp().getStage().hide();
				LoadAppFxml.loadBeanFxml(LoginBean.class).load(LoadAppFxml.loadingApp().getStage());
			} catch (UnknownHostException e) {
				logger.logger(e);
			} catch (Exception e) {
				logger.logger(e);
			}
		});
		logOut.setAccelerator(new KeyCharacterCombination("O", KeyCombination.ALT_DOWN));
		logOut.visibleProperty().bind(Bindings.createBooleanBinding(() -> LoginUtil.isRemember()));
		items.add(logOut);
		var close = addItem(BTN_CERRAR, event -> {
			try {
				loginSvc.logout(LoginUtil.getUsuarioLogin(), InetAddress.getLocalHost().getHostAddress(), false);
			} catch (UnknownHostException e) {
				logger.logger(e);
			} catch (Exception e) {
				logger.logger(e);
			}
			Platform.exit();
			System.exit(0);
		});
		close.setAccelerator(new KeyCharacterCombination("C", KeyCombination.ALT_DOWN));
		items.add(close);
	}

	/**
	 * Carga todos los menus
	 */
	public final void load() {
		loadPrincipal();
		loadMenus();
		mapaMenu.entrySet().forEach(entry -> add(entry.getValue()));
	}

	@Override
	public Log logger() {
		return logger;
	}
}
