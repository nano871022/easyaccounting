package org.pyt.app.beans.centroCosto;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.pyt.app.components.DataTableFXML;
import org.pyt.common.annotations.FXMLFile;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.ABean;
import org.pyt.common.common.Log;
import org.pyt.common.common.SelectList;
import org.pyt.common.constants.ParametroConstants;
import org.pyt.common.exceptions.CentroCostosException;

import com.pyt.service.dto.CentroCostoDTO;
import com.pyt.service.interfaces.ICentroCostosSvc;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

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
	private HBox paginador;
	private DataTableFXML<CentroCostoDTO, CentroCostoDTO> dt;

	@FXML
	public void initialize() {
		NombreVentana = "Lista Empresas";
		registro = new CentroCostoDTO();
		SelectList.put(estado, ParametroConstants.mapa_estados_parametros);
		estado.getSelectionModel().selectFirst();
		lazy();
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
					lista = centroCostoSvc.getCentroCostos(filter, page, rows);
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
				if(StringUtils.isNotBlank((String)SelectList.get(estado, ParametroConstants.mapa_estados_parametros))) {
					filtro.setEstado((String)SelectList.get(estado, ParametroConstants.mapa_estados_parametros));
				}
				try {
					if (StringUtils.isNotBlank(orden.getText())) {
						filtro.setOrden(Integer.valueOf(orden.getText()));
					}
				} catch (Exception e) {
					Log.logger(e);
				}
				return filtro;
			}
		};
	}

	public void clickTable() {
		btnMod.setVisible(isSelected());
	}

	public void add() {
		getController(CentroCostoCRUBean.class);
	}

	public void search() {
		dt.search();
	}

	public void del() {
		try {
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
