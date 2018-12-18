package org.pyt.app.beans.dinamico;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.ADto;
import org.pyt.common.common.SelectList;
import org.pyt.common.common.ValidateValues;
import org.pyt.common.constants.AppConstants;
import org.pyt.common.constants.ParametroConstants;
import org.pyt.common.exceptions.DocumentosException;
import org.pyt.common.exceptions.ParametroException;
import org.pyt.common.exceptions.ReflectionException;
import org.pyt.common.exceptions.validates.ValidateValueException;

import com.pyt.service.dto.DetalleContableDTO;
import com.pyt.service.dto.DetalleDTO;
import com.pyt.service.dto.DocumentoDTO;
import com.pyt.service.dto.DocumentosDTO;
import com.pyt.service.dto.ParametroDTO;
import com.pyt.service.interfaces.IDocumentosSvc;
import com.pyt.service.interfaces.IParametrosSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Se encarga de controlar el formulario dinamico para documentos
 * 
 * @author Alejandro Parra
 * @since 07-07-2018
 */
@FXMLFile(path = "view/dinamico", file = "formulario.fxml",name="DocumentoDinamico")
public class DocumentoBean extends DinamicoBean<DocumentoDTO> {
	@Inject(resource = "com.pyt.service.implement.ParametrosSvc")
	private IParametrosSvc parametrosSvc;
	@Inject(resource = "com.pyt.service.implement.DocumentosSvc")
	private IDocumentosSvc documentoSvc;
	@FXML
	private VBox central;
	@FXML
	private Label titulo;
	@FXML
	private ChoiceBox<String> tipoDocumentos;
	private ParametroDTO tipoDocumento;
	private List<ParametroDTO> listTipoDocumento;
	private ValidateValues valid;

	@FXML
	public void initialize() {
		super.initialize();
		valid = new ValidateValues();
		registro = new DocumentoDTO();
		tipoDocumento = new ParametroDTO();
		try {
			listTipoDocumento = parametrosSvc.getAllParametros(tipoDocumento, ParametroConstants.GRUPO_TIPO_DOCUMENTO);
		} catch (ParametroException e) {
			error(e);
		}
		tipoDocumento = new ParametroDTO();
		tipoDocumentos.onActionProperty().set(e -> loadField());
		SelectList.put(tipoDocumentos, listTipoDocumento, FIELD_NAME);
		titulo.setText("");
	}

	/**
	 * Se encarga de realizar la busqueda de los campos configurados para el tipo de
	 * docuumento seleccionado
	 */
	public final void loadField() {
		DocumentosDTO docs = new DocumentosDTO();
		ParametroDTO tipoDoc = SelectList.get(tipoDocumentos, listTipoDocumento, FIELD_NAME);
		if (tipoDoc != null) {
			docs.setDoctype(tipoDoc);
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
	public final void load() {
		registro = new DocumentoDTO();
	}

	public final void load(DocumentoDTO registro) {
		try {
			this.registro = registro;
			BigDecimal valores = sumaDetalles();
			this.registro.setValor(valores);
			SelectList.selectItem(tipoDocumentos, listTipoDocumento, FIELD_NAME, registro.getTipoDocumento().getValor(),
					"valor");
			documentosSvc.update(registro, userLogin);
		} catch (DocumentosException e) {
			error(e);
		}
	}

	private final BigDecimal sumaDetalles() {
		BigDecimal suma = new BigDecimal(0);
		try {
			DetalleDTO detalle = new DetalleDTO();
			detalle.setCodigoDocumento(registro.getCodigo());
			DetalleContableDTO detalleContable = new DetalleContableDTO();
			detalleContable.setCodigoDocumento(registro.getCodigo());
			List<DetalleDTO> listDetalles = documentosSvc.getAllDetalles(detalle);
			suma = suma.add(suma(listDetalles, "valorNeto"));
			List<DetalleContableDTO> listDetContable = documentosSvc.getAllDetalles(detalleContable);
		} catch (DocumentosException e) {
			error(e);
		}
		return suma;
	}

	private final <T extends ADto> BigDecimal suma(List<T> list, String nombre) {
		BigDecimal suma = new BigDecimal(0);
		BigDecimal valor = null;
		try {
			for (T dto : list) {
				valor = valid.cast(dto.get(nombre), BigDecimal.class);
				if (valor != null) {
					suma = suma.add(valor);
				}
			}
		} catch (ReflectionException e) {
			error(e);
		} catch (ValidateValueException e) {
			error(e);
		}
		return suma;
	}

	/**
	 * Se encarga de guardar todo
	 */
	@SuppressWarnings("unchecked")
	public final void guardar() {
		loadData();
		if (valid()) {
			try {
				registro.setTipoDocumento(SelectList.get(tipoDocumentos, listTipoDocumento, FIELD_NAME));
				if (StringUtils.isNotBlank(registro.getCodigo())) {
					documentosSvc.update(registro, userLogin);
					notificar("Se agrego el nuevo documento.");
				} else {
					registro = documentosSvc.insert(registro, userLogin);
					notificar("Se agrego el nuevo documento.");
				}
				comunicacion.setComando(AppConstants.COMMAND_PANEL_TIPO_DOC, registro);
			} catch (DocumentosException e) {
				error(e);
			}
		}
	}

	/**
	 * Se encarga de cancelar el almacenamiento de los datos
	 */
	public final void cancelar() {
       getController(ListaDocumentosBean.class);
	}
}
