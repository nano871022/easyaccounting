package org.pyt.app.beans.dinamico;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.pyt.app.components.ConfirmPopupBean;
import org.pyt.app.components.DataTableFXML;
import org.pyt.app.components.IGenericFilter;
import org.pyt.app.components.PopupBean;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.I18n;
import org.pyt.common.common.Log;
import org.pyt.common.common.SelectList;
import org.pyt.common.constants.ParametroConstants;
import org.pyt.common.exceptions.DocumentosException;
import org.pyt.common.exceptions.LoadAppFxmlException;

import com.pyt.service.dto.DocumentoDTO;
import com.pyt.service.dto.ParametroDTO;
import com.pyt.service.interfaces.IDocumentosSvc;
import com.pyt.service.interfaces.IParametrosSvc;
import com.pyt.service.pojo.GenericPOJO;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.ABean;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

/**
 * Se encarga de controlar la intefase que enlista todos los documentos creados
 * 
 * @author Alejandro Parra
 * @since 02-07-2018
 */
@FXMLFile(path = "view/dinamico", file = "listaDocumentos.fxml", nombreVentana = "Lista de Documentos")
public class ListaDocumentosBean extends ABean<DocumentoDTO> implements IGenericFilter<DocumentoDTO> {
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
	private DataTableFXML<DocumentoDTO, DocumentoDTO> dataTable;
	@FXML
	private GridPane filterTable;
	protected DocumentoDTO filter;
	private Map<String, GenericPOJO<DocumentoDTO>> filters;
	private ParametroDTO tipoDocumento;

	@FXML
	public void initialize() {
		try {
			registro = new DocumentoDTO();
			filter = assingValuesParameterized(getInstaceOfGenericADto());
			configFilters();
			lazy();
		} catch (Exception e) {
			error(e);
		}
	}

	public void lazy() {
		dataTable = new DataTableFXML<DocumentoDTO, DocumentoDTO>(paginador, tabla) {

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
				if(tipoDocumento != null && StringUtils.isNotBlank(tipoDocumento.getCodigo())) {
					filter.setTipoDocumento(tipoDocumento);
				}
				return filter;
			}
		};
	}

	public final void agregar() {
		getController(PanelBean.class).load(new DocumentoDTO());
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
					"¿Desea eliminar este registro?");
		} catch (LoadAppFxmlException e) {
			error(e);
		}
	}

	public final void setDelete(Boolean valid) {
		if (valid) {
			try {
				controllerPopup(PopupBean.class).load("Esta funcionalidad no esta activada.", PopupBean.TIPOS.WARNING);
			} catch (LoadAppFxmlException e) {
				error(e);
			}
		}
	}

	@Override
	public Log getLogger() {
		return logger;
	}

	@Override
	public I18n getI18n() {
		return i18n();
	}

	@Override
	public Class<DocumentoDTO> getClazz() {
		return DocumentoDTO.class;
	}

	@Override
	public DocumentoDTO getFilter() {
		return filter;
	}

	@Override
	public GridPane getGridPaneFilter() {
		return filterTable;
	}

	@Override
	public void setFilter(DocumentoDTO filter) {
		this.filter = filter;
	}

	@Override
	public Map<String, GenericPOJO<DocumentoDTO>> getFilters() {
		return filters;
	}

	@Override
	public void setFilters(Map<String, GenericPOJO<DocumentoDTO>> filters) {
		this.filters = filters;
	}

	@Override
	public void setClazz(Class<DocumentoDTO> clazz) {
		// TODO Auto-generated method stub

	}

	@Override
	public DataTableFXML<DocumentoDTO, DocumentoDTO> getTable() {
		return dataTable;
	}
	
	public final void loadParameters(String tipoDocumento) {
		try {
		if(tipoDocumento.trim().contains("tipoDocumento")) {
			var grupo = parametroSvc.getIdByParametroGroup(ParametroConstants.GRUPO_TIPO_DOCUMENTO);
			var parametro = new ParametroDTO();
			parametro.setValor2(tipoDocumento.substring(tipoDocumento.indexOf("$")+1));
			parametro.setEstado("1");
			parametro.setGrupo(grupo);
			var tdoc = parametroSvc.getParametro(parametro);
			this.tipoDocumento = tdoc;
		}
		}catch(Exception e) {
			logger.logger("Se presento error en el procesamiento del parametro que identifica el tipo de documento.",e);
		}
	}

}
