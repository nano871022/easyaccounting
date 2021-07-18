package co.com.japl.ea.app.components;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.common.OptI18n;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.ABean;
import co.com.japl.ea.dto.system.LanguagesDTO;
import co.com.japl.ea.exceptions.LoadAppFxmlException;
import co.com.japl.ea.utls.LoadAppFxml;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
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
	private Label mensaje;
	@FXML
	private ImageView imagen;
	@FXML
	private BorderPane panel;

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
	@SuppressWarnings("unchecked")
	public <T> void load(T mensaje, TIPOS tipo) {
		var message = "";
		if (mensaje instanceof OptI18n e) {
			if (!e.isFound()) {
				this.mensaje.setOnMouseClicked(event -> {
					try {
						if (event.getClickCount() == 2) {
							var popup = new PopupFromBean(GenericBeans.class, LanguagesDTO.class);
							try {
								LoadAppFxml.loadBeanFX(popup);
								GenericBeans bean = (GenericBeans) popup.getBean();
								bean.openPopup();
								var dto = bean.addValueToField(e.get(), "code");
								bean.load(dto);
							} catch (LoadAppFxmlException | SecurityException ex) {
								logger().logger(ex);
							} catch (Exception ex) {
								logger().logger(ex);
							}
						}
					} catch (Exception ex) {
						error(ex);
					}
				});
			}
			message = e.get();
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
