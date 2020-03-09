package org.pyt.app.beans.persona;

import java.util.List;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.controlsfx.glyphfont.FontAwesome.Glyph;
import org.pyt.app.components.ConfirmPopupBean;
import org.pyt.common.annotations.Inject;
import org.pyt.common.constants.PermissionConstants;
import org.pyt.common.exceptions.EmpleadoException;

import com.pyt.service.dto.PersonaDTO;
import com.pyt.service.interfaces.IEmpleadosSvc;

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
 * Bean encargado de listar los trabajadores
 * 
 * @author alejandro parra
 * @since 21/06/2018
 */
@FXMLFile(path = "view/persona", file = "listPersonas.fxml")
public class PersonaBean extends AGenericInterfacesBean<PersonaDTO> {
	@Inject(resource = "com.pyt.service.implement.EmpleadosSvc")
	private IEmpleadosSvc empleadosSvc;
	@FXML
	private javafx.scene.control.TableView<PersonaDTO> tabla;
	@FXML
	private HBox paginador;
	@FXML
	private HBox buttons;
	@FXML
	private GridPane filter;
	private MultiValuedMap<TypeGeneric, ConfigGenericFieldDTO> fieldsConfig;

	@FXML
	public void initialize() {
		NombreVentana = i18n("fxml.personasbean");
		registro = new PersonaDTO();
		loadDataModel(paginador, tabla);
		loadFields(TypeGeneric.FILTER);
		loadColumns();
		visibleButtons();
		fieldsConfig = new ArrayListValuedHashMap<>();
		findFields(TypeGeneric.FILTER, PersonaDTO.class, PersonaBean.class)
				.forEach(row -> fieldsConfig.put(TypeGeneric.FILTER, row));
		findFields(TypeGeneric.COLUMN, PersonaDTO.class, PersonaBean.class)
				.forEach(row -> fieldsConfig.put(TypeGeneric.COLUMN, row));
		ButtonsImpl.Stream(HBox.class).setLayout(buttons).setName("fxml.btn.save").action(this::add).icon(Glyph.SAVE)
				.isVisible(save).setName("fxml.btn.edit").action(this::set).icon(Glyph.EDIT).isVisible(edit)
				.setName("fxml.btn.delete").action(this::del).icon(Glyph.REMOVE).isVisible(delete)
				.setName("fxml.btn.view").action(this::set).icon(Glyph.FILE_TEXT).isVisible(view).build();
	}

	public void clickTable() {
		visibleButtons();
	}

	public void add() {
		getController(PersonaCRUBean.class);
	}

	public void del() {
		try {
			controllerPopup(ConfirmPopupBean.class).load("#{PersonaBean.delete}",
					i18n("mensaje.personabean.wish.deleted.selected.row"));
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
				notificarI18n("mensaje.personabean.deleted.person");
				dataTable.search();
			} else {
				alertaI18n("warn.personabean.no.selected.person");
			}
		} catch (EmpleadoException e) {
			error(e);
		}
	}

	public void set() {
		registro = dataTable.getSelectedRow();
		if (registro != null) {
			getController(PersonaCRUBean.class).load(registro);
		} else {
			alertaI18n("warn.personbean.no.selected.person");
		}
	}

	@Override
	protected void visibleButtons() {
		var save = PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_CREATE, PersonaBean.class,
				getUsuario().getGrupoUser());
		var edit = dataTable.isSelected() && PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_UPDATE,
				PersonaBean.class, getUsuario().getGrupoUser());
		var delete = dataTable.isSelected() && PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_DELETE,
				PersonaBean.class, getUsuario().getGrupoUser());
		var view = !save && !edit && !delete && PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_READ,
				PersonaBean.class, getUsuario().getGrupoUser());
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
	public TableView<PersonaDTO> getTableView() {
		return tabla;
	}

	@Override
	public Integer getMaxColumns(TypeGeneric typeGeneric) {
		return 4;
	}

	@Override
	public List<ConfigGenericFieldDTO> getListGenericsFields(TypeGeneric typeGeneric) {
		return (List<ConfigGenericFieldDTO>) fieldsConfig.get(typeGeneric);
	}

	@Override
	public Class<PersonaDTO> getClazz() {
		return PersonaDTO.class;
	}

	@Override
	public GridPane getGridPane(TypeGeneric typeGeneric) {
		return filter;
	}

	@Override
	public PersonaDTO getFilterToTable(PersonaDTO filter) {
		return filter;
	}
}
