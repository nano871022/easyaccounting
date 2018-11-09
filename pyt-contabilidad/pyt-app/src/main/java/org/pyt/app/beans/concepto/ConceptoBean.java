package org.pyt.app.beans.concepto;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.pyt.app.components.ConfirmPopupBean;
import org.pyt.app.components.DataTableFXML;
import org.pyt.common.annotations.FXMLFile;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.ABean;
import org.pyt.common.common.LoadAppFxml;
import org.pyt.common.common.SelectList;
import org.pyt.common.constants.ParametroConstants;
import org.pyt.common.exceptions.DocumentosException;
import org.pyt.common.exceptions.EmpresasException;
import org.pyt.common.exceptions.ParametroException;

import com.pyt.service.dto.ConceptoDTO;
import com.pyt.service.dto.EmpresaDTO;
import com.pyt.service.dto.ParametroDTO;
import com.pyt.service.interfaces.IDocumentosSvc;
import com.pyt.service.interfaces.IEmpresasSvc;
import com.pyt.service.interfaces.IParametrosSvc;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * Bean encargado de crear las empresas
 * 
 * @author alejandro parra
 * @since 07/05/2018
 */
@FXMLFile(path = "view/concepto", file = "listConcepto.fxml")
public class ConceptoBean extends ABean<ConceptoDTO> {
	@Inject(resource = "com.pyt.service.implement.DocumentosSvc")
	private IDocumentosSvc documentoSvc;
	@Inject(resource = "com.pyt.service.implement.ParametrosSvc")
	private IParametrosSvc parametroSvc;
	@Inject(resource = "com.pyt.service.implement.EmpresaSvc")
	private IEmpresasSvc empresaSvc;
	@FXML
	private javafx.scene.control.TableView<ConceptoDTO> tabla;
	@FXML
	private ChoiceBox<String> empresa;
	@FXML
	private TextField nombre;
	@FXML
	private TextField descripcion;
	@FXML
	private ChoiceBox<String> estado;
	@FXML
	private Button btnMod;
	@FXML
	private Button btnDel;
	@FXML
	private HBox paginador;
	@FXML
	private TableColumn<ConceptoDTO, String> columnEmpresa;
	@FXML
	private TableColumn<ConceptoDTO, String> columnEstado;
	private DataTableFXML<ConceptoDTO, ConceptoDTO> dt;
	private List<ParametroDTO> listEstado;
	private List<EmpresaDTO> listEmpresa;

	@FXML
	public void initialize() {
		NombreVentana = "Lista de Conceptos";
		registro = new ConceptoDTO();
		ParametroDTO pEstado = new ParametroDTO();
		EmpresaDTO pEmpresa = new EmpresaDTO();
		try {
			listEstado = parametroSvc.getAllParametros(pEstado,ParametroConstants.GRUPO_ESTADO_CONCEPTO);
			listEmpresa = empresaSvc.getAllEmpresas(pEmpresa);
		} catch (EmpresasException e) {
			error(e);
		} catch (ParametroException e) {
			error(e);
		}
		SelectList.put(estado, listEstado, "nombre");
		SelectList.put(empresa, listEmpresa, "nombre");
		estado.getSelectionModel().selectFirst();
		empresa.getSelectionModel().selectFirst();
		
		columnEstado.setCellValueFactory(e->new SimpleStringProperty(e.getValue().getEstado().getNombre()));
		columnEmpresa.setCellValueFactory(e->new SimpleStringProperty(e.getValue().getEmpresa().getNombre()));
		lazy();
	}

	/**
	 * encargada de crear el objeto que va controlar la tabla
	 */
	public void lazy() {
		dt = new DataTableFXML<ConceptoDTO, ConceptoDTO>(paginador, tabla) {
			@Override
			public List<ConceptoDTO> getList(ConceptoDTO filter, Integer page, Integer rows) {
				List<ConceptoDTO> lista = new ArrayList<ConceptoDTO>();
				try {
					lista = documentoSvc.getConceptos(filter, page-1, rows);
				} catch (DocumentosException e) {
					error(e);
				}
				return lista;
			}

			@Override
			public Integer getTotalRows(ConceptoDTO filter) {
				Integer count = 0;
				try {
					count = documentoSvc.getTotalRows(filter);
				} catch (DocumentosException e) {
					error(e);
				}
				return count;
			}

			@Override
			public ConceptoDTO getFilter() {
				ConceptoDTO filtro = new ConceptoDTO();
				if (StringUtils.isNotBlank(nombre.getText())) {
					filtro.setNombre(nombre.getText());
				}
				if (StringUtils.isNotBlank(descripcion.getText())) {
					filtro.setDescripcion(descripcion.getText());
				}
				if (StringUtils.isNotBlank(empresa.getValue())) {
					filtro.setEmpresa(SelectList.get(empresa, listEmpresa, "nombre"));
				}
				if (StringUtils.isNotBlank(empresa.getValue())) {
					filtro.setEstado(SelectList.get(estado, listEstado, "nombre"));
				}
				return filtro;
			}
		};
	}

	public void clickTable() {
		btnMod.setVisible(isSelected());
		btnDel.setVisible(isSelected());
	}

	public void add() {
		getController(ConceptoCRUBean.class);
	}

	public void search() {
		dt.search();
	}

	public void del() {
		try {
			LoadAppFxml.loadBeanFxml(new Stage(), ConfirmPopupBean.class).load("#{ConceptoBean.delete}", "¿Desea eliminar los registros seleccionados?");
		}catch(Exception e) {
			error(e);
		}
	}
	public void setDelete(Boolean valid) {
		try {
			if(!valid)return;
			registro = dt.getSelectedRow();
			if (registro != null) {
				documentoSvc.delete(registro, userLogin);
				notificar("Se ha eliminado el concepto.");
				dt.search();
			} else {
				notificar("No se ha seleccionado un concepto.");
			}
		} catch (DocumentosException e) {
			error(e);
		}
	}

	public void set() {
		registro = dt.getSelectedRow();
		if (registro != null) {
			getController(ConceptoCRUBean.class).load(registro);
		} else {
			notificar("No se ha seleccionado un concepto.");
		}
	}

	public Boolean isSelected() {
		return dt.isSelected();
	}

	public DataTableFXML<ConceptoDTO, ConceptoDTO> getDt() {
		return dt;
	}
}
