package org.pyt.app.beans.dinamico;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.pyt.common.annotations.FXMLFile;
import org.pyt.common.annotations.Inject;
import org.pyt.common.annotations.SubcribirToComunicacion;
import org.pyt.common.common.ABean;
import org.pyt.common.common.Comunicacion;
import org.pyt.common.constants.AppConstants;
import org.pyt.common.exceptions.DocumentosException;
import org.pyt.common.interfaces.IComunicacion;

import com.pyt.service.dto.DocumentoDTO;
import com.pyt.service.dto.DocumentosDTO;
import com.pyt.service.interfaces.IDocumentosSvc;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * + Se encarga de controlar el panel de navegacion
 * 
 * @author Alejandro Parra
 * @since 02-07-2018
 */
@FXMLFile(path = "view/dinamico", file = "panel.fxml", nombreVentana = "Panel Navegacion")
public class PanelBean extends ABean<DocumentoDTO> implements IComunicacion {
	@SuppressWarnings("rawtypes")
	@Inject
	@SubcribirToComunicacion(comando = AppConstants.COMMAND_PANEL_TIPO_DOC)
	private Comunicacion comunicacion;
	@Inject(resource = "com.pyt.service.implement.DocumentosScv")
	private IDocumentosSvc documentosSvc;
	@FXML
	private VBox left;
	@FXML
	private HBox superior;

	@FXML
	public void initialize() {
		registro = new DocumentoDTO();
	}

	public final void load(DocumentoDTO registro) {
		this.registro = registro;

	}

	public final void loadType() {
		try {
			Map<String, Class> mapa = new HashMap<String, Class>();
			DocumentosDTO dto = new DocumentosDTO();
			dto.setDoctype(registro.getTipoDocumento());
			List<DocumentosDTO> lista = documentosSvc.getDocumentos(dto);
			for (DocumentosDTO doc : lista) {
				mapa.put(doc.getClaseControlar().getSimpleName(), doc.getClaseControlar());
			}
		} catch (DocumentosException e) {
			error(e);
		}
	}

	private final void loadType(Map<String, Class> mapa) {
		Set<String> sets = mapa.keySet();
		for (String key : sets) {
			Button btn = new Button(key.replace("DTO", ""));
			btn.onActionProperty().set(e -> {
			});
			left.getChildren().add(btn);
		}
	}

	@Override
	public <T> void get(String comando, T valor) {
		switch (comando) {
		case AppConstants.COMMAND_PANEL_TIPO_DOC:
			if (valor instanceof DocumentoDTO) {
				registro = (DocumentoDTO) valor;
				loadType();
			}
			break;
		}
	}

}
