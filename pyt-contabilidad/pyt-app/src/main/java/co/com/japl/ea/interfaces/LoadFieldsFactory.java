package co.com.japl.ea.interfaces;

import org.pyt.common.abstracts.ADto;

import com.pyt.service.dto.DocumentosDTO;

import co.com.japl.ea.dto.system.ConfigGenericFieldDTO;

public final class LoadFieldsFactory {
	private LoadFieldsFactory() {
	}

	public static <D extends ADto> IFieldsCreator getInstance(D genericConfig) {
		IFieldsCreator fieldCreator = null;
		if (genericConfig instanceof DocumentosDTO) {
			fieldCreator.setFieldGeneric(genericConfig);
			return fieldCreator;
		} else if (genericConfig instanceof ConfigGenericFieldDTO) {
			fieldCreator.setFieldGeneric(genericConfig);
			return fieldCreator;
		}
		throw new RuntimeException("Opcion de generico no valida.");
	}
}
