package org.pyt.common.common;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.abstracts.ADto;
import org.pyt.common.validates.ValidateValues;

/**
 * Esta clase contiene algunas utilidades para aplicar sobre dtos de la
 * aplicación
 * 
 * @author Alejandro Parra
 * @since 14-02-2020
 */
public class DtoUtils {
	private DtoUtils() {
	}

	/**
	 * si el dto no se encuentra instanciado o tiene instancia pero todos los campos
	 * se encuentran vacios
	 * 
	 * @param <D> {@link ADto}
	 * @param dto {@link ADto}
	 * @return {@link Boolean}
	 */
	public static <D extends ADto> Boolean isBlank(D dto) {
		return dto == null || dto.getNameFields().stream().map(row -> dto.get(row) == null).reduce(true,
				(valid, row) -> valid &= row);
	}

	/**
	 * Verifíca sí el dto suministrado tiene una instancia y alguno de sus campos no
	 * se encuentra vacio.
	 * 
	 * @param <D> {@link ADto}
	 * @param dto {@link ADto}
	 * @return {@link Boolean}
	 */

	public static <D extends ADto> Boolean isNotBlank(D dto) {
		return dto != null
				&& dto.getNameFields().stream().map(row -> dto.get(row) != null).anyMatch(row -> row == true);
	}

	/**
	 * verifíca sí el dto suministrado tiene una instancia y el campo código no se
	 * encunetra vacío.
	 * 
	 * @param <D> {@link ADto}
	 * @param dto {@link Adto}
	 * @return {@link Boolean}
	 */
	public static <D extends ADto> Boolean haveCode(D dto) {
		return dto != null && StringUtils.isNotBlank(dto.getCodigo());
	}
	
	public static <D extends ADto> Boolean haveNotCode(D dto) {
		return dto == null || StringUtils.isBlank(dto.getCodigo());
	}

	public static <D extends ADto> Boolean isNotBlankFields(D dto, String... fieldsName) {
		var vv = new ValidateValues();
		return isNotBlank(dto) && Arrays.asList(fieldsName).stream().map(nameField -> vv.isNotBlank(dto.get(nameField)))
				.allMatch(value -> value == true);
	}
}
