package org.pyt.app.beans.languages;

import java.util.Locale;
import java.util.Optional;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.Inject;
import org.pyt.common.constants.InsertResourceConstants;
import org.pyt.common.constants.StylesPrincipalConstant;
import org.pyt.common.validates.ValidFields;

import com.pyt.service.interfaces.IConfigGenericFieldSvc;
import com.pyt.service.interfaces.IGenericServiceSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.AGenericInterfacesFieldBean;
import co.com.japl.ea.dto.system.LanguagesDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

@FXMLFile(file = "languages.fxml", path = "view/languages")
public class LanguageBean extends AGenericInterfacesFieldBean<LanguagesDTO> {
	@Inject(resource = "com.pyt.service.implement.GenericServiceSvc")
	private IGenericServiceSvc<LanguagesDTO> languagesSvc;
	@Inject(resource = InsertResourceConstants.CONST_RESOURCE_IMPL_SVC_CONFIG_GENERIC_FIELD)
	private IConfigGenericFieldSvc configSvc;
	@FXML
	private GridPane gridPaneLanguages;
	@FXML
	private Label lblTitle;
	public static final String CONST_FIELD_NAME_LANGUAGES_CODE = "code";
	public static final String CONST_FIELD_NAME_LANGUAGES_TEXT = "text";
	public static final String CONST_FIELD_NAME_LANGUAGES_STATE = "state";
	public static final String CONST_FIELD_NAME_LANGUAGES_IDIOM = "idiom";
	private boolean openedPopup;
	private MultiValuedMap<String, Object> toChoiceBox;
	@FXML
	private BorderPane panel;
	@FXML
	private Button btnNew;
	@FXML
	private Button btnSave;

	@FXML
	public void initialize() {
		try {
			toChoiceBox = new ArrayListValuedHashMap<>();
			openedPopup = false;
			registro = new LanguagesDTO();
			registro.setIdiom(Locale.getDefault().toString());
			registro.setState("1");
			setClazz(LanguagesDTO.class);
			fields = configSvc.getFieldToFields(this.getClass(), LanguagesDTO.class);
			loadFields(TypeGeneric.FIELD, StylesPrincipalConstant.CONST_GRID_STANDARD);
		} catch (Exception e) {
			error(e);
		}
	}

	public final void load() {
		registro = new LanguagesDTO();
		registro.setIdiom(Locale.getDefault().toString());
		registro.setState("1");
		btnSave.setText(i18n().valueBundle("fxml.btn.add").get());
		loadValueIntoForm(TypeGeneric.FIELD, "state");
	}

	public final void load(LanguagesDTO dto) {
		registro = dto;
		loadFields(TypeGeneric.FIELD, StylesPrincipalConstant.CONST_GRID_STANDARD);
		btnSave.setText(i18n().valueBundle("fxml.btn.edit").get());
	}

	public final void addCode(String code) {
		if (Optional.ofNullable(registro).isPresent()) {
			registro.setCode(code);
			var split = code.split("\\.");
			registro.setText(split.length > 1 ? split[split.length - 1] : null);
			loadFields(TypeGeneric.FIELD);
		}
	}

	public final void openPopup() {
		openedPopup = true;
		btnNew.setVisible(false);
	}

	@Override
	public GridPane getGridPane(TypeGeneric typeGeneric) {
		return gridPaneLanguages;
	}

	public final Boolean valid() {
		Boolean valid = true;
		valid &= ValidFields.valid((TextField) getMapFields(TypeGeneric.FIELD).get(CONST_FIELD_NAME_LANGUAGES_CODE)
				.stream().findFirst().get(), true, 1, 100, i18n().valueBundle("msn.error.field.empty"));
		valid &= ValidFields.valid((TextField) getMapFields(TypeGeneric.FIELD).get(CONST_FIELD_NAME_LANGUAGES_TEXT)
				.stream().findFirst().get(), true, 1, 100, i18n().valueBundle("msn.error.field.empty"));
		valid &= ValidFields.valid((TextField) getMapFields(TypeGeneric.FIELD).get(CONST_FIELD_NAME_LANGUAGES_STATE)
				.stream().findFirst().get(), true, 1, 2, i18n().valueBundle("msn.error.field.empty"));
		valid &= ValidFields.valid((TextField) getMapFields(TypeGeneric.FIELD).get(CONST_FIELD_NAME_LANGUAGES_IDIOM)
				.stream().findFirst().get(), true, 2, 10, i18n().valueBundle("msn.error.field.empty"));
		return valid;
	}

	public final void add() {
		try {
			if (valid()) {
				if (StringUtils.isBlank(registro.getCodigo())) {
					languagesSvc.insert(registro, getUsuario());
					notificar(i18n().valueBundle("mensaje.language.inserted"));
				} else {
					languagesSvc.update(registro, getUsuario());
					notificar(i18n().valueBundle("mensaje.language.updated"));
				}
				if (openedPopup) {
					cancel();
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
		if (openedPopup) {
			((Stage) panel.getScene().getWindow()).close();
			destroy();
		} else {
			getController(ListLanguagesBean.class);
		}
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
