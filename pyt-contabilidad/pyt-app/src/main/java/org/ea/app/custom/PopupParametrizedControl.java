package org.ea.app.custom;

import java.io.IOException;

import org.pyt.common.common.I18n;
import org.pyt.common.common.Log;
import org.pyt.common.constants.CustomFXMLConstant;
import org.pyt.common.constants.LanguageConstant;
import org.pyt.common.interfaces.ICaller;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class PopupParametrizedControl extends HBox   {
	private Log logger = Log.Log(this.getClass());
	@FXML
	private TextField value;
	@FXML
	private Button btnClean;
	@FXML
	private Button btnOpen;
	private ICaller popupOpen;
	private ICaller cleanValue;
	private I18n languages;

	public PopupParametrizedControl() {
		languages = new I18n();
		FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource(CustomFXMLConstant.POPUP_PARAMETRIZED));
		fxmlloader.setRoot(this);
		fxmlloader.setController(this);
		try {
			fxmlloader.load();
		} catch (IOException exception) {
			logger.logger(exception);
		}

	}

	@FXML
	public void initialize() {
		value.setEditable(false);
	}

	public StringProperty textProperty() {
		return value.textProperty();
	}

	public String getText() {
		return textProperty().get();
	}

	public void setText(String value) {
		textProperty().set(value);
	}
	
	public void setPopupOpenAction(ICaller caller) {
		popupOpen = caller;
	}
	
	public void setCleanValue(ICaller caller) {
		cleanValue = caller;
	}
	
	@FXML
	public void openPopup() throws Exception {
		if (popupOpen == null) {
			throw new Exception(languages.valueBundle(LanguageConstant.POPUP_PARAMETRIZED_ERROR_OPEN_POPUP));
		}
		popupOpen.caller();
	}

	@FXML
	public void cleanValue() throws Exception {
		if (cleanValue == null) {
			throw new Exception(languages.valueBundle(LanguageConstant.POPUP_PARAMETRIZED_ERROR_CLEAN_VALUE));
		}
		cleanValue.caller();
	}
}
