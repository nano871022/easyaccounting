package org.pyt.app.beans.languages;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.Inject;

import com.pyt.service.interfaces.IGenericServiceSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.AGenericInterfacesFieldBean;
import co.com.japl.ea.dto.system.LanguagesDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

@FXMLFile(file = "languages.fxml", path = "view/languages")
public class LanguageBean extends AGenericInterfacesFieldBean<LanguagesDTO> {
	@Inject(resource = "com.pyt.service.implement.GenericServiceSvc")
	private IGenericServiceSvc<LanguagesDTO> languagesSvc;
	@FXML
	private GridPane gridPaneLanguages;
	@FXML
	private Label lblTitle;

	@FXML
	public void initialize() {
		try {
			registro = new LanguagesDTO();
			setClazz(LanguagesDTO.class);
			configFields();
		} catch (Exception e) {
			error(e);
		}
	}

	public final void load() {
		registro = new LanguagesDTO();
	}

	public final void load(LanguagesDTO dto) {
		registro = dto;
	}

	@Override
	public GridPane getGridPaneField() {
		return gridPaneLanguages;
	}

	public final Boolean valid() {
		Boolean valid = true;

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

	public final void cancel() {
		getController(ListLanguagesBean.class);
	}

	@Override
	public Integer countFieldsInRow() {
		return 2;
	}

}
