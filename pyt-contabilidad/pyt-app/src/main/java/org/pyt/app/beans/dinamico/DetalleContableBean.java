package org.pyt.app.beans.dinamico;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.Inject;
import org.pyt.common.exceptions.DocumentosException;

import com.pyt.service.dto.DetalleContableDTO;
import com.pyt.service.dto.DocumentosDTO;
import com.pyt.service.dto.ParametroDTO;
import com.pyt.service.interfaces.IDocumentosSvc;
import com.pyt.service.interfaces.IParametrosSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 * Se encarga de controlar el formulario dinamico para documentos
 * 
 * @author Alejandro Parra
 * @since 07-07-2018
 */
@FXMLFile(path = "view/dinamico", file = "detalleContable.fxml")
public class DetalleContableBean extends DinamicoBean<DetalleContableDTO, DocumentosDTO, DocumentosDTO> {
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
	private Map<String, List> mapListSelects;

	@FXML
	public void initialize() {
		super.initialize();
		registro = new DetalleContableDTO();
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
			docs.setClaseControlar(DetalleContableDTO.class);
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
	public final void load(VBox centro, ParametroDTO tipoDoc, String codigoDocumento) {
		registro = new DetalleContableDTO();
		tipoDocumento = tipoDoc;
		this.centro = centro;
		this.codigoDocumento = codigoDocumento;
		titulo.setText(titulo.getText() + ": " + tipoDoc.getNombre());
		loadField();
	}

	public final void load(VBox centro, DetalleContableDTO registro, ParametroDTO tipoDoc, String codigoDocumento) {
		this.registro = registro;
		tipoDocumento = tipoDoc;
		this.centro = centro;
		this.codigoDocumento = codigoDocumento;
		titulo.setText(titulo.getText() + ": " + tipoDoc.getNombre());
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
					notificar("Se actualizo el detalle contable.");
				} else {
					registro.setCodigoDocumento(codigoDocumento);
					registro = documentosSvc.insert(registro, userLogin);
					notificar("Se agrego el nuevo detalle contable.");
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
			getController(centro, ListaDetalleContableBean.class).load(centro, tipoDocumento, codigoDocumento);
		} catch (Exception e) {
			error(e);
		}
	}

	@Override
	public GridPane GridPane() {
		return new GridPane();
	}

	@Override
	public Integer maxColumns() {
		return 2;
	}

	@Override
	public Map<String, List> listToChoiceBoxs() {
		return mapListSelects;
	}
}
