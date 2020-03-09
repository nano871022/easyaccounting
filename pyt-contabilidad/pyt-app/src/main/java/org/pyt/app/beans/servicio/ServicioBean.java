package org.pyt.app.beans.servicio;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.controlsfx.glyphfont.FontAwesome.Glyph;
import org.pyt.app.components.ConfirmPopupBean;
import org.pyt.common.annotations.Inject;
import org.pyt.common.constants.PermissionConstants;
import org.pyt.common.exceptions.ServiciosException;

import com.pyt.service.dto.ServicioDTO;
import com.pyt.service.interfaces.IServiciosSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.AGenericInterfacesBean;
import co.com.japl.ea.common.button.apifluid.ButtonsImpl;
import co.com.japl.ea.dto.system.ConfigGenericFieldDTO;
import co.com.japl.ea.utls.PermissionUtil;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

/**
 * Bean encargado de crear las empresas
 * 
 * @author alejandro parra
 * @since 07/05/2018
 */
@FXMLFile(path = "view/servicio", file = "listServicio.fxml")
public class ServicioBean extends AGenericInterfacesBean<ServicioDTO> {
	@Inject(resource = "com.pyt.service.implement.ServiciosSvc")
	private IServiciosSvc serviciosSvc;
	@FXML
	private javafx.scene.control.TableView<ServicioDTO> tabla;
	@FXML
	private HBox paginador;
	@FXML
	private HBox buttons;
	@FXML
	private GridPane filter;
	private MultiValuedMap<TypeGeneric, ConfigGenericFieldDTO> fieldsConfig;

	@FXML
	public void initialize() {
		NombreVentana = i18n().get("fxml.title.list.services");
		fieldsConfig = new ArrayListValuedHashMap<>();
		filtro = new ServicioDTO();
		findFields(TypeGeneric.FILTER, ServicioDTO.class, ServicioBean.class)
				.forEach(row -> fieldsConfig.put(TypeGeneric.FILTER, row));
		findFields(TypeGeneric.COLUMN, ServicioDTO.class, ServicioBean.class)
				.forEach(row -> fieldsConfig.put(TypeGeneric.COLUMN, row));
		loadDataModel(paginador, tabla);
		loadFields(TypeGeneric.FILTER);
		loadColumns();
		visibleButtons();
		ButtonsImpl.Stream(HBox.class).setLayout(buttons).setName("fxml.btn.save").action(this::add).icon(Glyph.SAVE)
				.isVisible(save).setName("fxml.btn.edit").action(this::set).icon(Glyph.EDIT).isVisible(edit)
				.setName("fxml.btn.delete").action(this::del).icon(Glyph.REMOVE).isVisible(delete)
				.setName("fxml.btn.view").action(this::set).icon(Glyph.FILE_TEXT).isVisible(view).build();
	}

	public void add() {
		getController(ServicioCRUBean.class);
	}

	public void del() {
		try {
			controllerPopup(ConfirmPopupBean.class).load("#{ServicioBean.delete}",
					i18n("wish.do.delete.selected.rows"));
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
				serviciosSvc.delete(registro, getUsuario());
				notificarI18n("mensaje.service.have.been.deleted");
				dataTable.search();
			} else {
				alertaI18n("err.service.havent.been.selected");
			}
		} catch (ServiciosException e) {
			error(e);
		}
	}

	public void set() {
		registro = dataTable.getSelectedRow();
		if (registro != null) {
			getController(ServicioCRUBean.class).load(registro);
		} else {
			alertaI18n("err.service.havent.been.selected");
		}
	}

	@Override
	protected void visibleButtons() {
		var save = PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_CREATE, ServicioBean.class,
				getUsuario().getGrupoUser());
		var edit = dataTable.isSelected() && PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_UPDATE,
				ServicioBean.class, getUsuario().getGrupoUser());
		var delete = dataTable.isSelected() && PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_DELETE,
				ServicioBean.class, getUsuario().getGrupoUser());
		var view = !save && PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_READ, ServicioBean.class,
				getUsuario().getGrupoUser());
		this.save.setValue(save);
		this.edit.setValue(edit);
		this.delete.setValue(delete);
		this.view.setValue(view);
	}

	@Override
	public void selectedRow(MouseEvent eventHandler) {
		visibleButtons();
	}

	@Override
	public TableView<ServicioDTO> getTableView() {
		return tabla;
	}

	@Override
	public Integer getMaxColumns(TypeGeneric typeGeneric) {
		return 4;
	}

	@Override
	public List<ConfigGenericFieldDTO> getListGenericsFields(TypeGeneric typeGeneric) {
		return fieldsConfig.get(typeGeneric).stream().collect(Collectors.toList());
	}

	@Override
	public Class<ServicioDTO> getClazz() {
		return ServicioDTO.class;
	}

	@Override
	public GridPane getGridPane(TypeGeneric typeGeneric) {
		return filter;
	}

	@Override
	public ServicioDTO getFilterToTable(ServicioDTO filter) {
		return filter;
	}
}
