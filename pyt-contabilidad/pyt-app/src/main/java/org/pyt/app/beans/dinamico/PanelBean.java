package org.pyt.app.beans.dinamico;

import static org.pyt.common.constants.CustomFXMLConstant.FILE_NAME_PANEL;
import static org.pyt.common.constants.CustomFXMLConstant.PACKAGE_DINAMIC;
import static org.pyt.common.constants.CustomFXMLConstant.WINDOW_NAME_PANEl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.controlsfx.glyphfont.FontAwesome.Glyph;
import org.pyt.common.annotations.Inject;
import org.pyt.common.annotations.SubcribirToComunicacion;
import org.pyt.common.common.Comunicacion;
import org.pyt.common.common.DtoUtils;
import org.pyt.common.constants.AppConstants;
import org.pyt.common.constants.InsertResourceConstants;
import org.pyt.common.constants.languages.Documento;
import org.pyt.common.exceptions.DocumentosException;
import org.pyt.common.interfaces.IComunicacion;

import com.pyt.service.dto.DocumentoDTO;
import com.pyt.service.dto.DocumentosDTO;
import com.pyt.service.interfaces.IDocumentosSvc;
import com.pyt.service.interfaces.IGenericServiceSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.ABean;
import co.com.japl.ea.common.button.apifluid.ButtonsImpl;
import co.com.japl.ea.dto.contabilidad.CuotaDTO;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * + Se encarga de controlar el panel de navegacion
 * 
 * @author Alejandro Parra
 * @since 02-07-2018
 */
@FXMLFile(path = PACKAGE_DINAMIC, file = FILE_NAME_PANEL, nombreVentana = WINDOW_NAME_PANEl)
public class PanelBean extends ABean<DocumentoDTO> implements IComunicacion {
	@SuppressWarnings("rawtypes")
	@Inject
	@SubcribirToComunicacion(comando = AppConstants.COMMAND_PANEL_TIPO_DOC)
	private Comunicacion comunicacion2;
	@Inject(resource = InsertResourceConstants.CONST_RESOURCE_IMPL_SVC_DOCUMENTOS_SVC)
	private IDocumentosSvc documentosSvc;
	@Inject
	private IGenericServiceSvc<CuotaDTO> paymentSvc;
	@FXML
	private VBox left;
	@FXML
	private VBox central;
	@FXML
	private HBox superior;
	@SuppressWarnings("rawtypes")
	private Map<String, Class> mapa;
	private BooleanProperty detailBoolean;
	private BooleanProperty detailAccountBoolean;
	private BooleanProperty paymentBoolean;

	@FXML
	public void initialize() {
		detailBoolean = new SimpleBooleanProperty(false);
		detailAccountBoolean = new SimpleBooleanProperty(false);
		paymentBoolean = new SimpleBooleanProperty(false);
		registro = new DocumentoDTO();
		var min = 160;
		var max = 240;
		ButtonsImpl.Stream(VBox.class).widthMinMaxAllBtn(min, max).setLayout(left).setName("fxml.btn.document")
				.action(this::documentAction).action(this::documentAction).BR().setName("fxml.btn.detail")
				.action(this::detailAction).isVisible(detailBoolean).BR().setName("fxml.btn.accounting.detail")
				.action(this::detailAccountingAction).isVisible(detailAccountBoolean).BR().setName("fxml.btn.pay")
				.action(this::payment).isVisible(paymentBoolean).BR().setName("fxml.btn.return").BR()
				.action(() -> getController(ListaDocumentosBean.class)
						.loadParameters(registro.getTipoDocumento().getValor2()))
				.icon(Glyph.BACKWARD).build();
	}

	public final void load(DocumentoDTO registro) {
		this.registro = registro;
		if (registro != null && StringUtils.isNotBlank(registro.getCodigo())) {
			loadType();
			getController(central, DocumentoBean.class).load(registro);
			validQuotes();
			visibleButtons();
		} else if (registro == null || StringUtils.isBlank(registro.getCodigo())) {
			getController(central, DocumentoBean.class).load(registro.getTipoDocumento());
		}
	}

	private void validQuotes() {
		try {
			CuotaDTO filter = new CuotaDTO();
			filter.setDocumento(registro);
			var count = paymentSvc.getTotalRows(filter);
			paymentBoolean.set(count > 0);
		} catch (Exception e) {
			error(e);
		}
	}

	@SuppressWarnings("rawtypes")
	public final void loadType() {
		try {
			mapa = new HashMap<String, Class>();
			DocumentosDTO dto = new DocumentosDTO();
			dto.setDoctype(registro.getTipoDocumento());
			documentosSvc.getDocumentos(dto).stream()
					.filter(doc -> !doc.getClaseControlar().getSimpleName().contains("DetalleConceptoDTO"))
					.forEach(doc -> mapa.put(doc.getClaseControlar().getSimpleName(), doc.getClaseControlar()));
		} catch (DocumentosException e) {
			error(e);
		}
	}

	public void documentAction() {
		if (DtoUtils.haveCode(registro)) {
			getController(central, DocumentoBean.class).load(registro);
		} else {
			getController(central, DocumentoBean.class).load(registro.getTipoDocumento());
		}
	}

	public void detailAction() {
		try {
			if (DtoUtils.haveCode(registro)) {
				getController(central, ListaDetalleBean.class).load(central, registro.getTipoDocumento(),
						registro.getCodigo());
			}
		} catch (Exception e1) {
			errorI18n(Documento.CONST_ERR_LOAD_LIST_DETAIL);
		}
	}

	public void detailAccountingAction() {
		try {
			if (DtoUtils.haveCode(registro)) {
				getController(central, ListaDetalleContableBean.class).load(central, registro.getTipoDocumento(),
						registro.getCodigo());
			}
		} catch (Exception e1) {
			errorI18n(Documento.CONST_ERR_LOAD_LIST_DET_COUNT);
		}
	}

	public void payment() {
		try {
			if (DtoUtils.haveCode(registro)) {
				getController(central, ListCuotaBean.class).load(registro);
			}
		} catch (Exception e) {
			errorI18n(Documento.CONST_ERR_LOAD_LIST_PAYMENT);
		}

	}

	@Override
	public <T> void get(String comando, T valor) {
		switch (comando) {
		case AppConstants.COMMAND_PANEL_TIPO_DOC:
			if (valor instanceof DocumentoDTO) {
				registro = (DocumentoDTO) valor;
				getController(PanelBean.class).load(registro);
			}
			break;
		}
	}

	@Override
	protected void visibleButtons() {
		var valid = DtoUtils.haveCode(registro);
		detailBoolean.setValue(valid);
		detailAccountBoolean.setValue(valid);
	}

}
