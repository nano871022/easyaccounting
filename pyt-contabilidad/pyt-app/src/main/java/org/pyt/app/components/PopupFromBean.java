package org.pyt.app.components;

import org.pyt.common.abstracts.ADto;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.Comunicacion;
import org.pyt.common.common.Log;
import org.pyt.common.constants.CSSConstant;
import org.pyt.common.exceptions.LoadAppFxmlException;

import co.com.japl.ea.beans.abstracts.ABean;
import co.com.japl.ea.interfaces.IBean;
import co.com.japl.ea.interfaces.INotificationMethods;
import co.com.japl.ea.utls.LoadAppFxml;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class PopupFromBean<D extends ADto, B extends ABean<D>> implements IBean<D>, INotificationMethods {

	private Class<D> clazz;
	private Stage primaryStage;
	private Double width;
	private Double height;
	private String tittleWindowI18n;
	@SuppressWarnings("rawtypes")
	@Inject
	private Comunicacion comunicacion;
	private BorderPane panel;
	private Log logger = Log.Log(this.getClass());
	private Object bean;

	public PopupFromBean(Class<D> clazz) {
		this.clazz = clazz;
		panel = new BorderPane();
	}

	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		if (width != null) {
			primaryStage.setWidth(width);
		}
		if (height != null) {
			primaryStage.setHeight(height);
		}
		primaryStage.setTitle(i18n().valueBundle(tittleWindowI18n).get());
		Scene scene = new Scene(panel);
		scene.getStylesheets().add(CSSConstant.CONST_PRINCIPAL);
		primaryStage.setScene(scene);
		primaryStage.hide();
	}

	public void initialize() throws LoadAppFxmlException {
		bean = LoadAppFxml.loadBeanFxml2(primaryStage, (Class) clazz);
		showWindow();
	}

	public void showWindow() {
		primaryStage.show();
	}

	public void sizeWindow(Integer width, Integer height) {
		this.width = width.doubleValue();
		this.height = height.doubleValue();
	}

	@Override
	public Log logger() {
		return logger;
	}

	@Override
	public Comunicacion comunicacion() {
		return comunicacion;
	}

	/**
	 * Se encarga la ventana que esta cargando este bean generico, si es un popup lo
	 * cierra, pero si se carga sobre la aplicacion principal la cierra tambien.
	 */
	protected final void closeWindow() {
		primaryStage.close();
	}

	@Override
	public D getRegistro() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setRegistro(D registro) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getNombreVentana() {
		return tittleWindowI18n;
	}

	@Override
	public void setNombreVentana(String nombreVentana) {
		tittleWindowI18n = nombreVentana;
	}

	@Override
	public <T> T meThis() {
		return (T) this;
	}

	public Object getBean() {
		return bean;
	}

}
