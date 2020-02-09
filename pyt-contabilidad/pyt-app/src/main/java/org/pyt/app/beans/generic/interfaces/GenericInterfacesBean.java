package org.pyt.app.beans.generic.interfaces;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.abstracts.ADto;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.SelectList;
import org.pyt.common.constants.LanguageConstant;
import org.pyt.common.constants.ParametroConstants;
import org.pyt.common.validates.ValidFields;
import org.pyt.common.validates.ValidateValues;

import com.pyt.service.dto.ParametroDTO;
import com.pyt.service.interfaces.IGenericServiceSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.ABean;
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
	@FXML
	private Label lbField;
	@FXML
	private ChoiceBox<String> cbField;
	@FXML
	private Label lbGroup;
	@FXML
	private ChoiceBox<String> cbGroup;
	@FXML
	private Label lbDefault;
	@FXML
	private TextField tbDefault;
	@FXML
	private CheckBox chkDefault;
	@FXML
	private CheckBox chkVisible;
	private ValidateValues validateValues;
	@FXML
	private TextField txtOrder;

	@FXML
	private void initialize() {
		titulo.setText(i18n().valueBundle(LanguageConstant.FXML_LBL_TITLE_GENERIC_INTERFACES).get());
		SelectList.put(chbState, ParametroConstants.mapa_estados_parametros);
		registro = new ConfigGenericFieldDTO();
		validateValues = new ValidateValues();
		lbField.setVisible(false);
		cbField.setVisible(false);
		lbGroup.setVisible(false);
		cbGroup.setVisible(false);
		lbDefault.setVisible(false);
		tbDefault.setVisible(false);
		chkDefault.setSelected(false);
		chkVisible.setSelected(true);
		chkDefault.selectedProperty().addListener(event -> tbDefault.setVisible(chkDefault.isSelected()));
		txtClassDto.textProperty().addListener((obs, s1, s2) -> {
			verifyChange();
		});
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
			if (StringUtils.isNotBlank(txtName.getText())) {
				registro.setName(txtName.getText());
			}
			if (StringUtils.isNotBlank(txtAlias.getText())) {
				registro.setAlias(txtAlias.getText());
			}
			if (StringUtils.isNotBlank(txtDescription.getText())) {
				registro.setDescription(txtDescription.getText());
			}
			if (StringUtils.isNotBlank(txtClassDto.getText())) {
				registro.setClassPath(txtClassDto.getText());
			}
			if (StringUtils.isNotBlank(txtClassBean.getText())) {
				registro.setClassPathBean(txtClassBean.getText());
			}
			if (StringUtils.isNotBlank(txtWidth.getText())) {
				registro.setWidth(validateValues.cast(txtWidth.getText(), Double.class));
			}
			if (StringUtils.isNotBlank(txtOrder.getText())) {
				registro.setOrden(validateValues.cast(txtOrder.getText(), Integer.class));
			}
			if (StringUtils.isNotBlank(tbDefault.getText())) {
				registro.setValueDefault(tbDefault.getText());
			}
			if (chkVisible.isSelected()) {
				registro.setIsVisible(chkVisible.isSelected());
			}
			registro.setGroup(
					validateValues.cast(SelectList.get(cbGroup, ParametroConstants.MAPA_GRUPOS), String.class));
			registro.setFieldShow(validateValues.cast(SelectList.get(cbField), String.class));
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
		if (registro.getIsColumn() != null) {
			chkIsColumn.setSelected(registro.getIsColumn());
		}
		if (registro.getIsFilter() != null) {
			chkIsFilter.setSelected(registro.getIsFilter());
		}
		if (registro.getIsRequired() != null) {
			chkIsRequired.setSelected(registro.getIsRequired());
		}
		if (registro.getIsVisible() != null) {
			chkVisible.setSelected(registro.getIsVisible());
		}
		if (StringUtils.isNotBlank(registro.getValueDefault())) {
			tbDefault.setText(registro.getValueDefault());
			chkDefault.setSelected(true);
		}
		SelectList.selectItem(chbState, ParametroConstants.mapa_estados_parametros, registro.getState());
		SelectList.selectItem(cbGroup, ParametroConstants.MAPA_GRUPOS, registro.getGroup());
		SelectList.selectItem(cbField, registro.getFieldShow());
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
		valid &= ValidFields.valid(cbGroup, ParametroConstants.mapa_grupo, true,
				i18n().valueBundle("msn.form.field.error.empty.group"));
		valid &= ValidFields.valid(cbField, true, i18n().valueBundle("msn.form.field.error.empty.field"));
		valid &= ValidFields.valid(tbDefault, false, 3, 100, i18n().valueBundle("msn.form.field.error.empty.default"));
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
					configGenericSvc.insert(registro, getUsuario());
					notificar(i18n().valueBundle("message.insert.generic.interface"));
				} else {
					configGenericSvc.update(registro, getUsuario());
					notificar(i18n().valueBundle("message.update.generic.interface"));
				}
			}
		} catch (Exception e) {
			error(e);
		}
	}

	private void verifyChange() {
		try {
			if (StringUtils.isNotBlank(txtClassDto.getText()) && StringUtils.isNotBlank(txtName.getText())) {
				Class<?> clazz = Class.forName(txtClassDto.getText());
				Field field = clazz.getDeclaredField(txtName.getText());
				if (field.getType().asSubclass(ADto.class) != null) {
					var instance = field.getType().getDeclaredConstructor().newInstance();
					if (instance instanceof ParametroDTO) {
						SelectList.put(cbGroup, ParametroConstants.MAPA_GRUPOS);
						cbGroup.setVisible(true);
						lbGroup.setVisible(true);
					}
					SelectList.add(cbField, ((ADto) instance).getNameFields());
					cbField.setVisible(true);
					lbField.setVisible(true);
					return;
				}
			}
			cbField.setVisible(false);
			lbField.setVisible(false);
			cbGroup.setVisible(false);
			lbGroup.setVisible(false);
		} catch (ClassCastException | InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException
				| ClassNotFoundException e) {
			logger.logger(e);
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
