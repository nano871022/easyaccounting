package co.com.japl.ea.app.beans.parametroInventario;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.controlsfx.glyphfont.FontAwesome.Glyph;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.SelectList;
import org.pyt.common.constants.ParametroConstants;
import org.pyt.common.constants.PermissionConstants;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.app.beans.interfaces.ListCRUDBean;
import co.com.japl.ea.app.components.ConfirmPopupBean;
import co.com.japl.ea.beans.abstracts.AListBasicBean;
import co.com.japl.ea.common.button.apifluid.ButtonsImpl;
import co.com.japl.ea.dto.dto.inventario.ParametroInventarioDTO;
import co.com.japl.ea.dto.interfaces.inventarios.IParametroInventariosSvc;
import co.com.japl.ea.exceptions.ParametroException;
import co.com.japl.ea.utls.DataTableFXMLUtil;
import co.com.japl.ea.utls.PermissionUtil;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
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
@FXMLFile(path = "view/parametroInventarios", file = "ListParametrosInventarios.fxml", nombreVentana = "Parametros del Sistema de inventarios")
public class ParametrosInventariosBean extends AListBasicBean<ParametroInventarioDTO, ParametroInventarioDTO>
		implements ListCRUDBean {
	@Inject()
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
	private DataTableFXMLUtil<ParametroInventarioDTO, ParametroInventarioDTO> lazyFiltrar;
	private ParametroInventarioDTO filtrarGrupo;
	private ParametroInventarioDTO seleccionFiltro;
	@FXML
	private TextField filtroGrupo;
	@FXML
	private HBox buttons;
	@FXML
	private HBox addGroup;
	@FXML
	private HBox modifyGroup;
	private BooleanProperty editFilter;

	@FXML
	public void initialize() {
		registro = new ParametroInventarioDTO();
		SelectList.put(estado, ParametroConstants.mapa_estados_parametros);
		SelectList.put(grupo, ParametroConstants.mapa_grupo);
		editFilter = new SimpleBooleanProperty();
		estado.getSelectionModel().selectFirst();
		grupo.getSelectionModel().selectFirst();
		addGroup.setVisible(true);
		modifyGroup.setVisible(false);
		tabla.setVisible(false);
		lazy();
		lazy2();
		visibleButtons();
		ButtonsImpl.Stream(HBox.class).setLayout(buttons).setName("fxml.btn.add").action(this::createBtn)
				.icon(Glyph.SAVE).isVisible(save).setName("fxml.btn.edit").action(this::modifyBtn).icon(Glyph.EDIT)
				.isVisible(edit).setName("fxml.btn.delete").action(this::deleteBtn).icon(Glyph.REMOVE).isVisible(delete)
				.build();
		ButtonsImpl.Stream(HBox.class).setLayout(addGroup).setName("fxml.btn.search").action(this::buscarFiltro)
				.icon(Glyph.SEARCH).isVisible(true).setName("fxml.btn.add.group").action(this::nuevoFiltro)
				.icon(Glyph.SAVE).isVisible(save).build();
		ButtonsImpl.Stream(HBox.class).setLayout(modifyGroup).setName("fxml.btn.edit.group").action(this::modifyFiltro)
				.icon(Glyph.EDIT).isVisible(editFilter).build();
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
				if (seleccionFiltro != null && StringUtils.isNotBlank(seleccionFiltro.getCodigo())) {
					filtro.setGrupo(seleccionFiltro.getCodigo());
				}
				return filtro;
			}
		};

	}

	@Override
	public void clickTable() {
		visibleButtons();
	}

	public void clickTableFiltrar() {
		if (lazyFiltrar.isSelected()) {
			tabla.setVisible(true);
			seleccionFiltro = lazyFiltrar.getSelectedRow();
			dataTable.search();
		} else {
			tabla.setVisible(false);
		}
		visibleButtons();
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
					i18n("warn.parametroinventario.wish.delete.rows.selected"));
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
				notificarI18n("mensaje.parametroinventario.delete");
				dataTable.search();
			} else {
				alertaI18n("warn.parametroinventario.no.selected.company");
			}
		} catch (ParametroException e) {
			error(e);
		}
	}

	@Override
	protected void visibleButtons() {
		var save = PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_CREATE,
				ParametrosInventariosBean.class, getUsuario().getGrupoUser());
		var edit = dataTable.isSelected() && PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_UPDATE,
				ParametrosInventariosBean.class, getUsuario().getGrupoUser());
		var editFilter = lazyFiltrar.isSelected() && PermissionUtil.INSTANCE().havePerm(
				PermissionConstants.CONST_PERM_UPDATE, ParametrosInventariosBean.class, getUsuario().getGrupoUser());
		var delete = dataTable.isSelected() && PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_DELETE,
				ParametrosInventariosBean.class, getUsuario().getGrupoUser());
		this.save.setValue(save);
		this.edit.setValue(edit);
		this.editFilter.setValue(editFilter);
		this.delete.setValue(delete);
	}

}
