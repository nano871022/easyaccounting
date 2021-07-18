package co.com.japl.ea.app.components;

import org.pyt.common.constants.CSSConstant;

import co.com.japl.ea.beans.abstracts.ABean;
import co.com.japl.ea.beans.abstracts.APopupFromBean;
import co.com.japl.ea.common.abstracts.ADto;
import co.com.japl.ea.exceptions.LoadAppFxmlException;
import co.com.japl.ea.utls.LoadAppFxml;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class PopupFromBean<D extends ADto, B extends ABean<D>> extends APopupFromBean<D> {

	private BorderPane panel;
	private ScrollPane scrollPanel;
	private String tittleWindowI18n;
	private Object bean;
	private Class<B> clazzBean;

	public PopupFromBean(Class<D> clazz) {
		super(clazz);
		panel = new BorderPane();
		scrollPanel = new ScrollPane();
	}

	public PopupFromBean(Class<B> clazz, Class<D> clazzDto) {
		super(clazzDto);
		clazzBean = clazz;
		panel = new BorderPane();
		scrollPanel = new ScrollPane();
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
		panel.setCenter(scrollPanel);
		scene.getStylesheets().add(CSSConstant.CONST_PRINCIPAL);
		primaryStage.setScene(scene);
		primaryStage.setMinHeight(200);
		primaryStage.hide();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void load() throws LoadAppFxmlException {
		if (AGenericsBeans.class.isAssignableFrom(clazzBean)) {
			try {
				bean = LoadAppFxml.beanFxmlGeneric(scrollPanel, (Class) clazz, (Class) clazzBean);
			} catch (Exception e) {
				logger.logger(e);
			}
		} else {
			bean = LoadAppFxml.loadBeanFxml2(primaryStage, (Class) clazz);
		}
		showWindow();
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

	public Object getBean() {
		return bean;
	}

}
