package org.pyt.app.beans.dinamico;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.pyt.app.components.ConfirmPopupBean;
import org.pyt.app.components.DataTableFXML;
import org.pyt.app.components.PopupBean;
import org.pyt.common.abstracts.ABean;
import org.pyt.common.annotations.Inject;
import org.pyt.common.exceptions.DocumentosException;
import org.pyt.common.exceptions.LoadAppFxmlException;

import com.pyt.service.dto.DocumentoDTO;
import com.pyt.service.interfaces.IDocumentosSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
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
public class ListaDocumentosBean extends ABean<DocumentoDTO> {
	@Inject(resource = "com.pyt.service.implement.DocumentosSvc")
	private IDocumentosSvc documentosSvc;
	@FXML
	private TableView<DocumentoDTO> tabla;
	@FXML
	private HBox paginador;
	@FXML
	private HBox titulo;
	private DataTableFXML<DocumentoDTO, DocumentoDTO> dataTable;
	private GridPane filterTable;

	@FXML
	public void initialize() {
		registro = new DocumentoDTO();
		lazy();
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
				DocumentoDTO filter = new DocumentoDTO();
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

}
