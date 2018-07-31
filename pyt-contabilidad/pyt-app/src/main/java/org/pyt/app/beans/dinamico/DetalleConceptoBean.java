package org.pyt.app.beans.dinamico;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.FXMLFile;
import org.pyt.common.annotations.Inject;
import org.pyt.common.exceptions.DocumentosException;

import com.pyt.service.dto.DetalleConceptoDTO;
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
	private VBox centro;
	@FXML
	private Label titulo;
	private ParametroDTO tipoDocumento;
	private String codigoDocumento;

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
			docs.setClaseControlar(DetalleConceptoDTO.class);
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
	public final void load(VBox central, ParametroDTO tipoDoc, String codigoDocumento) {
		registro = new DetalleConceptoDTO();
		tipoDocumento = tipoDoc;
		this.codigoDocumento = codigoDocumento;
		this.centro = central;
		loadField();
	}

	public final void load(VBox central, DetalleConceptoDTO registro, ParametroDTO tipoDoc) {
		this.registro = registro;
		tipoDocumento = tipoDoc;
		this.centro = central;
		loadField();
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
					notificar("Se actualizo el detalle concepto.");
				} else {
					registro.setCodigoDocumento(codigoDocumento);
					registro = documentosSvc.insert(registro, userLogin);
					notificar("Se agrego el nuevo detalle concepto.");
				}
			} catch (DocumentosException e) {
				error(e);
			}
		}
	}

	/**
	 * Se encarga de cancelar el almacenamiento de los datos
	 */
	public final void regresar() {
		try {
			getController(centro, ListaDetalleConceptoBean.class).load(centro, tipoDocumento, codigoDocumento);
		} catch (Exception e) {
			error(e);
		}
	}
}
