package co.com.japl.ea.interfaces;

import java.lang.reflect.Field;

import org.pyt.common.abstracts.ADto;
import org.pyt.common.common.UtilControlFieldFX;

import com.pyt.service.dto.DocumentosDTO;
import com.pyt.service.dto.ParametroDTO;

import javafx.scene.Node;
import javafx.scene.control.TextField;

public final class DocumentosDTOCreator implements IFieldsCreator {

	private DocumentosDTO field;
	private UtilControlFieldFX controlFieldUtil;
	private INotificationMethods notificationMethods;

	public DocumentosDTOCreator(INotificationMethods notificationMethods) {
		controlFieldUtil = new UtilControlFieldFX();
		this.notificationMethods = notificationMethods;
	}

	@Override
	public <D extends ADto> void setFieldGeneric(D field) {
		this.field = (DocumentosDTO) field;
	}

	@Override
	public String getLabelText() {
		return field.getFieldLabel();
	}

	@Override
	public String getNameField() {
		return field.getFieldName();
	}

	@Override
	public String getToolTip() {
		return field.getPutNameShow();
	}

	@Override
	public Node create() {
		try {
			Field propertie = field.getClaseControlar().getDeclaredField(field.getFieldName());
			var node = controlFieldUtil.getFieldByField(propertie);
			return node;
		} catch (NoSuchFieldException | SecurityException e) {
			notificationMethods.logger().logger("Error en creacion del campo", e);
		}
		return new TextField("Generic Field");
	}

	@Override
	public String getNameFieldToShowInComboBox() {
		return field.getPutNameAssign();
	}

	@Override
	public ParametroDTO getParametroDto() {
		var parametro = new ParametroDTO();
		parametro.setGrupo(field.getSelectNameGroup());
		return parametro;
	}
}
