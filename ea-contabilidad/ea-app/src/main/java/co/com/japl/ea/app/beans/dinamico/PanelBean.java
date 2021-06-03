package co.com.japl.ea.app.beans.dinamico;

import static org.pyt.common.constants.CustomFXMLConstant.FILE_NAME_PANEL;
import static org.pyt.common.constants.CustomFXMLConstant.PACKAGE_DINAMIC;
import static org.pyt.common.constants.CustomFXMLConstant.WINDOW_NAME_PANEl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.controlsfx.glyphfont.Glyph;
import org.pyt.common.annotations.Inject;
import org.pyt.common.annotations.SubcribirToComunicacion;
import org.pyt.common.common.Comunicacion;
import org.pyt.common.constants.AppConstants;
import org.pyt.common.constants.InsertResourceConstants;
import org.pyt.common.constants.languages.Documento;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.ABean;
import co.com.japl.ea.common.interfaces.IComunicacion;
import co.com.japl.ea.dto.dto.DetalleContableDTO;
import co.com.japl.ea.dto.dto.DetalleDTO;
import co.com.japl.ea.dto.dto.DocumentoDTO;
import co.com.japl.ea.dto.dto.DocumentosDTO;
import co.com.japl.ea.dto.interfaces.IDocumentosSvc;
import co.com.japl.ea.exceptions.DocumentosException;
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
@FXMLFile(path = PACKAGE_DINAMIC, file = FILE_NAME_PANEL, nombreVentana = WINDOW_NAME_PANEl)
public class PanelBean extends ABean<DocumentoDTO> implements IComunicacion {
	@SuppressWarnings("rawtypes")
	@Inject
	@SubcribirToComunicacion(comando = AppConstants.COMMAND_PANEL_TIPO_DOC)
	private Comunicacion comunicacion2;
	@Inject(resource = InsertResourceConstants.CONST_RESOURCE_IMPL_SVC_DOCUMENTOS_SVC)
	private IDocumentosSvc documentosSvc;
	@FXML
	private VBox left;
	@FXML
	private VBox central;
	@FXML
	private HBox superior;
	@SuppressWarnings("rawtypes")
	private Map<String, Class> mapa;

	@FXML
	public void initialize() {
		registro = new DocumentoDTO();
	}

	public final void load(DocumentoDTO registro) {
		this.registro = registro;
		if (registro != null && StringUtils.isNotBlank(registro.getCodigo())) {
			loadType();
			loadType(mapa);
			getController(central, DocumentoBean.class).load(registro);
		} else if (registro == null || StringUtils.isBlank(registro.getCodigo())) {
			getController(central, DocumentoBean.class).load(registro.getTipoDocumento());
		}
	}

	@SuppressWarnings("rawtypes")
	public final void loadType() {
		try {
			mapa = new HashMap<String, Class>();
			DocumentosDTO dto = new DocumentosDTO();
			dto.setDoctype(registro.getTipoDocumento());
			List<DocumentosDTO> lista = documentosSvc.getDocumentos(dto);
			for (DocumentosDTO doc : lista) {
				if (!doc.getClaseControlar().getSimpleName().contains("DetalleConceptoDTO"))
					mapa.put(doc.getClaseControlar().getSimpleName(), doc.getClaseControlar());
			}
		} catch (DocumentosException e) {
			error(e);
		}
	}

	@SuppressWarnings("rawtypes")
	private final void loadType(Map<String, Class> mapa) {
		Set<String> sets = mapa.keySet();
		for (String key : sets) {
			Button btn = new Button(key.replace("DTO", ""));
			btn.setMaxWidth(Double.MAX_VALUE);
			btn.setMinHeight(40);
			if (mapa.get(key) == DocumentoDTO.class) {
				btn.onActionProperty().set(e -> {
					if (registro != null && StringUtils.isNotBlank(registro.getCodigo())) {
						getController(central, DocumentoBean.class).load(registro);
					} else {
						getController(central, DocumentoBean.class).load(registro.getTipoDocumento());
					}
				});
			}
			if (mapa.get(key) == DetalleDTO.class) {
				btn.onActionProperty().set(e -> {
					if (registro != null && StringUtils.isNotBlank(registro.getCodigo())) {
						try {
							getController(central, ListaDetalleBean.class).load(central, registro.getTipoDocumento(),
									registro.getCodigo());
						} catch (Exception e1) {
							error(i18n().valueBundle(Documento.CONST_ERR_LOAD_LIST_DETAIL));
						}
					}
				});
			}
			if (mapa.get(key) == DetalleContableDTO.class) {
				btn.onActionProperty().set(e -> {
					if (registro != null && StringUtils.isNotBlank(registro.getCodigo())) {
						try {
							getController(central, ListaDetalleContableBean.class).load(central,
									registro.getTipoDocumento(), registro.getCodigo());
						} catch (Exception e1) {
							error(i18n().valueBundle(Documento.CONST_ERR_LOAD_LIST_DET_COUNT));
						}
					}
				});
			}
			if (left != null) {
				left.getChildren().add(btn);
			}
		} // end for
		Button regresar = new Button(Documento.CONST_RETURN);
		regresar.setGraphic(new Glyph("FontAwesome", org.controlsfx.glyphfont.FontAwesome.Glyph.BACKWARD));
		regresar.setMaxWidth(Double.MAX_VALUE);
		regresar.onActionProperty().set(e -> {
			getController(ListaDocumentosBean.class).loadParameters(registro.getTipoDocumento().getValor2());
		});
		left.getChildren().add(regresar);
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
		// TODO Auto-generated method stub

	}

}
