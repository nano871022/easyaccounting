package org.pyt.app.beans.parametroInventario;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.pyt.app.beans.interfaces.ListCRUDBean;
import org.pyt.app.components.ConfirmPopupBean;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.SelectList;
import org.pyt.common.constants.ParametroConstants;
import org.pyt.common.exceptions.ParametroException;

import com.pyt.service.dto.inventario.ParametroInventarioDTO;
import com.pyt.service.interfaces.inventarios.IParametroInventariosSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.AListBasicBean;
import co.com.japl.ea.utls.DataTableFXMLUtil;
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
@FXMLFile(path = "view/parametroInventarios", file = "ListParametrosInventarios.fxml", nombreVentana = "Parametros del Sistema de inventarios")
public class ParametrosInventariosBean extends AListBasicBean<ParametroInventarioDTO, ParametroInventarioDTO>
		implements ListCRUDBean {
	@Inject(resource = "com.pyt.service.implement.inventario.ParametroInventariosSvc")
	private IParametroInventariosSvc parametrosSvc;
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
	private TableView<ParametroInventarioDTO> filtrar;
	@FXML
	private TableView<ParametroInventarioDTO> tabla;
	@FXML
	private Button modify;
	@FXML
	private Button add;
	@FXML
	private Button del;
	@FXML
	private Button addGroup;
	@FXML
	private Button modifyGroup;
	private DataTableFXMLUtil<ParametroInventarioDTO, ParametroInventarioDTO> lazyFiltrar;
	private ParametroInventarioDTO filtrarGrupo;
	private ParametroInventarioDTO seleccionFiltro;
	@FXML
	private TextField filtroGrupo;

	@FXML
	public void initialize() {
		registro = new ParametroInventarioDTO();
		SelectList.put(estado, ParametroConstants.mapa_estados_parametros);
		SelectList.put(grupo, ParametroConstants.mapa_grupo);
		estado.getSelectionModel().selectFirst();
		grupo.getSelectionModel().selectFirst();
		add.setVisible(false);
		modify.setVisible(false);
		del.setVisible(false);
		addGroup.setVisible(true);
		modifyGroup.setVisible(false);
		tabla.setVisible(false);
		lazy();
		lazy2();
	}

	public void lazy2() {
		lazyFiltrar = new DataTableFXMLUtil<ParametroInventarioDTO, ParametroInventarioDTO>(paginador2, filtrar) {

			@Override
			public Integer getTotalRows(ParametroInventarioDTO filter) {
				try {
					return parametrosSvc.totalCount(filter);
				} catch (ParametroException e) {
					error(e);
				}
				return 0;
			}

			@Override
			public List<ParametroInventarioDTO> getList(ParametroInventarioDTO filter, Integer page, Integer rows) {
				List<ParametroInventarioDTO> lista = new ArrayList<ParametroInventarioDTO>();
				try {
					lista = parametrosSvc.getParametros(filter, page, rows);
				} catch (ParametroException e) {
					error(e);
				}
				return lista;
			}

			@Override
			public ParametroInventarioDTO getFilter() {
				ParametroInventarioDTO filtro = new ParametroInventarioDTO();
				if (StringUtils.isNotBlank(filtroGrupo.getText())) {
					filtro.setNombre(filtroGrupo.getText());
				}
				filtro.setGrupo("*");
				return filtro;
			}
		};
	}

	@Override
	public void lazy() {
		dataTable = new DataTableFXMLUtil<ParametroInventarioDTO, ParametroInventarioDTO>(paginador, tabla) {

			@Override
			public Integer getTotalRows(ParametroInventarioDTO filter) {
				Integer count = 0;
				try {
					count = parametrosSvc.totalCount(filter);
				} catch (ParametroException e) {
					logger.logger(e);
					error(e);
				}
				return count;
			}

			@Override
			public List<ParametroInventarioDTO> getList(ParametroInventarioDTO filter, Integer page, Integer rows) {
				List<ParametroInventarioDTO> lista = new ArrayList<ParametroInventarioDTO>();
				try {
					lista = parametrosSvc.getParametros(filter, page, rows);
				} catch (ParametroException e) {
					logger.logger(e);
					error(e);
				}
				return lista;
			}

			@Override
			public ParametroInventarioDTO getFilter() {
				ParametroInventarioDTO filtro = new ParametroInventarioDTO();
				if (!nombre.getText().isEmpty()) {
					filtro.setNombre(nombre.getText());
				}
				if (!descripcion.getText().isEmpty()) {
					filtro.setDescripcion(descripcion.getText());
				}
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
		del.setVisible(isSelected());
		add.setVisible(!isSelected());
	}

	public void clickTableFiltrar() {
		if (lazyFiltrar.isSelected()) {
			tabla.setVisible(true);
			seleccionFiltro = lazyFiltrar.getSelectedRow();
			dataTable.search();
			modifyGroup.setVisible(true);
			addGroup.setVisible(false);
			add.setVisible(true);
		} else {
			tabla.setVisible(false);
			addGroup.setVisible(true);
			modifyGroup.setVisible(false);
		}
	}

	public void buscarFiltro() {
		lazyFiltrar.search();
	}

	public void nuevoFiltro() {
		ParametroInventarioDTO parametro = new ParametroInventarioDTO();
		parametro.setGrupo("*");
		getController(ParametrosInventariosCRUBean.class).load(parametro);
	}

	public void modifyFiltro() {
		if (lazyFiltrar.isSelected()) {
			getController(ParametrosInventariosCRUBean.class).load(lazyFiltrar.getSelectedRow());
		}
	}

	@Override
	public void searchBtn() {
		dataTable.search();
	}

	@Override
	public void createBtn() {
		ParametroInventarioDTO dto = new ParametroInventarioDTO();
		dto.setGrupo(seleccionFiltro.getCodigo());
		getController(ParametrosInventariosCRUBean.class).load(dto);
	}

	@Override
	public void modifyBtn() {
		if (dataTable.isSelected()) {
			registro = dataTable.getSelectedRow();
			getController(ParametrosInventariosCRUBean.class).load(registro);
		}
	}

	@Override
	public void deleteBtn() {
		try {
			this.controllerPopup(ConfirmPopupBean.class).load("#{ParametrosBean.delete}",
					"¿Desea eliminar los registros seleccionados?");
		} catch (Exception e) {
			error(e);
		}
	}

	public void setDelete(Boolean valid) {
		try {
			if (!valid)
				return;
			registro = dataTable.getSelectedRow();
			if (registro != null) {
				parametrosSvc.delete(registro, getUsuario());
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
