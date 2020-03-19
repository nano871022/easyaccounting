package co.com.japl.ea.utls;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import org.pyt.app.components.PopupFromBean;
import org.pyt.app.components.PopupGenBean;
import org.pyt.common.abstracts.ADto;
import org.pyt.common.common.I18n;
import org.pyt.common.common.Log;
import org.pyt.common.constants.AppConstants;
import org.pyt.common.constants.CSSConstant;
import org.pyt.common.exceptions.LoadAppFxmlException;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.ABean;
import co.com.japl.ea.beans.abstracts.AGenericToBean;
import co.com.japl.ea.beans.abstracts.APopupFromBean;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.LoadException;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Se encarga de cargar una aplicacion
 * 
 * @author Alejandro Parra
 * @since 2018-05-22
 */
public final class LoadAppFxml<P extends Pane, C extends Control> {
	private Stage stage;
	@SuppressWarnings("rawtypes")
	private static LoadAppFxml app;
	private P lastLayout;
	private C lastContro;
	private static Log logger = Log.Log(LoadAppFxml.class);
	private static I18n i18n = I18n.instance();
	private ABean<?> currentControlScroller;

	/**
	 * Se encarga de contruir el objeto loadappfxml como singleton
	 * 
	 * @return {@link LoadAppFxml}
	 */
	@SuppressWarnings("unchecked")
	private final static <S extends Pane, X extends Control> LoadAppFxml<S, X> loadApp() {
		if (app == null) {
			app = new LoadAppFxml<>();
		}
		return app;
	}

	/**
	 * Se encarga de generar una aplicacion cargada segun el bean que debe tener
	 * anotada el file fxml y el titulo de la ventana.
	 * 
	 * @param primaryStage {@link Stage}
	 * @param class1       extended of {@link ABean}
	 * @param title        {@link String} nombre de la ventana
	 * @throws {@link LoadAppFxmlException}
	 */
	public final static <S extends ADto, T extends ABean<S>> T loadBeanFxml(Stage primaryStage, Class<T> class1)
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

	public final static <S extends ADto, T extends APopupFromBean<S>> T loadBeanPopup(Stage primaryStage,
			Class<T> class1) throws LoadAppFxmlException {
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

	public final static <T extends AGenericToBean<D>, D extends ADto> T loadBeanFX(T genericBean)
			throws LoadAppFxmlException {
		try {
			Stage stg = new Stage();
			genericBean.start(stg);
			genericBean.initialize();

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
		} catch (Exception e) {
			throw new LoadAppFxmlException(i18n.get("err.loadappfxml.stage.didnt.found"), e);
		}
		return genericBean;
	}

	public final static <T extends APopupFromBean<D>, D extends ADto> T loadBeanPopupBean(T genericBean)
			throws LoadAppFxmlException {
		try {
			Stage stg = new Stage();
			genericBean.start(stg);
			genericBean.load();

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
		} catch (Exception e) {
			throw new LoadAppFxmlException(i18n.get("err.loadappfxml.stage.didnt.found"), e);
		}
		return genericBean;
	}

	@SuppressWarnings("rawtypes")
	public final static <P extends PopupFromBean> P loadBeanFX(P genericBean) throws LoadAppFxmlException {
		try {
			Stage stg = new Stage();
			genericBean.start(stg);
			genericBean.load();
		} catch (InstantiationException e) {
			throw new LoadAppFxmlException(i18n.valueBundle("err.instance.problem").get(), e);
		} catch (IllegalAccessException e) {
			throw new LoadAppFxmlException(i18n.valueBundle("err.ilegal.access").get(), e);
		} catch (IllegalArgumentException e) {
			throw new LoadAppFxmlException(i18n.valueBundle("err.ilegal.argument").get(), e);
		} catch (InvocationTargetException e) {
			throw new LoadAppFxmlException(i18n.valueBundle("err.invoke.object.problem").get(), e);
		} catch (NoSuchMethodException e) {
			throw new LoadAppFxmlException(i18n.valueBundle("err.method.not.found").get(), e);
		} catch (SecurityException e) {
			throw new LoadAppFxmlException(i18n.valueBundle("err.security.problem").get(), e);
		} catch (Exception e) {
			throw new LoadAppFxmlException(i18n.valueBundle("err.dont.can.get.stage").get(), e);
		}
		return genericBean;
	}

	/**
	 * Se encarga de generar una aplicacion cargada segun el bean que debe tener
	 * anotada el file fxml y el titulo de la ventana.
	 * 
	 * @param primaryStage {@link Stage}
	 * @param class1       extended of {@link ABean}
	 * @param title        {@link String} nombre de la ventana
	 * @throws {@link LoadAppFxmlException}
	 */
	@SuppressWarnings({ "rawtypes", "static-access" })
	public final static <S extends ADto, T extends ABean> T loadBeanFxml2(Stage primaryStage, Class<T> class1)
			throws LoadAppFxmlException {
		String file, title;
		loadApp().setStage(primaryStage);
		if (!loadApp().isStage()) {
			throw new LoadAppFxmlException(i18n.instance().get("err.stage.wasnt.entered"));
		}
		try {
			var lbean = class1.getDeclaredConstructor().newInstance();
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
			throw new LoadAppFxmlException(i18n.instance().get("err.instancing.problem"), e);
		} catch (IllegalAccessException e) {
			throw new LoadAppFxmlException(i18n.instance().get("err.ilegal.access"), e);
		} catch (IllegalArgumentException e) {
			throw new LoadAppFxmlException(i18n.instance().get("err.ilegal.argument"), e);
		} catch (InvocationTargetException e) {
			throw new LoadAppFxmlException(i18n.instance().get("err.invoke.object.problem"), e);
		} catch (NoSuchMethodException e) {
			throw new LoadAppFxmlException(i18n.instance().get("err.method.wasnt.found"), e);
		} catch (SecurityException e) {
			throw new LoadAppFxmlException(i18n.instance().get("err.security.problem"), e);
		} catch (IOException e) {
			throw new LoadAppFxmlException(i18n.valueBundle("err.io.exception").get(), e);
		}
	}

	/**
	 * Se encarga de generar una aplicacion cargada segun el bean que debe tener
	 * anotada el file fxml y el titulo de la ventana.
	 * 
	 * @param primaryStage {@link Stage}
	 * @param bean         {@link Object}
	 * @throws {@link LoadAppFxmlException}
	 */
	@SuppressWarnings("static-access")
	public final static <S extends ADto, T extends Object> T loadFxml(Stage primaryStage, Class<T> controller)
			throws LoadAppFxmlException {
		String file, title;
		loadApp().setStage(primaryStage);
		if (!loadApp().isStage()) {
			throw new LoadAppFxmlException(i18n.instance().get("err.stage.wasnt.entered"));
		}
		try {
			var fxml = controller.getAnnotation(FXMLFile.class);
			if (fxml != null) {

				file = fxml.path() + AppConstants.SLASH + fxml.file();
				title = fxml.nombreVentana();
				if (file.substring(0, 1).compareTo(AppConstants.SLASH) != 0) {
					file = AppConstants.SLASH + file;
				}
				URL url = controller.getResource(file);
				FXMLLoader loader = new FXMLLoader(url, getLanguage());
				Parent root = loader.load();
				root.autosize();
				Scene scene = new Scene(root);
				scene.getStylesheets().add(CSSConstant.CONST_PRINCIPAL);
				loadApp().getStage().setTitle(title);
				loadApp().getStage().setScene(scene);
				loadApp().getStage().sizeToScene();
				loadApp().getStage().show();
				loadApp().getStage().setOnCloseRequest(e -> {
					Platform.exit();
					System.exit(0);
				});
				return loader.getController();
			} else {
				throw new LoadAppFxmlException(i18n.instance()
						.valueBundle("err.controller.wasnt.annoted.with.fxml", controller.getName()).get());
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
			throw new LoadAppFxmlException(i18n.instance()
					.valueBundle("err.process.on.throws.nullpointer", controller.getCanonicalName()).get());
		} catch (SecurityException e) {
			throw new LoadAppFxmlException(i18n.instance().get("err.security.problem"), e);
		} catch (IOException e) {
			throw new LoadAppFxmlException(i18n.valueBundle("err.io.exception").get(), e);
		}
	}

	/**
	 * Se encarga de generar una aplicacion cargada segun el bean que debe tener
	 * anotada el file fxml y el titulo de la ventana.
	 * 
	 * @param bean  extended of {@link ABean}
	 * @param title {@link String} nombre de la ventana
	 * @throws {@link LoadAppFxmlException}
	 */
	public final static <S extends ADto, T extends ABean<S>> T loadBeanFxml(Class<T> bean) throws LoadAppFxmlException {
		return loadBeanFxml(null, bean);
	}

	public final static <L extends Pane, S extends ADto, T extends ABean<S>> T BeanFxml(L layout, Class<T> bean)
			throws LoadAppFxmlException {
		String file;

		if (!loadApp().isLastLayout()) {
			loadApp().setLastLayout(layout);
		}
		if (!loadApp().isLastLayout()) {
			throw new LoadAppFxmlException(i18n.get("err.loadappfxml.stage.does.send.and.doesnt.save"));
		}
		try {
			T lbean = bean.getDeclaredConstructor().newInstance();
			file = lbean.pathFileFxml();
			if (file.substring(0, 1).compareTo(AppConstants.SLASH) != 0) {
				file = AppConstants.SLASH + file;
			}
			URL url = bean.getResource(file);
			FXMLLoader loader = new FXMLLoader(url, getLanguage());
			Parent root = loader.load();
			loadApp().getLastLayout().getChildren().clear();
			loadApp().getLastLayout().getChildren().add(root);
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
			throw new LoadAppFxmlException(i18n.get("err.loadappfxml.field.not.found"), e);
		} catch (SecurityException e) {
			throw new LoadAppFxmlException(i18n.get("err.loadappfxml.security"), e);
		} catch (IOException e) {
			throw new LoadAppFxmlException(i18n.valueBundle("err.io.exception").get(), e);
		}
	}

	public final static <L extends Pane, S extends ADto, T extends ABean<S>> T BeanFxml(Class<T> bean)
			throws LoadAppFxmlException {
		String file;

		try {
			T lbean = bean.getDeclaredConstructor().newInstance();
			file = lbean.pathFileFxml();
			if (file.substring(0, 1).compareTo(AppConstants.SLASH) != 0) {
				file = AppConstants.SLASH + file;
			}
			URL url = bean.getResource(file);
			FXMLLoader loader = new FXMLLoader(url, getLanguage());
			return loader.getController();
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

	/**
	 * Obtiene el fxml apartir del bean y la etiqueta FXMLBean
	 * 
	 * @param bean {@link Class}
	 * @return {@link Parent}
	 * @throws {@link LoadAppFxmlException}
	 */
	private final static <S extends ADto, T extends ABean<S>> FXMLLoader loadFxml(Class<T> bean)
			throws LoadAppFxmlException {
		URL url = null;
		String file = null;
		try {
			T lBean = bean.getDeclaredConstructor().newInstance();
			file = lBean.pathFileFxml();
			if (file == null) {
				throw new LoadAppFxmlException(i18n.valueBundle("err.file.is.empty").get());
			}
			if (file.substring(0, 1).compareTo(AppConstants.SLASH) != 0) {
				file = AppConstants.SLASH + file;
			}
			url = bean.getResource(file);
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

	/**
	 * Se encarga de cargar un fxml de controlador
	 * 
	 * @param layout {@link ScrollPane}
	 * @param bean   {@link Class}
	 * @return {@link ABean}
	 * @throws {@link LoadAppFxmlException}
	 */
	public final static <L extends ScrollPane, S extends ADto, T extends ABean<S>> T BeanFxmlScroller(L layout,
			Class<T> bean) throws LoadAppFxmlException {
		if (!loadApp().isLastContro()) {
			loadApp().setLastContro(layout);
		}
		if (!loadApp().isLastContro()) {
			throw new LoadAppFxmlException(i18n.valueBundle("err.stage.dont.send.and.dont.found.save").get());
		}
		FXMLLoader loader = loadFxml(bean);
		Parent root;
		try {
			root = loader.load();
			((ScrollPane) loadApp().getLastContro()).setContent(root);
			T controller = loader.getController();
			loadApp().currentControlScroller = controller;
			return controller;
		} catch (IllegalStateException e) {
			throw new LoadAppFxmlException(i18n.valueBundle("err.can.load").get(), e);
		} catch (LoadException e) {
			e.printStackTrace();
			throw new LoadAppFxmlException(i18n.valueBundle("err.interface.cant.load.selected").get(), e);
		} catch (IOException e) {
			throw new LoadAppFxmlException(i18n.valueBundle("err.io.exception").get(), e);
		}
	}

	/**
	 * Se encarga de cargar un archivo fxml dentro de un panel indicado
	 * 
	 * @param layout
	 * @param bean   {@link Class}
	 * @return {@link ABean}
	 * @throws {@link LoadAppFxmlException}
	 */
	public final static <L extends Pane, S extends ADto, T extends ABean<S>> T beanFxmlPane(L layout, Class<T> bean)
			throws LoadAppFxmlException {
		FXMLLoader loader = loadFxml(bean);
		try {
			Parent root = loader.load();
			layout.getChildren().clear();
			layout.getChildren().add(root);
			return loader.getController();
		} catch (IOException e) {
			throw new LoadAppFxmlException(i18n.valueBundle("err.io.exception").get(), e);
		}
	}

	public static final void addCommands(KeyCombination keyboard, Runnable runnable) {
		if (loadApp().getLastContro() != null && loadApp().getLastContro().getScene() != null) {
			var accelerator = loadApp().getLastContro().getScene().getAccelerators();
			accelerator.put(keyboard, runnable);
		}
	}

	public static final <D extends ADto> void addCommandsToPopup(KeyCombination keyboard, Class<D> dto) {
		Runnable runnable = () -> {
			try {
				var popupBean = new PopupGenBean<D>(dto);
				loadBeanFX(popupBean);
			} catch (Exception e) {
				logger.logger(e);
			}
		};
		addCommands(keyboard, runnable);
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

	private final static ResourceBundle getLanguage() {
		return ResourceBundle.getBundle(AppConstants.RESOURCE_BUNDLE, Locale.getDefault());
	}

	@SuppressWarnings("unchecked")
	public static final <D extends ADto, B extends ABean<D>> B getCurrentControl() {
		return (B) loadApp().currentControlScroller;
	}
}
