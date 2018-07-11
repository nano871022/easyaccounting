package org.pyt.app.beans.dinamico;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.FXMLFile;
import org.pyt.common.annotations.Inject;
import org.pyt.common.exceptions.DocumentosException;

import com.pyt.service.dto.DetalleConceptoDTO;
import com.pyt.service.dto.DocumentoDTO;
import com.pyt.service.dto.DocumentosDTO;
import com.pyt.service.dto.ParametroDTO;
import com.pyt.service.interfaces.IDocumentosSvc;
import com.pyt.service.interfaces.IParametrosSvc;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Se encarga de controlar el formulario dinamico para documentos	
 * 
 * @author Alejandro Parra
 * @since 07-07-2018
 */
@FXMLFile(path = "view/dinamico", file = "detalleConcepto.fxml")
public class DetalleConceptoBean extends DinamicoBean<DetalleConceptoDTO> {
	@Inject(resource = "com.pyt.service.implement.ParametrosSvc")
	private IParametrosSvc parametrosSvc;
	@Inject(resource = "com.pyt.service.implement.DocumentosSvc")
	private IDocumentosSvc documentoSvc;
	@FXML
	private VBox central;
	@FXML
	private Label titulo;
	private ParametroDTO tipoDocumento;

	@FXML
	public void initialize() {
		super.initialize();
		registro = new DetalleConceptoDTO();
		tipoDocumento = new ParametroDTO();
	}

	/**
	 * Se encarga de realizar la busqueda de los campos configurados para el tipo de
	 * docuumento seleccionado
	 */
	public final void loadField() {
		DocumentosDTO docs = new DocumentosDTO();
		if (tipoDocumento != null) {
			docs.setDoctype(tipoDocumento);
			docs.setClaseControlar(DocumentoDTO.class);
			try {
				campos = documentosSvc.getDocumentos(docs);
			} catch (DocumentosException e) {
				error(e);
			}
		}
		central.getChildren().clear();
		central.getChildren().add(loadGrid());
	}

	/**
	 * Se encarga de cargar un nuevo registro
	 */
	public final void load(ParametroDTO tipoDoc) {
		registro = new DetalleConceptoDTO();
		tipoDocumento = tipoDoc;
	}

	public final void load(DetalleConceptoDTO registro,ParametroDTO tipoDoc) {
		this.registro = registro;
		tipoDocumento = tipoDoc;
	}

	/**
	 * Se encarga de guardar todo
	 */
	public final void guardar() {
		loadData();
		if (valid()) {
			try {
				if (StringUtils.isNotBlank(registro.getCodigo())) {
					documentosSvc.update(registro, userLogin);
					notificar("Se agrego el nuevo detalle.");
				} else {
					registro = documentosSvc.insert(registro, userLogin);
					notificar("Se agrego el nuevo detalle.");
				}
			} catch (DocumentosException e) {
				error(e);
			}
		}
	}

	/**
	 * Se encarga de cancelar el almacenamiento de los datos
	 */
	public final void cancelar() {

	}
}
