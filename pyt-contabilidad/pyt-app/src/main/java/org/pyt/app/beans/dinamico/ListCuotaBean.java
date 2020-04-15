package org.pyt.app.beans.dinamico;

import static org.pyt.common.constants.InsertResourceConstants.CONST_RESOURCE_IMPL_SVC_CONFIG_GENERIC_FIELD;

import java.util.List;

import org.controlsfx.glyphfont.FontAwesome.Glyph;
import org.pyt.app.components.ConfirmPopupBean;
import org.pyt.common.annotations.Inject;
import org.pyt.common.constants.LanguageConstant;
import org.pyt.common.constants.PermissionConstants;
import org.pyt.common.constants.StylesPrincipalConstant;

import com.pyt.service.interfaces.IConfigGenericFieldSvc;
import com.pyt.service.interfaces.IGenericServiceSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.AGenericInterfacesBean;
import co.com.japl.ea.common.button.apifluid.ButtonsImpl;
import co.com.japl.ea.dto.contabilidad.CuotaDTO;
import co.com.japl.ea.dto.system.ConfigGenericFieldDTO;
import co.com.japl.ea.utls.PermissionUtil;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

@FXMLFile(file = "list_cuota.fxml", path = "view/dinamico")
public class ListCuotaBean extends AGenericInterfacesBean<CuotaDTO> {

	@Inject(resource = "com.pyt.service.implement.GenericServiceSvc")
	private IGenericServiceSvc<CuotaDTO> quoteSvc;
	@FXML
	private TableView<CuotaDTO> tabla;
	@FXML
	private HBox filterTabla;
	@FXML
	private HBox paginator;
	private GridPane gridPane;
	@Inject(resource = CONST_RESOURCE_IMPL_SVC_CONFIG_GENERIC_FIELD)
	private IConfigGenericFieldSvc configGenericSvc;
	private List<ConfigGenericFieldDTO> listFilters;
	private List<ConfigGenericFieldDTO> listColumns;
	@FXML
	private HBox buttons;

	@FXML
	public void initialize() {
		try {
			filtro = new CuotaDTO();
			gridPane = new GridPane();
			gridPane.setHgap(10);
			gridPane.setVgap(10);
			filterTabla.getChildren().addAll(gridPane);
			listFilters = configGenericSvc.getFieldToFilters(this.getClass(), CuotaDTO.class);
			listColumns = configGenericSvc.getFieldToColumns(this.getClass(), CuotaDTO.class);
			loadDataModel(paginator, tabla);
			loadFields(TypeGeneric.FILTER, StylesPrincipalConstant.CONST_GRID_STANDARD);
			loadColumns(StylesPrincipalConstant.CONST_TABLE_CUSTOM);
			visibleButtons();
			ButtonsImpl.Stream(HBox.class).setLayout(buttons).setName("fxml.btn.add").action(this::add).icon(Glyph.SAVE)
					.isVisible(save).setName("fxml.btn.edit").action(this::set).icon(Glyph.EDIT).isVisible(edit)
					.setName("fxml.btn.delete").action(this::del).icon(Glyph.REMOVE).isVisible(delete)
					.setName("fxml.btn.view").action(this::set).icon(Glyph.FILE_TEXT).isVisible(view).build();
		} catch (Exception e) {
			error(e);
		}
	}

	public final void add() {
		try {
			getController(CuotaBean.class).load();
		} catch (Exception e) {
			error(e);
		}
	}

	public final void del() {
		try {
			controllerPopup(ConfirmPopupBean.class).load("#{ListCuotaBean.delete}",
					i18n().valueBundle(LanguageConstant.LANGUAGE_WARNING_DELETE_ROW));
		} catch (Exception e) {
			error(e);
		}
	}

	public final void setDelete(Boolean valid) {
		try {
			if (valid) {
				var list = dataTable.getSelectedRows();
				if (list != null && list.size() > 0) {
					list.forEach(dto -> {
						try {
							quoteSvc.delete(dto, getUsuario());
							alerta(String.format(LanguageConstant.LANGUAGE_SUCCESS_DELETE_PERMISSION_ROW_CODE,
									dto.getCodigo()));
						} catch (Exception e) {
							error(e);
						}
					});
					dataTable.search();
				}
			}
		} catch (Exception e) {
			error(e);
		}
	}

	public final void clickTable() {
		visibleButtons();
	}

	public final void set() {
		try {
			var dto = dataTable.getSelectedRow();
			getController(CuotaBean.class).load(dto);
		} catch (Exception e) {
			error(e);
		}
	}

	@Override
	public TableView<CuotaDTO> getTableView() {
		return tabla;
	}

	@Override
	public GridPane getGridPane(TypeGeneric typeGeneric) {
		return gridPane;
	}

	@Override
	public Integer getMaxColumns(TypeGeneric typeGeneric) {
		return 2;
	}

	@Override
	public CuotaDTO getFilterToTable(CuotaDTO filter) {
		return filter;
	}

	@Override
	public void selectedRow(MouseEvent eventHandler) {
		clickTable();
	}

	@Override
	public List<ConfigGenericFieldDTO> getListGenericsFields(TypeGeneric typeGeneric) {
		switch (typeGeneric) {
		case FILTER:
			return listFilters;
		case COLUMN:
			return listColumns;
		}
		return null;
	}

	@Override
	public Class<CuotaDTO> getClazz() {
		return CuotaDTO.class;
	}

	@Override
	protected void visibleButtons() {
		var save = PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_CREATE, ListCuotaBean.class,
				getUsuario().getGrupoUser());
		var edit = dataTable.isSelected() && PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_UPDATE,
				ListCuotaBean.class, getUsuario().getGrupoUser());
		var delete = dataTable.isSelected() && PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_DELETE,
				ListCuotaBean.class, getUsuario().getGrupoUser());
		var view = !save && !edit && !delete && PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_READ,
				ListCuotaBean.class, getUsuario().getGrupoUser());
		this.save.setValue(save);
		this.edit.setValue(edit);
		this.delete.setValue(delete);
		this.view.setValue(view);
	}
}
