package org.pyt.app.beans.parametros;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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

/**
 * Esta pantalla se encarga de controlar la pagina de listado de parametros
 * 
 * @author Alejandro Parra
 * @since 22-06-2018
 */
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
	private HBox paginador2;
	@FXML
	private TableView<ParametroDTO> filtrar;
	@FXML
	private TableView<ParametroDTO> tabla;
	@FXML
	private Button modify;
	private DataTableFXML<ParametroDTO, ParametroDTO> lazyFiltrar;
	private ParametroDTO filtrarGrupo;
	private ParametroDTO seleccionFiltro;
	@FXML
	private TextField filtroGrupo;

	@FXML
	public void initialize() {
		registro = new ParametroDTO();
		SelectList.put(estado, ParametroConstants.mapa_estados_parametros);
		SelectList.put(grupo, ParametroConstants.mapa_grupo);
		estado.getSelectionModel().selectFirst();
		grupo.getSelectionModel().selectFirst();
		modify.setVisible(false);
		tabla.setVisible(false);
		lazy();
		lazy2();
	}

	public void lazy2() {
		lazyFiltrar = new DataTableFXML<ParametroDTO, ParametroDTO>(paginador2, filtrar) {

			@Override
			public Integer getTotalRows(ParametroDTO filter) {
				try {
					return parametrosSvc.totalCount(filter);
				} catch (ParametroException e) {
					error(e);
				}
				return 0;
			}

			@Override
			public List<ParametroDTO> getList(ParametroDTO filter, Integer page, Integer rows) {
				List<ParametroDTO> lista = new ArrayList<ParametroDTO>();
				try {
					lista = parametrosSvc.getParametros(filter, page - 1, rows);
				} catch (ParametroException e) {
					error(e);
				}
				return lista;
			}

			@Override
			public ParametroDTO getFilter() {
				ParametroDTO filtro = new ParametroDTO();
				if (StringUtils.isNotBlank(filtroGrupo.getText())) {
					filtro.setNombre(filtroGrupo.getText());
				}
				filtro.setGrupo("*");
				return filtro;
			}
		};
	}

	public void buscarFiltro() {
		lazyFiltrar.search();
	}

	public void nuevoFiltro() {
		getController(ParametrosCRUBean.class).load(new ParametroDTO());
	}

	public void modifyFiltro() {
		if (lazyFiltrar.isSelected()) {
			getController(ParametrosCRUBean.class).load(lazyFiltrar.getSelectedRow());
		}
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
					lista = parametrosSvc.getParametros(getFilter(), page - 1, rows);
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
				if (StringUtils.isNotBlank(estado.getValue())) {
					filtro.setEstado((String) ParametroConstants.mapa_estados_parametros.get(estado.getValue()));
				}
				if (StringUtils.isNotBlank(seleccionFiltro.getCodigo())) {
					filtro.setGrupo(seleccionFiltro.getCodigo());
				}
				return filtro;
			}
		};

	}

	@Override
	public void clickTable() {
		modify.setVisible(isSelected());
	}

	public void clickTableFiltrar() {
		if (lazyFiltrar.isSelected()) {
			tabla.setVisible(true);
			seleccionFiltro = lazyFiltrar.getSelectedRow();
			dataTable.search();
		} else {
			tabla.setVisible(false);
		}
	}

	@Override
	public void searchBtn() {
		dataTable.search();
	}

	@Override
	public void createBtn() {
		getController(ParametrosCRUBean.class).load(new ParametroDTO());
	}

	@Override
	public void modifyBtn() {
		if (dataTable.isSelected()) {
			registro = dataTable.getSelectedRow();
			getController(ParametrosCRUBean.class).load(registro);
		}
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
