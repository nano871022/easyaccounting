package co.com.japl.ea.utls;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.common.I18n;
import org.pyt.common.common.Log;
import org.pyt.common.constants.AppConstants;
import org.pyt.common.constants.PropertiesConstants;

import co.com.japl.ea.beans.abstracts.ABean;
import co.com.japl.ea.common.abstracts.ADto;
import co.com.japl.ea.common.properties.CachePropertiesPOM;
import co.com.japl.ea.common.reflection.ReflectionUtils;
import co.com.japl.ea.exceptions.LoadAppFxmlException;
import co.com.japl.ea.interfaces.IBean;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

@SuppressWarnings("rawtypes")
public abstract class AppFXML<P extends Pane, C extends Control> {
	protected static Log logger = Log.Log(LoadAppFxml.class);
	protected static I18n i18n = I18n.instance();
	protected static LoadAppFxml app;
	protected Stage stage;
	protected P lastLayout;
	protected C lastContro;
	protected ABean<?> currentControlScroller;

	public static final void clean() {
		loadApp().currentControlScroller = null;
		loadApp().lastContro = null;
		loadApp().lastLayout = null;
	}

	@SuppressWarnings("static-access")
	private static final Optional<Scene> getScene() {
		var lastControl = loadApp().getLastControl();
		if (lastControl != null && lastControl.getScene() != null) {
			return Optional.of(lastControl.getScene());
		} else if (loadApp().getStage() != null && loadApp().getStage().getScene() != null) {
			return Optional.of(loadApp().getStage().getScene());
		}
		return Optional.empty();
	}

	/**
	 * Se encarga de contruir el objeto loadappfxml como singleton
	 * 
	 * @return {@link LoadAppFxml}
	 */
	@SuppressWarnings("unchecked")
	protected final static <S extends Pane, X extends Control> LoadAppFxml<S, X> loadApp() {
		if (app == null) {
			app = new LoadAppFxml<>();
		}
		return app;
	}

	protected static <S extends ADto, T extends IBean<S>> T beanFxmlBasic(Stage primaryStage, Class<T> class1)
			throws LoadAppFxmlException {
		String file, title;
		loadApp().setStage(primaryStage);
		if (!loadApp().isStage()) {
			throw new LoadAppFxmlException(i18n.get("err.loadappfxml.stage.didnt.sent.and.didnt.saved"));
		}
		try {
			T lbean = class1.getDeclaredConstructor().newInstance();
			file = lbean.pathFileFxml();
			title = lbean.getNombreVentana();
			if (file.substring(0, 1).compareTo(AppConstants.SLASH) != 0) {
				file = AppConstants.SLASH + file;
			}
			URL url = class1.getResource(file);
			FXMLLoader loader = new FXMLLoader(url, getLanguage());
			Parent root = loader.load();
			Scene scene = new Scene(root);
			loadApp().getStage().setTitle(title);
			loadApp().getStage().setScene(scene);
			loadApp().getStage().show();
			return loader.getController();
		} catch (InstantiationException e) {
			throw new LoadAppFxmlException(i18n.get("err.loadappfxml.instance"), e);
		} catch (IllegalAccessException e) {
			throw new LoadAppFxmlException(i18n.get("err.loadappfxml.ilegal.access"), e);
		} catch (IllegalArgumentException e) {
			throw new LoadAppFxmlException(i18n.get("err.loadappfxml.ilegal.argument"), e);
		} catch (InvocationTargetException e) {
			throw new LoadAppFxmlException(i18n.get("err.loadappfxml.object.invoke"), e);
		} catch (NoSuchMethodException e) {
			throw new LoadAppFxmlException(i18n.get("err.loadappfxml.method.didnt.found"), e);
		} catch (SecurityException e) {
			throw new LoadAppFxmlException(i18n.get("err.loadappfxml.security"), e);
		} catch (IOException e) {
			throw new LoadAppFxmlException(i18n.get("err.loadappfxml.io"), e);
		}
	}

	protected final static <S extends ADto, T extends ABean<S>> FXMLLoader loadFxml(Class<T> bean)
			throws LoadAppFxmlException {
		try {
			T lBean = bean.getDeclaredConstructor().newInstance();
			String file = lBean.pathFileFxml();
			if (file == null) {
				throw new LoadAppFxmlException(i18n.valueBundle("err.file.is.empty").get());
			}
			if (file.substring(0, 1).compareTo(AppConstants.SLASH) != 0) {
				file = AppConstants.SLASH + file;
			}
			URL url = bean.getResource(file);
			return new FXMLLoader(url, getLanguage());
		} catch (InstantiationException e) {
			throw new LoadAppFxmlException(i18n.valueBundle("err.in.installing").get(), e);
		} catch (IllegalAccessException e) {
			throw new LoadAppFxmlException(i18n.valueBundle("err.ilegal.access").get(), e);
		} catch (IllegalArgumentException e) {
			throw new LoadAppFxmlException(i18n.valueBundle("err.ilegal.argument").get(), e);
		} catch (InvocationTargetException e) {
			throw new LoadAppFxmlException(i18n.valueBundle("err.in.invoke", bean.getCanonicalName()).get(), e);
		} catch (NoSuchMethodException e) {
			throw new LoadAppFxmlException(i18n.valueBundle("err.dont.found.method").get(), e);
		} catch (SecurityException e) {
			throw new LoadAppFxmlException(i18n.valueBundle("err.security").get(), e);
		}
	}

	protected static final void loadTitleWindow(Stage stage, String... title) {
		var titleAdd = Arrays.asList(title).stream().reduce((val1, val2) -> val1 + "/" + val2);
		var profile = CachePropertiesPOM.instance().load(LoadAppFxml.class, PropertiesConstants.PROP_APP_POM)
				.get("profile").filter(value -> value.contentEquals("dev"));
		String titleBar = "";
		if (profile.isPresent()) {
			titleBar += " -- Warning is DEVELOP Mode -- ";
		}
		if (titleAdd.isPresent() && StringUtils.isNotBlank(titleAdd.get())) {
			titleBar += titleAdd.get();
		}
		if (StringUtils.isNotBlank(titleBar)) {
			stage.setTitle(titleBar);
		}
	}

	protected static <T> void callPostLoad(T controller) {
		try {
			ReflectionUtils.instanciar().invoke(controller, "postLoad");
		} catch (Exception e) {
			logger.DEBUG(controller.getClass().getTypeName() + "::" + e.getMessage());
		}
	}

	public static final void addCommands(KeyCombination keyboard, Runnable runnable) {
		var scene = getScene();
		if (scene.isPresent()) {
			var accelerator = scene.get().getAccelerators();
			accelerator.put(keyboard, runnable);
		}
	}

	protected final static ResourceBundle getLanguage() {
		return ResourceBundle.getBundle(AppConstants.RESOURCE_BUNDLE, Locale.getDefault());
	}

	public P getLastLayout() {
		return lastLayout;
	}

	public Boolean isLastLayout() {
		return lastLayout != null;
	}

	public void setLastLayout(P lastLayout) {
		this.lastLayout = lastLayout;
	}

	public final void setStage(Stage stages) {
		if (stages != null) {
			stage = stages;
		}
	}

	public final void cleanStage() {
		stage = null;
	}

	public final Stage getStage() {
		return stage;
	}

	public final Boolean isStage() {
		if (stage != null)
			return true;
		return false;
	}

	public final C getLastContro() {
		return lastContro;
	}

	@SuppressWarnings("unchecked")
	public static final <C extends Control> C getLastControl() {
		return (C) loadApp().lastContro;
	}

	public final void setLastContro(C lastContro) {
		this.lastContro = lastContro;
	}

	public final Boolean isLastContro() {
		return lastContro != null;
	}

	public static final LoadAppFxml loadingApp() {
		return loadApp();
	}

	@SuppressWarnings("unchecked")
	public static final <D extends ADto, B extends ABean<D>> B getCurrentControl() {
		return (B) loadApp().currentControlScroller;
	}
}
