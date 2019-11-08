package co.com.japl.ea.utls;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import org.pyt.common.abstracts.ADto;
import org.pyt.common.common.Log;
import org.pyt.common.constants.AppConstants;
import org.pyt.common.exceptions.LoadAppFxmlException;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.ABean;
import co.com.japl.ea.beans.abstracts.AGenericToBean;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.LoadException;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.control.ScrollPane;
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
			throw new LoadAppFxmlException("No se envio el stage y tampoco se encuentra alamacenada.");
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
			throw new LoadAppFxmlException("Problema en instanciacion.", e);
		} catch (IllegalAccessException e) {
			throw new LoadAppFxmlException("Acceso ilegal.", e);
		} catch (IllegalArgumentException e) {
			throw new LoadAppFxmlException("Argumento ilegal.", e);
		} catch (InvocationTargetException e) {
			throw new LoadAppFxmlException("Problema en el objetivo de invocacion.", e);
		} catch (NoSuchMethodException e) {
			throw new LoadAppFxmlException("No se encontro el metodo.", e);
		} catch (SecurityException e) {
			throw new LoadAppFxmlException("Problema de seguridad.", e);
		} catch (IOException e) {
			throw new LoadAppFxmlException("Problema en I/O.", e);
		}
	}

	public final static <T extends AGenericToBean<D>, D extends ADto> T loadBeanFX(T genericBean)
			throws LoadAppFxmlException {
		try {
			Stage stg = new Stage();
			genericBean.start(stg);
			genericBean.initialize();

		} catch (InstantiationException e) {
			throw new LoadAppFxmlException("Problema en instanciacion.", e);
		} catch (IllegalAccessException e) {
			throw new LoadAppFxmlException("Acceso ilegal.", e);
		} catch (IllegalArgumentException e) {
			throw new LoadAppFxmlException("Argumento ilegal.", e);
		} catch (InvocationTargetException e) {
			throw new LoadAppFxmlException("Problema en el objetivo de invocacion.", e);
		} catch (NoSuchMethodException e) {
			throw new LoadAppFxmlException("No se encontro el metodo.", e);
		} catch (SecurityException e) {
			throw new LoadAppFxmlException("Problema de seguridad.", e);
		} catch (Exception e) {
			throw new LoadAppFxmlException("Problema al obtener el stage.", e);
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
	@SuppressWarnings("rawtypes")
	public final static <S extends ADto, T extends ABean> T loadBeanFxml2(Stage primaryStage, Class<T> class1)
			throws LoadAppFxmlException {
		String file, title;
		loadApp().setStage(primaryStage);
		if (!loadApp().isStage()) {
			throw new LoadAppFxmlException("No se envio el stage y tampoco se encuentra alamacenada.");
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
			throw new LoadAppFxmlException("Problema en instanciacion.", e);
		} catch (IllegalAccessException e) {
			throw new LoadAppFxmlException("Acceso ilegal.", e);
		} catch (IllegalArgumentException e) {
			throw new LoadAppFxmlException("Argumento ilegal.", e);
		} catch (InvocationTargetException e) {
			throw new LoadAppFxmlException("Problema en el objetivo de invocacion.", e);
		} catch (NoSuchMethodException e) {
			throw new LoadAppFxmlException("No se encontro el metodo.", e);
		} catch (SecurityException e) {
			throw new LoadAppFxmlException("Problema de seguridad.", e);
		} catch (IOException e) {
			throw new LoadAppFxmlException("Problema en I/O.", e);
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
	public final static <S extends ADto, T extends Object> T loadFxml(Stage primaryStage, Class<T> controller)
			throws LoadAppFxmlException {
		String file, title;
		loadApp().setStage(primaryStage);
		if (!loadApp().isStage()) {
			throw new LoadAppFxmlException("No se envio el stage y tampoco se encuentra alamacenada.");
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
				throw new LoadAppFxmlException("El " + controller.getName() + " no se encuentra anotado con @FXML");
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
			throw new LoadAppFxmlException(
					"Se presento un null pointer exception con el procesamiento de " + controller.getCanonicalName());
		} catch (SecurityException e) {
			throw new LoadAppFxmlException("Problema de seguridad.", e);
		} catch (IOException e) {
			throw new LoadAppFxmlException("Problema en I/O.", e);
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
			throw new LoadAppFxmlException("No se envio el stage y tampoco se encuentra alamacenada.");
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
			throw new LoadAppFxmlException("Problema en instanciacion.", e);
		} catch (IllegalAccessException e) {
			throw new LoadAppFxmlException("Acceso ilegal.", e);
		} catch (IllegalArgumentException e) {
			throw new LoadAppFxmlException("Argumento ilegal.", e);
		} catch (InvocationTargetException e) {
			throw new LoadAppFxmlException("Problema en el objetivo de invocacion.", e);
		} catch (NoSuchMethodException e) {
			throw new LoadAppFxmlException("No se encontro el metodo.", e);
		} catch (SecurityException e) {
			throw new LoadAppFxmlException("Problema de seguridad.", e);
		} catch (IOException e) {
			throw new LoadAppFxmlException("Problema en I/O.", e);
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
			throw new LoadAppFxmlException("Problema en instanciacion.", e);
		} catch (IllegalAccessException e) {
			throw new LoadAppFxmlException("Acceso ilegal.", e);
		} catch (IllegalArgumentException e) {
			throw new LoadAppFxmlException("Argumento ilegal.", e);
		} catch (InvocationTargetException e) {
			throw new LoadAppFxmlException("Problema en el objetivo de invocacion.", e);
		} catch (NoSuchMethodException e) {
			throw new LoadAppFxmlException("No se encontro el metodo.", e);
		} catch (SecurityException e) {
			throw new LoadAppFxmlException("Problema de seguridad.", e);
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
				throw new LoadAppFxmlException("El archivo se encuentra en vacio.");
			}
			if (file.substring(0, 1).compareTo(AppConstants.SLASH) != 0) {
				file = AppConstants.SLASH + file;
			}
			url = bean.getResource(file);
			return new FXMLLoader(url, getLanguage());
		} catch (InstantiationException e) {
			throw new LoadAppFxmlException("Problema en instanciacion.", e);
		} catch (IllegalAccessException e) {
			throw new LoadAppFxmlException("Acceso ilegal.", e);
		} catch (IllegalArgumentException e) {
			throw new LoadAppFxmlException("Argumento ilegal.", e);
		} catch (InvocationTargetException e) {
			throw new LoadAppFxmlException("Problema en el " + bean.getCanonicalName() + " de invocacion.", e);
		} catch (NoSuchMethodException e) {
			throw new LoadAppFxmlException("No se encontro el metodo.", e);
		} catch (SecurityException e) {
			throw new LoadAppFxmlException("Problema de seguridad.", e);
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
			throw new LoadAppFxmlException("No se envio el stage y tampoco se encuentra alamacenada.");
		}
		FXMLLoader loader = loadFxml(bean);
		Parent root;
		try {
			root = loader.load();
			((ScrollPane) loadApp().getLastContro()).setContent(root);
			return loader.getController();
		} catch (IllegalStateException e) {
			throw new LoadAppFxmlException("Problema en cargar load", e);
		} catch (LoadException e) {
			e.printStackTrace();
			throw new LoadAppFxmlException("No se puedde cargar la interfaz seleccionada.", e);
		} catch (IOException e) {
			throw new LoadAppFxmlException("Problema en I/O.", e);
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
			throw new LoadAppFxmlException("Problema en I/O.", e);
		}
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

	public final void setLastContro(C lastContro) {
		this.lastContro = lastContro;
	}

	public final Boolean isLastContro() {
		return lastContro != null;
	}

	private final static ResourceBundle getLanguage() {
		return ResourceBundle.getBundle(AppConstants.RESOURCE_BUNDLE, Locale.getDefault());
	}
}
