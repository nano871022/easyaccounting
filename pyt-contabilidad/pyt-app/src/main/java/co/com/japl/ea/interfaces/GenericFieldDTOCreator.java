package co.com.japl.ea.interfaces;

import java.lang.reflect.Field;

import org.pyt.common.abstracts.ADto;
import org.pyt.common.common.UtilControlFieldFX;

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
	public String getLabelText() {
		if (field.getAlias() != null && field.getAlias().trim().isEmpty()) {
			return field.getAlias();
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
			var node = controlFieldUtil.getFieldByField(propertie);
			return node;
		} catch (NoSuchFieldException | SecurityException | ClassNotFoundException e) {
			notificationMethods.logger().logger("Error en creacion del campo", e);
		}
		return new TextField("Generic Field");
	}

	@Override
	public String getNameFieldToShowInComboBox() {
		throw new RuntimeException("No se tiene implementado para mostrar un campo en concreto.");
	}

	@Override
	public ParametroDTO getParametroDto() {
		return new ParametroDTO();
	}
}
