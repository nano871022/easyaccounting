package org.pyt.app.load;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.controlsfx.glyphfont.FontAwesome.Glyph;
import org.pyt.common.abstracts.ADto;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.I18n;
import org.pyt.common.common.ListUtils;
import org.pyt.common.common.Log;
import org.pyt.common.constants.AppConstants;
import org.pyt.common.constants.PropertiesConstants;
import org.pyt.common.exceptions.GenericServiceException;
import org.pyt.common.exceptions.LoadAppFxmlException;
import org.pyt.common.properties.PropertiesUtils;
import org.pyt.common.reflection.Reflection;

import com.pyt.service.interfaces.IGenericServiceSvc;
import com.pyt.service.interfaces.IUsersSvc;

import co.com.japl.ea.beans.abstracts.ABean;
import co.com.japl.ea.dto.system.LanguagesDTO;
import co.com.japl.ea.dto.system.MenuDTO;
import co.com.japl.ea.dto.system.MenuPermUsersDTO;
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
		if (ListUtils.isNotBlank(list)) {
			list.stream().forEach(menuDto -> {
				var menus = menuDto.getUrl().split(AppConstants.SLASH);
				var menu = getMenu(menus[0]);
				processMenu(menu, menuDto.getClassPath(), 0, menus);//
			});
		} else {
			propertiesMenu.keySet().forEach(key -> {
				var menus = ((String) key).split(AppConstants.SLASH);
				var menu = getMenu(menus[0]);
				processMenu(menu, (String) propertiesMenu.get(key), 0, menus);
			});
		}
	}

	private final <T extends ADto, B extends ABean<T>> void processMenu(Menu menu, String classString, Integer position,
			String... menusSplit) {
		var nameLabel = nameLabel(menusSplit[position + 1]);
		if (menusSplit.length > position) {
			var founds = menu.getItems().stream().filter(menuFilter -> menuFilter.getText().contentEquals(nameLabel))
					.toArray(Menu[]::new);
			Menu found = null;
			if (ListUtils.isBlank(founds)) {
				if (menusSplit.length - 1 == position + 1) {
					var nameLabel2 = "";
					if (nameLabel.contains("?")) {
						nameLabel2 = nameLabel.substring(0, nameLabel.indexOf("?"));
					} else {
						nameLabel2 = nameLabel;
					}
					var menuItem = new MenuItem(nameLabel2);
					menuItem.onActionProperty().set(event -> onActionProperty(classString, nameLabel));
					menu.getItems().add(menuItem);
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
		key = nameLabel(key).trim();
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

	private final MenuItem addItem(String nombre, EventHandler<ActionEvent> event, Glyph... log) {
		nombre = nameLabel(nombre);
		MenuItem item = new MenuItem(nombre);
		if (ListUtils.isNotBlank(log)) {
			item.setGraphic(new org.controlsfx.glyphfont.Glyph("FontAwesome", log[0]));
		}
		item.onActionProperty().set(event);
		return item;
	}

	private final void loadPrincipal() {
		Menu principal = getMenu(MENU_PRINCIPAL);
		principal.setId("1");
		ObservableList<MenuItem> items = getItems(principal);
		var logOut = addItem(BTN_LOGOUT, event -> {
			try {
				loginSvc.logout(LoginUtil.getUsuarioLogin(), InetAddress.getLocalHost().getHostAddress(),
						LoginUtil.isRemember());
				LoginUtil.deleteRemember();
			} catch (UnknownHostException e) {
				logger.logger(e);
			} catch (Exception e) {
				logger.logger(e);
			}
			Platform.exit();
			System.exit(0);
		}, Glyph.EJECT);
		logOut.visibleProperty().bind(Bindings.createBooleanBinding(() -> LoginUtil.isRemember()));
		items.add(logOut);
		items.add(addItem(BTN_CERRAR, event -> {
			try {
				loginSvc.logout(LoginUtil.getUsuarioLogin(), InetAddress.getLocalHost().getHostAddress(), false);
			} catch (UnknownHostException e) {
				logger.logger(e);
			} catch (Exception e) {
				logger.logger(e);
			}
			Platform.exit();
			System.exit(0);
		}, Glyph.SIGN_OUT));
	}

	/**
	 * Carga todos los menus
	 */
	public final void load() {
		loadPrincipal();
		loadMenus();

		mapaMenu.entrySet().stream().filter(valor -> valor.getKey().contentEquals(MENU_PRINCIPAL))
				.map(valor -> valor.getValue()).peek(mensaje -> logger().info(mensaje.toString())).forEach(this::add);
		mapaMenu.entrySet().stream().filter(valor -> !valor.getKey().contentEquals(MENU_PRINCIPAL))
				.peek(mensaje -> logger().info(mensaje.toString())).map(valor -> valor.getValue()).forEach(this::add);

		var usuario = getMenu(LoginUtil.getUsuarioLogin().getNombre().replace("_", " "));
		usuario.setId("usuario");
		usuario.setGraphic(new org.controlsfx.glyphfont.Glyph("FontAwesome", Glyph.USER_SECRET));
		this.add(usuario);
	}

	@Override
	public Log logger() {
		return logger;
	}
}
