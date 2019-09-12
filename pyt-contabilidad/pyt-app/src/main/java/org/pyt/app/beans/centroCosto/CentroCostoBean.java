package org.pyt.app.beans.centroCosto;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.pyt.app.components.ConfirmPopupBean;
import org.pyt.app.components.DataTableFXML;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.SelectList;
import org.pyt.common.constants.ParametroConstants;
import org.pyt.common.exceptions.CentroCostosException;
import org.pyt.common.exceptions.ParametroException;

import com.pyt.service.dto.CentroCostoDTO;
import com.pyt.service.dto.ParametroDTO;
import com.pyt.service.interfaces.ICentroCostosSvc;
import com.pyt.service.interfaces.IParametrosSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.ABean;
import co.com.japl.ea.utls.LoadAppFxml;
import javafx.beans.property.SimpleObjectProperty;
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
@FXMLFile(path = "view/centroCosto", file = "listCentroCosto.fxml")
public class CentroCostoBean extends ABean<CentroCostoDTO> {
	@Inject(resource = "com.pyt.service.implement.CentroCostoSvc")
	private ICentroCostosSvc centroCostoSvc;
	@Inject(resource = "com.pyt.service.implement.ParametrosSvc")
	private IParametrosSvc parametroSvc;
	@FXML
	private javafx.scene.control.TableView<CentroCostoDTO> tabla;
	@FXML
	private TextField orden;
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
	private TableColumn<CentroCostoDTO, String> estados;
	private DataTableFXML<CentroCostoDTO, CentroCostoDTO> dt;
	private List<ParametroDTO> listEstados;
	private final static String FIELD_NAME = "nombre";

	@FXML
	public void initialize() {
		NombreVentana = "Lista Centro de Costos";
		registro = new CentroCostoDTO();
		try {
			listEstados = parametroSvc.getAllParametros(new ParametroDTO(),
					ParametroConstants.GRUPO_ESTADO_CENTRO_COSTO);
		} catch (ParametroException e) {
			error(e);
		}
		SelectList.put(estado, listEstados, FIELD_NAME);
		estados.setCellValueFactory(e -> {
			SimpleObjectProperty<String> property = new SimpleObjectProperty<String>();
			property.setValue(nombreEstado(e.getValue().getEstado()));
			return property;
		});
		lazy();
	}

	/**
	 * Se encarga de apartir del valor de un estado obtener el nombre de este estado
	 * @param value {@link String}
	 * @return {@link String}
	 */
	public final String nombreEstado(String value) {
		for (ParametroDTO parametro : listEstados) {
			if (parametro.getValor().equalsIgnoreCase(value)) {
				return parametro.getNombre();
			}
		}
		return value;
	}

	/**
	 * encargada de crear el objeto que va controlar la tabla
	 */
	public void lazy() {
		dt = new DataTableFXML<CentroCostoDTO, CentroCostoDTO>(paginador, tabla) {
			@Override
			public List<CentroCostoDTO> getList(CentroCostoDTO filter, Integer page, Integer rows) {
				List<CentroCostoDTO> lista = new ArrayList<CentroCostoDTO>();
				try {
					lista = centroCostoSvc.getCentroCostos(filter, page-1, rows);
				} catch (CentroCostosException e) {
					error(e);
				}
				return lista;
			}

			@Override
			public Integer getTotalRows(CentroCostoDTO filter) {
				Integer count = 0;
				try {
					count = centroCostoSvc.getTotalRows(filter);
				} catch (CentroCostosException e) {
					error(e);
				}
				return count;
			}

			@Override
			public CentroCostoDTO getFilter() {
				CentroCostoDTO filtro = new CentroCostoDTO();
				if (StringUtils.isNotBlank(nombre.getText())) {
					filtro.setNombre(nombre.getText());
				}
				if (StringUtils.isNotBlank(descripcion.getText())) {
					filtro.setDescripcion(descripcion.getText());
				}
				if (SelectList.get(estado, listEstados, FIELD_NAME) != null) {
					filtro.setEstado((String) SelectList.get(estado, listEstados, FIELD_NAME).getValor());
				}
				try {
					if (StringUtils.isNotBlank(orden.getText())) {
						filtro.setOrden(Integer.valueOf(orden.getText()));
					}
				} catch (Exception e) {
					logger.logger(e);
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
		getController(CentroCostoCRUBean.class);
	}

	public void search() {
		dt.search();
	}

	public void del() {
		try {
			controllerPopup(ConfirmPopupBean.class).load("#{CentroCostoBean.delete}", "¿Desea eliminar los registros seleccionados?");
		}catch(Exception e) {
			error(e);
		}
	}
	public void setDelete(Boolean valid) {
		try {
			if(!valid)return;
			registro = dt.getSelectedRow();
			if (registro != null) {
				centroCostoSvc.delete(registro, userLogin);
				notificar("Se ha eliminaro la empresa.");
				dt.search();
			} else {
				notificar("No se ha seleccionado una empresa.");
			}
		} catch (CentroCostosException e) {
			error(e);
		}
	}

	public void set() {
		registro = dt.getSelectedRow();
		if (registro != null) {
			getController(CentroCostoCRUBean.class).load(registro);
		} else {
			notificar("No se ha seleccionado una empresa.");
		}
	}

	public Boolean isSelected() {
		return dt.isSelected();
	}

	public DataTableFXML<CentroCostoDTO, CentroCostoDTO> getDt() {
		return dt;
	}
}
