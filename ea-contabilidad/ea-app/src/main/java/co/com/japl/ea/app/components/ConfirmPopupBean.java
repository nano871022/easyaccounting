package co.com.japl.ea.app.components;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.common.OptI18n;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.app.components.PopupBean.TIPOS;
import co.com.japl.ea.beans.abstracts.ABean;
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
		mensaje.setWrapText(true);
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

	@Override
	protected void visibleButtons() {
		// TODO Auto-generated method stub

	}
}
