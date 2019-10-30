package org.pyt.app.beans.dinamico;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.lang3.StringUtils;
import org.pyt.app.components.ConfirmPopupBean;
import org.pyt.app.components.PopupBean;
import org.pyt.common.annotations.Inject;
import org.pyt.common.constants.ParametroConstants;
import org.pyt.common.constants.StylesPrincipalConstant;
import org.pyt.common.exceptions.DocumentosException;
import org.pyt.common.exceptions.LoadAppFxmlException;

import com.pyt.service.dto.DocumentoDTO;
import com.pyt.service.dto.DocumentosDTO;
import com.pyt.service.dto.ParametroDTO;
import com.pyt.service.interfaces.IDocumentosSvc;
import com.pyt.service.interfaces.IParametrosSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.AListGenericDinamicBean;
import co.com.japl.ea.utls.DataTableFXMLUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

/**
 * Se encarga de controlar la intefase que enlista todos los documentos creados
 * 
 * @author Alejandro Parra
 * @since 02-07-2018
 */
@FXMLFile(path = "view/dinamico", file = "listaDocumentos.fxml", nombreVentana = "Lista de Documentos")
public class ListaDocumentosBean extends AListGenericDinamicBean<DocumentoDTO, DocumentosDTO, DocumentoDTO> {
	@Inject(resource = "com.pyt.service.implement.DocumentosSvc")
	private IDocumentosSvc documentosSvc;
	@Inject(resource = "com.pyt.service.implement.ParametrosSvc")
	private IParametrosSvc parametroSvc;
	@FXML
	private TableView<DocumentoDTO> tabla;
	@FXML
	private HBox paginador;
	@FXML
	private HBox titulo;
	@FXML
	private Label lblTitle;
	private DataTableFXMLUtil<DocumentoDTO, DocumentoDTO> dataTable;
	@FXML
	private GridPane filterTable;
	protected DocumentoDTO filter;
	private ParametroDTO tipoDocumento;
	private Map<String, List> mapListSelects;

	@FXML
	public void initialize() {
		try {
			registro = new DocumentoDTO();
			filter = new DocumentoDTO();
			lazy();
		} catch (Exception e) {
			error(e);
		}
	}

	private void searchFilters() {
		try {
			if (tipoDocumento == null || StringUtils.isBlank(tipoDocumento.getCodigo())) {
				throw new Exception(i18n().valueBundle("document_Type_didnt_found."));
			}
			var documentos = new DocumentosDTO();
			documentos.setClaseControlar(DocumentoDTO.class);
			documentos.setDoctype(tipoDocumento);
			documentos.setFieldFilter(true);
			genericFields = documentosSvc.getDocumentos(documentos);
			loadFields(TypeGeneric.FILTER, StylesPrincipalConstant.CONST_GRID_STANDARD);
		} catch (Exception e) {
			logger.logger(e);
		}
	}

	private void searchColumns() {
		try {
			if (tipoDocumento == null || StringUtils.isBlank(tipoDocumento.getCodigo())) {
				throw new Exception(i18n().valueBundle("document_Type_didnt_found."));
			}
			var documentos = new DocumentosDTO();
			documentos.setClaseControlar(DocumentoDTO.class);
			documentos.setDoctype(tipoDocumento);
			documentos.setFieldColumn(true);
			genericColumns = documentosSvc.getDocumentos(documentos);
			loadColumns(StylesPrincipalConstant.CONST_TABLE_CUSTOM);
		} catch (Exception e) {
			logger.logger(e);
		}
	}

	public void lazy() {
		dataTable = new DataTableFXMLUtil<DocumentoDTO, DocumentoDTO>(paginador, tabla) {

			@Override
			public Integer getTotalRows(DocumentoDTO filter) {
				try {
					return documentosSvc.getTotalCount(filter);
				} catch (DocumentosException e) {
					error(e);
				}
				return 0;
			}

			@Override
			public List<DocumentoDTO> getList(DocumentoDTO filter, Integer page, Integer rows) {
				List<DocumentoDTO> lista = new ArrayList<DocumentoDTO>();
				try {
					lista = documentosSvc.getDocumentos(filter, page - 1, rows);
				} catch (DocumentosException e) {
					error(e);
				}
				return lista;
			}

			@Override
			public DocumentoDTO getFilter() {
				if (tipoDocumento != null && StringUtils.isNotBlank(tipoDocumento.getCodigo())) {
					filter.setTipoDocumento(tipoDocumento);
				}
				return filter;
			}
		};
	}

	public final void agregar() {
		var documento = new DocumentoDTO();
		documento.setTipoDocumento(tipoDocumento);
		getController(PanelBean.class).load(documento);
	}

	public final void modificar() {
		registro = dataTable.getSelectedRow();
		if (registro != null && StringUtils.isNotBlank(registro.getCodigo())) {
			getController(PanelBean.class).load(registro);
		} else {
			error("No se ha seleccionado ningun documento.");
		}
	}

	public final void eliminar() {
		try {
			controllerPopup(ConfirmPopupBean.class).load("#{ListaDocumentosBean.delete}",
					i18n().valueBundle("want_delete_this_record"));
		} catch (LoadAppFxmlException e) {
			error(e);
		}
	}

	public final void setDelete(Boolean valid) {
		if (valid) {
			try {
				controllerPopup(PopupBean.class).load(i18n().valueBundle("this_functionality_isnt_active"),
						PopupBean.TIPOS.WARNING);
			} catch (LoadAppFxmlException e) {
				error(e);
			}
		}
	}

	public final void loadParameters(String... tipoDocumento) {
		try {
			if (tipoDocumento.length == 1 && tipoDocumento[0].trim().contains("tipoDocumento")) {
				var grupo = parametroSvc.getIdByParametroGroup(ParametroConstants.GRUPO_TIPO_DOCUMENTO);
				var parametro = new ParametroDTO();
				parametro.setValor2(tipoDocumento[0].substring(tipoDocumento[0].indexOf("$") + 1));
				parametro.setEstado(ParametroConstants.COD_ESTADO_PARAMETRO_ACTIVO_STR);
				parametro.setGrupo(grupo);
				var tdoc = parametroSvc.getParametro(parametro);
				this.tipoDocumento = tdoc;
				lblTitle.setText(this.tipoDocumento.getNombre());
				dataTable.search();
				searchFilters();
				searchColumns();
			}
		} catch (Exception e) {
			logger.logger(i18n().valueBundle("document_type_had_error_in_its_processing"), e);
		}
	}

	@Override
	public TableView<DocumentoDTO> getTableView() {
		return tabla;
	}

	@Override
	public javafx.scene.layout.GridPane getGridPane(TypeGeneric typeGeneric) {
		return filterTable;
	}

	@Override
	public DocumentoDTO getInstanceDto(TypeGeneric typeGeneric) {
		return filter;
	}

	@Override
	public MultiValuedMap<String, Object> getMapListToChoiceBox() {
		return null;
	}

	@Override
	public Integer getMaxColumns(TypeGeneric typeGeneric) {
		return 4;
	}

	@Override
	public List<DocumentoDTO> getListGenericsFields(TypeGeneric typeGeneric) {
		return null;
	}

	@Override
	public Class<DocumentoDTO> getClazz() {
		return DocumentoDTO.class;
	}

	@Override
	public void selectedRow(MouseEvent eventHandler) {
	}

	@Override
	public DataTableFXMLUtil<DocumentoDTO, DocumentoDTO> getTable() {
		return dataTable;
	}

}
