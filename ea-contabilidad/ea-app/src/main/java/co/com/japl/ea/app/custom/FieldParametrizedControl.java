package co.com.japl.ea.app.custom;

import java.io.IOException;
import java.util.List;

import org.pyt.common.common.Log;
import org.pyt.common.common.SelectList;
import org.pyt.common.constants.CustomFXMLConstant;

import co.com.japl.ea.common.abstracts.ADto;
import co.com.japl.ea.common.interfaces.ICaller;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.HBox;

/**
 * Se crea campo personalizado para mostrar un {@link ChoiceBox} o el custom
 * {@link popupParametrizedControl}
 * 
 * @author josepals
 *
 */
public class FieldParametrizedControl<T extends ADto> extends HBox {
	private Log logger = Log.Log(this.getClass());
	@FXML
	private ChoiceBox<T> cbValues;
	@FXML
	private PopupParametrizedControl popupParam;
	private Boolean isPopup;

	public FieldParametrizedControl() {
		FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource(CustomFXMLConstant.FIELD_PARAMETRIZED));
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
		isPopup = true;
		cbValues.setVisible(false);
		popupParam.setVisible(false);
	}

	public StringProperty textProperty() {
		if (isPopup) {
			return popupParam.textProperty();
		}
		return null;
	}

	public String getText() {
		if (isPopup) {
			return textProperty().get();
		}
		return null;
	}

	public void setText(String value) {
		if (isPopup) {
			textProperty().set(value);
		}
	}

	public void setList(List<T> list) {
		if (!isPopup) {
			SelectList.put(cbValues, list);
		}
	}

	public T selected() {
		if (!isPopup) {
			return SelectList.get(cbValues);
		}
		return null;
	}

	public Boolean isSelected() {
		if (!isPopup) {
			return SelectList.get(cbValues) != null;
		}
		return false;
	}

	public void setPopupOpenAction(ICaller caller) {
		popupParam.setPopupOpenAction(caller);
	}

	public void setCleanValue(ICaller caller) {
		popupParam.setCleanValue(caller);
	}

	public void setIsPopup(Boolean isPopup) {
		this.isPopup = isPopup;
	}

}
