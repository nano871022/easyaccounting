package org.pyt.app.beans.parametros;

import java.util.ArrayList;
import java.util.List;

import org.pyt.app.beans.abstracts.AListBasicBean;
import org.pyt.app.beans.interfaces.ListCRUDBean;
import org.pyt.app.components.DataTableFXML;
import org.pyt.common.annotations.FXMLFile;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.Log;
import org.pyt.common.common.SelectList;
import org.pyt.common.constants.ParametroConstants;
import org.pyt.common.exceptions.ParametroException;

import com.pyt.service.dto.ParametroDTO;
import com.pyt.service.interfaces.IParametrosSvc;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

@FXMLFile(path = "view/parametros", file = "ListParametros.fxml", nombreVentana = "Parametros del Sistema")
public class ParametrosBean extends AListBasicBean<ParametroDTO, ParametroDTO> implements ListCRUDBean {
	@Inject(resource = "com.pyt.service.implement.ParametrosSvc")
	private IParametrosSvc parametrosSvc;
	@FXML
	private TextField nombre;
	@FXML
	private TextField descripcion;
	@FXML
	private ChoiceBox<String> estado;
	@FXML
	private ChoiceBox<String> grupo;
	@FXML
	private HBox paginador;
	@FXML
	private TableView<ParametroDTO> tabla;
	@FXML
	private Button modify;

	@FXML
	public void initialize() {
		registro = new ParametroDTO();
		SelectList.put(estado, ParametroConstants.mapa_estados_parametros);
		SelectList.put(grupo, ParametroConstants.mapa_grupo);
		estado.getSelectionModel().selectFirst();
		grupo.getSelectionModel().selectFirst();
		modify.setVisible(false);
		lazy();
	}

	@Override
	public void lazy() {
		dataTable = new DataTableFXML<ParametroDTO, ParametroDTO>(paginador, tabla) {

			@Override
			public Integer getTotalRows(ParametroDTO filter) {
				Integer count = 0;
				try {
					count = parametrosSvc.totalCount(getFilter());
				} catch (ParametroException e) {
					Log.logger(e);
					error(e);
				}
				return count;
			}

			@Override
			public List<ParametroDTO> getList(ParametroDTO filter, Integer page, Integer rows) {
				List<ParametroDTO> lista = new ArrayList<ParametroDTO>();
				try {
					lista = parametrosSvc.getParametros(getFilter(), page, rows);
				} catch (ParametroException e) {
					Log.logger(e);
					error(e);
				}
				return lista;
			}

			@Override
			public ParametroDTO getFilter() {
				ParametroDTO filtro = new ParametroDTO();
				filtro.setNombre(nombre.getText());
				filtro.setDescripcion(descripcion.getText());
				filtro.setEstado((String) ParametroConstants.mapa_estados_parametros.get(estado.getValue()));
				filtro.setGrupo((String) ParametroConstants.mapa_grupo.get(grupo.getValue()));
				return filtro;
			}
		};

	}

	@Override
	public void clickTable() {
		modify.setVisible(isSelected());
	}

	@Override
	public void searchBtn() {
		dataTable.search();
	}

	@Override
	public void createBtn() {
		// TODO Auto-generated method stub
	}

	@Override
	public void modifyBtn() {
		// TODO Auto-generated method stub
	}

	@Override
	public void deleteBtn() {
		try {
			registro = dataTable.getSelectedRow();
			if (registro != null) {
				parametrosSvc.delete(registro, userLogin);
				notificar("Se ha eliminado el parametro.");
				dataTable.search();
			} else {
				notificar("No se ha seleccionado una empresa.");
			}
		} catch (ParametroException e) {
			error(e);
		}
	}

}
