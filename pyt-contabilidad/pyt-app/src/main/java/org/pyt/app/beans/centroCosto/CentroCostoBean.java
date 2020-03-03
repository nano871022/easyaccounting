package org.pyt.app.beans.centroCosto;

import java.util.List;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.controlsfx.glyphfont.FontAwesome.Glyph;
import org.pyt.app.components.ConfirmPopupBean;
import org.pyt.common.annotations.Inject;
import org.pyt.common.constants.PermissionConstants;
import org.pyt.common.exceptions.CentroCostosException;

import com.pyt.service.dto.CentroCostoDTO;
import com.pyt.service.interfaces.ICentroCostosSvc;
import com.pyt.service.interfaces.IParametrosSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.AGenericInterfacesBean;
import co.com.japl.ea.common.button.apifluid.ButtonsImpl;
import co.com.japl.ea.dto.system.ConfigGenericFieldDTO;
import co.com.japl.ea.utls.PermissionUtil;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

/**
 * Bean encargado de crear las empresas
 * 
 * @author alejandro parra
 * @since 07/05/2018
 */
@FXMLFile(path = "view/centroCosto", file = "listCentroCosto.fxml")
public class CentroCostoBean extends AGenericInterfacesBean<CentroCostoDTO> {
	@Inject(resource = "com.pyt.service.implement.CentroCostoSvc")
	private ICentroCostosSvc centroCostoSvc;
	@Inject(resource = "com.pyt.service.implement.ParametrosSvc")
	private IParametrosSvc parametroSvc;
	@FXML
	private javafx.scene.control.TableView<CentroCostoDTO> tabla;
	@FXML
	private HBox paginador;
	@FXML
	private GridPane filter;
	@FXML
	private FlowPane buttons;
	private final static String FIELD_NAME = "nombre";
	private MultiValuedMap<TypeGeneric, ConfigGenericFieldDTO> config;

	@FXML
	public void initialize() {
		NombreVentana = i18n().get("fxml.title.list.costcenter");
		filtro = new CentroCostoDTO();
		save = new SimpleBooleanProperty();
		edit = new SimpleBooleanProperty();
		delete = new SimpleBooleanProperty();
		view = new SimpleBooleanProperty();
		config = new ArrayListValuedHashMap<>();
		loadDataModel(paginador, tabla);
		configFields();
		visibleButtons();
		loadFields(TypeGeneric.FILTER);
		loadColumns();
		ButtonsImpl.Stream(FlowPane.class).setLayout(buttons).setName("fxml.btn.save").action(this::add).isVisible(save)
				.icon(Glyph.SAVE).setName("fxml.btn.edit").action(this::set).isVisible(edit).icon(Glyph.EDIT)
				.setName("fxml.btn.delete").action(this::del).isVisible(delete).icon(Glyph.REMOVE)
				.setName("fxml.btn.view").action(this::set).isVisible(view).icon(Glyph.FILE_TEXT).build();
	}

	private void visibleButtons() {
		var save = PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_CREATE, CentroCostoBean.class,
				getUsuario().getGrupoUser());
		var edit = isSelected() && PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_UPDATE,
				CentroCostoBean.class, getUsuario().getGrupoUser());
		var delete = isSelected() && PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_DELETE,
				CentroCostoBean.class, getUsuario().getGrupoUser());
		var view = !save && PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_READ,
				CentroCostoBean.class, getUsuario().getGrupoUser());
		this.save.setValue(save);
		this.edit.setValue(edit);
		this.delete.setValue(delete);
		this.view.setValue(view);
	}

	private void configFields() {
		findFields(TypeGeneric.FILTER, CentroCostoDTO.class, CentroCostoBean.class)
				.forEach(centroCosto -> config.put(TypeGeneric.FILTER, centroCosto));
		findFields(TypeGeneric.COLUMN, CentroCostoDTO.class, CentroCostoBean.class)
				.forEach(centroCosto -> config.put(TypeGeneric.COLUMN, centroCosto));
	}

	public void clickTable() {
		visibleButtons();
	}

	public void add() {
		getController(CentroCostoCRUBean.class);
	}

	public void search() {
		dataTable.search();
	}

	public void del() {
		try {
			controllerPopup(ConfirmPopupBean.class).load("#{CentroCostoBean.delete}",
					i18n().get("mensaje.whish.do.delete.selected.rows"));
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
				centroCostoSvc.delete(registro, getUsuario());
				notificarI18n("mensaje.enterprise.have.been.deleted");
				dataTable.search();
			} else {
				alertaI18n("mensaje.enterprise.wasnt.selected");
			}
		} catch (CentroCostosException e) {
			error(e);
		}
	}

	public void set() {
		registro = dataTable.getSelectedRow();
		if (registro != null) {
			getController(CentroCostoCRUBean.class).load(registro);
		} else {
			alertaI18n("mensaje.enterprise.wasnt.selected");
		}
	}

	public Boolean isSelected() {
		return dataTable.isSelected();
	}

	@Override
	public void selectedRow(MouseEvent eventHandler) {
		visibleButtons();
	}

	@Override
	public TableView<CentroCostoDTO> getTableView() {
		return tabla;
	}

	@Override
	public Integer getMaxColumns(TypeGeneric typeGeneric) {
		return 4;
	}

	@Override
	public List<ConfigGenericFieldDTO> getListGenericsFields(TypeGeneric typeGeneric) {
		return (List<ConfigGenericFieldDTO>) config.get(typeGeneric);
	}

	@Override
	public Class<CentroCostoDTO> getClazz() {
		return CentroCostoDTO.class;
	}

	@Override
	public GridPane getGridPane(TypeGeneric typeGeneric) {
		return filter;
	}

	@Override
	public CentroCostoDTO getFilterToTable(CentroCostoDTO filter) {
		return filter;
	}
}
