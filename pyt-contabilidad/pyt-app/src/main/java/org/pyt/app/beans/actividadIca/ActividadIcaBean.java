package org.pyt.app.beans.actividadIca;

import java.util.List;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.controlsfx.glyphfont.FontAwesome.Glyph;
import org.pyt.app.components.ConfirmPopupBean;
import org.pyt.common.annotations.Inject;
import org.pyt.common.constants.PermissionConstants;
import org.pyt.common.exceptions.ActividadIcaException;
import org.pyt.common.exceptions.LoadAppFxmlException;

import com.pyt.service.dto.ActividadIcaDTO;
import com.pyt.service.interfaces.IActividadIcaSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.AGenericInterfacesBean;
import co.com.japl.ea.common.button.apifluid.ButtonsImpl;
import co.com.japl.ea.dto.system.ConfigGenericFieldDTO;
import co.com.japl.ea.utls.DataTableFXMLUtil;
import co.com.japl.ea.utls.PermissionUtil;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

/**
 * Bean encargado de crear las actividades ica
 * 
 * @author alejandro parra
 * @since 07/05/2018
 */
@FXMLFile(path = "view/actividadIca", file = "listActividadIca.fxml")
public class ActividadIcaBean extends AGenericInterfacesBean<ActividadIcaDTO> {
	@Inject(resource = "com.pyt.service.implement.ActividadIcaSvc")
	private IActividadIcaSvc actividadIcaSvc;
	@FXML
	private javafx.scene.control.TableView<ActividadIcaDTO> tabla;
	@FXML
	private HBox paginador;
	@FXML
	private GridPane filter;
	@FXML
	private FlowPane buttons;
	private BooleanProperty save;
	private BooleanProperty edit;
	private BooleanProperty delete;
	private BooleanProperty view;
	private MultiValuedMap<TypeGeneric, ConfigGenericFieldDTO> config;

	@FXML
	public void initialize() {
		NombreVentana = i18n().valueBundle("fxml.title.list.ica.activity").get();
		config = new ArrayListValuedHashMap<>();
		filtro = new ActividadIcaDTO();
		save = new SimpleBooleanProperty();
		edit = new SimpleBooleanProperty();
		delete = new SimpleBooleanProperty();
		view = new SimpleBooleanProperty();
		loadDataModel(paginador, tabla);
		visibleButtons();
		configFields();
		ButtonsImpl.Stream(FlowPane.class).setLayout(buttons).setName("fxml.btn.save").isVisible(save).icon(Glyph.SAVE)
				.action(this::add).setName("fxml.btn.edit").isVisible(edit).icon(Glyph.EDIT).action(this::set)
				.setName("fxml.btn.delete").isVisible(delete).icon(Glyph.REMOVE).action(this::del)
				.setName("fxml.btn.view").isVisible(view).icon(Glyph.FILE_TEXT).action(this::set).build();
		loadColumns();
		loadFields(TypeGeneric.FILTER);
	}

	private void configFields() {
		findFields(TypeGeneric.FILTER, ActividadIcaDTO.class, ActividadIcaBean.class)
				.forEach(ica -> config.put(TypeGeneric.FILTER, ica));
		findFields(TypeGeneric.COLUMN, ActividadIcaDTO.class, ActividadIcaBean.class)
				.forEach(ica -> config.put(TypeGeneric.COLUMN, ica));
	}

	private void visibleButtons() {
		var save = PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_CREATE, ActividadIcaBean.class,
				getUsuario().getGrupoUser());
		var edit = isSelected() && PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_UPDATE,
				ActividadIcaBean.class, getUsuario().getGrupoUser());
		var delete = isSelected() && PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_DELETE,
				ActividadIcaBean.class, getUsuario().getGrupoUser());
		var view = !save && PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_READ,
				ActividadIcaBean.class, getUsuario().getGrupoUser());
		this.save.setValue(save);
		this.edit.setValue(edit);
		this.delete.setValue(delete);
		this.view.setValue(view);

	}

	public void clickTable() {
		visibleButtons();
	}

	public void add() {
		getController(ActividadIcaCRUBean.class);
	}

	public void search() {
		dataTable.search();
	}

	public void del() {
		try {
			controllerPopup(ConfirmPopupBean.class).load("#{ActividadIcaBean.delete}",
					i18n().valueBundle("whish.delete.selected.rows").get());
		} catch (LoadAppFxmlException e) {
			error(e);
		}
	}

	public void setDelete(Boolean valid) {
		try {
			if (!valid)
				return;
			registro = dataTable.getSelectedRow();
			if (registro != null) {
				actividadIcaSvc.delete(registro, getUsuario());
				notificarI18n("mensaje.ica.activity.was.deleted");
				dataTable.search();
			} else {
				alerta("mensaje.ica.activity.wasnt.selected");
			}
		} catch (ActividadIcaException e) {
			error(e);
		}
	}

	public void set() {
		registro = dataTable.getSelectedRow();
		if (registro != null) {
			getController(ActividadIcaCRUBean.class).load(registro);
		} else {
			alerta("mensaje.ica.activity.havent.been.selected");
		}
	}

	public Boolean isSelected() {
		return dataTable.isSelected();
	}

	public DataTableFXMLUtil<ActividadIcaDTO, ActividadIcaDTO> getDt() {
		return dataTable;
	}

	@Override
	public void selectedRow(MouseEvent eventHandler) {
		visibleButtons();
	}

	@Override
	public TableView<ActividadIcaDTO> getTableView() {
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
	public Class<ActividadIcaDTO> getClazz() {
		return ActividadIcaDTO.class;
	}

	@Override
	public GridPane getGridPane(TypeGeneric typeGeneric) {
		return filter;
	}

	@Override
	public ActividadIcaDTO getFilterToTable(ActividadIcaDTO filter) {
		return filtro;
	}
}
