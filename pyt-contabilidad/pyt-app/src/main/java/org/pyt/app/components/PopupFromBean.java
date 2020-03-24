package org.pyt.app.components;

import org.pyt.common.abstracts.ADto;
import org.pyt.common.constants.CSSConstant;
import org.pyt.common.exceptions.LoadAppFxmlException;

import co.com.japl.ea.beans.abstracts.ABean;
import co.com.japl.ea.beans.abstracts.APopupFromBean;
import co.com.japl.ea.utls.LoadAppFxml;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class PopupFromBean<D extends ADto, B extends ABean<D>> extends APopupFromBean<D> {

	private BorderPane panel;
	private String tittleWindowI18n;
	private Object bean;

	public PopupFromBean(Class<D> clazz) {
		super(clazz);
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void load() throws LoadAppFxmlException {
		bean = LoadAppFxml.loadBeanFxml2(primaryStage, (Class) clazz);
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
