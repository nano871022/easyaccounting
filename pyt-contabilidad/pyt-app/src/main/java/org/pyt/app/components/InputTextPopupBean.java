package org.pyt.app.components;

import org.apache.commons.lang3.StringUtils;
import org.pyt.app.components.PopupBean.TIPOS;
import org.pyt.common.common.OptI18n;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.ABean;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

@FXMLFile(path = "view/component", file = "inputText_popup.fxml", nombreVentana = "Confirmacion")
public class InputTextPopupBean extends ABean {

	@FXML
	private TextArea mensaje;
	@FXML
	private TextArea textEntered;
	@FXML
	private ImageView imagen;
	@FXML
	private BorderPane panel;
	private String caller;

	@FXML
	public void initialize() {
		mensaje.setText("");
		textEntered.setText("");
	}

	/**
	 * Se encarga de cargar el mensaje en el popup
	 * 
	 * @param mensaje {@link String}
	 */
	public void load(String caller, OptI18n mensaje) {
		load(caller, mensaje.get());
	}

	public void load(String caller, String mensaje) {
		this.caller = caller;
		this.mensaje.setText(mensaje);
		loadImage();
	}

	/**
	 * Se encarag de cargar la iagen en imageview
	 * 
	 * @param tipo {@link TIPOS}
	 */
	private void loadImage() {
		String imagen = pathImage();
		if (StringUtils.isNotBlank(imagen)) {
			Image image = new Image(imagen);
			this.imagen.setImage(image);
		}
	}

	/**
	 * Se encarga de obtener el nombre de la imagen a cargar segun el tipo
	 * 
	 * @param tipo {@link TIPOS}
	 * @return {@link String}
	 */
	private final String pathImage() {
		return null;
	}

	/**
	 * Se encarga de confirmar el popup
	 */
	@SuppressWarnings("unchecked")
	public final void accept() {
		try {
			close();
			if (StringUtils.isNotBlank(caller)) {
				String value = null;
				if (StringUtils.isNotBlank(textEntered.getText())) {
					value = textEntered.getText();
				}
				caller(caller, value);
			}
		} catch (Exception e) {
			error(e);
		}
	}

	/**
	 * Se encarga de cerrar el popup
	 */
	public final void cancelar() {
		try {
			close();
			if (StringUtils.isNotBlank(caller)) {
				caller(caller, false);
			}
		} catch (Exception e) {
			error(e);
		}
	}

	/**
	 * Se ecnarga de cerrar el popup
	 */
	private final void close() {
		Stage stage = (Stage) panel.getScene().getWindow();
		stage.close();
	}
}
