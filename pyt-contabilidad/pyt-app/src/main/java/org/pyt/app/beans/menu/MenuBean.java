package org.pyt.app.beans.menu;

import static org.pyt.common.constants.FxmlBeanConstant.CONST_MENU_FXML;
import static org.pyt.common.constants.FxmlBeanConstant.CONST_PATH_MENU;
import static org.pyt.common.constants.InsertResourceConstants.CONST_RESOURCE_IMPL_SVC_CONFIG_GENERIC_FIELD;
import static org.pyt.common.constants.InsertResourceConstants.CONST_RESOURCE_IMPL_SVC_GENERIC_SERVICE;
import static org.pyt.common.constants.LanguageConstant.CONST_MSN_ERR_FIELD_EMPTY;
import static org.pyt.common.constants.StylesPrincipalConstant.CONST_GRID_STANDARD;
import static org.pyt.common.constants.languages.Menu.CONST_MSN_MENU_INSERTED;
import static org.pyt.common.constants.languages.Menu.CONST_MSN_MENU_UPDATED;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.UtilControlFieldFX;
import org.pyt.common.validates.ValidFields;

import com.pyt.service.interfaces.IConfigGenericFieldSvc;
import com.pyt.service.interfaces.IGenericServiceSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.AGenericInterfacesFieldBean;
import co.com.japl.ea.dto.system.MenuDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

@FXMLFile(file = CONST_MENU_FXML, path = CONST_PATH_MENU)
public class MenuBean extends AGenericInterfacesFieldBean<MenuDTO> {
	@Inject(resource = CONST_RESOURCE_IMPL_SVC_GENERIC_SERVICE)
	private IGenericServiceSvc<MenuDTO> menusSvc;
	@FXML
	private GridPane gridPane;
	@FXML
	private Label lblTitle;
	public static final String CONST_FIELD_NAME_MENUS_URL = "url";
	public static final String CONST_FIELD_NAME_MENUS_CLASS_PATH = "classPath";
	public static final String CONST_FIELD_NAME_MENUS_STATE = "state";
	@Inject(resource = CONST_RESOURCE_IMPL_SVC_CONFIG_GENERIC_FIELD)
	private IConfigGenericFieldSvc configGenericSvc;

	@FXML
	public void initialize() {
		try {
			registro = new MenuDTO();
			setClazz(MenuDTO.class);
			fields = configGenericSvc.getFieldToFields(this.getClass(), MenuDTO.class);
			this.loadFields(TypeGeneric.FIELD, CONST_GRID_STANDARD);
		} catch (Exception e) {
			error(e);
		}
	}

	public final void load() {
		registro = new MenuDTO();
	}

	public final void load(MenuDTO dto) {
		registro = dto;
		var util = new UtilControlFieldFX();
		util.loadValuesInFxml(dto, gridPane);
	}

	@Override
	public GridPane getGridPane(TypeGeneric typeGeneric) {
		return gridPane;
	}

	public final Boolean valid() {
		Boolean valid = true;
		valid &= ValidFields.valid((TextField) getMapFields(TypeGeneric.FIELD).get(CONST_FIELD_NAME_MENUS_URL), true, 1,
				100, i18n().valueBundle(CONST_MSN_ERR_FIELD_EMPTY));
		valid &= ValidFields.valid((TextField) getMapFields(TypeGeneric.FIELD).get(CONST_FIELD_NAME_MENUS_CLASS_PATH),
				true, 1, 100, i18n().valueBundle(CONST_MSN_ERR_FIELD_EMPTY));
		valid &= ValidFields.valid((TextField) getMapFields(TypeGeneric.FIELD).get(CONST_FIELD_NAME_MENUS_STATE), true,
				1, 2, i18n().valueBundle(CONST_MSN_ERR_FIELD_EMPTY));
		return valid;
	}

	public final void add() {
		try {
			if (valid()) {
				if (StringUtils.isBlank(registro.getCodigo())) {
					menusSvc.insert(registro, getUsuario());
					notificar(i18n().valueBundle(CONST_MSN_MENU_INSERTED));
				} else {
					menusSvc.update(registro, getUsuario());
					notificar(i18n().valueBundle(CONST_MSN_MENU_UPDATED));
				}
			}
		} catch (Exception e) {
			error(e);
		}
	}

	public final void cancel() {
		getController(ListMenusBean.class);
	}

	@Override
	public Integer getMaxColumns(TypeGeneric typeGeneric) {
		return 2;
	}

	@Override
	public MultiValuedMap<String, Object> getMapListToChoiceBox() {
		var map = new ArrayListValuedHashMap<String, Object>();
		return map;
	}

}
