package org.pyt.app.beans.generic.interfaces;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.SelectList;
import org.pyt.common.constants.LanguageConstant;
import org.pyt.common.constants.ParametroConstants;
import org.pyt.common.validates.ValidFields;
import org.pyt.common.validates.ValidateValues;

import com.pyt.service.interfaces.IGenericServiceSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.ABean;
import co.com.japl.ea.dto.system.ConfigGenericFieldDTO;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

@FXMLFile(path = "/view/genericInterfaces", file = "generic_interfaces.fxml")
public class GenericInterfacesBean extends ABean<ConfigGenericFieldDTO> {
	@Inject(resource = "com.pyt.service.implement.GenericServiceSvc")
	private IGenericServiceSvc<ConfigGenericFieldDTO> configGenericSvc;
	@FXML
	private TextField txtName;
	@FXML
	private TextField txtDescription;
	@FXML
	private TextField txtAlias;
	@FXML
	private TextField txtClassDto;
	@FXML
	private TextField txtClassBean;
	@FXML
	private TextField txtWidth;
	@FXML
	private CheckBox chkIsFilter;
	@FXML
	private CheckBox chkIsColumn;
	@FXML
	private CheckBox chkIsRequired;
	@FXML
	private ChoiceBox<String> chbState;
	@FXML
	private Label titulo;
	private ValidateValues validateValues;
	@FXML
	private TextField txtOrder;

	@FXML
	private void initialize() {
		titulo.setText(i18n().valueBundle(LanguageConstant.FXML_LBL_TITLE_GENERIC_INTERFACES));
		registro = new ConfigGenericFieldDTO();
		SelectList.put(chbState, ParametroConstants.mapa_estados_parametros);
		validateValues = new ValidateValues();
	}

	public final void load() {
		registro = new ConfigGenericFieldDTO();
	}

	public final void load(ConfigGenericFieldDTO dto) {
		registro = dto;
		putFxml();
	}

	private final void loadFxml() {
		try {
			if (!txtName.getText().isEmpty()) {
				registro.setName(txtName.getText());
			}
			if (!txtAlias.getText().isEmpty()) {
				registro.setAlias(txtAlias.getText());
			}
			if (!txtDescription.getText().isEmpty()) {
				registro.setDescription(txtDescription.getText());
			}
			if (!txtClassDto.getText().isEmpty()) {
				registro.setClassPath(txtClassDto.getText());
			}
			if (!txtClassBean.getText().isEmpty()) {
				registro.setClassPathBean(txtClassBean.getText());
			}
			if (!txtWidth.getText().isEmpty()) {
				registro.setWidth(validateValues.cast(txtWidth.getText(), Double.class));
			}
			if (!txtOrder.getText().isEmpty()) {
				registro.setOrden(validateValues.cast(txtOrder.getText(), Integer.class));
			}

			registro.setState(validateValues.cast(SelectList.get(chbState, ParametroConstants.mapa_estados_parametros),
					Integer.class));
			registro.setIsColumn(chkIsColumn.isSelected());
			registro.setIsFilter(chkIsFilter.isSelected());
			registro.setIsRequired(chkIsRequired.isSelected());
		} catch (Exception e) {
			error(e);
		}
	}

	private final void putFxml() {
		txtName.setText(registro.getName());
		txtAlias.setText(registro.getAlias());
		txtDescription.setText(registro.getDescription());
		txtClassDto.setText(registro.getClassPath());
		txtClassBean.setText(registro.getClassPathBean());
		if (registro.getWidth() != null) {
			txtWidth.setText(registro.getWidth().toString());
		}
		if (registro.getOrden() != null) {
			txtOrder.setText(registro.getOrden().toString());
		}
		SelectList.selectItem(chbState, ParametroConstants.mapa_estados_parametros, registro.getState());
		if (registro.getIsColumn() != null) {
			chkIsColumn.setSelected(registro.getIsColumn());
		}
		if (registro.getIsFilter() != null) {
			chkIsFilter.setSelected(registro.getIsFilter());
		}
		if (registro.getIsRequired() != null) {
			chkIsRequired.setSelected(registro.getIsRequired());
		}
	}

	private final boolean validRecord() {
		var valid = true;
		valid &= ValidFields.valid(txtName, true, 3, 100, i18n().valueBundle("msn.form.field.error.empty.name"));
		valid &= ValidFields.valid(txtClassDto, true, 10, 100,
				i18n().valueBundle("msn.form.field.error.empty.classdto"));
		valid &= ValidFields.valid(txtClassBean, true, 10, 100,
				i18n().valueBundle("msn.form.field.error.empty.classbean"));
		valid &= ValidFields.valid(chbState, ParametroConstants.mapa_estados_parametros, true,
				i18n().valueBundle("msn.form.field.error.empty.state"));
		return valid;
	}

	@FXML
	public void cancel() {
		getController(ListGenericInterfacesBean.class);
	}

	@FXML
	public void add() {
		try {
			loadFxml();
			if (validRecord()) {
				if (StringUtils.isBlank(registro.getCodigo())) {
					configGenericSvc.insert(registro, userLogin);
					notificar(i18n().valueBundle("message.insert.generic.interface"));
				} else {
					configGenericSvc.update(registro, userLogin);
					notificar(i18n().valueBundle("message.update.generic.interface"));
				}
			}
		} catch (Exception e) {
			error(e);
		}
	}

	public void newRow() {
		load();
		putFxml();
	}

	public void copy() {
		registro.setCodigo(null);
		load(registro);
	}
}
