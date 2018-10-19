package org.pyt.app.components;

import org.pyt.common.annotations.FXMLFile;
import org.pyt.common.common.ABean;
/**
 * Se encarga de controlar el popup de errores, notificaciones y warning
 * @author Alejandro Parra
 * @since 18/10/2018
 */

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
@FXMLFile(path = "view/component", file = "popup.fxml")
public class PopupBean extends ABean {
	public static enum TIPOS  {WARNING,INFO,ERROR};
	@FXML
	private Label mensaje;
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
	 * @param mensaje {@link String}
	 */
	public void load(String mensaje,TIPOS tipo) {
		this.mensaje.setText(mensaje);
		loadImage(tipo);
	}
	
	private void loadImage(TIPOS tipo) {
		
	}
	
	public final void cancelar() {
		Stage stage = (Stage)panel.getScene().getWindow();
		stage.close();
	}
}
