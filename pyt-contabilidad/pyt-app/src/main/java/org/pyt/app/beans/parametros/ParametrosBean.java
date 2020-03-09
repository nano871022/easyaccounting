package org.pyt.app.beans.parametros;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.controlsfx.glyphfont.FontAwesome.Glyph;
import org.pyt.app.beans.interfaces.ListCRUDBean;
import org.pyt.app.components.ConfirmPopupBean;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.SelectList;
import org.pyt.common.constants.ParametroConstants;
import org.pyt.common.constants.PermissionConstants;
import org.pyt.common.exceptions.ParametroException;

import com.pyt.service.dto.ParametroDTO;
import com.pyt.service.interfaces.IParametrosSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.AListBasicBean;
import co.com.japl.ea.common.button.apifluid.ButtonsImpl;
import co.com.japl.ea.utls.DataTableFXMLUtil;
import co.com.japl.ea.utls.PermissionUtil;
import javafx.fxml.FXML;
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
	private DataTableFXMLUtil<ParametroDTO, ParametroDTO> lazyFiltrar;
	private ParametroDTO filtrarGrupo;
	private ParametroDTO seleccionFiltro;
	@FXML
	private TextField filtroGrupo;
	@FXML
	private HBox buttons;
	@FXML
	private HBox addGroup;
	@FXML
	private HBox modifyGroup;

	@FXML
	public void initialize() {
		registro = new ParametroDTO();
		SelectList.put(estado, ParametroConstants.mapa_estados_parametros);
		SelectList.put(grupo, ParametroConstants.mapa_grupo);
		estado.getSelectionModel().selectFirst();
		grupo.getSelectionModel().selectFirst();
		addGroup.setVisible(true);
		modifyGroup.setVisible(false);
		tabla.setVisible(false);
		lazy();
		lazy2();
		ButtonsImpl.Stream(HBox.class).setLayout(buttons).setName("fxml.btn.save").action(this::createBtn)
				.icon(Glyph.SAVE).isVisible(save).setName("fxml.btn.edit").action(this::modifyBtn).icon(Glyph.EDIT)
				.isVisible(edit).setName("fxml.btn.delete").action(this::deleteBtn).icon(Glyph.REMOVE).isVisible(delete)
				.build();
		ButtonsImpl.Stream(HBox.class).setLayout(addGroup).setName("fxml.btn.search").action(this::buscarFiltro)
				.icon(Glyph.SEARCH).setName("fxml.btn.save").action(this::nuevoFiltro).icon(Glyph.SAVE).isVisible(save)
				.build();
		ButtonsImpl.Stream(HBox.class).setLayout(modifyGroup).setName("fxml.btn.edit").action(this::modifyFiltro)
				.icon(Glyph.EDIT).isVisible(edit).build();
	}

	public void lazy2() {
		lazyFiltrar = new DataTableFXMLUtil<ParametroDTO, ParametroDTO>(paginador2, filtrar) {

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
					lista = parametrosSvc.getParametros(filter, page, rows);
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

	@Override
	public void lazy() {
		dataTable = new DataTableFXMLUtil<ParametroDTO, ParametroDTO>(paginador, tabla) {

			@Override
			public Integer getTotalRows(ParametroDTO filter) {
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
			public List<ParametroDTO> getList(ParametroDTO filter, Integer page, Integer rows) {
				List<ParametroDTO> lista = new ArrayList<ParametroDTO>();
				try {
					lista = parametrosSvc.getParametros(filter, page - 1, rows);
				} catch (ParametroException e) {
					logger.logger(e);
					error(e);
				}
				return lista;
			}

			@Override
			public ParametroDTO getFilter() {
				ParametroDTO filtro = new ParametroDTO();
				if (StringUtils.isNotBlank(nombre.getText())) {
					filtro.setNombre(nombre.getText());
				}
				if (StringUtils.isNotBlank(descripcion.getText())) {
					filtro.setDescripcion(descripcion.getText());
				}
				if (StringUtils.isNotBlank(estado.getValue())) {
					filtro.setEstado((String) ParametroConstants.mapa_estados_parametros.get(estado.getValue()));
				}
				if (StringUtils.isNotBlank(lazyFiltrar.getSelectedRow().getCodigo())) {
					filtro.setGrupo(lazyFiltrar.getSelectedRow().getCodigo());
				}
				return filtro;
			}
		};

	}

	public void clickTableFiltrar() {
		if (lazyFiltrar.isSelected()) {
			tabla.setVisible(true);
			seleccionFiltro = lazyFiltrar.getSelectedRow();
			dataTable.search();
			modifyGroup.setVisible(true);
			addGroup.setVisible(false);
		} else {
			tabla.setVisible(false);
			addGroup.setVisible(true);
			modifyGroup.setVisible(false);
		}
		visibleButtons();
	}

	public void buscarFiltro() {
		lazyFiltrar.search();
	}

	public void nuevoFiltro() {
		ParametroDTO parametro = new ParametroDTO();
		getController(ParametrosCRUBean.class).load(parametro);
	}

	public void modifyFiltro() {
		if (lazyFiltrar.isSelected()) {
			getController(ParametrosCRUBean.class).load(lazyFiltrar.getSelectedRow());
		}
	}

	@Override
	public void searchBtn() {
		dataTable.search();
	}

	@Override
	public void createBtn() {
		ParametroDTO dto = new ParametroDTO();
		dto.setGrupo(seleccionFiltro.getCodigo());
		getController(ParametrosCRUBean.class).load(dto);
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
			this.controllerPopup(ConfirmPopupBean.class).load("#{ParametrosBean.delete}",
					i18n().valueBundle("mensaje.wish.delete.selected.rows").get());
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
				notificarI18n("mensaje.parametro.was.deleted");
				dataTable.search();
			} else {
				alertaI18n("warn.parametros.no.selected");
			}
		} catch (ParametroException e) {
			error(e);
		}
	}

	@Override
	protected void visibleButtons() {
		var save = PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_CREATE, ParametrosBean.class,
				getUsuario().getGrupoUser());
		var edit = dataTable.isSelected() && PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_UPDATE,
				ParametrosBean.class, getUsuario().getGrupoUser());
		var delete = dataTable.isSelected() && PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_DELETE,
				ParametrosBean.class, getUsuario().getGrupoUser());
		this.save.setValue(save);
		this.edit.setValue(edit);
		this.delete.setValue(delete);
	}

	@Override
	public void clickTable() {
		visibleButtons();
	}

}
