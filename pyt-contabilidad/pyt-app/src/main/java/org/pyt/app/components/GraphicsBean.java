package org.pyt.app.components;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.ABean;
import javafx.fxml.FXML;

@FXMLFile(path = "view/component", file = "graphics.fxml", nombreVentana = "Graficas")
public class GraphicsBean extends ABean {

	@FXML
	public void initialize() {
	}

	/**
	 * Se encarga de cargar el mensaje en el popup
	 * 
	 * @param mensaje {@link String}
	 */
	public void load() {
	}

	@Override
	protected void visibleButtons() {
		// TODO Auto-generated method stub

	}

}
