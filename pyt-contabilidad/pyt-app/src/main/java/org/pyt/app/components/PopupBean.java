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
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

@SuppressWarnings("rawtypes")
@FXMLFile(path = "view/component", file = "popup.fxml", nombreVentana = "")
public class PopupBean extends ABean {
	public static enum TIPOS {
		WARNING, INFO, ERROR
	};

	@FXML
	private TextArea mensaje;
	@FXML
	private ImageView imagen;
	@FXML
	private BorderPane panel;

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
	public void load(String mensaje, TIPOS tipo) {
		this.mensaje.setText(mensaje);
		loadImage(tipo);
	}
	/**
	 * Se encarag de cargar la iagen en imageview
	 * @param tipo {@link TIPOS}
	 */
	private void loadImage(TIPOS tipo) {
		String imagen = pathImage(tipo);
		if (StringUtils.isNotBlank(imagen)) {
			Image image = new Image(imagen);
			this.imagen.setImage(image);
		}
	}
	/**
	 * Se encarga de obtener el nombre de la imagen a cargar segun el tipo
	 * @param tipo {@link TIPOS}
	 * @return {@link String}
	 */
	private final String pathImage(TIPOS tipo) {
		String imagen = "";
		switch(tipo) {
			case WARNING: imagen = "alert.png";break;
			case INFO: imagen = "ok.png";break;
			case ERROR: imagen = "error.png";break;
			default:
				imagen = "imagenes_popup.png";
		}
		return "images/"+imagen;
	}
	/**
	 * Se encarga de cerrar el popup
	 */
	public final void cancelar() {
		Stage stage = (Stage) panel.getScene().getWindow();
		stage.close();
		destroy();
	}
}
