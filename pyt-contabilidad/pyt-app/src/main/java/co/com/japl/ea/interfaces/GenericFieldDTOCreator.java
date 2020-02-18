package co.com.japl.ea.interfaces;

import java.lang.reflect.Field;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.abstracts.ADto;
import org.pyt.common.common.OptI18n;
import org.pyt.common.common.UtilControlFieldFX;
import org.pyt.common.constants.ParametroConstants;

import com.pyt.service.dto.ParametroDTO;

import co.com.japl.ea.dto.system.ConfigGenericFieldDTO;
import javafx.scene.Node;
import javafx.scene.control.TextField;

public final class GenericFieldDTOCreator implements IFieldsCreator {

	private ConfigGenericFieldDTO field;
	private UtilControlFieldFX controlFieldUtil;
	private INotificationMethods notificationMethods;

	public GenericFieldDTOCreator(INotificationMethods notificationMethods) {
		controlFieldUtil = new UtilControlFieldFX();
		this.notificationMethods = notificationMethods;
	}

	@Override
	public <D extends ADto> void setFieldGeneric(D field) {
		this.field = (ConfigGenericFieldDTO) field;
	}

	@Override
	public OptI18n getLabelText() {
		if (field.getAlias() != null && !field.getAlias().trim().isEmpty()) {
			return OptI18n.noChange(field.getAlias());
		}
		var classPath = field.getClassPath().contains("Class") ? field.getClassPath().substring(5)
				: field.getClassPath();
		return notificationMethods.i18n().valueBundle(classPath + "." + field.getName());
	}

	@Override
	public String getNameField() {
		return field.getName();
	}

	@Override
	public String getToolTip() {
		return field.getDescription();
	}

	@Override
	public Node create() {
		try {
			var clazz = Class.forName(field.getClassPath());
			Field propertie = clazz.getDeclaredField(field.getName());
			if (StringUtils.isBlank(field.getFieldShow()) && StringUtils.isNotBlank(field.getNameGroup())) {
				return controlFieldUtil.getChoiceBox(propertie);
			} else if ("password".contentEquals(field.getName())) {
				return controlFieldUtil.getPasswordField(propertie);
			}
			var node = controlFieldUtil.getFieldByField(propertie);
			return node;
		} catch (NoSuchFieldException | SecurityException | ClassNotFoundException e) {
			notificationMethods.logger().logger("Error en creacion del campo", e);
		}
		return new TextField("Generic Field");
	}

	@Override
	public String getNameFieldToShowInComboBox() {
		return field.getFieldShow();
	}

	@Override
	public ParametroDTO getParametroDto() {
		var parametro = new ParametroDTO();
		parametro.setGrupo(field.getNameGroup());
		parametro.setEstado(ParametroConstants.COD_ESTADO_PARAMETRO_ACTIVO_STR);
		return parametro;
	}

	@Override
	public String getValueDefault() {
		return field.getValueDefault();
	}

	@Override
	public Boolean isVisible() {
		return field.getIsVisible();
	}

	@Override
	public Boolean hasValueDefault() {
		return StringUtils.isNotBlank(field.getValueDefault());
	}

	@Override
	public Boolean isRequired() {
		return Optional.ofNullable(field.getIsRequired()).orElse(false);
	}

	@Override
	public Integer getOrder() {
		return Optional.ofNullable(field.getOrden()).orElse(0);
	}
}
