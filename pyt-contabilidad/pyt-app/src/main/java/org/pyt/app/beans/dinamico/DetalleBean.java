package org.pyt.app.beans.dinamico;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.Inject;
import org.pyt.common.exceptions.DocumentosException;
import org.pyt.common.exceptions.validates.ValidateValueException;

import com.pyt.service.dto.DetalleDTO;
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
	public final void load(VBox panel, ParametroDTO tipoDoc, String codigoDocumento) throws Exception {
		if (tipoDoc == null || StringUtils.isBlank(tipoDoc.getCodigo()))
			throw new Exception("No se suministro el tipo de documento.");
		if (panel == null)
			throw new Exception("El panel no se suministro.");
		registro = new DetalleDTO();
		tipoDocumento = tipoDoc;
		panelCentral = panel;
		this.codigoDocumento = codigoDocumento;
		titulo.setText(titulo.getText() + ": " + tipoDoc.getNombre());
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
			getController(panelCentral, ListaDetalleBean.class).load(panelCentral, tipoDocumento, codigoDocumento);
		} catch (Exception e) {
			error("No se logro cargar el panel de lista de detalle.");
		}
	}

	@Override
	public void methodChanges() {
		try {
			getField("valorIva");
			getField("impuestoConsumo");
			getField("valorConsumo");
			getField("valorBruto");
			getField("porcentajeIva");
			Double iVa = null;
			Double cons = null;
			if (registro.getValorIva() == null) {
				registro.setValorIva(new BigDecimal(0));
			}
			if (registro.getValorConsumo() == null) {
				registro.setValorConsumo(new BigDecimal(0));
			}
			if (registro.getPorcentajeIva() == null) {
				registro.setPorcentajeIva((long) 0);
			} else {
				iVa = registro.getPorcentajeIva().doubleValue();
				if (iVa >= 1 && iVa <= 100) {
					iVa = iVa / 100;
				}
				if (registro.getValorBruto() != null) {
					registro.setValorIva(registro.getValorBruto().multiply(getValid().cast(iVa, BigDecimal.class)));
				}
			}
			if (registro.getImpuestoConsumo() == null) {
				registro.setImpuestoConsumo((long) 0);
			} else {
				cons = registro.getImpuestoConsumo().doubleValue();
				if (cons >= 1 && cons <= 100) {
					cons = cons / 100;
				}
				if (registro.getValorBruto() != null) {
					registro.setValorConsumo(
							registro.getValorBruto().multiply(getValid().cast(cons, BigDecimal.class)));
				}
			}
			if (registro.getValorConsumo() != null && registro.getValorIva() != null
					&& registro.getValorBruto() != null) {
				registro.setValorNeto(
						registro.getValorConsumo().add(registro.getValorIva().add(registro.getValorBruto())));
			}
			putValueField("valorIva", registro.getValorIva());
			putValueField("valorConsumo", registro.getValorConsumo());
			putValueField("valorNeto", registro.getValorNeto());
		} catch (ValidateValueException e) {
			error(e);
		}
	}

	@Override
	public javafx.scene.layout.GridPane GridPane() {
		return new GridPane();
	}

	@Override
	public Integer maxColumns() {
		return 2;
	}
}
