package org.pyt.app.beans.generic.interfaces;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.controlsfx.glyphfont.FontAwesome.Glyph;
import org.pyt.common.abstracts.ADto;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.DtoUtils;
import org.pyt.common.common.SelectList;
import org.pyt.common.common.UtilControlFieldFX;
import org.pyt.common.constants.LanguageConstant;
import org.pyt.common.constants.ParametroConstants;
import org.pyt.common.constants.PermissionConstants;
import org.pyt.common.exceptions.ParametroException;
import org.pyt.common.validates.ValidFields;
import org.pyt.common.validates.ValidateValues;

import com.pyt.service.dto.ParametroDTO;
import com.pyt.service.interfaces.IGenericServiceSvc;
import com.pyt.service.interfaces.IParametrosSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.ABean;
import co.com.japl.ea.common.button.apifluid.ButtonsImpl;
import co.com.japl.ea.dto.system.ConfigGenericFieldDTO;
import co.com.japl.ea.utls.PermissionUtil;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

@FXMLFile(path = "/view/genericInterfaces", file = "generic_interfaces.fxml")
public class GenericInterfacesBean extends ABean<ConfigGenericFieldDTO> {
	@Inject(resource = "com.pyt.service.implement.GenericServiceSvc")
	private IGenericServiceSvc<ConfigGenericFieldDTO> configGenericSvc;
	@Inject
	private IParametrosSvc parametroSvc;
	@FXML
	private TextField txtName;
	@FXML
	private TextField format;
	@FXML
	private ChoiceBox<String> cbName;
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
	private CheckBox chkGroup;
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
	private ChoiceBox<ParametroDTO> cbGroup;
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
	private List<ParametroDTO> listParam;
	@FXML
	private HBox buttons;
	private UtilControlFieldFX utilFx;

	@FXML
	private void initialize() {
		utilFx = new UtilControlFieldFX();
		titulo.setText(i18n().valueBundle(LanguageConstant.FXML_LBL_TITLE_GENERIC_INTERFACES).get());
		titulo.getStyleClass().add("titulo-page");
		titulo.setGraphic(
				new org.controlsfx.glyphfont.Glyph("FontAwesome", org.controlsfx.glyphfont.FontAwesome.Glyph.TAGS));
		SelectList.put(chbState, ParametroConstants.mapa_estados_parametros);
		registro = new ConfigGenericFieldDTO();
		validateValues = new ValidateValues();
		hidenFields();
		configFieldProperty();
		visibleButtons();
		ButtonsImpl.Stream(HBox.class).setLayout(buttons).setName("fxml.btn.save").action(this::add).icon(Glyph.SAVE)
				.isVisible(save).setName("fxml.btn.edit").action(this::add).icon(Glyph.EDIT).isVisible(edit)
				.setName("fxml.btn.cancel").action(this::cancel).setName("fxml.btn.new").action(this::newRow)
				.icon(Glyph.SAVE).isVisible(edit).setName("fxml.btn.copy").action(this::copy).icon(Glyph.COPY)
				.isVisible(edit).build();

	}

	private void hidenFields() {
		lbField.setVisible(false);
		cbField.setVisible(false);
		lbGroup.setVisible(false);
		cbGroup.setVisible(false);
		lbDefault.setVisible(false);
		tbDefault.setVisible(false);
		chkDefault.setSelected(false);
		chkVisible.setSelected(true);
		chkGroup.setSelected(false);
		chkGroup.setVisible(false);
		cbName.setVisible(false);
	}

	private void configFieldProperty() {

		utilFx.change(chkIsColumn, value -> registro.setIsColumn(value));
		utilFx.change(chkIsFilter, value -> registro.setIsFilter(value));
		utilFx.change(chkIsRequired, value -> registro.setIsRequired(value));
		utilFx.change(chkVisible, value -> registro.setIsVisible(value));
		utilFx.change(chkGroup, this::manejaGrupo);
		utilFx.change(chkDefault, value -> {
			tbDefault.setVisible(chkDefault.isSelected());
			lbDefault.setVisible(chkDefault.isSelected());
		});
		utilFx.focusOut(txtClassDto, () -> {
			if (!validClass(txtClassDto.getText())) {
				error(i18n().valueBundle("err.dto.no.exists", txtClassDto.getText()));
			} else {
				verifyChange();
			}
		});
		utilFx.focusOut(txtClassBean, () -> {
			if (!validClass(txtClassBean.getText())) {
				error(i18n().valueBundle("err.class.no.exists", txtClassBean.getText()));
			}
		});
		utilFx.change(txtClassBean, value -> registro.setClassPathBean(value));
		utilFx.change(tbDefault, value -> registro.setFieldShow(value));
		utilFx.change(txtClassDto, value -> registro.setClassPath(value));
		utilFx.change(txtDescription, value -> registro.setDescription(value));
		utilFx.change(txtAlias, value -> registro.setAlias(value));
		utilFx.change(txtOrder, value -> {
			if (value != null && !value.contains("null")) {
				registro.setOrden(validateValues.cast(value, Integer.class));
			} else {
				registro.setOrden(null);
			}
		});
		utilFx.change(txtWidth, value -> registro.setWidth(validateValues.cast(value, Double.class)));
		utilFx.change(txtName, value -> {
			registro.setName(value);
			txtDescription.setText(value);
			if (value != null) {
				txtAlias.setText(value.substring(0, 1).toUpperCase() + value.substring(1));
			} else {
				txtAlias.setText(null);
			}
			verifyChange();
		});
		utilFx.change(format, registro::setFormat);
		SelectList.selectChange(cbName, change -> {
			txtName.setText(change);
			txtDescription.setText(change);
			txtAlias.setText(change.substring(0, 1).toUpperCase() + change.substring(1));
			verifyChange();
		});
		SelectList.selectChange(chbState, value -> {
			var val = ParametroConstants.mapa_estados_parametros.get(value);
			registro.setState((Integer) val);
		});
		SelectList.selectChange(cbGroup, change -> {
			registro.setNameGroup(Optional.ofNullable(change).orElse(new ParametroDTO()).getNombre());
			verifyChange();
		});
	}

	private final void countRowsBetweenBeanAndDTO() {
		try {
			if (StringUtils.isNotBlank(registro.getClassPath())
					&& StringUtils.isNotBlank(registro.getClassPathBean())) {
				var registro = new ConfigGenericFieldDTO();
				registro.setClassPathBean(this.registro.getClassPathBean());
				registro.setClassPath(this.registro.getClassPath());
				registro.setState(1);
				var count = configGenericSvc.getTotalRows(registro);
				this.registro.setOrden(count + 1);
				txtOrder.setText(String.valueOf(this.registro.getOrden()));
			}
		} catch (Exception e) {
			error(e);
		}
	}

	private final boolean validClass(String clazz) {
		try {
			if (StringUtils.isNotBlank(clazz)) {
				Class.forName(clazz);
			}
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}

	public final void load() {
		registro = new ConfigGenericFieldDTO();
	}

	public final void load(ConfigGenericFieldDTO dto) {
		registro = dto;
		putFxml();
		visibleButtons();
	}

	private final void putFxml() {
		txtName.setText(registro.getName());
		txtAlias.setText(registro.getAlias());
		txtDescription.setText(registro.getDescription());
		txtClassDto.setText(registro.getClassPath());
		txtClassBean.setText(registro.getClassPathBean());
		format.setText(registro.getFormat());
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
		loadGroupParam();
		if (StringUtils.isNotBlank(registro.getNameGroup())) {
			SelectList.selectItem(cbGroup, listParam, ParametroConstants.FIELD_NAME_PARAM, registro.getNameGroup());
			chkGroup.setVisible(true);
			chkGroup.setSelected(true);
			lbGroup.setVisible(true);
		}
		if (StringUtils.isNotBlank(registro.getFieldShow())) {
			SelectList.selectItem(cbField, registro.getFieldShow());
			cbField.setVisible(true);
			lbField.setVisible(true);
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
		valid &= ValidFields.valid(validClass(registro.getClassPath()), txtClassDto, true, null, null,
				i18n().valueBundle("msn.form.field.error.class.dto.not.exists"));
		valid &= ValidFields.valid(validClass(registro.getClassPathBean()), txtClassDto, true, null, null,
				i18n().valueBundle("msn.form.field.error.class.bean.not.exists"));
		return valid;
	}

	@FXML
	public void cancel() {
		getController(ListGenericInterfacesBean.class);
	}

	@FXML
	public void add() {
		try {
			if (validRecord()) {
				if (StringUtils.isBlank(registro.getCodigo())) {
					if (registro.getOrden() == null) {
						registro.setOrden(getCount());
					}
					configGenericSvc.insert(registro, getUsuario());
					notificar(i18n().valueBundle("message.insert.generic.interface"));
				} else {
					configGenericSvc.update(registro, getUsuario());
					notificar(i18n().valueBundle("message.update.generic.interface"));
				}
			}
			visibleButtons();
		} catch (Exception e) {
			error(e);
		}
	}

	private void verifyChange() {
		try {
			if (StringUtils.isNotBlank(txtClassDto.getText()) && StringUtils.isNotBlank(txtName.getText())) {
				Class<?> clazz = Class.forName(txtClassDto.getText());
				Field field = clazz.getDeclaredField(txtName.getText());
				boolean asSubClassDTO = false;
				try {
					asSubClassDTO = field.getType().asSubclass(ADto.class) != null;
				} catch (ClassCastException e) {
					logger.DEBUG(e);
				}
				if (asSubClassDTO) {
					var instance = field.getType().getDeclaredConstructor().newInstance();
					if (instance instanceof ParametroDTO) {
						loadGroupParam();
						cbGroup.setVisible(true);
						lbGroup.setVisible(true);
					}
					SelectList.put(cbField, ((ADto) instance).getNameFields());
					cbField.setVisible(true);
					lbField.setVisible(true);
					chkGroup.setVisible(false);
					return;
				}
			} else if (StringUtils.isNotBlank(txtClassDto.getText()) && StringUtils.isBlank(txtName.getText())) {
				Class<?> clazz = Class.forName(txtClassDto.getText());
				SelectList.put(cbName, ((ADto) clazz.getConstructor().newInstance()).getNameFields());
				cbName.setVisible(true);
				txtName.setVisible(false);
				chkGroup.setVisible(false);
				return;
			}
			cbField.setVisible(false);
			lbField.setVisible(false);
			cbGroup.setVisible(false);
			lbGroup.setVisible(false);
			cbName.setVisible(false);
			txtName.setVisible(true);
			chkGroup.setVisible(true);
		} catch (ClassCastException | InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException
				| ClassNotFoundException e) {
			logger.logger(e);
		}
	}

	private void loadGroupParam() {
		try {
			if (listParam == null || listParam.size() == 0) {
				var parametro = new ParametroDTO();
				parametro.setEstado(ParametroConstants.COD_ESTADO_PARAMETRO_ACTIVO_STR);
				parametro.setGrupo(ParametroConstants.GRUPO_PRINCIPAL);
				listParam = parametroSvc.getAllParametros(parametro);
			}
			if (listParam != null && listParam.size() > 0
					&& (cbGroup.getItems() == null || cbGroup.getItems().size() == 0)) {
				SelectList.addItems(cbGroup, listParam, ParametroConstants.FIELD_NAME_PARAM);
			}
		} catch (ParametroException e) {
			logger().DEBUG(e);
		}
	}

	private Integer getCount() {
		try {
			var dto = new ConfigGenericFieldDTO();
			dto.setClassPath(registro.getClassPath());
			dto.setClassPathBean(registro.getClassPathBean());
			return configGenericSvc.getTotalRows(dto);
		} catch (Exception e) {
			logger.DEBUG(e);
		}
		return 1;
	}

	private void manejaGrupo(boolean useGroup) {
		cbGroup.setVisible(useGroup);
		if (useGroup) {
			loadGroupParam();
		}
	}

	public void newRow() {
		load();
		countRowsBetweenBeanAndDTO();
		putFxml();
	}

	public void copy() {
		registro.setCodigo(null);
		registro.setAlias(null);
		registro.setName(null);
		registro.setDescription(null);
		registro.setOrden(null);
		registro.setFieldShow(null);
		registro.setNameGroup(null);
		registro.setFormat(null);
		chkGroup.setVisible(true);
		cbGroup.setVisible(false);
		cbField.setVisible(false);
		lbGroup.setVisible(false);
		lbField.setVisible(false);
		load(registro);
		SelectList.selectItem(cbGroup, listParam, ParametroConstants.FIELD_NAME_PARAM, registro.getNameGroup());
		SelectList.selectItem(cbField, registro.getFieldShow());
		countRowsBetweenBeanAndDTO();
		verifyChange();
		notificar(i18n().valueBundle("message.copy.generic.interface.success"));
	}

	protected void visibleButtons() {
		var save = !DtoUtils.haveCode(registro) && PermissionUtil.INSTANCE().havePerm(
				PermissionConstants.CONST_PERM_CREATE, ListGenericInterfacesBean.class, getUsuario().getGrupoUser());
		var edit = DtoUtils.haveCode(registro) && PermissionUtil.INSTANCE().havePerm(
				PermissionConstants.CONST_PERM_UPDATE, ListGenericInterfacesBean.class, getUsuario().getGrupoUser());
		this.save.setValue(save);
		this.edit.setValue(edit);
	}
}
