package org.pyt.common.common;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;

import org.pyt.common.annotations.FXMLFile;
import org.pyt.common.constants.AppConstants;
import org.pyt.common.exceptions.LoadAppFxmlException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Se encarga de cargar una aplicacion
 * 
 * @author Alejandro Parra
 * @since 2018-05-22
 */
public final class LoadAppFxml  {
	private static Stage stage;
	private static Parent lastLayout;

	public final static void setStage(Stage stages) {
		if (stages != null) {
			stage = stages;
		}
	}

	public final static void cleanStage() {
		stage = null;
	}

	public final static Stage getStage() {
		return stage;
	}

	public final static Boolean isStage() {
		if (stage != null)
			return true;
		return false;
	}

	/**
	 * Se encarga de generar una aplicacion cargada segun el bean que debe tener
	 * anotada el file fxml y el titulo de la ventana.
	 * 
	 * @param primaryStage
	 *            {@link Stage}
	 * @param bean
	 *            extended of {@link ABean}
	 * @param title
	 *            {@link String} nombre de la ventana
	 * @throws {@link
	 *             LoadAppFxmlException}
	 */
	public final static <S extends ADto, T extends ABean<S>> T loadBeanFxml(Stage primaryStage, Class<T> bean)
			throws LoadAppFxmlException {
		String file, title;
		setStage(primaryStage);
		if (!isStage()) {
			throw new LoadAppFxmlException("No se envio el stage y tampoco se encuentra alamacenada.");
		}
		try {
			T lbean = bean.getDeclaredConstructor().newInstance();
			file = lbean.pathFileFxml();
			title = lbean.getNombreVentana();
			if (file.substring(0, 1).compareTo(AppConstants.SLASH) != 0) {
				file = AppConstants.SLASH + file;
			}
			URL url = bean.getResource(file);
			FXMLLoader loader = new FXMLLoader(url);
			Parent root = loader.load();
			Scene scene = new Scene(root);
			stage.setTitle(title);
			stage.setScene(scene);
			stage.show();
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
	 * @param primaryStage
	 *            {@link Stage}
	 * @param bean
	 *            {@link Object}
	 * @throws {@link
	 *             LoadAppFxmlException}
	 */
	public final static <S extends ADto, T extends Object> T loadFxml(Stage primaryStage, Class<T> controller)
			throws LoadAppFxmlException {
		String file, title;
		setStage(primaryStage);
		if (!isStage()) {
			throw new LoadAppFxmlException("No se envio el stage y tampoco se encuentra alamacenada.");
		}
		try {
			FXMLFile fxml = controller.getAnnotation(FXMLFile.class);
			if (fxml != null) {

				file = fxml.path() + AppConstants.SLASH + fxml.file();
				title = fxml.nombreVentana();
				if (file.substring(0, 1).compareTo(AppConstants.SLASH) != 0) {
					file = AppConstants.SLASH + file;
				}
				URL url = controller.getResource(file);
				FXMLLoader loader = new FXMLLoader(url);
				Parent root = loader.load();
				root.autosize();
				Scene scene = new Scene(root);
				stage.setTitle(title);
				stage.setScene(scene);
				stage.sizeToScene();
				stage.show();
				return loader.getController();
			} else {
				throw new LoadAppFxmlException("El " + controller.getName() + " no se encuentra anotado con @FXML");
			}
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
	 * @param bean
	 *            extended of {@link ABean}
	 * @param title
	 *            {@link String} nombre de la ventana
	 * @throws {@link
	 *             LoadAppFxmlException}
	 */
	public final static <S extends ADto, T extends ABean<S>> T loadBeanFxml(Class<T> bean) throws LoadAppFxmlException {
		return loadBeanFxml(null, bean);
	}

	public final static <L extends Pane, S extends ADto, T extends ABean<S>> T BeanFxml(L layout, Class<T> bean)
			throws LoadAppFxmlException {
		String file;
		if (lastLayout == null) {
			lastLayout = layout;
		}
		if (lastLayout == null) {
			throw new LoadAppFxmlException("No se envio el stage y tampoco se encuentra alamacenada.");
		}
		try {
			T lbean = bean.getDeclaredConstructor().newInstance();
			file = lbean.pathFileFxml();
			if (file.substring(0, 1).compareTo(AppConstants.SLASH) != 0) {
				file = AppConstants.SLASH + file;
			}
			URL url = bean.getResource(file);
			FXMLLoader loader = new FXMLLoader(url);
			Parent root = loader.load();
			lastLayout.getChildrenUnmodifiable().add(root);
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
}
