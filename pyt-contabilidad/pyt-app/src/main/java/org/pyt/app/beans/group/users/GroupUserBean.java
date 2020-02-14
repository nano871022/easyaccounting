package org.pyt.app.beans.group.users;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.Inject;
import org.pyt.common.constants.InsertResourceConstants;
import org.pyt.common.validates.ValidFields;

import com.pyt.service.interfaces.IConfigGenericFieldSvc;
import com.pyt.service.interfaces.IGenericServiceSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.AGenericInterfacesFieldBean;
import co.com.japl.ea.dto.system.GroupUsersDTO;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

@FXMLFile(file = "group_user.fxml", path = "view/group_users")
public class GroupUserBean extends AGenericInterfacesFieldBean<GroupUsersDTO> {
	@Inject(resource = "com.pyt.service.implement.GenericServiceSvc")
	private IGenericServiceSvc<GroupUsersDTO> groupUsersSvc;
	@FXML
	private GridPane gridPane;
	@FXML
	private Label lblTitle;
	public static final String CONST_FIELD_NAME_GROUP_USERS_NAME = "name";
	public static final String CONST_FIELD_NAME_GROUP_USERS_STATE = "state";
	@Inject(resource = InsertResourceConstants.CONST_RESOURCE_IMPL_SVC_CONFIG_GENERIC_FIELD)
	private IConfigGenericFieldSvc configSvc;
	private MultiValuedMap<String, Object> toChoiceBox;

	@FXML
	public void initialize() {
		try {
			toChoiceBox = new ArrayListValuedHashMap<>();
			registro = new GroupUsersDTO();
			setClazz(GroupUsersDTO.class);
			fields = configSvc.getFieldToFields(this.getClass(), GroupUsersDTO.class);
			loadFields(TypeGeneric.FIELD);
		} catch (Exception e) {
			error(e);
		}
	}

	public final void load() {
		registro = new GroupUsersDTO();
		loadFields(TypeGeneric.FIELD);
	}

	public final void load(GroupUsersDTO dto) {
		registro = dto;
		loadFields(TypeGeneric.FIELD);
	}

	@Override
	public GridPane getGridPane(TypeGeneric typeGeneric) {
		return gridPane;
	}

	public final Boolean valid() {
		Boolean valid = true;
		valid &= getMapFields(TypeGeneric.FIELD).get(CONST_FIELD_NAME_GROUP_USERS_NAME).stream().map(node -> ValidFields
				.valid(registro.getName(), node, true, 1, 100, i18n().valueBundle("msn.error.field.empty"))).findFirst()
				.get();
		valid &= getMapFields(TypeGeneric.FIELD).get(CONST_FIELD_NAME_GROUP_USERS_STATE).stream().map(node -> {
			if (node instanceof ChoiceBox) {
				return (Boolean) ValidFields.valid(registro.getState(), node, true, null, null,
						i18n().valueBundle("msn.error.field.empty"));
			}
			return (Boolean) ValidFields.valid(registro.getState(), node, true, 1, 100,
					i18n().valueBundle("msn.error.field.empty"));
		}).findFirst().get();
		return valid;
	}

	public final void add() {
		try {
			if (valid()) {
				if (StringUtils.isBlank(registro.getCodigo())) {
					groupUsersSvc.insert(registro, getUsuario());
					notificar(i18n().valueBundle("mensaje.group.user.inserted"));
				} else {
					groupUsersSvc.update(registro, getUsuario());
					notificar(i18n().valueBundle("mensaje.group.user.updated"));
				}
			}
		} catch (Exception e) {
			error(e);
		}
	}

	public final void cancel() {
		getController(ListGroupUsersBean.class);
	}

	@Override
	public Integer getMaxColumns(TypeGeneric typeGeneric) {
		return 2;
	}

	@Override
	public MultiValuedMap<String, Object> getMapListToChoiceBox() {
		return toChoiceBox;
	}

}
