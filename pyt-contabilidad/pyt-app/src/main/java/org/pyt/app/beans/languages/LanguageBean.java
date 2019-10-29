package org.pyt.app.beans.languages;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.UtilControlFieldFX;
import org.pyt.common.constants.StylesPrincipalConstant;
import org.pyt.common.validates.ValidFields;

import com.pyt.service.interfaces.IGenericServiceSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.AGenericInterfacesFieldBean;
import co.com.japl.ea.dto.system.LanguagesDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

@FXMLFile(file = "languages.fxml", path = "view/languages")
public class LanguageBean extends AGenericInterfacesFieldBean<LanguagesDTO> {
	@Inject(resource = "com.pyt.service.implement.GenericServiceSvc")
	private IGenericServiceSvc<LanguagesDTO> languagesSvc;
	@FXML
	private GridPane gridPaneLanguages;
	@FXML
	private Label lblTitle;
	public static final String CONST_FIELD_NAME_LANGUAGES_CODE = "code";
	public static final String CONST_FIELD_NAME_LANGUAGES_TEXT = "text";
	public static final String CONST_FIELD_NAME_LANGUAGES_STATE = "state";
	public static final String CONST_FIELD_NAME_LANGUAGES_IDIOM = "idiom";

	@FXML
	public void initialize() {
		try {
			registro = new LanguagesDTO();
			setClazz(LanguagesDTO.class);
			loadFields(TypeGeneric.FIELD, StylesPrincipalConstant.CONST_GRID_STANDARD);
		} catch (Exception e) {
			error(e);
		}
	}

	public final void load() {
		registro = new LanguagesDTO();
	}

	public final void load(LanguagesDTO dto) {
		registro = dto;
		var util = new UtilControlFieldFX();
		util.loadValuesInFxml(dto, gridPaneLanguages);
	}

	@Override
	public GridPane getGridPane(TypeGeneric typeGeneric) {
		return gridPaneLanguages;
	}

	public final Boolean valid() {
		Boolean valid = true;
		valid &= ValidFields.valid((TextField) getMapFields(TypeGeneric.FIELD).get(CONST_FIELD_NAME_LANGUAGES_CODE),
				true, 1, 100, i18n().valueBundle("msn.error.field.empty"));
		valid &= ValidFields.valid((TextField) getMapFields(TypeGeneric.FIELD).get(CONST_FIELD_NAME_LANGUAGES_TEXT),
				true, 1, 100, i18n().valueBundle("msn.error.field.empty"));
		valid &= ValidFields.valid((TextField) getMapFields(TypeGeneric.FIELD).get(CONST_FIELD_NAME_LANGUAGES_STATE),
				true, 1, 2, i18n().valueBundle("msn.error.field.empty"));
		valid &= ValidFields.valid((TextField) getMapFields(TypeGeneric.FIELD).get(CONST_FIELD_NAME_LANGUAGES_IDIOM),
				true, 2, 10, i18n().valueBundle("msn.error.field.empty"));
		return valid;
	}

	public final void add() {
		try {
			if (valid()) {
				if (StringUtils.isBlank(registro.getCodigo())) {
					languagesSvc.insert(registro, userLogin);
					notificar(i18n().valueBundle("mensaje.language.inserted"));
				} else {
					languagesSvc.update(registro, userLogin);
					notificar(i18n().valueBundle("mensaje.language.updated"));
				}
			}
		} catch (Exception e) {
			error(e);
		}
	}

	public final void newRow() {
		load();
		this.clearFields(TypeGeneric.FIELD);
	}

	public final void cancel() {
		getController(ListLanguagesBean.class);
	}

	@Override
	public Integer getMaxColumns(TypeGeneric typeGeneric) {
		return 2;
	}

	@Override
	public MultiValuedMap<String, Object> getMapListToChoiceBox() {
		return null;
	}

}
