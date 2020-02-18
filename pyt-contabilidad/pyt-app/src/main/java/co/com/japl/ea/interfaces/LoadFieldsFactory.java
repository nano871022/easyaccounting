package co.com.japl.ea.interfaces;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.pyt.common.abstracts.ADto;
import org.pyt.common.annotation.generics.DefaultFieldToGeneric;
import org.pyt.common.annotation.generics.DefaultFieldToGeneric.Uses;
import org.pyt.common.common.Comunicacion;
import org.pyt.common.common.I18n;
import org.pyt.common.common.Log;

import com.pyt.service.dto.DocumentosDTO;

import co.com.japl.ea.dto.system.ConfigGenericFieldDTO;

public final class LoadFieldsFactory implements INotificationMethods {
	private Log logger = Log.Log(this.getClass());

	private LoadFieldsFactory() {
	}

	public static <F extends ADto, L extends ADto> List<L> getAnnotatedField(Class<L> clazzList, Class<F> clazzField,
			Uses typeGeneric) {
		List<L> campos = new ArrayList<>();
		if (clazzList == ConfigGenericFieldDTO.class) {
			Arrays.asList(clazzField.getDeclaredFields()).stream()
					.filter(field -> field.getDeclaredAnnotationsByType(DefaultFieldToGeneric.class) != null)
					.forEach(field -> {
						var list = field.getDeclaredAnnotationsByType(DefaultFieldToGeneric.class);
						Arrays.asList(list).stream().filter(annotation -> annotation.use() == typeGeneric)
								.forEach(annotation -> {
									var campo = new ConfigGenericFieldDTO();
									campo.setClassPathBean(annotation.simpleNameClazzBean());
									campo.setIsFilter(Uses.FILTER == typeGeneric);
									campo.setIsColumn(Uses.COLUMN == typeGeneric);
									campo.setClassPath(clazzField.toString());
									campo.setState(1);
									campo.setName(field.getName());
									if (annotation.width() > 0) {
										campo.setWidth(annotation.width());
									}
									campos.add((L) campo);
								});
					});
		}
		return campos;
	}

	public static <D extends ADto> IFieldsCreator getInstance(D genericConfig) {
		IFieldsCreator fieldCreator = null;
		if (genericConfig instanceof DocumentosDTO) {
			fieldCreator = new DocumentosDTOCreator(new LoadFieldsFactory());
			fieldCreator.setFieldGeneric(genericConfig);
			return fieldCreator;
		} else if (genericConfig instanceof ConfigGenericFieldDTO) {
			fieldCreator = new GenericFieldDTOCreator(new LoadFieldsFactory());
			fieldCreator.setFieldGeneric(genericConfig);
			return fieldCreator;
		}
		throw new RuntimeException(I18n.instance()
				.valueBundle("err.msn.option.generic.invalid", genericConfig.getClass().getSimpleName()).get());
	}

	@Override
	public Log logger() {
		return logger;
	}

	@Override
	public Comunicacion comunicacion() {
		return null;
	}
}
