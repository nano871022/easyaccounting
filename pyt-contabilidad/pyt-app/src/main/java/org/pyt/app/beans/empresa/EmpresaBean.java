package org.pyt.app.beans.empresa;

import java.util.List;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.controlsfx.glyphfont.FontAwesome.Glyph;
import org.pyt.app.components.ConfirmPopupBean;
import org.pyt.common.annotations.Inject;
import org.pyt.common.constants.PermissionConstants;
import org.pyt.common.exceptions.EmpresasException;

import com.pyt.service.dto.EmpresaDTO;
import com.pyt.service.interfaces.IEmpresasSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.AGenericInterfacesBean;
import co.com.japl.ea.common.button.apifluid.ButtonsImpl;
import co.com.japl.ea.dto.system.ConfigGenericFieldDTO;
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
 * Bean encargado de crear las empresas
 * 
 * @author alejandro parra
 * @since 07/05/2018
 */
@FXMLFile(path = "view", file = "empresa/listEmpresa.fxml")
public class EmpresaBean extends AGenericInterfacesBean<EmpresaDTO> {
	@Inject(resource = "com.pyt.service.implement.EmpresaSvc")
	private IEmpresasSvc empresaSvc;
	@FXML
	private javafx.scene.control.TableView<EmpresaDTO> tabla;
	@FXML
	private HBox paginador;
	@FXML
	private FlowPane buttons;
	@FXML
	private GridPane filters;
	private BooleanProperty save;
	private BooleanProperty edit;
	private BooleanProperty view;
	private BooleanProperty delete;
	private MultiValuedMap<TypeGeneric, ConfigGenericFieldDTO> configGenerics;

	@FXML
	public void initialize() {
		NombreVentana = i18n().get("fxml.title.list.enterprise");
		registro = new EmpresaDTO();
		filtro = new EmpresaDTO();
		configGenerics = new ArrayListValuedHashMap<>();
		loadProperties();
		loadDataModel(paginador, tabla);
		visibleButtons();
		ButtonsImpl.Stream(FlowPane.class).setLayout(buttons).setName("fxml.btn.save").action(this::add)
				.icon(Glyph.SAVE).isVisible(save).setName("fxml.btn.edit").action(this::set).icon(Glyph.SAVE)
				.isVisible(edit).setName("fxml.btn.delete").action(this::add).icon(Glyph.REMOVE).isVisible(delete)
				.setName("fxml.btn.view").action(this::add).icon(Glyph.FILE_TEXT).isVisible(view).build();

		findFields(TypeGeneric.FILTER, EmpresaDTO.class, EmpresaBean.class)
				.forEach(row -> configGenerics.put(TypeGeneric.FILTER, row));
		findFields(TypeGeneric.COLUMN, EmpresaDTO.class, EmpresaBean.class)
				.forEach(row -> configGenerics.put(TypeGeneric.COLUMN, row));
		loadColumns();
		loadFields(TypeGeneric.FILTER);
	}

	protected void loadProperties() {
		save = new SimpleBooleanProperty();
		edit = new SimpleBooleanProperty();
		view = new SimpleBooleanProperty();
		delete = new SimpleBooleanProperty();
	}

	public void add() {
		getController(EmpresaCRUBean.class);
	}

	public void search() {
		dataTable.search();
	}

	public void del() {
		try {
			controllerPopup(ConfirmPopupBean.class).load("#{EmpresaBean.delete}",
					i18n().get("mensaje.wish.do.delete.selected.rows"));
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
				empresaSvc.delete(registro, getUsuario());
				notificarI18n("mensaje.enterprise.have.been.deleted");
				dataTable.search();
			} else {
				alertaI18n("err.enterprise.have.been.selected");
			}
		} catch (EmpresasException e) {
			error(e);
		}
	}

	public void set() {
		registro = dataTable.getSelectedRow();
		if (registro != null) {
			getController(EmpresaCRUBean.class).load(registro);
		} else {
			alertaI18n("err.enterprise.havent.been.selected");
		}
	}

	public Boolean isSelected() {
		return dataTable.isSelected();
	}

	@Override
	public void selectedRow(MouseEvent eventHandler) {
		visibleButtons();
	}

	protected void visibleButtons() {
		var save = PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_CREATE, EmpresaBean.class,
				getUsuario().getGrupoUser());
		var update = isSelected() && PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_UPDATE,
				EmpresaBean.class, getUsuario().getGrupoUser());
		var delete = isSelected() && PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_DELETE,
				EmpresaBean.class, getUsuario().getGrupoUser());
		var read = !save && PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_READ, EmpresaBean.class,
				getUsuario().getGrupoUser());
		this.save.setValue(save);
		this.edit.setValue(update);
		this.delete.setValue(delete);
		this.view.setValue(read);
	}

	@Override
	public TableView<EmpresaDTO> getTableView() {
		return tabla;
	}

	@Override
	public Integer getMaxColumns(TypeGeneric typeGeneric) {
		return 4;
	}

	@Override
	public List<ConfigGenericFieldDTO> getListGenericsFields(TypeGeneric typeGeneric) {
		return (List<ConfigGenericFieldDTO>) configGenerics.get(typeGeneric);
	}

	@Override
	public Class<EmpresaDTO> getClazz() {
		return EmpresaDTO.class;
	}

	@Override
	public GridPane getGridPane(TypeGeneric typeGeneric) {
		return filters;
	}

	@Override
	public EmpresaDTO getFilterToTable(EmpresaDTO filter) {
		return filtro;
	}
}
