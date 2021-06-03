package co.com.japl.ea.app.components;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.common.OptI18n;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.ABean;
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
	 * @param mensaje {@link String}
	 */
	public <T> void load(T mensaje, TIPOS tipo) {
		var message = "";
		if (mensaje instanceof OptI18n) {
			message = ((OptI18n) mensaje).get();
		} else {
			message = (String) mensaje;
		}
		this.mensaje.setText(message);
		loadImage(tipo);
	}

	/**
	 * Se encarag de cargar la iagen en imageview
	 * 
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
	 * 
	 * @param tipo {@link TIPOS}
	 * @return {@link String}
	 */
	private final String pathImage(TIPOS tipo) {
		String imagen = "";
		switch (tipo) {
		case WARNING:
			imagen = "alert.png";
			break;
		case INFO:
			imagen = "ok.png";
			break;
		case ERROR:
			imagen = "error.png";
			break;
		default:
			imagen = "imagenes_popup.png";
		}
		return "images/" + imagen;
	}

	/**
	 * Se encarga de cerrar el popup
	 */
	public final void cancelar() {
		Stage stage = (Stage) panel.getScene().getWindow();
		stage.close();
		destroy();
	}

	@Override
	protected void visibleButtons() {
		// TODO Auto-generated method stub

	}
}