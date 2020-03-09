package org.pyt.app.beans.persona;

import java.util.List;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.glyphfont.FontAwesome.Glyph;
import org.ea.app.custom.PopupParametrizedControl;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.DtoUtils;
import org.pyt.common.constants.ParametroConstants;
import org.pyt.common.constants.PermissionConstants;
import org.pyt.common.exceptions.EmpleadoException;

import com.pyt.service.dto.ParametroDTO;
import com.pyt.service.dto.PersonaDTO;
import com.pyt.service.interfaces.IEmpleadosSvc;
import com.pyt.service.interfaces.IParametrosSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.AGenericInterfacesFieldBean;
import co.com.japl.ea.common.button.apifluid.ButtonsImpl;
import co.com.japl.ea.dto.system.ConfigGenericFieldDTO;
import co.com.japl.ea.utls.PermissionUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

/**
 * Se encarga de procesar la pantalla de creacion y actualizacion de una empresa
 * 
 * @author Alejandro Parra
 * @since 2018-05-22
 */
@FXMLFile(path = "/view", file = "persona/personas.fxml")
public class PersonaCRUBean extends AGenericInterfacesFieldBean<PersonaDTO> {
	@Inject(resource = "com.pyt.service.implement.ParametrosSvc")
	private IParametrosSvc parametroSvc;
	@Inject(resource = "com.pyt.service.implement.EmpleadosSvc")
	private IEmpleadosSvc empleadosSvc;
	@FXML
	private Label titulo;
	@FXML
	private BorderPane pane;
	@FXML
	private PopupParametrizedControl profesion;
	private List<ParametroDTO> lTipoDocumentos;
	public final static String FIELD_NAME = "nombre";
	public final static String FIELD_CODE = "codigo";
	public final static String FIELD_VALOR = "valor";
	private MultiValuedMap<TypeGeneric, ConfigGenericFieldDTO> fieldsConfig;
	@FXML
	private HBox buttons;
	@FXML
	private GridPane fields;

	@FXML
	public void initialize() {
		NombreVentana = i18n("fxml.personacrubean");
		titulo.setText(NombreVentana);
		registro = new PersonaDTO();
		fieldsConfig = new ArrayListValuedHashMap<>();
		setClazz(PersonaDTO.class);
		visibleButtons();
		loadFields();
		loadFields(TypeGeneric.FIELD);
		ButtonsImpl.Stream(HBox.class).setLayout(buttons).setName("fxml.btn.save").action(this::add).icon(Glyph.SAVE)
				.isVisible(save).setName("fxml.btn.save").action(this::add).icon(Glyph.EDIT).isVisible(edit)
				.setName("fxml.btn.save").action(this::cancel).build();

	}

	public void load(PersonaDTO dto) {
		if (dto != null && dto.getCodigo() != null) {
			registro = dto;
			titulo.setText(i18n("fxml.personacrubean.edit"));
			visibleButtons();
		} else {
			errorI18n("err.personacrubean.person.edit.invalid");
			cancel();
		}
	}

	public void popupProfesion() {
		try {
			controllerGenPopup(ParametroDTO.class).addDefaultValuesToGenericParametrized("estado", "A")
					.addDefaultValuesToGenericParametrized(ParametroConstants.FIELD_NAME_GROUP,
							parametroSvc.getIdByParametroGroup(ParametroConstants.GRUPO_PROFESIONES))
					.load("#{PersonaCRUBean.profesion}");
		} catch (Exception e) {
			error(e);
		}
	}

	public void setProfesion(ParametroDTO profesion) {
		registro.setProfesion(profesion);
		this.profesion.setText(profesion.getNombre());
	}

	public void add() {
		try {
			if (StringUtils.isNotBlank(registro.getCodigo())) {
				empleadosSvc.update(registro, getUsuario());
				notificarI18n("mensaje.personacrubean.person.added.succefull");
				cancel();
			} else {
				empleadosSvc.insert(registro, getUsuario());
				notificarI18n("mensaje.personacrubean.person.saved.succefull");
				cancel();
			}
		} catch (EmpleadoException e) {
			error(e);
		}
	}

	public void cancel() {
		getController(PersonaBean.class);
	}

	@Override
	protected void visibleButtons() {
		var save = !DtoUtils.haveCode(registro) && PermissionUtil.INSTANCE()
				.havePerm(PermissionConstants.CONST_PERM_CREATE, PersonaBean.class, getUsuario().getGrupoUser());
		var edit = DtoUtils.haveCode(registro) && PermissionUtil.INSTANCE()
				.havePerm(PermissionConstants.CONST_PERM_UPDATE, PersonaBean.class, getUsuario().getGrupoUser());
		this.save.setValue(save);
		this.edit.setValue(edit);
	}

	@Override
	public GridPane getGridPane(TypeGeneric typeGeneric) {
		return fields;
	}

	@Override
	public Integer getMaxColumns(TypeGeneric typeGeneric) {
		return 4;
	}

}
