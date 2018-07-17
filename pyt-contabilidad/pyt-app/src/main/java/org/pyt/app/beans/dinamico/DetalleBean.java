package org.pyt.app.beans.dinamico;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.FXMLFile;
import org.pyt.common.annotations.Inject;
import org.pyt.common.exceptions.DocumentosException;

import com.pyt.service.dto.DetalleDTO;
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
@FXMLFile(path = "view/dinamico", file = "detalle.fxml")
public class DetalleBean extends DinamicoBean<DetalleDTO> {
	@Inject(resource = "com.pyt.service.implement.ParametrosSvc")
	private IParametrosSvc parametrosSvc;
	@Inject(resource = "com.pyt.service.implement.DocumentosSvc")
	private IDocumentosSvc documentoSvc;
	@FXML
	private VBox central;
	@FXML
	private Label titulo;
	private ParametroDTO tipoDocumento;
	private VBox panelCentral;
	private String codigoDocumento;

	@FXML
	public void initialize() {
		super.initialize();
		registro = new DetalleDTO();
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
			docs.setClaseControlar(DetalleDTO.class);
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
	public final void load(VBox panel, ParametroDTO tipoDoc,String codigoDocumento) throws Exception {
		if (tipoDoc == null || StringUtils.isBlank(tipoDoc.getCodigo()))
			throw new Exception("No se suministro el tipo de documento.");
		if (panel == null)
			throw new Exception("El panel no se suministro.");
		registro = new DetalleDTO();
		tipoDocumento = tipoDoc;
		panelCentral = panel;
		this.codigoDocumento = codigoDocumento;
		loadField();
	}

	public final void load(VBox panel, DetalleDTO registro, ParametroDTO tipoDoc) throws Exception {
		if (tipoDoc == null || StringUtils.isBlank(tipoDoc.getCodigo()))
			throw new Exception("No se suministro el tipo de documento.");
		if (registro == null || StringUtils.isBlank(registro.getCodigo()))
			throw new Exception("No se suministro el detalle a modificar.");
		if (panel == null)
			throw new Exception("El panel no se suministro.");
		this.registro = registro;
		tipoDocumento = tipoDoc;
		codigoDocumento = registro.getCodigoDocumento();
		panelCentral = panel;
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
					registro.setCodigoDocumento(codigoDocumento);
					documentosSvc.update(registro, userLogin);
					notificar("Se actualizo el detalle.");
					loadField();
				} else {
					registro.setCodigoDocumento(codigoDocumento);
					registro = documentosSvc.insert(registro, userLogin);
					notificar("Se agrego el nuevo detalle.");
					loadField();
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
			getController(panelCentral, ListaDetalleBean.class).load(panelCentral, tipoDocumento,codigoDocumento);
		} catch (Exception e) {
			error("No se logro cargar el panel de lista de detalle.");
		}
	}
}
