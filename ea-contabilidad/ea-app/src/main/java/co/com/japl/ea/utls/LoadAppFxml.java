package co.com.japl.ea.utls;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;

import org.pyt.common.constants.AppConstants;
import org.pyt.common.constants.CSSConstant;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.app.components.ListGenericBeans;
import co.com.japl.ea.app.components.PopupFromBean;
import co.com.japl.ea.app.components.PopupGenBean;
import co.com.japl.ea.app.load.Template;
import co.com.japl.ea.beans.abstracts.ABean;
import co.com.japl.ea.beans.abstracts.AGenericToBean;
import co.com.japl.ea.beans.abstracts.APopupFromBean;
import co.com.japl.ea.common.abstracts.ADto;
import co.com.japl.ea.exceptions.LoadAppFxmlException;
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
public final class LoadAppFxml<P extends Pane, C extends Control> extends AppFXML<P, C> {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public final static <S extends ADto, T extends ABean> T loadBeanFxml2(Stage stage, Class<T> clazz)
			throws LoadAppFxmlException {
		return (T) beanFxmlBasic(stage, clazz);
	}

	public final static <S extends ADto, T extends ABean<S>> T loadBeanFxml(Stage stage, Class<T> clazz)
			throws LoadAppFxmlException {
		return beanFxmlBasic(stage, clazz);
	}

	public final static <S extends ADto, T extends ABean<S>> T loadBeanFxml(Class<T> bean) throws LoadAppFxmlException {
		return loadBeanFxml(null, bean);
	}

	public final static <S extends ADto, T extends APopupFromBean<S>> T loadBeanPopup(Stage stage, Class<T> clazz)
			throws LoadAppFxmlException {
		return beanFxmlBasic(stage, clazz);
	}

	public final static <L extends Pane, S extends ADto, T extends ABean<S>> T BeanFxml(Class<T> bean)
			throws LoadAppFxmlException {
		try {
			return loadFxml(bean).getController();
		} catch (IllegalArgumentException e) {
			throw new LoadAppFxmlException(i18n.valueBundle("err.ilegal.argument").get(), e);
		} catch (SecurityException e) {
			throw new LoadAppFxmlException(i18n.valueBundle("err.security").get(), e);
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
			genericBean.setStage(stg);
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
	 * @param bean         {@link Object}
	 * @throws {@link LoadAppFxmlException}
	 */
	@SuppressWarnings("static-access")
	public final static <S extends ADto, T extends Object> T loadFxml(Stage primaryStage, Class<T> controller)
			throws LoadAppFxmlException {
		loadApp().setStage(primaryStage);
		if (!loadApp().isStage()) {
			throw new LoadAppFxmlException(i18n.instance().get("err.stage.wasnt.entered"));
		}
		if (controller == Template.class) {
			loadApp().stageTemplate = primaryStage;
		}
		try {
			var fxml = controller.getAnnotation(FXMLFile.class);
			if (fxml != null) {

				String file = fxml.path() + AppConstants.SLASH + fxml.file();
				String title = fxml.nombreVentana();
				loadTitleWindow(primaryStage, title);
				if (file.substring(0, 1).compareTo(AppConstants.SLASH) != 0) {
					file = AppConstants.SLASH + file;
				}
				URL url = controller.getResource(file);
				FXMLLoader loader = new FXMLLoader(url, getLanguage());
				Parent root = loader.load();
				root.autosize();
				Scene scene = new Scene(root);
				scene.getStylesheets().add(CSSConstant.CONST_PRINCIPAL);
				loadApp().getStage().setScene(scene);
				loadApp().getStage().sizeToScene();
				loadApp().getStage().show();
				loadApp().getStage().setOnCloseRequest(e -> {
					Platform.exit();
					System.exit(0);
				});
				callPostLoad(loader.getController());
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

	public final static <L extends Pane, S extends ADto, T extends ABean<S>> T BeanFxml(L layout, Class<T> bean)
			throws LoadAppFxmlException {
		if (!loadApp().isLastLayout()) {
			loadApp().setLastLayout(layout);
		}
		if (!loadApp().isLastLayout()) {
			throw new LoadAppFxmlException(i18n.get("err.loadappfxml.stage.does.send.and.doesnt.save"));
		}
		try {
			T lbean = bean.getDeclaredConstructor().newInstance();
			String file = lbean.pathFileFxml();
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

	public final static <L extends ScrollPane, S extends ADto, T extends ABean<S>> T BeanFxmlScroller(L layout,
			Class<T> bean) throws LoadAppFxmlException {
		if (!loadApp().isLastContro()) {
			loadApp().setLastContro(layout);
		}
		if (!loadApp().isLastContro()) {
			throw new LoadAppFxmlException(i18n.valueBundle("err.stage.dont.send.and.dont.found.save").get());
		}
		FXMLLoader loader = loadFxml(bean);
		try {
			Parent root = loader.load();
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

	public static final <D extends ADto> void addCommandsToPopup(KeyCombination keyboard, Class<D> dto) {
		Runnable runnable = () -> {
			try {
				var popupBean = new PopupGenBean<D>(dto);
				loadBeanFX(popupBean);
				popupBean.load("");
			} catch (Exception e) {
				logger.logger(e);
			}
		};
		addCommands(keyboard, runnable);
	}

	public static <S extends ADto, N extends ScrollPane, L extends ListGenericBeans<S>> L beanFxmlGeneric(N layout,
			Class<S> dto, Class<L> class1) throws Exception {
		String file, title;
		if (!loadApp().isLastContro()) {
			loadApp().setLastContro(layout);
		}
		if (!loadApp().isLastContro()) {
			throw new LoadAppFxmlException(i18n.valueBundle("err.stage.dont.send.and.dont.found.save").get());
		}
		try {
			L lbean = class1.getDeclaredConstructor().newInstance();
			title = lbean.getNombreVentana();
			Parent root = lbean.load(dto);
			((ScrollPane) loadApp().getLastContro()).setContent(root);
			loadApp().currentControlScroller = lbean;
			lbean.initialize();
			return lbean;
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

}
