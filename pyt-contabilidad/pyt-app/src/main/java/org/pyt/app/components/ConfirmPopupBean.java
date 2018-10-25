package org.pyt.app.components;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.FXMLFile;
import org.pyt.common.common.ABean;
/**
 * Se encarga de controlar el popup de errores, notificaciones y warning
 * @author Alejandro Parra
 * @since 18/10/2018
 */

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

@FXMLFile(path = "view/component", file = "confirm_popup.fxml", nombreVentana = "Confirmacion")
public class ConfirmPopupBean extends ABean {

	@FXML
	private Label mensaje;
	@FXML
	private ImageView imagen;
	@FXML
	private BorderPane panel;
	private String caller;

	@FXML
	public void initialize() {
		mensaje.setText("");
	}

	/**
	 * Se encarga de cargar el mensaje en el popup
	 * 
	 * @param mensaje
	 *            {@link String}
	 */
	public void load(String caller, String mensaje) {
		this.caller = caller;
		this.mensaje.setText(mensaje);
		loadImage();
	}

	/**
	 * Se encarag de cargar la iagen en imageview
	 * 
	 * @param tipo
	 *            {@link TIPOS}
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
	 * @param tipo
	 *            {@link TIPOS}
	 * @return {@link String}
	 */
	private final String pathImage() {
		String imagen = "";
		imagen = "confirm.png";
		return "images/" + imagen;
	}

	/**
	 * Se encarga de confirmar el popup
	 */
	@SuppressWarnings("unchecked")
	public final void confirmar() {
		try {
			close();
			if (StringUtils.isNotBlank(caller)) {
				caller(caller, true);
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
