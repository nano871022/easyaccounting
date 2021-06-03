package co.com.japl.ea.pyt.app.beans.trabajador;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.controlsfx.glyphfont.FontAwesome.Glyph;
import org.pyt.common.annotations.Inject;
import org.pyt.common.constants.PermissionConstants;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.app.components.ConfirmPopupBean;
import co.com.japl.ea.beans.abstracts.AGenericInterfacesBean;
import co.com.japl.ea.common.button.apifluid.ButtonsImpl;
import co.com.japl.ea.dto.dto.TrabajadorDTO;
import co.com.japl.ea.dto.interfaces.IEmpleadosSvc;
import co.com.japl.ea.dto.system.ConfigGenericFieldDTO;
import co.com.japl.ea.exceptions.EmpleadoException;
import co.com.japl.ea.utls.PermissionUtil;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

/**
 * Bean encargado de listar los trabajadores
 * 
 * @author alejandro parra
 * @since 21/06/2018
 */
@FXMLFile(path = "view/persona", file = "listTrabajador.fxml")
public class TrabajadorBean extends AGenericInterfacesBean<TrabajadorDTO> {
	@Inject(resource = "com.pyt.service.implement.EmpleadosSvc")
	private IEmpleadosSvc empleadosSvc;
	@FXML
	private javafx.scene.control.TableView<TrabajadorDTO> tabla;
	@FXML
	private HBox paginador;
	@FXML
	private HBox buttons;
	@FXML
	private GridPane filter;
	private MultiValuedMap<TypeGeneric, ConfigGenericFieldDTO> fieldsConfig;

	@FXML
	public void initialize() {
		NombreVentana = i18n().get("fxml.title.list.employed");
		registro = new TrabajadorDTO();
		filtro = new TrabajadorDTO();
		fieldsConfig = new ArrayListValuedHashMap<>();
		findFields(TypeGeneric.COLUMN, TrabajadorDTO.class, TrabajadorBean.class)
				.forEach(row -> fieldsConfig.put(TypeGeneric.COLUMN, row));
		findFields(TypeGeneric.FILTER, TrabajadorDTO.class, TrabajadorBean.class)
				.forEach(row -> fieldsConfig.put(TypeGeneric.FILTER, row));
		loadDataModel(paginador, tabla);
		loadColumns();
		loadFields(TypeGeneric.FILTER);
		visibleButtons();
		ButtonsImpl.Stream(HBox.class).setLayout(buttons).setName("fxml.btn.add").action(this::add).icon(Glyph.SAVE)
				.isVisible(save).setName("fxml.btn.edit").action(this::set).icon(Glyph.SAVE).isVisible(edit)
				.setName("fxml.btn.delete").action(this::del).icon(Glyph.SAVE).isVisible(delete)
				.setName("fxml.btn.view").action(this::set).icon(Glyph.SAVE).isVisible(view).build();
	}

	public void add() {
		getController(TrabajadorCRUBean.class);
	}

	public void del() {
		try {
			controllerPopup(ConfirmPopupBean.class).load("#{TrabajadorBean.delete}",
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
				empleadosSvc.delete(registro, getUsuario());
				notificarI18n("mensaje.employe.have.been.deleted");
				dataTable.search();
			} else {
				alertaI18n("mensaje.employe.havent.been.selected");
			}
		} catch (EmpleadoException e) {
			error(e);
		}
	}

	public void set() {
		registro = dataTable.getSelectedRow();
		if (registro != null) {
			getController(TrabajadorCRUBean.class).load(registro);
		} else {
			alertaI18n("warn.employe.havent.been.selected");
		}
	}

	@Override
	protected void visibleButtons() {
		var save = PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_CREATE, TrabajadorBean.class,
				getUsuario().getGrupoUser());
		var edit = dataTable.isSelected() && PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_UPDATE,
				TrabajadorBean.class, getUsuario().getGrupoUser());
		var delete = dataTable.isSelected() && PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_DELETE,
				TrabajadorBean.class, getUsuario().getGrupoUser());
		var view = !save && PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_READ,
				TrabajadorBean.class, getUsuario().getGrupoUser());
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
	public TableView<TrabajadorDTO> getTableView() {
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
	public Class<TrabajadorDTO> getClazz() {
		return TrabajadorDTO.class;
	}

	@Override
	public GridPane getGridPane(TypeGeneric typeGeneric) {
		return filter;
	}

	@Override
	public TrabajadorDTO getFilterToTable(TrabajadorDTO filter) {
		return filter;
	}
}
