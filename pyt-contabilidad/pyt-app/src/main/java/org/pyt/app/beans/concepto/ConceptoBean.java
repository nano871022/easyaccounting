package org.pyt.app.beans.concepto;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.ea.app.custom.PopupParametrizedControl;
import org.pyt.app.components.ConfirmPopupBean;
import org.pyt.app.components.DataTableFXML;
import org.pyt.app.components.PopupGenBean;
import org.pyt.common.abstracts.ABean;
import org.pyt.common.annotations.Inject;
import org.pyt.common.constants.ParametroConstants;
import org.pyt.common.exceptions.DocumentosException;

import com.pyt.service.dto.ConceptoDTO;
import com.pyt.service.dto.EmpresaDTO;
import com.pyt.service.dto.ParametroDTO;
import com.pyt.service.interfaces.IDocumentosSvc;
import com.pyt.service.interfaces.IEmpresasSvc;
import com.pyt.service.interfaces.IParametrosSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

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
	private PopupParametrizedControl empresa;
	@FXML
	private TextField nombre;
	@FXML
	private TextField descripcion;
	@FXML
	private PopupParametrizedControl estado;
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
	private ConceptoDTO filter;

	@FXML
	public void initialize() {
		NombreVentana = "Lista de Conceptos";
		registro = new ConceptoDTO();
		filter = new ConceptoDTO();
		estado.setPopupOpenAction(()->popupEstado());
		estado.setCleanValue(()->filter.setEstado(null));
		empresa.setPopupOpenAction(()->popupEmpresa());
		empresa.setCleanValue(()->filter.setEmpresa(null));
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
				if (filter.getEmpresa() != null) {
					filtro.setEmpresa(filter.getEmpresa());
				}
				if (filter.getEstado() != null) {
					filtro.setEstado(filter.getEstado());
				}
				return filtro;
			}
		};
	}

	public void clickTable() {
		btnMod.setVisible(isSelected());
		btnDel.setVisible(isSelected());
	}
	
	public final void popupEstado() {
		try {
			((PopupGenBean<ParametroDTO>) controllerPopup(new PopupGenBean<ParametroDTO>(ParametroDTO.class))
					.setWidth(350)
					.addDefaultValuesToGenericParametrized(ParametroConstants.FIELD_NAME_GROUP, ParametroConstants.GRUPO_ESTADO_CONCEPTO)
					).load("#{ConceptoBean.estado}");
		} catch (Exception e) {
			error(e);
		}
	}
	
	public final void setEstado(ParametroDTO parametro) {
		registro.setEstado(parametro);
		estado.setText(parametro.getDescripcion());
	}
	
	public final void popupEmpresa() {
		try {
			((PopupGenBean<EmpresaDTO>) controllerPopup(new PopupGenBean<EmpresaDTO>(EmpresaDTO.class))
					.setWidth(350)
					).load("#{ConceptoBean.empresa}");
		} catch (Exception e) {
			error(e);
		}
	}
	
	public final void setEmpresa(EmpresaDTO empresa) {
		registro.setEmpresa(empresa);
		this.empresa.setText(empresa.getNombre());
	}
	
	public void add() {
		getController(ConceptoCRUBean.class);
	}

	public void search() {
		dt.search();
	}

	public void del() {
		try {
			controllerPopup(ConfirmPopupBean.class).load("#{ConceptoBean.delete}", "¿Desea eliminar los registros seleccionados?");
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
